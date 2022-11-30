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


/**
    Group 64

    @Author
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */


/**
 * Class controls the showing of all the search results.
 * Allows user to go back or create new album containing search results.
 */
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

    /**
     * Method lets user back to the main userview from search results screen.
     * @param actionEvent
     * @throws IOException
     */
    public void goBack(ActionEvent actionEvent) throws IOException {
        userName = userName;
        AnchorPane root = FXMLLoader.load(getClass().getResource("../FXMLFiles/UserView.fxml"));
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);

        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }


    /**
     * Method creates a new album from the search results and stores it.
     * @param actionEvent
     * @throws IOException
     */

    public void createNewAlbumFromResult(ActionEvent actionEvent) throws IOException {
        String newAlbumName = null;
        User currentUser = UserDataBaseController.getUserWithName(userName);
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
        Label promptLable = (Label) newHbox.getChildren().get(0);
        promptLable.setText("New Album");
        TextField textField = (TextField) newHbox.getChildren().get(1);
        textField.setPromptText("new album name");
        textField.positionCaret(0);
        textField.requestFocus();
        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if(clickedButton.get() == ButtonType.APPLY){
            newAlbumName = textField.getText().trim();
            if(newAlbumName == null || newAlbumName == ""){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.getDialogPane().setStyle("-fx-background-color: #222831");
                alert.setTitle("Enter Album Name");
                alert.setHeaderText("You must enter a album name!");
                Optional<ButtonType> buttonType = alert.showAndWait();
                createNewAlbumFromResult(actionEvent);
            }else if (currentUser.getAlbumWithName(newAlbumName) != null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.getDialogPane().setStyle("-fx-background-color: #222831");
                alert.setTitle("Enter Different Name");
                alert.setHeaderText("Album name you entered already exists!");
                Optional<ButtonType> buttonType = alert.showAndWait();
                createNewAlbumFromResult(actionEvent);
            }
            else{
                CreateNewAlbum(newAlbumName,actionEvent );

            }
        }



    }

    /**
     * Helper method to create new album from search results.
     * @param newAlbumName
     * @param actionEvent
     * @throws IOException
     */
    public void CreateNewAlbum(String newAlbumName, ActionEvent actionEvent) throws IOException {
        Album newAlbum = new Album();
        newAlbum.setAlbumName(newAlbumName);
        newAlbum.setUsername(userName);
        for(Photo photo : photos){
            newAlbum.addPhoto(photo);
        }
        currentUser.addAlbum(newAlbum);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setStyle("-fx-background-color: #222831");
        alert.setTitle("New Album Created");
        alert.setHeaderText("New Album: " + newAlbumName + " created!");
        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get() == ButtonType.OK){
            this.goBack(actionEvent);
        }

    }
}
