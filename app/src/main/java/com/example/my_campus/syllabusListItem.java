package com.example.my_campus;

public class syllabusListItem {
    private String name;
    private int file;


    public syllabusListItem(String name, int file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }
    public int getFile() { return file; }
}
