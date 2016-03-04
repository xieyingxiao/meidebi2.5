package com.meidebi.app.service.bean.comment;

import java.util.List;
//@Table(name = "CommentListBean")
public class CommentListBean {
	private int commentnum;
	private List<CommentBean> commentlist;
	public int getCommentnum() {
		return commentnum;
	}

	public void setCommentnum(int commentnum) {
		this.commentnum = commentnum;
	}

	public List<CommentBean> getCommentlist() {
		return commentlist;
	}

	public void setCommentlist(List<CommentBean> commentlist) {
		this.commentlist = commentlist;
	}
}
