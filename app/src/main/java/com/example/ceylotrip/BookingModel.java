package com.example.ceylotrip;

public class BookingModel {
    // Firebase එකේ සේව් කරපු නම් වලින්ම මේවා තියෙන්න ඕනේ
    String packageName;
    String travelDate;
    String status;
    int totalPrice;

    // Firebase එකට Data ගන්න මේ හිස් (Empty) Constructor එක අනිවාර්යයෙන්ම ඕනේ!
    public BookingModel() {
    }

    // සාමාන්‍ය Constructor එක
    public BookingModel(String packageName, String travelDate, String status, int totalPrice) {
        this.packageName = packageName;
        this.travelDate = travelDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    // Getters (Data එළියට ගන්න)
    public String getPackageName() { return packageName; }
    public String getTravelDate() { return travelDate; }
    public String getStatus() { return status; }
    public int getTotalPrice() { return totalPrice; }
}