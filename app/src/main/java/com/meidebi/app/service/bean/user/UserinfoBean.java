package com.meidebi.app.service.bean.user;

import com.orm.dsl.Table;

@Table(name = "UserinfoBean")
public class UserinfoBean {
    private String nickname;
    private String id;
    private String authuserid;
    private String photo;
    private String level;
    private String contribution;//贡献值
    private String headImgUrl;
    private String score;//积分
    private String money;//铜币
    private int isSign;
    private String coins;// 积分
    private String totallevel;//等级
    private String copper;//铜币
    private String username;
    private int messagenum;
    private String name;
    private String userid;
    private String showdancount;
    private String linkcount;
    private String fancount;
    private int signtimes = 0;


    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSigntimes(int signtimes) {
        this.signtimes = signtimes;
    }

    public int getSigntimes() {
        return signtimes;
    }

    public void setShowdancount(String showdancount) {
        this.showdancount = showdancount;
    }

    public void setLinkcount(String linkcount) {
        this.linkcount = linkcount;
    }

    public void setFancount(String fancount) {
        this.fancount = fancount;
    }

    public String getShowdancount() {
        return showdancount;
    }

    public String getLinkcount() {
        return linkcount;
    }

    public String getFancount() {
        return fancount;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMessagenum() {
        return messagenum;
    }

    public void setMessagenum(int messagenum) {
        this.messagenum = messagenum;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getTotallevel() {
        return totallevel;
    }

    public void setTotallevel(String totallevel) {
        this.totallevel = totallevel;
    }

    public String getCopper() {
        return copper;
    }

    public void setCopper(String copper) {
        this.copper = copper;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContribution() {
        return contribution;
    }

    public void setContribution(String contribution) {
        this.contribution = contribution;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAuthuserid() {
        return authuserid;
    }

    public void setAuthuserid(String authuserid) {
        this.authuserid = authuserid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
