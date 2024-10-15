package com.example.my_campus;

public class ListItem {
    private String name;
    private int icon;
    private String designation;
    private String phone;
    private String email;

    public ListItem(String name, int icon, String designation, String phone, String email) {
        this.name = name;
        this.icon = icon;
        this.designation = designation;
        this.phone= phone;
        this.email= email;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }
    public String getDesignation() {
        return designation;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }

}
