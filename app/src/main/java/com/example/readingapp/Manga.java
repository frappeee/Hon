package com.example.readingapp;

public class Manga {
    private String title;
    private String imageURL;
    private String link;

    public Manga() {

    }

    public Manga(String title, String imageURL, String link) {
        this.title = title;
        this.imageURL = imageURL;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getLink() {
        return link;
    }
}
