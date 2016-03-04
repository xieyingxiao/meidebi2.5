package com.meidebi.app.service.init;

import java.util.ArrayList;
import java.util.List;

import com.meidebi.app.R;

public class ScoreDirectionInit {
	private static ScoreDirectionInit instance = new ScoreDirectionInit();
	private List<ScoreDirectionBean> list = new ArrayList<ScoreDirectionBean>();
	public List<ScoreDirectionBean> getList() {
		return list;
	}
	public void setList(List<ScoreDirectionBean> list) {
		this.list = list;
	}

	public static ScoreDirectionInit getInstance() {
		return instance;
	}

	public List<ScoreDirectionBean> get() {
		return list;
	}

	public ScoreDirectionInit() {
		BuildCategory();
	}
	private void BuildCategory() {
		// TODO Auto-generated method stub
		ScoreDirectionBean s1 = new ScoreDirectionBean();
		s1.setName("积分和铜币");
		s1.setUrl("http://m.meidebi.com/article-9.html");
		list.add(s1);
		ScoreDirectionBean s2 = new ScoreDirectionBean();
		s2.setName("贡献值");
		s2.setUrl("http://m.meidebi.com/article-66.html");
		list.add(s2);
	}
}
