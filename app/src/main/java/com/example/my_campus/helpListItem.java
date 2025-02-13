package com.example.my_campus;

public class helpListItem {
    public String question;
    public String askedBy;
    public String askedTime;
    public String documentId;

    helpListItem(String question, String askedByName, String askedTime, String documentId){
        this.question = question;
        this.askedBy = askedByName;
        this.askedTime = askedTime;
        this.documentId = documentId;
    }
}
