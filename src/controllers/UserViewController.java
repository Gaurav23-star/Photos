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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
    Group 64

    @Author
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */

/**
 * Class controls the userview screen.
 */

public class UserViewController implements Initializable {


    @FXML
    private Label greetUser;
    @FXML
    private VBox albumLayoutVbox;

    public static String userName;

    @FXML Button logoutButton;
    @FXML Button searchButton;
    @FXML Button newAlbumButton;
    @FXML Button searchByDateButton;
    EventHandler<MouseEvent> handler = this::eventHandlerOn;
    EventHandler<MouseEvent> handler2 = this::eventHandlerOff;
    public static User currentUser;


    /**
     * Method to log out the user and load the home screen of the application.
     * @param actionEvent
     * @throws IOException
     */

    public void logout(ActionEvent actionEvent) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("../FXMLFiles/Main.fxml"));
        Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    /**
     * Method to initialize all event listeners on buttons clicking.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        greetUser.setText("Hello, " + userName);

        User user = UserDataBaseController.getUserWithName(userName);
        currentUser = user;
        createUserAlbumList();
        searchButton.setOnMouseEntered(handler);
        searchButton.setOnMouseExited(handler2);
        logoutButton.setOnMouseEntered(handler);
        logoutButton.setOnMouseExited(handler2);
        newAlbumButton.setOnMouseEntered(handler);
        newAlbumButton.setOnMouseExited(handler2);
        searchByDateButton.setOnMouseEntered(handler);
        searchByDateButton.setOnMouseExited(handler2);


    }

    /**
     * Method creates a new album list view with all the albums user currently has.
     */
    public void createUserAlbumList(){
            try {
                for(int i = 0; i < currentUser.getTotalAlbums(); i++){
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/albumLayout.fxml"));
                    HBox albumBox = fxmlLoader.load();
                    albumLayoutController albumLayoutController = fxmlLoader.getController();
                    albumLayoutController.setData(currentUser.albums.get(i));
                    albumLayoutVbox.getChildren().add(albumBox);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }

    /**
     * Handles the mouse hover event on buttons
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
     * Method load the dialog box to create new album.
     * @param actionEvent
     * @throws IOException
     */
    public void createNewAlbum(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/createNewAlbum.fxml"));
        DialogPane albumlistDialog = fxmlLoader.load();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(albumlistDialog);
        createNewAlbumController controller = fxmlLoader.getController();
        controller.createNewAlbum(actionEvent,userName,dialog);

        albumLayoutVbox.getChildren().clear();
        initialize(null,null);
    }

    public void refresh(){
        albumLayoutVbox.getChildren().clear();
        createUserAlbumList();
    }

    /**
     * Method loads the dialog box to search by date.
     * @param actionEvent
     * @throws IOException
     */
    public void searchByDate(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/searchByDateDialog.fxml"));
        DialogPane dateRangePicker = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dateRangePicker);
        SeachByDateController seachByDateController = fxmlLoader.getController();
        seachByDateController.searchByDate(dialog, userName, actionEvent);
    }

    /**
     * Method loads the dialog box to search by tags.
     * @param actionEvent
     * @throws IOException
     */
    public void searchByTags(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLFiles/searchBYTagDialog.fxml"));
        DialogPane dateRangePicker = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dateRangePicker);
        searchByTagController searchByTagController = fxmlLoader.getController();
        searchByTagController.searchByTag(dialog, userName, actionEvent);
    }
}

