package src.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static src.controllers.UserViewController.currentUser;
import static src.controllers.UserViewController.userName;

public class searchResultsController implements Initializable {

    @FXML
    private Button createNewAlbumFromSearchButton;

    @FXML
    private Button goBackButton;

    @FXML
    private GridPane gridPaneDisplayigResults;

    EventHandler<MouseEvent> handler = this::eventHandlerOn;
    EventHandler<MouseEvent> handler2 = this::eventHandlerOff;

    public ArrayList<Photo> photos;
    public void start(ArrayList<Photo> results) throws IOException {
        photos = results;
        int column = 0;
        int row = 1;
        for(Photo photo : results){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/searchResultsPhotos.fxml"));
            HBox photoBox = fxmlLoader.load();
            searchResultPhotosController searchResultPhotosController = fxmlLoader.getController();
            searchResultPhotosController.setPhoto(photo);
            if(column == 3){
                column = 0;
                row++;
            }
            gridPaneDisplayigResults.add(photoBox, column++, row);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        createNewAlbumFromSearchButton.setOnMouseEntered(handler);
        createNewAlbumFromSearchButton.setOnMouseExited(handler2);
        goBackButton.setOnMouseEntered(handler);
        goBackButton.setOnMouseExited(handler2);
    }

    public void eventHandlerOn(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#00ADB5");
    }
    public void eventHandlerOff(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#EEEEEE");
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        userName = userName;
        AnchorPane root = FXMLLoader.load(getClass().getResource("../FXMLFiles/UserView.fxml"));
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);

        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }


    public void createNewAlbumFromResult(ActionEvent actionEvent) throws IOException {
        String newAlbumName = null;
        do{
            newAlbumName = showDialogForInputName(false);
            if(newAlbumName == null){
                showDialogForInputName(true);
            }
        }while (newAlbumName == null);

        if(newAlbumName != null){
            Album newAlbum = new Album();
            newAlbum.setAlbumName(newAlbumName);
            newAlbum.setUsername(userName);
            for(Photo photo : photos){
                newAlbum.addPhoto(photo);
            }
            currentUser.addAlbum(newAlbum);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setStyle("-fx-background-color: #222831");
        alert.setTitle("New Album Created");
        alert.setHeaderText("New Album: " + newAlbumName + " created!");
        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get() == ButtonType.OK){
            this.goBack(actionEvent);
        }



    }

    public String showDialogForInputName(boolean isSecondTime){

        //load the dialoge box with albums drop down menu
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../fxmlfiles/dialogBox.fxml"));
        DialogPane albumlistDialog = null;
        try {
            albumlistDialog = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(albumlistDialog);
        dialog.setTitle("Create New Album");
        HBox newHbox = (HBox) albumlistDialog.getChildren().get(3);
        VBox vBox = (VBox) albumlistDialog.getChildren().get(4);



        Label titleLable = (Label) vBox.getChildren().get(0);
        titleLable.setText("Create New Album");
        if(isSecondTime){
            Label warning = (Label) vBox.getChildren().get(1);
            warning.setVisible(true);
        }
        Label promptLable = (Label) newHbox.getChildren().get(0);
        promptLable.setText("New Album");
        TextField textField = (TextField) newHbox.getChildren().get(1);
        textField.setPromptText("new album name");
        textField.positionCaret(0);
        textField.requestFocus();
        String newAlbumName = null;
        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if(clickedButton.get() == ButtonType.APPLY){
            if(textField.getText() != null && textField.getText().trim() != ""){
                return textField.getText();
            }
            else{
                return null;
            }
        }
        return null;



    }
}
