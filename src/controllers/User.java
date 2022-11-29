package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

/*
    Group 64

    Authors
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */
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

    public ArrayList<Photo> getPhotosWithTags(ArrayList<String> searchTags){
        String tag1;
        String tag2;
        String tag1Value;
        String tag2Value;
        String combination;
        ArrayList<Photo> searchResults = new ArrayList<>();

        for(String search : searchTags){
            String[] split = search.split(" ");
            for(String string : split){
                System.out.println(string);
            }
            tag1 = split[0];
            tag2 = split[3];
            tag1Value = split[1];
            tag2Value = split[4];
            combination = split[2];
            ArrayList<Photo> combinationReturned = null;

            if(combination.equalsIgnoreCase("and") || combination.equalsIgnoreCase("or")){

                    combinationReturned = photosWithTagCombination(tag1, tag1Value, tag2, tag2Value, combination);
                    if(combinationReturned != null && combinationReturned.size() > 0) {
                        for(Photo photo : combinationReturned){
                            if(!searchResults.contains(photo)){
                                searchResults.add(photo);
                            }
                        }
                    }
            }
            else{
                combinationReturned = photosWithSingleTag(tag1, tag1Value);
                if(combinationReturned != null && combinationReturned.size() > 0){
                    for(Photo photo : combinationReturned){
                        if(!searchResults.contains(photo)){
                            searchResults.add(photo);
                        }
                    }
                }
            }

        }
        return searchResults;
    }

    private ArrayList<Photo> photosWithSingleTag(String tag, String value){
        ArrayList<Photo> matchingPhotos = new ArrayList<>();
        for(Album album : albums){
            for(Photo photo : album.getPhotosInAlbum()){
                if(checkTagInPhoto(tag, value, photo)){
                    matchingPhotos.add(photo);
                }
            }
        }
        return matchingPhotos;
    }

    private ArrayList<Photo> photosWithTagCombination(String tag1, String tag1Value, String tag2, String tag2Value, String combination){
        ArrayList<Photo> matchingPhotos = new ArrayList<>();

        for(Album album : albums){
            for(Photo photo : album.getPhotosInAlbum()){
                Boolean tag1Combination = checkTagInPhoto(tag1, tag1Value, photo);
                Boolean tag2Combination = checkTagInPhoto(tag2, tag2Value, photo);
                if(combination.equalsIgnoreCase("and")){
                    System.out.println("looking for and");
                    if(tag1Combination && tag2Combination){
                        System.out.println("found for and");
                        matchingPhotos.add(photo);
                    }
                }
                else if(combination.equalsIgnoreCase("or")){
                    System.out.println("looking for or");
                    if(tag1Combination || tag2Combination){
                        System.out.println("found for or");
                        matchingPhotos.add(photo);
                    }
                }
            }
        }
        return matchingPhotos;
    }

    private boolean checkTagInPhoto(String tag1, String tag1Value, Photo photo){
        System.out.println("looking for " + tag1 + tag1Value);
        if(photo.containsTagWithValue(tag1, tag1Value)){
            return true;
        }
        return false;
    }

    public boolean containsTag(String tag){
        if(availableTags.contains(tag)){
            return true;
        }
        return false;

    }




}
