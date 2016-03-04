package com.meidebi.app.service.bean.base;

import com.meidebi.app.service.bean.comment.CommentListBean;
import com.meidebi.app.service.bean.detail.CoutryBean;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;

import java.io.Serializable;

/**
 * 主页原子 item obj
 * 
 * @author mdb-ii
 * 
 */
public class MsgBaseBean extends BaseItemBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int status;// :状态1、为可用，0、不可用
	private String category;// :分类id
	private String title;// ：标题
	private String orginurl;// ：原始的url
	private String redirecturl;// ：为跳转前的基本url
	private int ishot;// ：1、热门；0、非热门
	private String sethottime;// ：设置热门时间
	private int votesp;// :投票+的数量
	private int votem;// ：投票-的数量
	private int hit;// ：跳转数量
	private int showcount;// ：显示次数
	private int commentcount;// ：评价数量
	private int linktype;// ：链接类型,1、单品；2、活动；3、优惠卷活动
	private int siteid;// :商城ID
	private int timeout;// ：1、未过期；2、过期
	private String image;// ：图片地址
	private int isabroad;
	private String pic;
	private String headphoto;
	private String sharename;
	private CommentListBean userReviewsData;
	private CoutryBean contry;
	private String hasVoteSp ="0";
	private String hasVoteEm = "0";
 	private String cover;
	private int favnum;
	private long INSERTTIME;
	public long getINSERTTIME() {
		return INSERTTIME;
	}

	public void setINSERTTIME(long iNSERTTIME) {
		INSERTTIME = iNSERTTIME;
	}

    public int getFavnum() {
        return favnum;
    }


    public void setFavnum(int favnum) {
        this.favnum = favnum;
    }

    public String getCover() {
		if(!SharePrefUtility.getEnablePic()){
			return null;
		}
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
	
 
	public String getHasVoteSp() {
		return hasVoteSp;
	}

	public void setHasVoteSp(String hasVoteSp) {
		this.hasVoteSp = hasVoteSp;
	}

	public String getHasVoteEm() {
		return hasVoteEm;
	}

	public void setHasVoteEm(String hasVoteEm) {
		this.hasVoteEm = hasVoteEm;
	}

	public CoutryBean getContry() {
		return contry;
	}

	public void setContry(CoutryBean contry) {
		this.contry = contry;
	}

	public CommentListBean getUserReviewsData() {
		return userReviewsData;
	}

	public void setUserReviewsData(CommentListBean userReviewsData) {
		this.userReviewsData = userReviewsData;
	}

    private String sharephoto;

    public void setSharephoto(String sharephoto) {
        this.sharephoto = sharephoto;
    }

    public String getSharephoto() {
        return sharephoto;
    }
	
	public String getShareName() {
		return sharename;
	}

	public void setShareName(String shareName) {
		sharename = shareName;
	}

	public String getHeadphoto() {
		if(SharePrefUtility.getEnablePic()){
			return headphoto;
		}else{
			return null;
		}
	}

	public void setHeadphoto(String headphoto) {
		this.headphoto = headphoto;
	}

	public String getPic() {
		if(SharePrefUtility.getEnablePic()){
			return pic;
		}else{
			return null;
		}
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int isIsabroad() {
		return isabroad;
	}

	public void setIsabroad(int isabroad) {
		this.isabroad = isabroad;
	}

	public String getImage() {
		if (SharePrefUtility.getEnablePic()) {
			return image;
		} else {
			return null;
		}
	}

	public void setImage(String image) {
		this.image = image;
	}

	private int subsiteorwh;// ：仓库或者子站；1、仓库，2、子站
	private String description;// ：推荐适合的描述
	private String sitename;// :商城名称
	private String categoryname;// ：发布的分类名称
	private String price;// :单品价格
	private String remoteimage;
	private String imgUrl;

	public String getImgUrl() {
		if (SharePrefUtility.getEnablePic()) {
			return imgUrl;
		} else {
			return null;
		}
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getRemoteimage() {
		return remoteimage;
	}

	public void setRemoteimage(String remoteimage) {
		this.remoteimage = remoteimage;
	}

	// private int notchecked;
	// private int perfect;
	// private int votesm
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrginurl() {
		return orginurl;
	}

	public void setOrginurl(String orginurl) {
		this.orginurl = orginurl;
	}

	public String getRedirecturl() {
		return redirecturl;
	}

	public void setRedirecturl(String redirecturl) {
		this.redirecturl = redirecturl;
	}

	public int getIshot() {
		return ishot;
	}

	public void setIshot(int ishot) {
		this.ishot = ishot;
	}

	public String getSethottime() {
		return sethottime;
	}

	public void setSethottime(String sethottime) {
		this.sethottime = sethottime;
	}

	public int getVotesp() {
		return votesp;
	}

	public void setVotesp(int votesp) {
		this.votesp = votesp;
	}

	public int getVotem() {
		return votem;
	}

	public void setVotem(int votem) {
		this.votem = votem;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getShowcount() {
		return showcount;
	}

	public void setShowcount(int showcount) {
		this.showcount = showcount;
	}

	public int getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(int commentcount) {
		this.commentcount = commentcount;
	}

	public int getLinktype() {
		return linktype;
	}

	public void setLinktype(int linktype) {
		this.linktype = linktype;
	}

	public int getSiteid() {
		return siteid;
	}

	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getSubsiteorwh() {
		return subsiteorwh;
	}

	public void setSubsiteorwh(int subsiteorwh) {
		this.subsiteorwh = subsiteorwh;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
