package com.meidebi.app.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.meidebi.app.R;
import com.meidebi.app.service.bean.CatagerogyBean;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.dao.CategoryDao;
import com.meidebi.app.support.component.upush.UpushUtity;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class DialogPush {
    private MaterialDialog pushDialog;
    private Context context;
    PushCatAdapter pushCatAdapter;
    private List<CatagerogyBean> catagerogyBeans = new ArrayList<CatagerogyBean>();

    public MaterialDialog getPushDialog(final Context context) {
        this.context = context;
        if (pushDialog == null) {
            pushDialog = new MaterialDialog.Builder(context).backgroundColor(Color.WHITE)
                    .title("自定义推送信息分类,\n让推送信息不再打扰您")
                    .positiveText("暂不给我推送")
                    .negativeText("确定")
                    .customView(R.layout.dialog_push, false)
                    .cancelable(false)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            SharePrefUtility.setPushOn(false);

                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            if (pushCatAdapter.getData() != null) {
                                Gson gson = new Gson();
                                ListJson json = new ListJson();
                                json.setData(pushCatAdapter.getData());
                                String cates = gson.toJson(json);
                                SharePrefUtility.setCatList(cates);
                                UpushUtity.setCat(cates);
                            }
                            SharePrefUtility.setPushOn(true);
                            UpushUtity.OnStart(context);

                        }
                    })
                    .build();
            initView();
        }
        return pushDialog;
    }

    private void initView() {
        final RecyclerView recyclerView = (RecyclerView) pushDialog.getCustomView().findViewById(R.id.common_recy);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        pushCatAdapter = new PushCatAdapter(context, catagerogyBeans);
        recyclerView.setAdapter(pushCatAdapter);

        CategoryDao.getCategory(new RestHttpUtils.RestHttpHandler<ListJson>() {
            public void onSuccess(ListJson result) {
                catagerogyBeans = result.getData();
                pushCatAdapter.setData(catagerogyBeans);
                pushCatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFailed() {

            }
        });
    }


    class PushCatAdapter extends BaseRecyclerAdapter<CatagerogyBean> {


        public PushCatAdapter(Context context, List<CatagerogyBean> objects) {
            super(context, objects);

        }

        @Override
        public boolean useFooter() {
            return false;
        }

        @Override
        public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.adapter_push_cat, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
            setDataView((ViewHolder) holder, position);
        }

        private void setDataView(final ViewHolder holder, final int position) {// 显示数据
            final CatagerogyBean item = getItem(position);
            holder._ic.setText(item.getName());
            holder._ic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    item.setSetPush(b == true ? 1 : 0);
                }
            });
        }

        class ViewHolder extends BasicItemViewHolder {
            @InjectView(R.id.cb_push_select)
            CheckBox _ic;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.inject(this, itemView);
            }
        }
    }
}
