package com.example.my_campus;

public class routineListItem {
    private String name;
    private int file;


    public routineListItem(String name, int file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }
    public int getFile() { return file; }
}
