import yfinance as yf
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.preprocessing import MinMaxScaler
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense
import mysql.connector
from datetime import datetime

# 주가 예측 모델을 위한 함수와 클래스 정의
def create_sequences(data, sequence_length):
    sequences = []
    for i in range(len(data) - sequence_length):
        seq = data[i:i+sequence_length]
        target = data[i+sequence_length]
        sequences.append((seq, target))
    return sequences

# 원하는 주식 입력받기
stock_symbol = input("예측하고 싶은 주식을 입력해주세요 : ")

#epoch size 입력받기
epoch_size = input("epoch의 크기를 입력해주세요 : ")
epochs = int(epoch_size)

# 주식 데이터 가져오기
stock = yf.Ticker(stock_symbol)
stock_data = stock.history(period="3y")

# 종가 데이터만 사용
data = stock_data['Close'].dropna().values.reshape(-1, 1)

# 데이터 정규화
scaler = MinMaxScaler()
data = scaler.fit_transform(data)

# 데이터 분할 (학습 데이터와 테스트 데이터)
train_size = int(len(data) * 0.8)
train_data, test_data = data[:train_size], data[train_size:]

sequence_length = 10  # 시퀀스 길이
train_sequences = create_sequences(train_data, sequence_length)
test_sequences = create_sequences(test_data, sequence_length)

X_train, y_train = [seq[0] for seq in train_sequences], [seq[1] for seq in train_sequences]
X_test, y_test = [seq[0] for seq in test_sequences], [seq[1] for seq in test_sequences]

# Python 리스트를 NumPy 배열로 변환한 후 TensorFlow 텐서로 변환
X_train = np.array(X_train)
y_train = np.array(y_train)
X_test = np.array(X_test)
y_test = np.array(y_test)

# LSTM 모델 생성
model = Sequential()
model.add(LSTM(50, activation='relu', input_shape=(sequence_length, 1)))
model.add(Dense(1))
model.compile(optimizer='adam', loss='mean_squared_error')

# 모델 학습
model.fit(X_train, y_train, epochs=int(epoch_size), batch_size=32)

# 모델을 사용하여 미래 예측
future_steps = 90  # 3달(90일) 동안의 주가 예측을 위한 스텝 수
future_predictions = []

last_sequence = X_test[-1].reshape(1, sequence_length, 1)

for i in range(future_steps):
    prediction = model.predict(last_sequence)[0, 0]
    future_predictions.append(prediction)
    last_sequence = np.roll(last_sequence, -1, axis=1)
    last_sequence[0, -1, 0] = prediction

# 미래 예측 주가 역정규화
future_predictions = scaler.inverse_transform(np.array(future_predictions).reshape(-1, 1))

# 과거 주가 역정규화
train_data_transformed = scaler.inverse_transform(train_data)
test_data_transformed = scaler.inverse_transform(test_data)

# 미래 예측을 위한 날짜 범위 생성
last_date = stock_data.index[-1]  # 마지막 주식 데이터 날짜
future_dates = pd.date_range(start=last_date, periods=future_steps+1)[1:]  # 'closed' 인자 제거 및 첫 번째 날짜 제외

# 미래 예측 출력
print("미래 주가 예측 (3달치):")
for i, prediction in enumerate(future_predictions):
    print(f"다음 {future_dates[i].date()}일: {prediction[0]:.2f} USD")

# 그래프 그리기
plt.figure(figsize=(12, 6))
plt.plot(stock_data.index[:train_size], train_data_transformed, label='과거 실제 주가', color='red')
plt.plot(stock_data.index[train_size+sequence_length:], test_data_transformed[sequence_length:], label='과거 실제 주가', color='red', alpha=0.3)
plt.plot(future_dates, future_predictions, label='미래 예측 주가', color='blue')
plt.title(f'주가 예측 ({stock_symbol} - 3달치)')
plt.xlabel('날짜')
plt.ylabel('주가')
plt.legend()
plt.grid(True)
plt.show()


# MySQL 데이터베이스 연결 및 데이터 저장
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="123456",
    database="finance"
)

cur = conn.cursor()

# 테이블 생성 (없는 경우)
cur.execute('''
    CREATE TABLE IF NOT EXISTS stock_predictions (
        stock_symbol VARCHAR(10),
        prediction_date DATE,
        predicted_price DECIMAL(10, 2),
        PRIMARY KEY (stock_symbol, prediction_date)
    )
''')

# 예측 결과를 데이터베이스에 저장
# 주식 이름으로 기존 epoch 값 조회
cur.execute('SELECT id, epoch FROM stocks WHERE name = %s', (stock_symbol,))
result = cur.fetchone()

if result:
    stock_id, existing_epoch = result
    # 새로운 epoch 값이 기존 값보다 큰 경우에만 업데이트
    if epochs >= existing_epoch:
        # 기존 예측 데이터 삭제
        cur.execute('DELETE FROM stock_predictions WHERE id = %s', (stock_id,))
        # 주식 정보 업데이트
        cur.execute('UPDATE stocks SET epoch = %s WHERE id = %s', (epochs, stock_id))

        # 예측 결과를 데이터베이스에 저장
        for i, prediction in enumerate(future_predictions):
            prediction_date = future_dates[i].date()  # 날짜 추출
            predicted_price = float(prediction[0])  # 예측 가격 추출
            cur.execute('''
                INSERT INTO stock_predictions (id, prediction_date, predicted_price)
                VALUES (%s, %s, %s)
            ''', (stock_id, prediction_date, predicted_price))
else:
    # 새로운 주식 정보 삽입
    cur.execute('INSERT INTO stocks (name, epoch) VALUES (%s, %s)', (stock_symbol, epochs))
    stock_id = cur.lastrowid  # 새로 삽입된 주식의 ID

    # 예측 결과를 데이터베이스에 저장
    for i, prediction in enumerate(future_predictions):
        prediction_date = future_dates[i].date()  # 날짜 추출
        predicted_price = float(prediction[0])  # 예측 가격 추출
        cur.execute('''
            INSERT INTO stock_predictions (id, prediction_date, predicted_price)
            VALUES (%s, %s, %s)
        ''', (stock_id, prediction_date, predicted_price))



conn.commit()

# 커서와 연결 종료
cur.close()
conn.close()