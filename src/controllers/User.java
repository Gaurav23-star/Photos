package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class User implements Serializable {
    private ArrayList<String> availableTags = new ArrayList<>();

    public ArrayList<String> getAvailableTags() {
        return availableTags;
    }

    public void addTag(String tag) {
        this.availableTags.add(tag);
    }

    private String name;

    public ArrayList<Album> albums = new ArrayList<>();

    //public ArrayList<Album> albums = new ArrayList<>();

    public String getName(){
        return name;
    }
    public void setName(String name){ this.name = name;}

    public void addAlbum(Album album){
        albums.add(album);
    }

    public int getTotalAlbums(){
        return albums.size();
    }

    public Album getAlbumWithName(String albumName){
        for(Album album : albums){
            if(album.getAlbumName().equalsIgnoreCase(albumName)){
                return album;
            }
        }
        return null;
    }

    public Photo checkForDuplicatePhotoInDifferentAlbums(String uri){

        for(Album album : albums){
            for(Photo photo : album.getPhotosInAlbum()){
                if(photo.getImagSrc().equalsIgnoreCase(uri)){
                    return photo;
                }
            }
        }
        return null;
    }

    public ArrayList<Photo> getPhotosInRange(LocalDate startDate, LocalDate endDate){

        ArrayList<Photo> photosInRange = new ArrayList<>();
        for(Album album : albums){
            for(Photo photo : album.getPhotosInAlbum()){
                LocalDate filedate = Instant.ofEpochMilli(photo.getLastModifiedDate()).atZone(ZoneId.systemDefault()).toLocalDate();
                if(startDate == null && endDate != null){

                    if(filedate.isBefore(endDate)){
                        if(!photosInRange.contains(photo)){
                            photosInRange.add(photo);
                        }
                    }
                }
                else if(startDate != null && endDate == null){

                    if(filedate.isAfter(startDate)){
                        if(!photosInRange.contains(photo)){
                            photosInRange.add(photo);
                        }
                    }

                }
                else if(filedate.isAfter(startDate) && filedate.isBefore(endDate)){

                    if(!photosInRange.contains(photo)){
                        photosInRange.add(photo);
                    }
                }

            }
        }
        return photosInRange;

    }




}
