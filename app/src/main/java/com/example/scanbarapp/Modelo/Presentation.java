package com.example.scanbarapp.Modelo;

import java.util.ArrayList;

public class Presentation {

    private String id;
    private String type;

    public Presentation() {

    }

    public Presentation(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
