package com.example.noteanalytic1;


import java.io.Serializable;

public class Note implements Serializable {
    private String id;
    private String name;
    private String category;

    private Note() {}

    public Note(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
