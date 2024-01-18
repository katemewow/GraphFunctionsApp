package com.example.graphfunctionsapp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

                    // Обработка JSON и выполнение вычислений
                    String resultJson = processJson(jsonMessage);

                    // Отправка результата обратно клиенту
                    out.println(resultJson);
                }

            } catch (IOException e) {
                System.out.println("Ошибка при обработке клиента: " + e.getMessage());
            }
        }

        private String processJson(String jsonMessage) {
            return jsonMessage;
        }
    }
}

