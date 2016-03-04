package com.meidebi.app.service.bean.base;

import java.io.Serializable;

public class BaseItemBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String nickname;// 评论人的昵称
	private int createtime;// 评论的时间
	private int sharetime;
 
	public int getSharetime() {
		return sharetime;
	}

	public void setSharetime(int sharetime) {
		this.sharetime = sharetime;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
 
		return id;
	}


 
	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getCreatetime() {
		return createtime;
	}

	public void setCreatetime(int createtime) {
		this.createtime = createtime;
	}

}
