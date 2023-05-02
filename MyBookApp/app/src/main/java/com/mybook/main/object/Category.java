package com.mybook.main.object;

public class Category {
    private String id;
    private String category;
    private long timestamp;

    public Category() {
    }

    public Category(String id, String category, long timestamp) {
        this.id = id;
        this.category = category;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
