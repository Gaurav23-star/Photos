package src.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
    Group 64

    @Author
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */

/**
 * Class controls the search by tag event.
 * Displays the dialog box to select tags user wants to search then,
 * Gets the search results and displays them.
 */

public class searchByTagController implements Initializable {

    @FXML
    private ChoiceBox<String> choiceBox1;

    @FXML
    private ChoiceBox<String> choiceBox2;

    @FXML
    private ChoiceBox<String> combinationChoiceBox;

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;
    @FXML
    private ListView<String> listViewSearches;
    @FXML
    private Button addToSearchButton;

    private ArrayList<String> tagsToSearch = new ArrayList<>();
    private ArrayList<Photo> searchResults = new ArrayList<>();

    private User currentUser;
    EventHandler<MouseEvent> handler = this::eventHandlerOn;
    EventHandler<MouseEvent> handler2 = this::eventHandlerOff;

    public void searchByTag(Dialog<ButtonType> dialog, String userName, ActionEvent actionEvent) throws IOException {
        currentUser = UserDataBaseController.getUserWithName(userName);

        for(String tags : currentUser.getAvailableTags()){
            choiceBox1.getItems().add(tags);
            choiceBox2.getItems().add(tags);
        }
        combinationChoiceBox.getItems().add("N/A");
        combinationChoiceBox.getItems().add("AND");
        combinationChoiceBox.getItems().add("OR");



        combinationChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(combinationChoiceBox.getValue() != "N/A"){

                    choiceBox2.setVisible(true);
                    textField2.setVisible(true);
                }
                else{

                    choiceBox2.setVisible(false);
                    textField2.setVisible(false);
                }
            }
        });
        Optional<ButtonType> clickButton = dialog.showAndWait();

        if(clickButton.get() == ButtonType.APPLY){
            searchResults = currentUser.getPhotosWithTags(tagsToSearch);

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/search.fxml"));
            AnchorPane root = fxmlLoader.load();
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            mainStage.setScene(scene);
            searchResultsController searchResultsController = fxmlLoader.getController();
            searchResultsController.start(searchResults);
            mainStage.setResizable(false);
            mainStage.show();

        }


    }

    public void addTagToSearchList(ActionEvent actionEvent) {
        String tag1 = choiceBox1.getValue();
        String tag1Value = textField1.getText();
        String tag2 = null;
        String tag2Value = null;

        if(combinationChoiceBox.getSelectionModel().getSelectedItem() != null && !combinationChoiceBox.getSelectionModel().getSelectedItem().equals("N/A")){
            if(combinationChoiceBox.getSelectionModel().getSelectedItem().equals("AND") || combinationChoiceBox.getSelectionModel().getSelectedItem().equals("OR")){
                tag2 = choiceBox2.getValue();
                tag2Value = textField2.getText();
                listViewSearches.getItems().add(tag1 + ": " + tag1Value + " " + combinationChoiceBox.getSelectionModel().getSelectedItem() + " " + tag2 + ": " + tag2Value);
                tagsToSearch.add(tag1.toLowerCase().trim() + " " + tag1Value.toLowerCase().trim() + " " + combinationChoiceBox.getSelectionModel().getSelectedItem() + " " + tag2.toLowerCase().trim() + " " + tag2Value.toLowerCase().trim());
            }
        }
        else{
            if(combinationChoiceBox.getSelectionModel().getSelectedItem() == null || combinationChoiceBox.getSelectionModel().getSelectedItem().equals("N/A")){

                listViewSearches.getItems().add(tag1 + ": " + tag1Value);
                tagsToSearch.add(tag1.toLowerCase().trim() + " " + tag1Value.toLowerCase().trim() + " " + combinationChoiceBox.getSelectionModel().getSelectedItem() + " " + tag2 + " " + tag2Value);
            }
        }


        choiceBox1.getSelectionModel().clearSelection();
        choiceBox2.getSelectionModel().clearSelection();
        textField1.clear();
        textField2.clear();
        combinationChoiceBox.getSelectionModel().clearSelection();
        choiceBox2.setVisible(false);
        textField2.setVisible(false);

    }

    public void eventHandlerOn(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#00ADB5");
    }
    public void eventHandlerOff(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#EEEEEE");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addToSearchButton.setOnMouseEntered(handler);
        addToSearchButton.setOnMouseExited(handler2);

    }
}
