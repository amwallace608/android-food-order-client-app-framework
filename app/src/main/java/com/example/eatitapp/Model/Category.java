package com.example.eatitapp.Model;

public class Category {
    private String Name;
    private String Image;

    public Category(){

    }

    public Category(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public String getName() {
        return Name;
    }
}
