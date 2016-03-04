package com.meidebi.app.service.bean.user;

import android.text.SpannableString;
import android.text.TextUtils;

import com.meidebi.app.support.utils.content.ContentUtils;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;

public class MsgCenterBean {
	private String id;
	private String touserid;
	private String relateuserid;
	private String content;
	private String target;
	private String sysmsgid;
	private int createtime;
	private int isread;
	private String relatenickname;
	private String tonickname;
	private String relatephoto;
	private String con;
	private String mainid;
	private String commentid;
	private int type;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMainid() {
		return mainid;
	}

	public void setMainid(String mainid) {
		this.mainid = mainid;
	}

	public String getCommentid() {
		return commentid;
	}

	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}

	public String getCon() {
		return con;
	}

	public void setCon(String con) {
		this.con = con;
	}

	public String getRelatephoto() {
		if (SharePrefUtility.getEnablePic()) {
			return relatephoto;
		}
		return null;
	}

	public void setRelatephoto(String relatephoto) {
		this.relatephoto = relatephoto;
	}

	private transient SpannableString listViewSpannableString;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTouserid() {
		return touserid;
	}

	public void setTouserid(String touserid) {
		this.touserid = touserid;
	}

	public String getRelateuserid() {
		return relateuserid;
	}

	public void setRelateuserid(String relateuserid) {
		this.relateuserid = relateuserid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getSysmsgid() {
		return sysmsgid;
	}

	public void setSysmsgid(String sysmsgid) {
		this.sysmsgid = sysmsgid;
	}

	public int getCreatetime() {
		return createtime;
	}

	public void setCreatetime(int createtime) {
		this.createtime = createtime;
	}

	public int isIsread() {
		return isread;
	}

	public void setIsread(int isread) {
		this.isread = isread;
	}

	public String getRelatenickname() {
		return relatenickname;
	}

	public void setRelatenickname(String relatenickname) {
		this.relatenickname = relatenickname;
	}

	public String getTonickname() {
		return tonickname;
	}

	public void setTonickname(String tonickname) {
		this.tonickname = tonickname;
	}

	public SpannableString getListViewSpannableString() {
		if (!TextUtils.isEmpty(listViewSpannableString)) {
			return listViewSpannableString;
		} else {
			ContentUtils.addJustHighLightLinks(this);
			return listViewSpannableString;
		}
	}

	public void setListViewSpannableString(
			SpannableString listViewSpannableString) {
		this.listViewSpannableString = listViewSpannableString;
	}

}
