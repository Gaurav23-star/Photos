package src.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Optional;
import java.util.ResourceBundle;

public class tagsController implements Initializable {

    @FXML
    private Button addTagButton;

    @FXML
    private Button editTagButton;

    @FXML
    private Button removeTagButton;

    @FXML
    private ChoiceBox<String> tagChoiceBox;

    @FXML
    private ListView<String> tagListView;

    @FXML
    private Label tagTitle;

    @FXML
    private TextField tagValueTextField;
    @FXML private TextField tagTextField;

    private Photo currentPhoto = null;
    private User currentUser = null;
    private Hashtable<String, ArrayList<String>> tags = null;

    private boolean isEditing = false;
    private String valueBeforeEditing;

    EventHandler<MouseEvent> handler = this::eventHandlerOn;
    EventHandler<MouseEvent> handler2 = this::eventHandlerOff;



    public void manageTags(Photo photo, Dialog<ButtonType> dialog, User user, ListView<String> listView){
        currentPhoto = photo;
        currentUser = user;
        tags = photo.getTagsHashTable();

        for(String userTags : user.getAvailableTags()){
            tagChoiceBox.getItems().add(userTags);
        }

        for(String key : tags.keySet()){
            String tag = key.toString();
            if(tags.get(key).size() > 1){
                for(String value : tags.get(key)){
                    String val = value;
                    tagListView.getItems().add(tag + " : " + val);
                }
            }
            else{
                String val = tags.get(key).toString();
                val = val.substring(1, val.length()-1);
                tagListView.getItems().add(tag + " : " + val);
            }

        }
        tagChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(tagChoiceBox.getValue().equals("other")){
                    tagTextField.setVisible(true);
                }
                if(tagChoiceBox.getValue().equals("location") || tagChoiceBox.getValue().equals("person")){
                    tagTextField.setVisible(false);
                }
            }
        });


        dialog.setTitle("Manage Tags");
        if(tagListView != null && tagListView.getItems().size() > 0){
            tagListView.getSelectionModel().selectFirst();
            tagListView.requestFocus();
        }
        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if(clickedButton.get() == ButtonType.APPLY){
            listView.getItems().clear();
            for(String key : tags.keySet()){
                String value = tags.get(key).toString();
                value = value.substring(1,value.length()-1);
                listView.getItems().add(key + " : " + value);
            }
        }


    }

    public void addNewTag(ActionEvent actionEvent) {
        String tag = null;
        if(tagChoiceBox.getValue().equals("other")){
            tag = tagTextField.getText();
            currentUser.addTag(tag);

        }
        else{
                tag = tagChoiceBox.getValue();

        }
        String newTagValue = tagValueTextField.getText();

        if(tags != null){
            assert tag != null;
            if(tags.containsKey(tag.toLowerCase().trim())){
                if(tags.get(tag).contains(newTagValue.toLowerCase().trim())){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.getDialogPane().setStyle("-fx-background-color: #222831");
                    alert.setTitle("Warning");
                    alert.setHeaderText("Tag: " + tag + " with a value : " + newTagValue +  " already exists!");
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if(buttonType.get() == ButtonType.OK){
                        if(!isEditing){
                            tagValueTextField.clear();
                            tagTextField.clear();
                        }
                        return;
                    }
                }
            }
        }


        if(currentPhoto !=null){
            assert tag != null;
            if(tag.equals("location") && tags != null){
                if(tags.containsKey("location")){
                    tags.get("location").set(0, newTagValue.toLowerCase().trim());
                    for(String items : tagListView.getItems()){
                        if(items.toString().contains(tag.trim())){
                            int index = tagListView.getItems().indexOf(items);
                            tagListView.getItems().remove(index);
                            tagListView.getItems().add(index,tag + " : " + newTagValue);
                            break;
                        }
                    }
                }
                else{
                    if(isEditing){
                        tags.get(tag.toLowerCase()).remove(valueBeforeEditing);
                        tags.get(tag.toLowerCase()).add(newTagValue.toLowerCase().trim());
                        isEditing = false;
                        tagChoiceBox.setDisable(false);
                        tagTextField.setDisable(false);
                        tagListView.getItems().add(tag.trim() + " : " + newTagValue.trim());
                    }
                    else{
                        currentPhoto.addNewTag(tag.toLowerCase().trim(), newTagValue.toLowerCase().trim());
                        tagListView.getItems().add(tag.trim() + " : " + newTagValue.trim());
                    }

                }
            }else{
                if(isEditing){
                    tags.get(tag.toLowerCase()).remove(valueBeforeEditing);
                    tags.get(tag.toLowerCase()).add(newTagValue.toLowerCase().trim());
                    isEditing = false;
                    tagChoiceBox.setDisable(false);
                    tagTextField.setDisable(false);
                    tagListView.getItems().add(tag.trim() + " : " + newTagValue.trim());

                }else{
                    currentPhoto.addNewTag(tag.toLowerCase().trim(), newTagValue.toLowerCase().trim());
                    tagListView.getItems().add(tag.trim() + " : " + newTagValue.trim());
                }

            }
        }
        tagListView.getSelectionModel().selectLast();
        tagTextField.clear();
        tagValueTextField.clear();
        tagValueTextField.requestFocus();

    }

    public void removeSelectedTag(ActionEvent actionEvent) {
        if(tagListView != null & tagListView.getItems().size() > 0){
            String toRemove = tagListView.getSelectionModel().getSelectedItem();
            String stringToCompare;
            for(String key : tags.keySet()){
                String[] tagSelected = toRemove.split(":");
                String tagToRemove = tagSelected[0].toLowerCase().trim();
                String valueToRemove = tagSelected[1].toLowerCase().trim();

                if(key.equals(tagToRemove)){
                   if (tags.get(key).size() > 1){
                       tags.get(key).remove(valueToRemove);
                   }
                   else{
                       tags.remove(key);
                   }
                   tagListView.getItems().remove(tagListView.getSelectionModel().getSelectedItem());
                   return;
                }
            }
        }
    }

    public void editSelectedTag(ActionEvent actionEvent) {
        if(tagListView != null & tagListView.getItems().size() > 0) {
            String toRemove = tagListView.getSelectionModel().getSelectedItem();
            String[] breakString = toRemove.split(":");
            String tagToEdit = breakString[0].trim();
            String tagValueToEdit = breakString[1].trim();

            if(tagChoiceBox.getItems().contains(tagToEdit) || (tagToEdit.equalsIgnoreCase("location") || tagToEdit.equalsIgnoreCase("person"))){
                tagChoiceBox.getSelectionModel().select(tagToEdit);
                tagChoiceBox.setDisable(true);
            } else{
                tagChoiceBox.getSelectionModel().select("other");
                tagTextField.setVisible(true);
                tagChoiceBox.setDisable(true);
                tagTextField.setText(tagToEdit);
                tagTextField.setDisable(true);
            }
/*
            if(tagToEdit.equalsIgnoreCase("location")){
                tagChoiceBox.getSelectionModel().select("location");
                tagChoiceBox.setDisable(true);
            }else if(tagToEdit.equalsIgnoreCase("person")){
                tagChoiceBox.getSelectionModel().select("person");
                tagChoiceBox.setDisable(true);
            }else{
                tagChoiceBox.getSelectionModel().select("other");
                tagTextField.setVisible(true);
                tagChoiceBox.setDisable(true);
                tagTextField.setText(tagToEdit);
                tagTextField.setDisable(true);
            }

 */

            valueBeforeEditing = tagValueToEdit;
            tagValueTextField.setText(tagValueToEdit);
            tagTextField.positionCaret(tagToEdit.length());
            tagValueTextField.requestFocus();
            tagListView.getItems().remove(toRemove);
            isEditing = true;

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTagButton.setOnMouseEntered(handler);
        addTagButton.setOnMouseExited(handler2);
        editTagButton.setOnMouseEntered(handler);
        editTagButton.setOnMouseExited(handler2);
        removeTagButton.setOnMouseEntered(handler);
        removeTagButton.setOnMouseExited(handler2);

    }
    public void eventHandlerOn(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#00ADB5");
    }
    public void eventHandlerOff(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#EEEEEE");
    }

}
