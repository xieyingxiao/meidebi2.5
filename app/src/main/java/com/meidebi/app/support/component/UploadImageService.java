package com.meidebi.app.support.component;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.meidebi.app.base.config.HttpUrl;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.show.Draft;
import com.meidebi.app.service.bean.show.UploadToken;
import com.meidebi.app.support.component.bus.MainThreadBus;
import com.meidebi.app.support.component.bus.MainThreadBusProvider;
import com.meidebi.app.support.component.bus.UploadProgressEvent;
import com.meidebi.app.support.component.bus.UploadSucessEvent;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.support.utils.FileUtils;
import com.meidebi.app.support.utils.GsonUtils;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.ui.main.UploadShowOrderFragment;
import com.meidebi.app.ui.picker.AlbumUtil;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.squareup.otto.Subscribe;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 15-2-1.
 */
public class UploadImageService extends IntentService {
    private UploadManager uploadManager ;
     private AsyncHttpClient aClient = new AsyncHttpClient();
    public static   SparseArray<Draft> uploading_list = new SparseArray<Draft>();



    public static final String PHOTOS = "photos";
     public static final String DRAFT = "draft";
    public int uploading_num= 0 ;
     public static UploadImageService instanced;
      public UploadImageService(){
        super("UploadImageService");
    }
     public void onCreate() {
        super.onCreate();
        uploadManager = new UploadManager();
         instanced = this;
         MainThreadBusProvider.getInstance().register(this);
     }
     public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

     protected void onHandleIntent(final Intent intent) {
         final Draft draft = (Draft) intent.getSerializableExtra(DRAFT);

          AppLogger.e("draft"+draft.getContent());
          uploading_list.put(Integer.parseInt(draft.getId()), draft);
         onUploadProgressChange(new UploadProgressEvent(draft.getId(), 0, 0, draft));
    }






     public void onDestroy() {
         MainThreadBusProvider.getInstance().unregister(this);
         super.onDestroy();

    }



    public  void submitShow(List<AlbumUtil.PhotoEntry> usphotos, final Draft draft){
//        StringBuffer buffer = new StringBuffer();
//        boolean isfirst = true;
//        for (int i = 0; i < usphotos.size(); i++) {
//            if(isfirst){
//                buffer.append(usphotos.get(i).path);
//                isfirst =false;
//            }else{
//                buffer.append(",").append(usphotos.get(i).path);
//            }
//        }
        RequestParams params = new RequestParams();
        params.put("pics",draft.getAlreadyUpload    ());
        params.put("userkey",LoginUtil.getUid());
        params.put("content",draft.getContent());
        AppLogger.e("usphotos"+draft.getAlreadyUpload());
         RestHttpUtils.get(HttpUrl.UPLOAD_SUBMIT_SHOW, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                AppLogger.e("wrong" + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                AppLogger.e("server" + responseString);

                draft.setMstatus(UploadShowOrderFragment.Drafts_sendsucess);
                AppLogger.e("id"+draft.getId());
                SugarRecord.save(draft);
//                Intent broadcast = new Intent(AppAction.UPLOADFINISH);
//                Bundle bundle = new Bundle();
//                 bundle.putSerializable(DRAFT,draft);
//                broadcast.putExtras(bundle);
//                LocalBroadcastManager.getInstance(XApplication.getInstance())
//                        .sendBroadcast(broadcast);
                MainThreadBusProvider.getInstance().post(new UploadSucessEvent());
                if (ifSendCompetle()) {
                    stopSelf();
                }
            }
        });
    }

    private static Boolean ifSendCompetle(){
        return Select.from(Draft.class).where
                (new Condition[]{new Condition("mstatus").eq(UploadShowOrderFragment.Drafts_ready),new Condition("uid").eq(LoginUtil.getId())}).list().size()
                ==0?true:false;
    }


//    @Produce
//    public UploadProgressEvent uploadProgressEvent() {
//        // Provide an initial value for location based on the last known position.
//        return new UploadProgressEvent(id,position, prgress);
//        }

    private final Handler mHandler = new Handler(Looper.getMainLooper());


    @Subscribe
    public void onUploadProgressChange(final UploadProgressEvent event){

            if (Looper.myLooper() == Looper.getMainLooper()) {
                uploadImage(event);
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        uploadImage(event);
                    }
                });
            }

    }
    private void uploadImage(final UploadProgressEvent event){
        final List<AlbumUtil.PhotoEntry> unsendPhotos = GsonUtils.parseList(event.draft.getPicJson(),AlbumUtil.PhotoEntry.class);
        AppLogger.e("Subscribe");
        final int num = event.position;
        if(num<unsendPhotos.size()) {
            final AlbumUtil.PhotoEntry usPhoto = unsendPhotos.get(num);
            RequestParams params = new RequestParams();
            params.put("ext", FileUtils.getExtensionName(usPhoto.path));
            params.put("userkey", LoginUtil.getUid());
            aClient.get(this, HttpUrl.UPLOAD_GET_TOKEN, params, new TextHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                     stopSelf();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    event.draft.setMstatus(UploadShowOrderFragment.Drafts_sending);
                    CommonJson<UploadToken> json = (GsonUtils.fromJson(responseString, UploadToken.class));
                    final UploadToken token = json.getData();
                    uploadManager.put(usPhoto.path, token.getKey(), token.getToken(), new UpCompletionHandler() {
                        @Override
                        public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                            if (responseInfo != null && responseInfo.statusCode == 200) {// 上传成功
                                AppLogger.e("responseInfo" + responseInfo);

                                int finish_num = num + 1;
                                double progress = (Math.round(finish_num * 100 / unsendPhotos.size()));
                                AppLogger.e("sendbroadcast" + progress);
                                event.draft.setProgress(progress);
                                 uploading_list.put(Integer.parseInt(event.draft.getId()), event.draft);
                                if(num==0){
                                    event.draft.setAlreadyUpload(token.getFileUrl());

                                }else{
                                    event.draft.setAlreadyUpload(event.draft.getAlreadyUpload()+","+token.getFileUrl());

                                 }


                                AppLogger.e("position"+finish_num+" unsendPhotos.size()"+ unsendPhotos.size());

                                UploadProgressEvent nextevent = new UploadProgressEvent(event.draft.getId(), finish_num, (int) (progress), event.draft);
                                MainThreadBusProvider.getInstance().post(nextevent);
                                onUploadProgressChange(nextevent);
                            }
                        }
                    }, null);
                }
            });
        }else if(num == unsendPhotos.size()){
            AppLogger.e("num"+num);
                  submitShow(unsendPhotos, event.draft);

        }
    }
 }
