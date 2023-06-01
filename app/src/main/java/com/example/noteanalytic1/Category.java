package com.example.noteanalytic1;

import java.io.Serializable;

public class Category implements Serializable {
    String id;
    String name;

    private Category() {}

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String id, String name, String cat) {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
