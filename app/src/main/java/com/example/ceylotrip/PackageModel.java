package com.example.ceylotrip;

public class PackageModel {
    String title;
    String duration;
    String price;
    String imageUrl; // අපි Internet එකෙන් පින්තූර load කරන නිසා String පාවිච්චි කරමු

    // Constructor එක (Data ඇතුළත් කරන්න)
    public PackageModel(String title, String duration, String price, String imageUrl) {
        this.title = title;
        this.duration = duration;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getters (Data එළියට ගන්න)
    public String getTitle() { return title; }
    public String getDuration() { return duration; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}