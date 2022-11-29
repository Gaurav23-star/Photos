package src.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Optional;
import java.util.ResourceBundle;

public class DisplayPhotosInAlbumController implements Initializable {

    @FXML
    private VBox photosBox;
    public static String userName;
    public static String albumName;

    @FXML
    private VBox photoDetailsDisplayBox;
    @FXML
    private ImageView imageDisplayAlone;

    public static ImageView imageView;

    @FXML
    private Label albumNameTitle;

    @FXML
    private ListView<String> listTagsOfPhoto;


    @FXML Button closeApplicationButton;
    @FXML Button goBackButton;
    @FXML Button addButton;
    @FXML Button moveButton;
    @FXML Button editButton;
    @FXML Button copyButton;
    @FXML Button deleteButton;
    @FXML Label photoDateLabel;
    @FXML Label captionsLabel;
    @FXML Button addTagButton;
    public static Label photoDate;
    public static Label captions;
    public static ListView<String> listTags;

    EventHandler<MouseEvent> handler = this::eventHandlerOn;
    EventHandler<MouseEvent> handler2 = this::eventHandlerOff;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageView = imageDisplayAlone;
        photoDate = photoDateLabel;
        captions = captionsLabel;
        listTags = listTagsOfPhoto;

        albumNameTitle.setText(albumName);
        User user = UserDataBaseController.getUserWithName(userName);
        assert user != null;
        Album currentAlbum = user.getAlbumWithName(albumName);
        ArrayList<Photo> photosInAlbum = currentAlbum.getPhotosInAlbum();
        if(photosInAlbum.size() > 0){
            Image image = new Image(photosInAlbum.get(0).getImagSrc());

            imageView.setImage(image);
            imageView.setPreserveRatio(false);
            imageView.setFitHeight(203);
            imageView.setFitWidth(324);
            photoDate.setText(photosInAlbum.get(0).getPhotoDate());
            captions.setText(photosInAlbum.get(0).getCaptions());
            DisplayPhotosInAlbumController.listTags.getItems().clear();
            Hashtable<String, ArrayList<String>> tags = photosInAlbum.get(0).getTagsHashTable();
            for(String key : tags.keySet()){
                String value = tags.get(key).toString();
                value = value.substring(1, value.length()-1);
                DisplayPhotosInAlbumController.listTags.getItems().add(key + " : " + value);
            }

            this.photoDetailsDisplayBox.setVisible(true);
        }
        try {
            for(int i = 0; i < photosInAlbum.size(); i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/photoHoldBox.fxml"));
                HBox photoBox = fxmlLoader.load();
                photosLayoutController photosLayoutController = fxmlLoader.getController();
                photosLayoutController.setPhotos(photosInAlbum.get(i));
                photosBox.getChildren().add(photoBox);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        //add event listener to all button
        goBackButton.setOnMouseEntered(handler);
        goBackButton.setOnMouseExited(handler2);
        addButton.setOnMouseEntered(handler);
        addButton.setOnMouseExited(handler2);
        copyButton.setOnMouseEntered(handler);
        copyButton.setOnMouseExited(handler2);
        deleteButton.setOnMouseEntered(handler);
        deleteButton.setOnMouseExited(handler2);
        editButton.setOnMouseEntered(handler);
        editButton.setOnMouseExited(handler2);
        moveButton.setOnMouseEntered(handler);
        moveButton.setOnMouseExited(handler2);
        addTagButton.setOnMouseEntered(handler);
        addTagButton.setOnMouseExited(handler2);



    }


    public void goBackToUserView(ActionEvent actionEvent) throws IOException {
        UserViewController.userName = userName;
        AnchorPane root = FXMLLoader.load(getClass().getResource("../FXMLFiles/UserView.fxml"));
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);

        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    public void addNewPhoto(ActionEvent actionEvent) throws IOException {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Acceptable files",  "*.JPG", "*.BMP", "*.GIF", "*.JPEG", "*.PNG");
        fc.getExtensionFilters().add(extensionFilter);

        File file = fc.showOpenDialog(null);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm");

        if(file != null){
            User user = UserDataBaseController.getUserWithName(userName);
            assert user != null;
            Album album = user.getAlbumWithName(albumName);

            Photo duplicatePhoto = user.checkForDuplicatePhotoInDifferentAlbums(file.toURI().toString());
            Photo newPhoto;
            if(duplicatePhoto == null) {

                newPhoto = new Photo();
                newPhoto.setImagSrc(file.toURI().toString());
                newPhoto.setName("new photo");
                newPhoto.setPhotoDate(simpleDateFormat.format(file.lastModified()));
            }
            else{
                newPhoto = duplicatePhoto;

            }

                album.addPhoto(newPhoto);

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/photoHoldBox.fxml"));
                HBox photoBox = fxmlLoader.load();
                photosLayoutController photosLayoutController = fxmlLoader.getController();
                photosLayoutController.setPhotos(newPhoto);
                photosBox.getChildren().add(0, photoBox);
                imageView.setImage(new Image(newPhoto.getImagSrc()));
                imageView.setPreserveRatio(false);
                imageView.setFitHeight(203);
                imageView.setFitWidth(324);
                photoDate.setText(newPhoto.getPhotoDate());
                captions.setText(newPhoto.getCaptions());
                this.photoDetailsDisplayBox.setVisible(true);


        }

    }

    public void editPhotoDetails(ActionEvent actionEvent) {
        User user = UserDataBaseController.getUserWithName(userName);
        Album album = user.getAlbumWithName(albumName);
        Photo photo = album.getPhotoWithUrl(imageView.getImage().getUrl());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/dialogBox.fxml"));
        DialogPane albumlistDialog = null;
        try {
            albumlistDialog = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(albumlistDialog);
        dialog.setTitle("Add Captions");

        VBox vBox = (VBox) albumlistDialog.getChildren().get(4);
        Label titleLable = (Label) vBox.getChildren().get(0);
        titleLable.setText("Add/Change Caption");
        HBox hbox = (HBox) albumlistDialog.getChildren().get(3);
        Label promptLable = (Label) hbox.getChildren().get(0);
        promptLable.setText("New Caption");
        TextField textField = (TextField) hbox.getChildren().get(1);
        textField.setText(photo.getCaptions());
        textField.setPromptText("caption");
        textField.positionCaret(photo.getCaptions().length());
        textField.requestFocus();

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if(clickedButton.get() == ButtonType.APPLY){
            String newCaption = textField.getText();
            photo.setCaptions(newCaption);
            captions.setText(newCaption);
        }


    }

    public void movePhoto(ActionEvent actionEvent) throws IOException {
        User user = UserDataBaseController.getUserWithName(userName);
        Album album = user.getAlbumWithName(albumName);
        Photo photo = album.getPhotoWithUrl(imageView.getImage().getUrl());


        //load the dialoge box with albums drop down menu
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/selectAlbum.fxml"));
        DialogPane albumlistDialog = fxmlLoader.load();
        selectAlbumController selectAlbumController = fxmlLoader.getController();
        selectAlbumController.setData(user);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(albumlistDialog);
        dialog.setTitle("Select Album");
        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if(clickedButton.get() == ButtonType.OK){
            if(selectAlbumController.getAlbumSelected() != null){
                Album destinationAlbum = user.getAlbumWithName(selectAlbumController.getAlbumSelected());
                if(destinationAlbum != album) {
                    destinationAlbum.addPhoto(photo);
                    album.removePhoto(photo);
                    updateThePhotosView(album);
                }
            }
        }




    }
    public void copyPhoto(ActionEvent actionEvent) throws IOException {
        User user = UserDataBaseController.getUserWithName(userName);
        Album album = user.getAlbumWithName(albumName);
        Photo photo = album.getPhotoWithUrl(imageView.getImage().getUrl());

        //load the dialoge box with albums drop down menu
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/selectAlbum.fxml"));
        DialogPane albumlistDialog = fxmlLoader.load();
        selectAlbumController selectAlbumController = fxmlLoader.getController();
        selectAlbumController.setData(user);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(albumlistDialog);
        dialog.setTitle("Select Album");
        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if(clickedButton.get() == ButtonType.OK){
            if(selectAlbumController.getAlbumSelected() != null){
                Album destinationAlbum = user.getAlbumWithName(selectAlbumController.getAlbumSelected());
                if(destinationAlbum != album) {
                    destinationAlbum.addPhoto(photo);
                }
            }
        }


    }
    public void deletePhoto(ActionEvent actionEvent){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setStyle("-fx-background-color: #222831");
        alert.setTitle("Confirmation");
        alert.setHeaderText("Do you want to delete the Photo?");
        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get() == ButtonType.OK){
            User user = UserDataBaseController.getUserWithName(userName);
            Album album = user.getAlbumWithName(albumName);
            Photo photo = album.getPhotoWithUrl(imageView.getImage().getUrl());
            album.removePhoto(photo);
            updateThePhotosView(album);
        }

    }

    public void updateThePhotosView(Album album){
        photosBox.getChildren().clear();
        imageView.setImage(null);
        if(album.getAlbumSize() == 0){
            this.photoDetailsDisplayBox.setVisible(false);
        }
        initialize(null,null);

    }

    public void eventHandlerOn(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#00ADB5");
    }
    public void eventHandlerOff(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#EEEEEE");
    }

    public void addMoreTag(ActionEvent actionEvent) {
        User user = UserDataBaseController.getUserWithName(userName);
        Album album = user.getAlbumWithName(albumName);
        Photo photo = album.getPhotoWithUrl(imageView.getImage().getUrl());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/manageTags.fxml"));
        DialogPane tagsDialog = null;
        try {
            tagsDialog = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(tagsDialog);
        tagsController tagsController = fxmlLoader.getController();
        tagsController.manageTags(photo, dialog, user, listTagsOfPhoto);





    }

}
