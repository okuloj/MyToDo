package com.example.mytodo;

import java.io.Serializable;

public class CongViec implements Serializable {
    private int id;
    private int userid;
    private String title;
    private String time;

    public CongViec(int id, int userid, String title, String time) {
        this.id = id;
        this.userid = userid;
        this.title = title;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

