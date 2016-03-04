package com.meidebi.app.ui.setting;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meidebi.app.R;
import com.meidebi.app.service.bean.CatagerogyBean;
import com.meidebi.app.service.bean.base.CommonJson;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.bean.lbs.PushCatBean;
import com.meidebi.app.service.dao.CategoryDao;
import com.meidebi.app.service.dao.PushDao;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.shareprefelper.SharePrefUtility;
import com.meidebi.app.ui.view.preferenfragment.PreferenceFragment;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class PushContentFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private List<CheckBoxPreference> contentList = new ArrayList<CheckBoxPreference>();
    private String cates;
    private List<CatagerogyBean> cat_list = new ArrayList<CatagerogyBean>();

    private PushDao dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

    }

    private void initData() {
        CategoryDao.getCategory(new RestHttpUtils.RestHttpHandler<ListJson>() {
            @Override
            public void onSuccess(ListJson result) {
                cat_list = result.getData();
                PreferenceScreen preferenceScreen = getPreferenceManager()
                        .createPreferenceScreen(getActivity());
                preferenceScreen.getSharedPreferences()
                        .registerOnSharedPreferenceChangeListener(PushContentFragment.this);
                for (int i = 0; i < cat_list.size(); i++) {
                    CatagerogyBean bean = cat_list.get(i);
                    String key = bean.getId();
                    String value = bean.getName();
                    CheckBoxPreference checkBoxPreference = new CheckBoxPreference(
                            getActivity());
                    checkBoxPreference.setTitle(value);
                    checkBoxPreference.setKey(key);

                    if (bean.getSetPush() == 1) {
                        checkBoxPreference.setChecked(true);
                    }

                    preferenceScreen.addPreference(checkBoxPreference);
                    contentList.add(checkBoxPreference);
                }

                Toast.makeText(getActivity(), getString(R.string.push_cat_choose),
                        Toast.LENGTH_SHORT).show();

                setPreferenceScreen(preferenceScreen);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailed() {

            }
        });
       /* getDao().getNewSetting(new RestHttpUtils.RestHttpHandler<CommonJson>() {
            @Override
            public void onSuccess(CommonJson result) {
                PushCatBean datas = (PushCatBean) result.getData();
                //rebuildData(datas);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailed() {

            }
        });*/
    }

    public PushDao getDao() {
        if (dao == null) {
            dao = new PushDao();
        }
        return dao;
    }

    private void rebuildData(PushCatBean bean) {
        for (int i = 0; i < bean.getCates().size(); i++) {
            for (int j = 0; j < contentList.size(); j++) {
                CheckBoxPreference item = contentList.get(j);
                if (item.getKey()
                        .equals(String.valueOf(bean.getCates().get(i)))) {
                    item.setChecked(true);
                }
            }
        }
    }

    public String getChooseSet() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < contentList.size(); i++) {
            if (contentList.get(i).isChecked()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(contentList.get(i).getKey());
            }
        }
        return sb.toString();
    }

    public void saveSetting() {
        cates = getChooseSet();
        if (TextUtils.isEmpty(cates)) {
            cates = "0";
        }
        String[] ids = cates.split(",");
        for (CatagerogyBean info : cat_list) {
            for (String id : ids) {
                if (info.getId().equals(id)) {
                    info.setSetPush(1);
                }
            }
        }
        ListJson json = new ListJson();
        json.setData(cat_list);
        final String datas = new Gson().toJson(json);

        PushDao.submitNewSetting(cates, new RestHttpUtils.RestHttpHandler<CommonJson>() {
            @Override
            public void onSuccess(CommonJson result) {
                SharePrefUtility.setCatList(datas);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailed() {
                Toast.makeText(getActivity(), getString(R.string.push_cat_set_failure),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {

    }
}
