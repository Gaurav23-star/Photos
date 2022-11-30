package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
    Group 64

    @Author
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */


/**
 * Controller for the admin user screen.
 */

public class AdminController implements Initializable {
    @FXML
    ListView<String> userListView;

    @FXML
    private Button logoutButton;
    @FXML
    private Button addUserButton;
    @FXML
    private Button deleteUserButton;



    EventHandler<MouseEvent> handler = this::eventHandlerOn;
    EventHandler<MouseEvent> handler2 = this::eventHandlerOff;
    public static ObservableList<String> observableList = FXCollections.observableArrayList();

    /**
     * Method to react on logout request by admin. Loads the home screen of the application.
     * @param actionEvent
     * @throws IOException
     */

    public void logout(ActionEvent actionEvent) throws IOException {
        observableList.removeAll();
        userListView.getItems().clear();
        AnchorPane root = FXMLLoader.load(getClass().getResource("../FXMLFiles/Main.fxml"));
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        mainStage.setScene(scene);
        mainStage.show();

    }


    /**
     * Initialize method that loads all required data to present to the admin user.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(User user : UserDataBaseController.getUsersList()){
            observableList.add(user.getName());
        }
        observableList.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                userListView.setItems(observableList);
            }
        });

        userListView.setItems(observableList);
        userListView.requestFocus();
        if(observableList.size() > 0){
            userListView.getSelectionModel().selectFirst();
        }

        logoutButton.setOnMouseEntered(handler);
        logoutButton.setOnMouseExited(handler2);
        addUserButton.setOnMouseEntered(handler);
        addUserButton.setOnMouseExited(handler2);
        deleteUserButton.setOnMouseEntered(handler);
        deleteUserButton.setOnMouseExited(handler2);

    }

    /**
     * Handler to handle the mouse hover event on the buttons.
     * Chnages background color of the buttons.
     * @param mouseEvent
     */
    public void eventHandlerOn(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#00ADB5");
    }
    public void eventHandlerOff(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#EEEEEE");
    }


    /**
     * Method handles the adding new user request by the admin.
     * Adds the new user to the database with the given username.
     * @param actionEvent
     * @throws IOException
     */
    public void addNewUser(ActionEvent actionEvent) throws IOException {
        //load the dialoge box with albums drop down menu
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
        dialog.setTitle("Add User");

        VBox vBox = (VBox) albumlistDialog.getChildren().get(4);
        Label titleLable = (Label) vBox.getChildren().get(0);
        titleLable.setText("Add New User");
        HBox hbox = (HBox) albumlistDialog.getChildren().get(3);
        Label promptLable = (Label) hbox.getChildren().get(0);
        promptLable.setText("New Username");
        TextField textField = (TextField) hbox.getChildren().get(1);
        textField.setPromptText("username");
        textField.positionCaret(0);
        textField.requestFocus();

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if(clickedButton.get() == ButtonType.APPLY){
            String newUserName = textField.getText();

            if(newUserName != ""){
                if(checkForDuplicateUser(newUserName) != true){
                    User newUser = new User();
                    newUser.addTag("location");
                    newUser.addTag("person");
                    newUser.addTag("other");
                    newUser.setName(newUserName);
                    UserDataBaseController.addUser(newUser);
                    AdminController.observableList.add(newUserName);
                    userListView.getSelectionModel().selectLast();
                }
            }
        }

    }

    /**
     * Method called by add user to check if the user already exists with the new
     * intended username.
     * @param username
     * @return if the user already exists or not
     */
    public boolean checkForDuplicateUser(String username){

        for(User user : UserDataBaseController.getUsersList()){
            if(user.getName().equalsIgnoreCase(username)){
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes the selected user from the userdatabse.
     * @param actionEvent
     */
    public void deleteUser(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setStyle("-fx-background-color: #222831");
        alert.setTitle("Confirmation");
        alert.setHeaderText("Do you want to delete the user " + userListView.getSelectionModel().getSelectedItem() + "?");
        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get() == ButtonType.OK){
            String userNameSelected = userListView.getSelectionModel().getSelectedItem();
            User user = UserDataBaseController.getUserWithName(userNameSelected);
            UserDataBaseController.removeUser(user);
            observableList.remove(userNameSelected);

        }

    }
}
