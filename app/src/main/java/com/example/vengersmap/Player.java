package com.example.vengersmap;

import java.util.ArrayList;

public class Player {

    String email;
    String artName;
    ArrayList<Artifact> artifacts;


    Player(){}

    Player(String email, String artName){
        this.email = email;
        this.artName = artName;

    }
    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
