package src.controllers;

import java.io.*;
import java.util.ArrayList;

/*
    Group 64

    Authors
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */

public abstract class UserDataBaseController implements Serializable {
    private static ArrayList<User> usersList;
    public static void addUser(User user){
        usersList.add(user);

    }
    public static boolean removeUser(User user){
        if(usersList.contains(user)){
            usersList.remove(user);
            return true;
        }
        else return false;
    }
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
    public static void writeToDataBase() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("src/dataBase/userDataBase.dat");
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeObject(usersList);
    }
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
