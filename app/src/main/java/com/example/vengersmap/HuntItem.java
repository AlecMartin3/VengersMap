package com.example.vengersmap;


/**
 * Hunt class. Each hunt has a name, password, park and unique ID
 */
public class HuntItem {
    private String huntName;
    private String huntPassword;
    private String huntPark;
    private String id;



    public HuntItem() {}

    public HuntItem(String id, String huntName, String huntPassword, String huntPark){
        this.id = id;
        this.huntName = huntName;
        this.huntPassword = huntPassword;
        this.huntPark = huntPark;
    }
    public String getHuntName() {
        return huntName;
    }

    public void setHuntName(String huntName) {
        this.huntName = huntName;
    }

    public String getHuntPassword() {
        return huntPassword;
    }

    public void setHuntPassword(String huntPassword) {
        this.huntPassword = huntPassword;
    }
    public String getHuntPark() {
        return huntPark;
    }

    public void setHuntPark(String huntPark) {
        this.huntPark = huntPark;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
