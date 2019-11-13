package com.example.vengersmap;

import java.util.ArrayList;

public class Player {

    String email;
    ArrayList<Artifact> collection;

    Player(){}

    Player(String email, ArrayList<Artifact> collection){
        this.email = email;
        this.collection = collection;
    }
    public ArrayList<Artifact> getCollection() {
        return collection;
    }

    public void setCollection(ArrayList<Artifact> collection) {
        this.collection = collection;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
