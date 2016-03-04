package com.meidebi.app.service.bean.user;


import com.orm.dsl.Table;

@Table(name = "AccountBean")
public class AccountBean {
	private Boolean isLogin = false;
	private Boolean isSign = false;
	private String id="";
	private String username;
	private String photoUrl;
	private String level;
	private String contribution;//贡献值
 	private String score;//积分
	private String money;//铜币
	private int msgNum;
	private String userKey;
	private long logintime;

    public long getLogintime() {
		return logintime;
	}

	public void setLogintime(long logintime) {
		this.logintime = logintime;
	}

	public int getMsgNum() {
		return msgNum;
	}

	public void setMsgNum(int msgNum) {
		this.msgNum = msgNum;
	}

	public Boolean getIsSign() {
		return isSign;
	}

	public void setIsSign(Boolean isSign) {
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

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public Boolean getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}

}
