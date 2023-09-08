package src.controllers;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class represents the album. Stores the Album name, Username which albums belong to,
 * and list storing all the photos on the album.
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

    /**
     * Returns a photo to be represented as a thumbnail.
     * @return
     */
    public Photo getAlbumThumnail(){
        if(photosInAlbum.size() == 0){
            return null;
        }else {
            return photosInAlbum.get(0);
        }
    }

    /**
     * Returns the photo with passed parameter from the photos list
     * @param url
     * @return
     */
    public Photo getPhotoWithUrl(String url){
        for(Photo photo : this.photosInAlbum){
            if(photo.getImagSrc().equalsIgnoreCase(url)){
                return photo;
            }
        }
        return null;

    }

    /**
     * Returns the date range of photos in the album.
     * @return
     */
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
