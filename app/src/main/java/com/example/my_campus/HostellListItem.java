package com.example.my_campus;

public class HostellListItem {
    private String name;
    private int icon;
    private String designation;
    private String phone;

    public HostellListItem(String name, int icon, String designation, String phone) {
        this.name = name;
        this.icon = icon;
        this.designation = designation;
        this.phone=phone;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public String getDesignation(){ return designation; }

    public String getPhone() {
        return phone;
    }
}
