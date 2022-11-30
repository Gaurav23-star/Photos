package src.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

/**
    Group 64

    @Author
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */

/**
 * Class to control the dialog box which lets user to select the destination album location,
 * for moving or copying the photo
 */

public class selectAlbumController {

    @FXML
    private ChoiceBox<String> dropDownMenu;

    /**
     * Method populates the choice-box with all current albums of the user.
     * @param user
     */
    public void setData(User user){
        for(Album album : user.albums){
            dropDownMenu.getItems().add(album.getAlbumName());
        }
    }

    public String getAlbumSelected(){
        return dropDownMenu.getValue();
    }
}
