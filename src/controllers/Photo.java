package src.controllers;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

/*
    Group 64

    Authors
    gjp81: Gauravkumar Patel
    sm2246: Sami Munir

 */

public class Photo implements Serializable {
    private Hashtable<String, ArrayList<String>> tags = new Hashtable<>();

    private String imagSrc;
    private String albumName;
    private String name;
    private String photoDate;
    private String captions = "captions";

    private long lastModifiedDate;

    public void addNewTag(String key, String value){
        if(tags.containsKey(key)){
            tags.get(key).add(value);
        }
        else{
            tags.put(key, new ArrayList<>());
            tags.get(key).add(value);
        }
    }

    public boolean containsTagWithValue(String tag, String value){
        if(tags.containsKey(tag)){
            System.out.println(tags.get(tag).toString());
            if(tags.get(tag).contains(value)){
                System.out.println( tag + " contains " + value);
                return true;
            }
        }
        return false;
    }

    public void removeTag(String key, String value){
        if(tags.containsKey(key) && tags.get(key).size() > 1){
            tags.get(key).remove(value);
        }
        else{
            tags.remove(key);
        }
    }

    public String getTags(){
        return tags.toString();
    }
    public Hashtable<String, ArrayList<String>> getTagsHashTable(){
        return this.tags;
    }

    public long getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(long lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCaptions() {
        return captions;
    }

    public void setCaptions(String captions) {
        this.captions = captions;
    }

    public String getPhotoDate() {
        return photoDate;
    }

    public void setPhotoDate(String photoDate) {
        this.photoDate = photoDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagSrc() {
        return imagSrc;
    }

    public void setImagSrc(String imagSrc) {
        this.imagSrc = imagSrc;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
