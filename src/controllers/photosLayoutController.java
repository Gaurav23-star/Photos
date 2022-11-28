package src.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Hashtable;

public class photosLayoutController {

    @FXML
    private HBox photoHoldBox;

    @FXML
    private ImageView photoImage;


    public void setPhotos(Photo photo){
        Image image = new Image(photo.getImagSrc());
        photo.setImagSrc(image.getUrl());
        photoImage.setImage(image);
        photoImage.setPreserveRatio(false);
        photoImage.setFitWidth(280);
        photoImage.setFitHeight(170);




        photoHoldBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DisplayPhotosInAlbumController.listTags.getItems().clear();
                DisplayPhotosInAlbumController.imageView.setImage(image);
                DisplayPhotosInAlbumController.imageView.setFitHeight(203);
                DisplayPhotosInAlbumController.imageView.setFitWidth(324);
                DisplayPhotosInAlbumController.photoDate.setText(photo.getPhotoDate());
                DisplayPhotosInAlbumController.captions.setText(photo.getCaptions());
                Hashtable<String, ArrayList<String>> tags = photo.getTagsHashTable();
                for(String key : tags.keySet()){
                    String value = tags.get(key).toString();
                    value = value.substring(1, value.length()-1);
                    DisplayPhotosInAlbumController.listTags.getItems().add(key + " : " + value);
                }


                photoHoldBox.setStyle("-fx-background-color:#00ADB5");
                //photoHoldBox.setStyle("-fx-background-color:#528cde");
                //photoHoldBox.setOpacity(30);
            }
        });
        photoHoldBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                photoHoldBox.setStyle("-fx-background-color: #EEEEEE");
                //photoHoldBox.setOpacity(100);
            }
        });

    }


}
