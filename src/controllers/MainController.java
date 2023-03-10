package src.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;



/**
 * Main controller. Handles the login and loads appropriate user screen based on username input.
 * Controls the initial starting of the application and creating stock user again, if it is deleted.
 */

public class MainController implements Initializable, Serializable {

    @FXML
    TextField userNameTextField;
    public static void start() throws IOException {
        createStockUser();
    }


    @FXML Button logInButton;
    @FXML Button closeApplicationButton;
    @FXML private Label userDoesNotExistWarning;

    EventHandler<MouseEvent> handler = this::eventHandlerOn;
    EventHandler<MouseEvent> handler2 = this::eventHandlerOff;

    /**
     * Method loads admin screen.
     * @param event
     * @throws IOException
     */
    public void loadAdminScreen(ActionEvent event) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("../FXMLFiles/Admin.fxml"));
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    /**
     * Method handles the login event. Calls appropriate method to load screens based on input.
     * @param actionEvent
     * @throws IOException
     */
    public void logIn(ActionEvent actionEvent) throws IOException {

        String userNameText = userNameTextField.getText();
        if(userNameText.equalsIgnoreCase("admin")){
            loadAdminScreen(actionEvent);
        }
        else{
            loadUserScreen(actionEvent,userNameText);
        }
    }

    /**
     * Method loads user screen with all user data stored in the database.
     * @param actionEvent
     * @param userName
     * @throws IOException
     */
    private void loadUserScreen(ActionEvent actionEvent, String userName) throws IOException {
        if(UserDataBaseController.getUserWithName(userName) == null){
            userDoesNotExistWarning.setVisible(true);
            userNameTextField.clear();
            userNameTextField.positionCaret(0);
            userNameTextField.requestFocus();

        }else{
            userDoesNotExistWarning.setVisible(false);
            UserViewController.userName = userName;
            AnchorPane root = FXMLLoader.load(getClass().getResource("../FXMLFiles/UserView.fxml"));
            Stage mainStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);

            mainStage.setScene(scene);
            mainStage.setResizable(false);
            mainStage.show();
        }

    }


    /**
     * Method closes the application and saves all the data.
     * @param actionEvent
     * @throws IOException
     */
    public void closeApplication(ActionEvent actionEvent) throws IOException {
        UserDataBaseController.writeToDataBase();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Method creates stock user if it is deleted from the database.
     * @throws IOException
     */
    public static void createStockUser() throws IOException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
        long lastModifiedDate;

        //can not get the last modified date of the file from the stock photos in album with relative path,
        //so I have gotten the date from the absolute path of photos in my computer,
        //so do not change it because this path won't be available on bitbucket

        long lastModifiedDatePhoto1 = 1669313263472L;
        long lastModifiedDatePhoto2 = 1669313182737L;
        long lastModifiedDatePhoto3 = 1669313227743L;
        long lastModifiedDatePhoto4 = 1669313195156L;
        long lastModifiedDatePhoto5 = 1669313169370L;
        long lastModifiedDatePhoto6 = 1669313254523L;
        long lastModifiedDatePhoto7 = 1669313214096L;




        User stockUser = new User();
        stockUser.addTag("location");
        stockUser.addTag("person");
        stockUser.addTag("other");
        stockUser.setName("Stock");
        Album stockAlbum = new Album();
        stockAlbum.setUsername(stockUser.getName());
        stockAlbum.setAlbumName("Stock");


        Photo photo1 = new Photo();
        photo1.setName("photo1");
        photo1.setImagSrc("/data/img1.jpg");
        photo1.setPhotoDate(simpleDateFormat.format(lastModifiedDatePhoto1));
        photo1.setLastModifiedDate(lastModifiedDatePhoto1);


        Photo photo2 = new Photo();
        photo2.setName("photo2");
        photo2.setImagSrc("/data/img2.jpg");
        photo2.setPhotoDate(simpleDateFormat.format(lastModifiedDatePhoto2));
        photo2.setLastModifiedDate(lastModifiedDatePhoto2);


        Photo photo3 = new Photo();
        photo3.setName("photo3");
        photo3.setImagSrc("/data/img3.jpg");
        photo3.setPhotoDate(simpleDateFormat.format(lastModifiedDatePhoto3));
        photo3.setLastModifiedDate(lastModifiedDatePhoto3);

        Photo photo4 = new Photo();
        photo4.setName("photo4");
        photo4.setImagSrc("/data/img4.jpg");
        photo4.setPhotoDate(simpleDateFormat.format(lastModifiedDatePhoto4));
        photo4.setLastModifiedDate(lastModifiedDatePhoto4);

        Photo photo5 = new Photo();
        photo5.setName("photo5");
        photo5.setImagSrc("/data/img5.jpg");
        photo5.setPhotoDate(simpleDateFormat.format(lastModifiedDatePhoto5));
        photo5.setLastModifiedDate(lastModifiedDatePhoto5);

        Photo photo6 = new Photo();
        photo6.setName("photo6");
        photo6.setImagSrc("/data/img6.jpg");
        photo6.setPhotoDate(simpleDateFormat.format(lastModifiedDatePhoto6));
        photo6.setLastModifiedDate(lastModifiedDatePhoto6);

        Photo photo7 = new Photo();
        photo7.setName("photo7");
        photo7.setImagSrc("/data/img7.jpg");
        photo7.setPhotoDate(simpleDateFormat.format(lastModifiedDatePhoto7));
        photo7.setLastModifiedDate(lastModifiedDatePhoto7);


        stockAlbum.addPhoto(photo1);
        stockAlbum.addPhoto(photo2);
        stockAlbum.addPhoto(photo3);
        stockAlbum.addPhoto(photo4);
        stockAlbum.addPhoto(photo5);
        stockAlbum.addPhoto(photo6);
        stockAlbum.addPhoto(photo7);



        stockUser.addAlbum(stockAlbum);
        UserDataBaseController.addUser(stockUser);
    }

    /**
     * Handles the mouse hover events on the buttons
     * @param mouseEvent
     */
    public void eventHandlerOn(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#00ADB5");
    }
    public void eventHandlerOff(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color:#222831; -fx-text-fill:#EEEEEE");
    }


    /**
     * Method initializes the event listeners.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logInButton.setOnMouseEntered(handler);
        logInButton.setOnMouseExited(handler2);
        closeApplicationButton.setOnMouseEntered(handler);
        closeApplicationButton.setOnMouseExited(handler2);
    }


}
