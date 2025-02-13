package com.example.my_campus;


public class campusActivityItem {
    private String senderEmail;
    private String messageBody;
    private String sentTime;
    private String docID;

    public campusActivityItem(String senderEmail, String messageBody, String sentTime, String docID) {
        this.senderEmail = senderEmail;
        this.messageBody = messageBody;
        this.sentTime = sentTime;
        this.docID = docID;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getSentTime() {
        return sentTime;
    }

    public String getDocID(){
        return docID;
    }


}
