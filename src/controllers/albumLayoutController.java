package src.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class albumLayoutController {
    @FXML
    private HBox albumLayoutBox;
    @FXML
    private Label albumName;

    @FXML
    private ImageView albumPhoto;

    @FXML
    private Label albumSize;

    @FXML
    private Label dateRange;

    @FXML
    private Button deleteAlbumButton;
    @FXML
    private Button editAlbumButton;

    EventHandler<MouseEvent> handler = this::eventHandlerOn;
    EventHandler<MouseEvent> handler2 = this::eventHandlerOff;


    public void setData(Album album){
        Photo photo = album.getAlbumThumnail();

        if(photo != null){
            String imageSrc = photo.getImagSrc();
            Image image = new Image(imageSrc);
            albumPhoto.setImage(image);
            albumPhoto.setPreserveRatio(false);
        }

        albumName.setText(album.getAlbumName());
        albumSize.setText(String.valueOf(album.getAlbumSize()));
        if(album.getDateRange() != null){
            dateRange.setText(album.getDateRange());
        }

        String currentColor = "#EEEEEE";
        albumLayoutBox.setStyle("-fx-background-color: " + currentColor );
        albumLayoutBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                albumLayoutBox.setStyle("-fx-background-color:#00ADB5");
                //albumLayoutBox.setOpacity(30);
            }
        });
        albumLayoutBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                albumLayoutBox.setStyle("-fx-background-color:"+currentColor);
                //albumLayoutBox.setOpacity(100);
            }
        });

        albumLayoutBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    displayPhotosInAlbum(mouseEvent, album);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        deleteAlbumButton.setOnMouseEntered(handler);
        deleteAlbumButton.setOnMouseExited(handler2);
        editAlbumButton.setOnMouseEntered(handler);
        editAlbumButton.setOnMouseExited(handler2);

        deleteAlbumButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.getDialogPane().setStyle("-fx-background-color: #222831");

                alert.setTitle("Confirmation");
                alert.setHeaderText("Do you want to delete the album " + album.getAlbumName() + "?");
                Optional<ButtonType> buttonType = alert.showAndWait();

                if(buttonType.get() == ButtonType.OK) {
                    User user = UserDataBaseController.getUserWithName(album.getUsername());
                    String userName = album.getUsername();
                    user.albums.remove(album);
                    refreshAlbumScree(actionEvent, userName);

                }
            }
        });

        editAlbumButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
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
                dialog.setTitle("Edit Album Name");

                VBox vBox = (VBox) albumlistDialog.getChildren().get(4);
                Label titleLable = (Label) vBox.getChildren().get(0);
                titleLable.setText("Edit Album");
                HBox hbox = (HBox) albumlistDialog.getChildren().get(3);
                Label promptLable = (Label) hbox.getChildren().get(0);
                promptLable.setText("New Album Name");
                TextField textField = (TextField) hbox.getChildren().get(1);
                textField.setText(album.getAlbumName());
                textField.setPromptText("album name");
                textField.positionCaret(album.getAlbumName().length());
                textField.requestFocus();
                Optional<ButtonType> clickedButton = dialog.showAndWait();
                //here need to implement checker to find out duplicate album name
                if(clickedButton.get() == ButtonType.APPLY){
                    String newAlbumnName = textField.getText();
                    album.setAlbumName(newAlbumnName);
                    String userName = album.getUsername();
                    refreshAlbumScree(actionEvent, userName);
                }

            }
        });
    }

    public void displayPhotosInAlbum(MouseEvent mouseEvent, Album album) throws IOException {

        DisplayPhotosInAlbumController.userName = album.getUsername();
        DisplayPhotosInAlbumController.albumName = album.getAlbumName();
        AnchorPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../fxmlfiles/displayPhotosInAlbum.fxml")));
        Stage mainStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    public void eventHandlerOn(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#EEEEEE");
    }
    public void eventHandlerOff(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#222831; -fx-text-fill:#EEEEEE");
    }

    public void refreshAlbumScree( ActionEvent actionEvent, String userName){
        UserViewController.userName = userName;
        AnchorPane root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../FXMLFiles/UserView.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();

    }


}
