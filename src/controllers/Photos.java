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

/**
 * Class containing Main method to start the application.
 */

public class Photos extends Application {

    /**
     * Main method to start the application.
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        UserDataBaseController.readFromDataBase();
        launch(args);
    }


    /**
     * Starts the application.Creates an event listener on stage, that will save all the data when application is closed.
     * @param stage
     * @throws Exception
     */
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
