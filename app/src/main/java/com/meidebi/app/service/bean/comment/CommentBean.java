package com.meidebi.app.service.bean.comment;

import android.text.SpannableString;
import android.text.TextUtils;

import com.meidebi.app.service.bean.base.BaseItemBean;
import com.meidebi.app.support.utils.content.ContentUtils;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;

import java.util.List;

public class CommentBean extends BaseItemBean {
	private String comment;// 评论的内容
	private String fromid;// 被评论的ID（如果type为1，fromid 为链接的id，如果为2则为评论的id）
	private int type;// 1、为链接（包含单品，活动，优惠卷）
	private String userid;// 评论人
	private String touserid;// 评论的内容管理人，也可能是评论需要通知的对象人
	// private String parentid;//（无效，别用）
	private int replaycount;// 回复数量（当这个数大于0时，会有子评论，即评论的回复）
	private String tonickname;// 对应的是touserid的昵称
	private List<CommentBean> replayitems;// (评论的子评论)
	private transient SpannableString listViewSpannableString;
	private transient SpannableString listViewSpannableStringReferTo;
	private String content;
	private String photo;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhoto() {
		if (SharePrefUtility.getEnablePic()) {
			return photo;
		}
		return null;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public SpannableString getListViewSpannableStringReferTo() {
		if (!TextUtils.isEmpty(listViewSpannableStringReferTo)) {
			return listViewSpannableStringReferTo;
		} else {
			ContentUtils.addJustHighLightLinks(this);
			return listViewSpannableStringReferTo;
		}

	}

	public void setListViewSpannableStringReferTo(
			SpannableString listViewSpannableStringReferTo) {
		this.listViewSpannableStringReferTo = listViewSpannableStringReferTo;
	}

	private Boolean isParent = true;
	private Boolean isFirst = false;
	private Boolean isLast = false;
	private int referto = 0;
	private String refernickname;
	private String refercontent;
	private String referuserid;

	public String getRefercontent() {
		return refercontent;
	}

	public void setRefercontent(String refercontent) {
		this.refercontent = refercontent;
	}

	public String getReferuserid() {
		return referuserid;
	}

	public void setReferuserid(String referuserid) {
		this.referuserid = referuserid;
	}

	public String getRefernickname() {
		return refernickname;
	}

	public void setRefernickname(String refernickname) {
		this.refernickname = refernickname;
	}

	public int getReferto() {
		return referto;
	}

	public void setReferto(int referto) {
		this.referto = referto;
	}

	public Boolean getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Boolean isFirst) {
		this.isFirst = isFirst;
	}

	public Boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(Boolean isLast) {
		this.isLast = isLast;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFromid() {
		return fromid;
	}

	public void setFromid(String fromid) {
		this.fromid = fromid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTouserid() {
		return touserid;
	}

	public void setTouserid(String touserid) {
		this.touserid = touserid;
	}

	// public String getParentid() {
	// return parentid;
	// }
	// public void setParentid(String parentid) {
	// this.parentid = parentid;
	// }
	public int getReplaycount() {
		return replaycount;
	}

	public void setReplaycount(int replaycount) {
		this.replaycount = replaycount;
	}

	public String getTonickname() {
		return tonickname;
	}

	public void setTonickname(String tonickname) {
		this.tonickname = tonickname;
	}

	public List<CommentBean> getReplayitems() {
		return replayitems;
	}

	public void setReplayitems(List<CommentBean> replayitems) {
		this.replayitems = replayitems;
	}

}
