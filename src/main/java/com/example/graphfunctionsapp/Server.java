package com.example.graphfunctionsapp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Server implements Runnable {
    private final int port;
    private final TextArea textArea;

    public Server(int port, TextArea textArea) {
        this.port = port;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Platform.runLater(() -> textArea.appendText("Сервер запущен на порту " + port + "\n"));

            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, textArea)).start();
            }
        } catch (IOException e) {
            Platform.runLater(() -> textArea.appendText("Ошибка сервера: " + e.getMessage() + "\n"));
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final TextArea textArea;

        public ClientHandler(Socket socket, TextArea textArea) {
            this.clientSocket = socket;
            this.textArea = textArea;
        }

        public void run() {
            try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                // Чтение длины сообщения
                int length = in.readInt();
                if (length > 0) {
                    // Чтение самого сообщения
                    String jsonMessage = in.readUTF();
                    Platform.runLater(() -> textArea.appendText("Получено от клиента: " + jsonMessage + "\n"));

                    // Обработка JSON и выполнение вычислений
                    String resultJson = processJson(jsonMessage);

                    // Отправка результата обратно клиенту
                    out.println(resultJson);
                }

            } catch (IOException e) {
                Platform.runLater(() -> textArea.appendText("Ошибка при обработке клиента: " + e.getMessage() + "\n"));
            }
        }

        private String processJson(String jsonMessage) {
            return jsonMessage;
        }
    }
}

