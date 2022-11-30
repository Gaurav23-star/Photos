package src.controllers;

import java.io.*;
import java.util.ArrayList;

/**
    Group 64

    @Author
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */

/**
 * Class represents user data base and controls the database.
 */
public abstract class UserDataBaseController implements Serializable {
    private static ArrayList<User> usersList;
    public static void addUser(User user){
        usersList.add(user);

    }

    /**
     * Method to remove user from database.
     * @param user
     * @return true on successful removal of user
     */
    public static boolean removeUser(User user){
        if(usersList.contains(user)){
            usersList.remove(user);
            return true;
        }
        else return false;
    }

    /**
     * Method to get the user with given Username
     * @param userName
     * @return user with username
     */
    public static User getUserWithName(String userName){

        for(User user : usersList){
            if(user.getName().equalsIgnoreCase(userName)){
                return user;
            }
        }
        return null;
    }
    public static ArrayList<User> getUsersList(){
        return usersList;
    }

    /**
     * Method to Writes all the changes to the file and stores all the data when application is closed.
     * @throws IOException
     */
    public static void writeToDataBase() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("src/dataBase/userDataBase.dat");
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeObject(usersList);
    }

    /**
     * Method to read the data from previous saved session and populates data to restore the application's state to last session.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void readFromDataBase() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream;
        try{
            fileInputStream = new FileInputStream("src/dataBase/userDataBase.dat");
        } catch (Exception e) {
            usersList = new ArrayList<>();
            MainController.start();
            return;
        }
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        usersList = (ArrayList<User>) objectInputStream.readObject();
        if(UserDataBaseController.getUserWithName("stock") == null){
            MainController.start();
        }
        fileInputStream.close();
        objectInputStream.close();

    }

}
