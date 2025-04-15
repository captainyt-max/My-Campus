package com.example.my_campus;

public class ListItem {
    private String name;
    private String iconUrl;
    private String designation;
    private String phoneNumber;
    private String email;

    public ListItem(String name, String iconUrl, String designation, String phoneNumber, String email) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.designation = designation;
        this.phoneNumber = phoneNumber;
        this.email= email;
    }

    public String getName() {
        return name;
    }
    public String getIconUrl() {
        return iconUrl;
    }
    public String getDesignation() {
        return designation;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmail() {
        return email;
    }

}
