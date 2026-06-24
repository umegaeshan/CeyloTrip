package com.example.ceylotrip;

public class PlaceModel {
    String name, description, imageUrl, price;

    public PlaceModel(String name, String description, String imageUrl, String price) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getPrice() { return price; }
}