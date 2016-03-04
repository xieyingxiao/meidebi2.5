package com.meidebi.app.service.bean.show;

import com.orm.dsl.Table;

import java.io.Serializable;

/**
 * Created by mdb-ii on 15-1-30.
 */
@Table(name = "draft")
public class Draft implements Serializable{
    private String id;
    private String picJson;
    private String content;
    private int mstatus;//0 草稿 1成功 2发送中
    private String uid;
    private String title;
    private String cover;
    private String alreadyUpload;

    public String getAlreadyUpload() {
        return alreadyUpload;
    }

    public void setAlreadyUpload(String alreadyUpload) {
        this.alreadyUpload = alreadyUpload;
    }

    private double progress = 0 ;

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public double getProgress() {
        return progress;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    private long creatTime = System.currentTimeMillis();

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicJson() {
        return picJson;
    }

    public String getContent() {
        return content;
    }


    public void setPicJson(String picJson) {
        this.picJson = picJson;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMstatus() {
        return mstatus;
    }

    public void setMstatus(int mstatus) {
        this.mstatus = mstatus;
    }
}
