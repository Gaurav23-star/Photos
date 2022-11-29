package src.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

/*
    Group 64

    Authors
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */


public class selectAlbumController {

    @FXML
    private ChoiceBox<String> dropDownMenu;

    public void setData(User user){
        for(Album album : user.albums){
            dropDownMenu.getItems().add(album.getAlbumName());
        }
    }

    public String getAlbumSelected(){
        return dropDownMenu.getValue();
    }
}
