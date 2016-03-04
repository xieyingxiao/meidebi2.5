package com.meidebi.app.service.bean.show;

/**
 * Created by mdb-ii on 15-1-30.
 */
public class UploadToken {
    private String token;
    private String key;
    private String domain;



    private String fileUrl;


    public String getFileUrl() {
        fileUrl = domain+key;
        return fileUrl;
    }

    public String getToken() {
        return token;
    }

    public String getKey() {
        return key;
    }

    public String getDomain() {
        return domain;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
