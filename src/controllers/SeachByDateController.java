package src.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

/*
    Group 64

    Authors
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */

public class SeachByDateController {
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;

    public void searchByDate(Dialog<ButtonType> dialog, String username, ActionEvent actionEvent) throws IOException {
        ArrayList<Photo> results = null;
        User user = UserDataBaseController.getUserWithName(username);
        Album album = user.albums.get(0);
        Optional<ButtonType> clickButton = dialog.showAndWait();

        if(clickButton.get() == ButtonType.APPLY){
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            if(startDate != null || endDate != null){
                results = user.getPhotosInRange(startDate, endDate);
            }
            if(results != null){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/search.fxml"));
                AnchorPane root = fxmlLoader.load();
                Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 800, 600);
                mainStage.setScene(scene);
                searchResultsController searchResultsController = fxmlLoader.getController();
                searchResultsController.start(results);
                mainStage.setResizable(false);
                mainStage.show();

            }

        }


    }
}
