package com.example.eatitapp.Model;
//class for food object, will hold food data from JSON of firebase DB
public class Food {
    private String Name;
    private String Image;
    private String Description;
    private String Price;
    private String MenuId;
    //constructor
    public Food(String name, String image, String description, String price, String menuId) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        MenuId = menuId;
    }
    //no-arg constructor
    public Food(){

    }

    public String getName() {
        return Name;
    }

    public String getImage() {
        return Image;
    }

    public String getDescription() {
        return Description;
    }

    public String getPrice() {
        return Price;
    }

    public String getMenuId() {
        return MenuId;
    }
}
