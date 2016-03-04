package com.meidebi.app.service.bean;


import com.orm.dsl.Table;


@Table(name = "BannerBean")
public class BannerBean {
	private String id;
	private String imgUrl;
	private String title;
    private String link;

    public String getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
