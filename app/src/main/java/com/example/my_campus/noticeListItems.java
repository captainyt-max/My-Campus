package com.example.my_campus;

public class noticeListItems {
    private String fileName;
    private String uploadDate;
    private String downloadUrl;
    private String documentId;
    private String uploadTime;
    private String upladedBy;
    private String fileSize;

    public noticeListItems(String fileName, String uploadDate, String downloadUrl, String documentId, String uploadTime, String uploadedBy, String fileSize){
        this.fileName = fileName;
        this.uploadDate = uploadDate;
        this.downloadUrl = downloadUrl;
        this.documentId = documentId;
        this.uploadTime = uploadTime;
        this.upladedBy = uploadedBy;
        this.fileSize = fileSize;
    }

    public String getFileName(){
        return fileName;
    }

    public String getUploadDate(){
        return uploadDate;
    }

    public String getDownloadUrl(){
        return downloadUrl;
    }

    public String getDocumentId(){ return documentId;}

    public String getUploadTime(){
        return uploadTime;
    }

    public String getUploadedBy(){
        return upladedBy;
    }

    public String getFileSize(){
        return fileSize;
    }

}
