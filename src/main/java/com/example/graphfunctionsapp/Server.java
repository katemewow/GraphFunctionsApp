package com.example.graphfunctionsapp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import static com.example.graphfunctionsapp.Params.*;


public class Server implements Runnable {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server was started on port: " + port);

            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Server wasn't started on port: " + port + " with error: " + e.getMessage());

        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        private int maxSteps = 100;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                // Чтение длины сообщения
                int length = in.readInt();
                System.out.println("DEBUG --- Чтение длины сообщения --- " + length);
                if (length > 0) {
                    // Чтение самого сообщения
                    String jsonMessage = in.readUTF();
                    System.out.println("DEBUG --- Получено от клиента --- " + jsonMessage);

                    JSONObject json = new JSONObject(jsonMessage);
                    System.out.println("DEBUG --- Получено от клиента JSON --- " + json);

                    double t0 = getDoubleValueOfJson(T_0, json);
                    double tend = getDoubleValueOfJson(T_END, json);
                    double tStep = getDoubleValueOfJson(T_STEP, json);
                    double param1 = getFloatValueOfJson(PARAM1, json);
                    double param2 = getFloatValueOfJson(PARAM2, json);
                    maxSteps = getIntegerValueOfJson(T_EMIT, json); // limit
                    Function3D function = new Function3D();

                    int stepCounter = 0;
                     // Количество шагов до отправки группы точек
                    StringBuilder pointsBatch = new StringBuilder();

                    for (double x = t0; x <= tend; x += tStep) {
                        for (double y = t0; y <= tend; y += tStep) {
                            double z = function.compute(x, y, param1, param2);
                            stepCounter++;
                            pointsBatch.append(createJsonToAnswer(x, y, z)).append("\n");

                            if (stepCounter >= maxSteps) {
                                // Отправляем накопленные точки
                                out.print(pointsBatch);
                                out.flush();
                                pointsBatch.setLength(0); // Очищаем буфер
                                stepCounter = 0; // Сброс счетчика
                                try {
//                                    TimeUnit.SECONDS.sleep(1); // Приостановка выполнения на 1 секунду
                                    TimeUnit.MILLISECONDS.sleep(300); // Приостановка выполнения на 1 секунду
                                } catch (InterruptedException e) {
                                    // Обработка исключения, если поток прерван
                                } // Пауза перед следующей группой точек
                                out.println("--END OF BATCH--"); // Отправляем маркер конца пакета
                            } else {
                                out.print(pointsBatch);
                                pointsBatch.setLength(0);
                            }
                        }
                    }
                    out.println("end");
                }

            } catch (IOException e) {
                System.out.println("Ошибка при обработке клиента: " + e.getMessage());
            }
        }

        private String createJsonToAnswer(double x, double y, double z) {
            // Сериализация данных в JSON
            JSONObject json = new JSONObject();
            json.put(X.name(), x);
            json.put(Y.name(), y);
            json.put(Z.name(), z);

            return json.toString();
        }
    }
}

