package com.developerobaida.italytobanglalibro.models;

public class ModelCategory {

    String image,title,type;

    public ModelCategory(String image, String title,String type) {
        this.image = image;
        this.title = title;
        this.type = type;
    }

    public String getType() {
        return type;
    }


    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

}
