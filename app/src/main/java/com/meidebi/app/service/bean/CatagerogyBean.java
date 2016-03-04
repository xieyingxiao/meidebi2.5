package com.meidebi.app.service.bean;

/**
 * Created by mdb-ii on 15-2-9.
 */
public class CatagerogyBean {

    private String id;
    private String androidicon;
    private String name;
    private int setPush = 0;

    public int getSetPush() {
        return setPush;
    }

    public void setSetPush(int setPush) {
        this.setPush = setPush;
    }

    public String getId() {
        return id;
    }

    public String getAndroidicon() {
        return androidicon;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAndroidicon(String androidicon) {
        this.androidicon = androidicon;
    }

    public void setName(String name) {
        this.name = name;
    }
}