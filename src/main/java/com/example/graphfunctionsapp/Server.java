package com.example.graphfunctionsapp;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                // Чтение длины сообщения
                int length = in.readInt();
                if (length > 0) {
                    // Чтение самого сообщения
                    String jsonMessage = in.readUTF();
                    System.out.println("Получено от клиента:" + jsonMessage);
//

//                    // Обработка JSON и выполнение вычислений
//                    String resultJson = processJson(jsonMessage);
//
//                    // Отправка результата обратно клиенту
//                    out.println(resultJson);

                    // new version
                    // Обработка JSON и выполнение вычислений
                    JSONObject json = new JSONObject(jsonMessage);
                    System.out.println("Получено от клиента JSON:" + json);

                    Double t0 = getDoubleValueOfJson(T_0, json);
                    Double tend = getDoubleValueOfJson(T_END, json);
                    Double tstep = getDoubleValueOfJson(T_STEP, json);
                    Float param1 = getFloatValueOfJson(PARAM1, json);
                    Float param2 = getFloatValueOfJson(PARAM2, json);
                    Function3D function = new Function3D();

                    for (double x = t0; x <= tend; x += tstep) {
                        for (double y = t0; y <= tend; y += tstep) {
                            float z = function.compute((float)x, (float)y, param1, param2);
                            out.println(createJsonToAnswer(x, y, z));
                            Thread.sleep(500);
                        }
                    }
                    out.println("end");
                }

            } catch (IOException | InterruptedException e) {
                System.out.println("Ошибка при обработке клиента: " + e.getMessage());
            }
        }

        private String processJson(String jsonMessage) {
            return jsonMessage;
        }

        private String createJsonToAnswer(Double x, Double y, Float z) {
            // Сериализация данных в JSON
            JSONObject json = new JSONObject();
            json.put(X.name(), x);
            json.put(Y.name(), y);
            json.put(Z.name(), z);

            return json.toString();
        }
    }
}

