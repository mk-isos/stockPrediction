package com.example.stock_prediction.PythonRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StockPredictionPythonRunner {
    private static final Logger logger = LoggerFactory.getLogger(StockPredictionPythonRunner.class);

    public List<String> runPythonScript(String stockSymbol) {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "predict_stock.py", stockSymbol);
        List<String> predictions = new ArrayList<>();

        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                predictions.add(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                // 예외를 로깅하여 디버깅 및 모니터링을 위해 저장
                logger.error("Python script execution failed with exit code " + exitCode);
                throw new RuntimeException("Python script execution failed with exit code " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            // 예외를 로깅하여 디버깅 및 모니터링을 위해 저장
            logger.error("An error occurred while running the Python script", e);
            // 예외 처리
        }

        return predictions;
    }
}
