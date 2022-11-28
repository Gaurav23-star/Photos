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
    public void createNewAlbum(ActionEvent actionEvent, String userName, Dialog dialog) throws IOException {
        //load the dialoge box with albums drop down menu

        dialog.setTitle("Select Album");
        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if(clickedButton.get() == ButtonType.APPLY){

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
                    newAlbum.addPhoto(newPhoto);
                }
                User user = UserDataBaseController.getUserWithName(userName);
                if(user != null){
                    user.addAlbum(newAlbum);
                }
            }


        }




    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //albumName = newAlbumName.getText();
        uploadFromFiles.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FileChooser fc = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
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

    public void eventHandlerOn(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#00ADB5");
    }
    public void eventHandlerOff(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#222831; -fx-text-fill:#EEEEEE");
    }
}
