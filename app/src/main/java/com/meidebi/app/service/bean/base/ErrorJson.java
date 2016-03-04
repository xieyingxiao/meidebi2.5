package com.meidebi.app.service.bean.base;

/**
 * Created by Administrator on 2016/3/3.
 */
public class ErrorJson {
    private ErrorLink data;
    private String info;
    private int status;
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public ErrorLink getData() {
        return data;
    }

    public void setData(ErrorLink data) {
        this.data = data;
    }
}
