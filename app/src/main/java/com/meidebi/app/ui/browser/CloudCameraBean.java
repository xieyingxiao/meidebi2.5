package com.meidebi.app.ui.browser;

import java.util.ArrayList;
import java.util.List;

public class CloudCameraBean {
	private boolean isAdd = false;
	private List<BucketEntry> cloudPhotoList = new ArrayList<BucketEntry>();
	public boolean isAdd() {
		if(cloudPhotoList.size()==0){
			return false;
		}
		return isAdd;
	}
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	public List<BucketEntry> getCloudPhotoList() {
		return cloudPhotoList;
	}
	public void setCloudPhotoList(List<BucketEntry> cloudPhotoList) {
		this.cloudPhotoList = cloudPhotoList;
	}

	
}
