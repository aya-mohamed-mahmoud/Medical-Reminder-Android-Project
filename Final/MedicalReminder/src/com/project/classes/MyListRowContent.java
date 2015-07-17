package com.project.classes;

/**
 * Created by ahmedrashad on 3/10/15.
 */
public class MyListRowContent {

    String image;
    String header;
    String description;


    public  MyListRowContent() {

    }
    public MyListRowContent(String image, String header, String description){
        this.image = image;
        this.header = header;
        this.description = description;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}