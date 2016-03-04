package com.meidebi.app.support.component.lbs;

import com.baidu.location.BDLocation;

public interface ILocListener {
	void getLocSuccess(BDLocation location);

	public void getLocPoi(BDLocation poiLocation);
}
