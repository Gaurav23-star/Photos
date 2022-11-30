package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;


/**
    Group 64

    @Author
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */

/**
 * Class handles the creating new album and storing it into the database.
 */
public class createNewAlbumController implements Initializable {

    @FXML private TextField newAlbumName;
    @FXML private Button uploadFromFiles;
    @FXML private ListView<String> listSelectedPhotos;

    @FXML private Button applyButton;

    private static ObservableList<String> observableList = FXCollections.observableArrayList();
    private static ObservableList<URI> uris = FXCollections.observableArrayList();
    public  static String albumName;
    EventHandler<MouseEvent> handler = this::eventHandlerOn;
    EventHandler<MouseEvent> handler2 = this::eventHandlerOff;

    /**
     * Method creates a new Album, and stores it into user's albums list
     * @param actionEvent
     * @param userName
     * @param dialog
     * @throws IOException
     */
    public void createNewAlbum(ActionEvent actionEvent, String userName, Dialog dialog) throws IOException {
        //load the dialoge box with albums drop down menu

        dialog.setTitle("Select Album");
        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if(clickedButton.get() == ButtonType.APPLY){
            User user = UserDataBaseController.getUserWithName(userName);
            assert user != null;

            if (user.getAlbumWithName(newAlbumName.getText().trim()) != null){

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.getDialogPane().setStyle("-fx-background-color: #222831");
                alert.setTitle("Album Exists");
                alert.setHeaderText("Album with name already exists!");
                Optional<ButtonType> buttonType = alert.showAndWait();
                createNewAlbum(actionEvent, userName, dialog);

            }
            else if(newAlbumName.getText() == null || newAlbumName.getText().trim() == ""){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.getDialogPane().setStyle("-fx-background-color: #222831");
                alert.setTitle("Enter Album Name");
                alert.setHeaderText("You must enter a album name!");
                Optional<ButtonType> buttonType = alert.showAndWait();
                createNewAlbum(actionEvent, userName, dialog);
            }
            else{
                createNewAlbum(userName, user);
            }

        }

    }

    /**
     * Helper method to create new album, and put it on user albums list
     * @param userName
     * @param user
     */
    public void createNewAlbum(String userName, User user){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
        File myFile;

        if(observableList.size() > 0){
            Album newAlbum = new Album();
            newAlbum.setAlbumName(newAlbumName.getText());
            newAlbum.setUsername(userName);
            for(int i = 0; i < observableList.size(); i++){
                myFile = new File(uris.get(i));
                Photo newPhoto = new Photo();
                newPhoto.setAlbumName(newAlbum.getAlbumName());
                newPhoto.setName(observableList.get(i));
                newPhoto.setImagSrc(uris.get(i).toString());
                newPhoto.setPhotoDate(simpleDateFormat.format(myFile.lastModified()));
                newPhoto.setLastModifiedDate(myFile.lastModified());
                newAlbum.addPhoto(newPhoto);
            }

            if(user != null){
                user.addAlbum(newAlbum);
            }
        }

    }

    /**
     * Method to initialize all the mouseclick events.
     * Sets the event on button to open file chooser, when user clicks to add new photo from computer.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //albumName = newAlbumName.getText();
        uploadFromFiles.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FileChooser fc = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Acceptable files", "*.JPG", "*.BMP", "*.GIF", "*.JPEG", "*.PNG");
                fc.getExtensionFilters().add(extensionFilter);
                File file = fc.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());

                if(file != null){
                    listSelectedPhotos.getItems().add(file.getName());
                    observableList.add(file.getName());
                    uris.add(file.toURI());
                }
            }
        });
        uploadFromFiles.setOnMouseEntered(handler);
        uploadFromFiles.setOnMouseExited(handler2);
    }

    /**
     * Handles the mouse hover event.
     * @param mouseEvent
     */
    public void eventHandlerOn(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#00ADB5");
    }
    public void eventHandlerOff(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#222831; -fx-text-fill:#EEEEEE");
    }
}
