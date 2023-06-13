package com.example.productivity;

public class PdfItem {
    private String fileName;
    private String downloadUrl;

    public PdfItem(String fileName, String downloadUrl) {
        this.fileName = fileName;
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}

