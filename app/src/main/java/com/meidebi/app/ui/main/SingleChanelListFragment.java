package com.meidebi.app.ui.main;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.meidebi.app.R;
import com.meidebi.app.service.bean.BannerBean;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.LinkListJson;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.msg.MsgDetailBean;
import com.meidebi.app.service.bean.msg.OrderShowBean;
import com.meidebi.app.service.bean.msg.SingleBean;
import com.meidebi.app.service.dao.SingleDao;
import com.meidebi.app.support.lib.MyAsyncTask;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.Utility;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.support.utils.content.ContentUtils;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.adapter.MainCardAdapter;
import com.meidebi.app.ui.base.BaseRecycleViewFragment;
import com.meidebi.app.ui.msgdetail.MsgDetailActivity;
import com.meidebi.app.ui.view.VpBannerView;
import com.meidebi.app.ui.widget.DividerItemDecoration;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressLint("ValidFragment")public class SingleChanelListFragment extends BaseRecycleViewFragment<MsgDetailBean> {
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";

    private String chanelid = "";// 为全部 还是单品....
    private Boolean isHot = true;// 为全部 还是精华
    private String product_type = "0";// 产品分类...
    private SingleDao mainDao;
    private int position;
    private DBTask dbTask;
    private List<BannerBean> banner_list;
    private VpBannerView bannerView;
    private int progress_start,progress_end;

    private boolean useBanner;
    public void setProgress_start(int progress_start,int progress_end) {
        this.progress_start = progress_start;
        this.progress_end = progress_end;

    }

    private View view;

    public SingleDao getMainDao() {
        if (mainDao == null) {
            mainDao = new SingleDao(getActivity());
        }
        return mainDao;
    }

    public void setUseBanner(boolean useBanner) {
        this.useBanner = useBanner;
    }

    public SingleChanelListFragment() {
        enbleDBDATA = !SharePrefUtility.getAutoRefresh();
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    // get set end

    /**
     * @param isAll        是否全部
     * @param product_type 产品类型
     * @param chanelid     频道
     */
    public SingleChanelListFragment(boolean isHot, String product_type,
                                    String chanelid, int position) {
        this.product_type = product_type;
        this.chanelid = chanelid;
        this.isHot = isHot;
        this.position = position;
        enbleDBDATA = !SharePrefUtility.getAutoRefresh();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        enbleDBDATA = !SharePrefUtility.getAutoRefresh();
        View view = super.onCreateView(inflater, container, savedInstanceState);// sld
        if(mAdapter==null) {
            mAdapter = new MainCardAdapter(this, items_list);
            mAdapter.setData(items_list);
            mAdapter.setOnItemClickListener(this);

        }

        getSaveInstanceState(savedInstanceState);

        getListView().addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        getListView().setAdapter(mAdapter);

        Fragment parentFragment = getParentFragment();

        if (parentFragment instanceof ObservableScrollViewCallbacks) {

            getListView().setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);

        }

        //banner
        if(position==0&&useBanner&&banner_list==null) {
            getBanner();
        }else if(getIsLoadCompelte()&&mAdapter.getData()!=null&&mAdapter.getData().size()==0){
            onStartRefresh();
        }
        return view;

 	}

    private void getBanner(){

             getMainDao().indexBanner(new RestHttpUtils.RestHttpHandler<ListJson>() {
                @Override
                public void onSuccess(ListJson result) {
                    if (result != null) {
                        if (result.getData().size()>0) {
                            banner_list = result.getData();
                            View headView =getActivity().getLayoutInflater().inflate(R.layout.layout_vp_banner,null,false);
                            mAdapter.setHeadView(headView);
                            mAdapter.useHeader();
                            ((MainCardAdapter)mAdapter).setList_bannner(result.getData());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    onStartRefresh();
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFailed() {
                    onStartRefresh();

                }
            });

    }
    replaceJsonTask jsontask;


	/**
	 * 获取网络数据
	 * 
	 * @param type
	 *            数据类型
	 */
	protected void getData(final int type) {
		CommonJson<SingleBean> bean = null;
		getMainDao().setChanel(chanelid);
		getMainDao().setAll(isHot);
		getMainDao().setPage(mPage);
		getMainDao().setProduct_type(product_type);
        getMainDao().getResult(isHot,new RestHttpUtils.RestHttpHandler<LinkListJson>() {
                @Override
                public void onSuccess(LinkListJson result) {
                    if(type==GET_MORE_DATA) {
                        jsontask = new replaceJsonTask();
                        jsontask.setContent(result);
                        jsontask.setType(type);
                        jsontask.execute();
                    }else{
                        OnCallBack(type,result.getData().getLinklist());
                    }

                }

                @Override
                public void onStart() {
                    OnStart(type);
                }

                @Override
                public void onFailed() {
                    netWorkErr();
                }
            });

	}
    @Override
    public void OnItemClick(int position) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("bean", mAdapter.getData().get(position));
		IntentUtil.start_activity(getActivity(), MsgDetailActivity.class,
                bundle);
    }



	@Override
	public void getDBData() {
		dbTask = new DBTask();
		dbTask.execute();
	}

	class DBTask extends MyAsyncTask<Void, Void, List<MsgDetailBean>> {
		@Override
		protected void onPostExecute(List<MsgDetailBean> Result) {
			if (Result == null) {
				loadData();
			} else {
				mAdapter.setData(getItems_list());
				mAdapter.notifyDataSetChanged();
				onLoad();
				if (getActivity() != null) {
					AppLogger.e("" + Result.get(0).getINSERTTIME());
//					((MainActivity) getActivity()).showToast(Result.get(0)
//							.getINSERTTIME());
//					((SingelChanelVpFragment) getParentFragment())
//							.setData_insert_time(Result.get(0).getINSERTTIME());
				}
			}
		}

		@Override
		protected List<MsgDetailBean> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			mPage = 1;
			int IsHot = isHot ? 1 : 0;
			String sql = "SELECT MDB.*,C.INSERTTIME FROM MsgDetailBean AS MDB LEFT JOIN Chanel AS C ON MDB.ID = C.DID"
					+ " WHERE C.CATSTR = "
					+ product_type
					+ "  AND C.M_CHANEL = '"
					+ chanelid
					+ "'  AND C.IS_ALL = "
					+ IsHot + " ORDER BY C.INSERTTIME ASC";
			items_list = SugarRecord.sql(sql, MsgDetailBean.class);
			if (items_list != null) {
				if (items_list.size() != 0) {
					setItems_list(items_list);
					return items_list;
				}
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
//            mPullToRefreshLayout.post(new Runnable() {
//                @Override public void run() {
//                    mPullToRefreshLayout.setRefreshing(true);
//                }
//            });
 			super.onPreExecute();
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Utility.cancelTasks(dbTask);
        Utility.cancelTasks(jsontask);

        super.onDestroy();
	}

	@Override
	public void loadFirstPage() {

			super.loadFirstPage();
 	}


	@Override
	public void doSomething() {
//		((MainActivity) getActivity()).showNewestToast();
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("chanelid", chanelid);
		outState.putInt("position", position);
		outState.putBoolean("isHot", isHot);
		outState.putString("product_type", product_type);
	}

	private void getSaveInstanceState(Bundle outState) {
		if (outState != null) {
			chanelid = outState.getString("chanelid");
			position = outState.getInt("position");
            isHot = outState.getBoolean("isHot");
			product_type = outState.getString("product_type");
		}
	}





    class replaceJsonTask extends MyAsyncTask<Void,Void,LinkListJson> {
        LinkListJson content;
        private int type;

        public void setContent(LinkListJson content) {
            this.content = content;
        }

        public void setType(int type) {
            this.type = type;
        }



        @Override
        protected void onPostExecute(LinkListJson result) {
            OnCallBack(type,  result.getData().getLinklist());

        }

        @Override
        protected LinkListJson doInBackground(Void... params) {
            // TODO Auto-generated method stub

            Iterator<MsgDetailBean> Ite_new = content.getData().getLinklist().iterator();
            while (Ite_new.hasNext()) {
                MsgDetailBean new_odb = Ite_new.next();
                if (type == GET_MORE_DATA) {
                    Iterator<MsgDetailBean> Ite_old = mAdapter.getData().iterator();
                    while (Ite_old.hasNext()) {
                        MsgDetailBean old_data = Ite_old.next();
                        if (new_odb.getId().equals(old_data.getId())) {
                            AppLogger.e("remove " + new_odb.getTitle());
                            Ite_new.remove();
                        }
                    }
                }
            }

            return  content;
        }



    }

    @Override
    public void onReload() {
        if(position==0&&useBanner&&banner_list==null){
        getBanner();
        }else if(mAdapter.getData()!=null&&mAdapter.getData().size()==0){
            onStartRefresh();
        }
     }



}
