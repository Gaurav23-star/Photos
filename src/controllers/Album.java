package src.controllers;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;


/*
    Group 64

    Authors
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */

public class Album implements Serializable {
    private String albumName;
    private String username;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private ArrayList<Photo> photosInAlbum = new ArrayList<>();

    public int getAlbumSize() {
        return photosInAlbum.size();
    }


    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void addPhoto(Photo photo){
        photosInAlbum.add(photo);
    }

    public ArrayList<Photo> getPhotosInAlbum(){
        return photosInAlbum;
    }

    public void removePhoto(Photo photo){
        photosInAlbum.remove(photo);
    }

    public Photo getAlbumThumnail(){
        if(photosInAlbum.size() == 0){
            return null;
        }else {
            return photosInAlbum.get(0);
        }
    }

    public Photo getPhotoWithUrl(String url){
        for(Photo photo : this.photosInAlbum){
            if(photo.getImagSrc().equalsIgnoreCase(url)){
                return photo;
            }
        }
        return null;

    }

    public String getDateRange(){
        int lowest = 0;
        int highest = 0;
        if(photosInAlbum.size() == 0){
            return null;
        }

        for(Photo photo : photosInAlbum){
            if(photo.getLastModifiedDate() < photosInAlbum.get(lowest).getLastModifiedDate()){
                lowest = photosInAlbum.indexOf(photo);
            }else{
                if(photo.getLastModifiedDate() > photosInAlbum.get(highest).getLastModifiedDate()){
                    highest = photosInAlbum.indexOf(photo);
                }
            }
        }
        return photosInAlbum.get(lowest).getPhotoDate().substring(0,17) + " to " + photosInAlbum.get(highest).getPhotoDate().substring(0,17);

    }
}
