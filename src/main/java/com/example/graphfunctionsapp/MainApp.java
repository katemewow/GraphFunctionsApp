package com.example.graphfunctionsapp;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final static int PORT = 8081;

    @Override
    public void start(Stage primaryStage) {
        handleStartServerAction();
    }

    private void handleStartServerAction() {
        Thread serverThread = new Thread(new Server(PORT));
        serverThread.start();
        System.out.println("Сервер запущен");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
