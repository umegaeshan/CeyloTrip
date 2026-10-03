package com.example.ceylotrip;

import com.google.firebase.firestore.Exclude;

public class BookingModel {

    // Database එකේ Document ID එක තියාගන්න (මේක Database එකට යවන්නේ නෑ, App එකේ වැඩ වලට විතරයි)
    @Exclude
    private String documentId;

    String packageName;
    String travelDate;
    String status;
    int totalPrice;

    // Edit කරන්න අවශ්‍ය වෙන අනිත් දත්ත ටික
    int adults;
    int children;
    boolean hasLocalGuide;
    boolean hasTransport;

    public BookingModel() {
    }

    // Document ID එකට Getters සහ Setters
    @Exclude
    public String getDocumentId() { return documentId; }
    @Exclude
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    // අනිත් Getters
    public String getPackageName() { return packageName; }
    public String getTravelDate() { return travelDate; }
    public String getStatus() { return status; }
    public int getTotalPrice() { return totalPrice; }
    public int getAdults() { return adults; }
    public int getChildren() { return children; }
    public boolean isHasLocalGuide() { return hasLocalGuide; }
    public boolean isHasTransport() { return hasTransport; }
}