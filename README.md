# stockPrediction
## 주가 예측 및 데이터베이스 연동 LSTM 모델
이 프로젝트는 LSTM (Long Short-Term Memory) 모델을 사용하여 주가를 예측하고, 예측된 주가를 MySQL 데이터베이스에 저장하는 것을 목표로 합니다. 또한, 예측된 주가는 Matplotlib을 사용하여 시각화됩니다.

## 기능:
- yfinance를 사용하여 주식의 과거 데이터 가져오기  
- 데이터를 LSTM 모델 학습을 위해 정규화 및 준비  
- LSTM 모델을 학습하여 미래의 주가 예측  
- 과거 및 예측된 주가 시각화  
- 예측 결과를 MySQL 데이터베이스에 저장  
