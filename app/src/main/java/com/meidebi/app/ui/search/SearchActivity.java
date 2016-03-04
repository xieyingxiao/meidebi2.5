package com.meidebi.app.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meidebi.app.R;
import com.meidebi.app.service.bean.CatagerogyBean;
import com.meidebi.app.service.bean.base.ListJson;
import com.meidebi.app.service.dao.CategoryDao;
import com.meidebi.app.support.utils.RestHttpUtils;
import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.adapter.CatAdapter;
import com.meidebi.app.ui.adapter.base.InterRecyclerOnItemClick;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.commonactivity.CommonFragmentActivity;
import com.meidebi.app.ui.main.MainVpFragment;
import com.meidebi.app.ui.provider.PlacesSuggestionProvider;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchActivity extends BasePullToRefreshActivity implements InterRecyclerOnItemClick {


    @InjectView(R.id.common_recyclerview)
    RecyclerView recyclerView;
    CatAdapter catAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        ButterKnife.inject(this);
        setTitle("分类筛选");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        CategoryDao.getCategory(new RestHttpUtils.RestHttpHandler<ListJson>() {
            @Override
            public void onSuccess(ListJson result) {
                if (result.getData() == null) {
                    return;
                }
                catAdapter = new CatAdapter(SearchActivity.this, (List<CatagerogyBean>) result.getData());
                catAdapter.setOnItemClickListener(SearchActivity.this);
                recyclerView.setAdapter(catAdapter);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailed() {

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    PlacesSuggestionProvider.AUTHORITY, PlacesSuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            IntentUtil.start_activity(this, SearchResultActivity.class,
                    new BasicNameValuePair("keyword", query));
        }
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Tells your app's SearchView to use this activity's searchable configuration
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        //custom editext
        AutoCompleteTextView searchText = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchText.setHintTextColor(getResources().getColor(R.color.text_hint_color));
        searchText.setTextColor(getResources().getColor(R.color.text_black_color));
        searchText.setHint("请输入关键字");
        //hide icon
        ImageView searchIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchIcon.setLayoutParams(new LinearLayout.LayoutParams(0, 0));


        return true;
    }

    @Override
    public void OnItemClick(int position) {
        CatagerogyBean bean = catAdapter.getData().get(position);
        IntentUtil.start_activity(SearchActivity.this, CommonFragmentActivity.class,
                new BasicNameValuePair(CommonFragmentActivity.KEY, MainVpFragment.class.getName()),
                new BasicNameValuePair(CommonFragmentActivity.USETOOLBAR, MainVpFragment.class.getName()),
                new BasicNameValuePair(CommonFragmentActivity.PARAM, bean.getId()),
                new BasicNameValuePair(CommonFragmentActivity.PARAM2, bean.getName()));

    }

    @Override
    public void OnFoooterClick(int position) {
    }


}
