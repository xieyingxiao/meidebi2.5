package com.meidebi.app.service.task;

//public class CatagerogyInfoTask extends
//		MyAsyncTask<Void, List<BaseItemBean>, List<BaseItemBean>> {
//
//	private XException e;
//
//	public CatagerogyInfoTask() {
//	}
//
//	private CategoryDao dao;
//
//	public CategoryDao getDao() {
//		if (dao == null) {
//			dao = new CategoryDao();
//		}
//		return dao;
//	}
//
//	@Override
//	protected List<BaseItemBean> doInBackground(Void... params) {
//		CategoryJson json = null;
//		json = getDao().getCategory();
//		if (json != null) {
//			return json.getData();
//		} else {
//			cancel(true);
//		}
//		return null;
//	}
//
//	@Override
//	protected void onPostExecute(List<BaseItemBean> bean) {
//		XApplication.getInstance().setCatlist(bean);
//		super.onPostExecute(bean);
//	}
//
	
//}