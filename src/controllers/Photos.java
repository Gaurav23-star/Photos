package src.controllers;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;

public class Photos extends Application {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        UserDataBaseController.readFromDataBase();
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {

        AnchorPane root = FXMLLoader.load(getClass().getResource("../FXMLFiles/Main.fxml"));
        root.setBackground(Background.fill(Color.rgb(238, 238, 238)));
        Scene mainScene = new Scene(root, 800, 600);
        stage.setScene(mainScene);
        stage.setTitle("Photos");
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    UserDataBaseController.writeToDataBase();
                } catch (IOException e) {
                }
            }
        });
        stage.show();

    }
}
