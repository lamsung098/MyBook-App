package com.mybook.main.object;

import android.net.Uri;

public class Book {
    private String id;
    private String title;
    private String description;
    private String category;
    private String pdfURL;
    private long timestamp;

    public Book() {
    }

    public Book(String id, String title, String description, String category, String pdfURL, long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.pdfURL = pdfURL;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPdfURL() {
        return pdfURL;
    }

    public void setPdfURL(String pdfURL) {
        this.pdfURL = pdfURL;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
