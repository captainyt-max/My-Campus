package com.example.my_campus;

public class HostellListItem {
    private String name;
    private String iconUrl; // Changed from int to String
    private String designation;
    private String phoneNumber;

    public HostellListItem(String name, String iconUrl, String designation, String phoneNumber) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.designation = designation;
        this.phoneNumber = phoneNumber;
    }

    public String getName() { return name; }
    public String getIconUrl() { return iconUrl; }
    public String getDesignation() { return designation; }
    public String getPhoneNumber() { return phoneNumber; }
}
