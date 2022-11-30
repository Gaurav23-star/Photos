package src.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
    Group 64

    @Author
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */

/**
 * Class populates the imageview with the search results.
 */
public class searchResultPhotosController {


    @FXML private Label dateLable;

    @FXML private ImageView photoImageView;

    public void setPhoto(Photo photo){
        Image newImage = new Image(photo.getImagSrc());
        dateLable.setText(photo.getPhotoDate());
        photoImageView.setPreserveRatio(false);
        photoImageView.setImage(newImage);

    }



}
