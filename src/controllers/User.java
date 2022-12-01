package src.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

/**
    Group 64

    @Author
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */

/**
 * Class represents the user.
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

    /**
     * Method to check if the photo already exists in the user's state of application
     * @param uri
     * @return
     */
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

    /**
     * Method to get the all the photos that user has that are in the given range of dates.
     * @param startDate
     * @param endDate
     * @return List of photos in the date range
     */

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

    /**
     * Method to get the all the photos that user have with the given tags
     * @param searchTags
     * @return List of photos with searched tags.
     */
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

    /**
     * Helper method to search photos with tags.
     * @param tag
     * @param value
     * @return
     */
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

    /**
     * Helper method to search photos with tags
     * @param tag1
     * @param tag1Value
     * @param tag2
     * @param tag2Value
     * @param combination
     * @return
     */
    private ArrayList<Photo> photosWithTagCombination(String tag1, String tag1Value, String tag2, String tag2Value, String combination){
        ArrayList<Photo> matchingPhotos = new ArrayList<>();

        for(Album album : albums){
            for(Photo photo : album.getPhotosInAlbum()){
                Boolean tag1Combination = checkTagInPhoto(tag1, tag1Value, photo);
                Boolean tag2Combination = checkTagInPhoto(tag2, tag2Value, photo);
                if(combination.equalsIgnoreCase("and")){

                    if(tag1Combination && tag2Combination){

                        matchingPhotos.add(photo);
                    }
                }
                else if(combination.equalsIgnoreCase("or")){

                    if(tag1Combination || tag2Combination){

                        matchingPhotos.add(photo);
                    }
                }
            }
        }
        return matchingPhotos;
    }

    /**
     * Method to check if the photo contains the given tag
     * @param tag1
     * @param tag1Value
     * @param photo
     * @return
     */
    private boolean checkTagInPhoto(String tag1, String tag1Value, Photo photo){

        if(photo.containsTagWithValue(tag1, tag1Value)){
            return true;
        }
        return false;
    }

    /**
     * Method to check if the user has used given tag in the past, and it is available in the used tags list.
     * @param tag
     * @return boolean value based on whether tag exists or not.
     */
    public boolean containsTag(String tag){
        if(availableTags.contains(tag)){
            return true;
        }
        return false;

    }




}
