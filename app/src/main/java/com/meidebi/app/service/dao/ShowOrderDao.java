package com.meidebi.app.service.dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.gson.JsonParseException;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.base.error.XException;
import com.meidebi.app.base.net.HttpMethod;
import com.meidebi.app.base.net.HttpUtility;
import com.meidebi.app.service.bean.base.Chanel;
import com.meidebi.app.service.bean.base.LinkListBean;
import com.meidebi.app.service.bean.base.LinkListJson;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.msg.OrderShowBean;
import com.meidebi.app.service.bean.show.Draft;
import com.meidebi.app.support.component.UploadImageService;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.ui.main.UploadShowOrderFragment;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowOrderDao extends BaseDao {
	private Resources res = XApplication.getInstance().getResources();

	public ShowOrderDao(Activity activity) {
		super(activity);
	}

//	public void updateSomething(final List<String> picPath) {
//		// new Thread() {
//		// @Override
//		// public void run() {
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("userkey", LoginUtil.getUid());
//		// try {
//		// boolean result =
//		// HttpUtility.getInstance().executeUploadTask(HttpUrl.UPLOAD_IMAGE_TEXT,
//		// map, picPath.get(0),"image" );
//		// } catch (XException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// }
//		// }
//		// }.start();
//		final UploadRequest request = new UploadRequest(mActivity,
//				HttpUrl.UPLOAD_IMAGE_TEXT);
//		for (int i = 0; i < picPath.size(); i++) {
//			request.addFileToUpload(picPath.get(i), "userkey", "",
//					ContentType.IMAGE_JPEG); // You can find many common content
//												// types defined as static
//												// constants in the ContentType
//												// class
//		}
//		request.addParameter("userkey", LoginUtil.getUid());
//		AppLogger.e("userkey" + LoginUtil.getUid());
//		// request.addHeader("your-custom-header", "your-custom-value");
//
//		// request.addParameter("parameter-name", "parameter-value");
//		//
//		// //If you want to add an array of strings, you can simply to the
//		// following:
//		// request.addParameter("array-parameter-name", "value1");
//		// request.addParameter("array-parameter-name", "value2");
//		// request.addParameter("array-parameter-name", "valueN");
//
//		request.setNotificationConfig(
//				com.meidebi.app.R.drawable.ic_menu_upload, // Notification icon. You can
//													// use your own app's
//													// R.drawable.your_resource
//				res.getString(R.string.app_name), // You can use your string
//													// resource with:
//													// context.getString(R.string.your_string)
//				res.getString(R.string.moblie_uploading),
//				res.getString(R.string.moblie_upload_finish),
//				res.getString(R.string.moblie_upload_err), true); // Set this to
//																	// true if
//																	// you want
//																	// the
//																	// notification
//																	// to be
//																	// automatically
//																	// cleared
//																	// when
//																	// upload is
//																	// successful
//
//		try {
//			// Utility method that creates the intent and starts the upload
//			// service in the background
//			// As soon as the service starts, you'll see upload status in
//			// Android Notification Center :)
//			UploadService.startUpload(request);
//		} catch (Exception exc) {
//			// You will end up here only if you pass an incomplete UploadRequest
//			Log.e("AndroidUploadService", exc.getLocalizedMessage(), exc);
//		}
//	}
//
	public ListJson<LinkListBean<OrderShowBean>> getMapFromJson(boolean isHot) {
		// TODO Auto-generated method stub
        ListJson<LinkListBean<OrderShowBean>> mainJson;
        HashMap<String,String> map =new HashMap<String, String>();
        String is_Hot = isHot?"1":"0";
        map.put("hot", is_Hot);
		map.put("p", String.valueOf(page));
		try {
			String result = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.Get, HttpUrl.ORDERSHOW_URL, map);// 分类网址判断
			// AppLogger.e("result"+result);
			mainJson = gson.fromJson(result, ListJson.class);
			if (mainJson == null) {
				return null;
			}else{
				List<OrderShowBean> _list = ((LinkListBean<OrderShowBean>)mainJson.getData()).getLinklist();
				if (page == 1) {
						String[] args = {"OrderShow",is_Hot};
						SugarRecord.deleteAll(Chanel.class, "M_CHANEL=? and IS_ALL=?",args );
				}
					for (int i = 0; i < _list.size(); i++) {
						OrderShowBean bean = _list.get(i);
						if (page == 1) {
						Chanel chanelbean = new Chanel(); 
						chanelbean.setD_id(bean.getId());
						chanelbean.setmPage(String.valueOf(page));
						chanelbean.setmChanel("OrderShow");
						chanelbean.setIsAll(isHot);
						SugarRecord.save(chanelbean);
						}
						OrderShowBean dbbean = SugarRecord.findById(OrderShowBean.class, bean.getId());
						if(dbbean!=null){
						bean.setHasVoteSp(dbbean.getHasVoteSp());
						}
						dbbean = null;
					}
				
				SugarRecord.saveInTx(_list);
			}
			return mainJson;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
//


    public static void uploadUnsendPhoto(Context context) {
        if (LoginUtil.isAccountLogin()) {
            List<Draft> draftses = getSendDrafs();
            for (int j = 0; j < draftses.size(); j++) {
                Draft draft = draftses.get(j);
                startUploadService(context, draft);
            }
        }
    }

    public static void submitShow(final Context context ,ArrayList<String> sendedPhotos, final Draft draft){
        StringBuffer buffer = new StringBuffer();
        boolean isfirst = true;
        for (int i = 0; i < sendedPhotos.size(); i++) {
            if(isfirst){
                buffer.append(sendedPhotos.get(i));
                isfirst =false;
            }else{
                buffer.append(",").append(sendedPhotos.get(i));
            }
        }
        RequestParams params = new RequestParams();
        params.put("pics",buffer.toString());
        params.put("userkey",LoginUtil.getUid());
        params.put("content",draft.getContent());
        RestHttpUtils.get(HttpUrl.UPLOAD_SUBMIT_SHOW, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
             }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                 draft.setMstatus(UploadShowOrderFragment.Drafts_sendsucess);
                SugarRecord.save(draft);
                if (ifSendCompetle()) {
                    AppLogger.e("stop");
                    Intent intent = new Intent(context, UploadImageService.class);

                    context.stopService(intent);
                }
            }
        });
    }

    private static Boolean ifSendCompetle(){
        return getSendDrafs().size()
                ==0?true:false;
    }

    public static List<Draft> getSendDrafs(){
        return Select.from(Draft.class).where
                (new Condition[]{new Condition("mstatus").eq(UploadShowOrderFragment.Drafts_ready),new Condition("uid").eq(LoginUtil.getId())}).list();
    }


    public static void startUploadService(Context context,Draft draft){
        Intent intent = new Intent(context,UploadImageService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(UploadImageService.DRAFT,draft);
        intent.putExtras(bundle);
      context.startService(intent);
    }

    public void getResult(boolean isHot,RestHttpUtils.RestHttpHandler<LinkListJson> handler){
        RequestParams  map = new RequestParams();
        String is_Hot = isHot?"1":"0";
        map.put("hot", is_Hot);
          map.put("p", String.valueOf(page));
        getLinkListResult(HttpUrl.ORDERSHOW_URL, map, handler, OrderShowBean.class);
    }
}
