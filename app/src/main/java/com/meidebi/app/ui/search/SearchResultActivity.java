package com.meidebi.app.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meidebi.app.R;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.provider.PlacesSuggestionProvider;


public class SearchResultActivity extends BasePullToRefreshActivity  {
	private SearchResultFragment fragment;
    private SearchView searchView;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setTitle("搜索结果");
        if (savedInstanceState == null) {
            fragment = new SearchResultFragment(getIntent().getStringExtra(
                    "keyword"));
            addFragment( fragment);
        }

        handleIntent(getIntent());
	}

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search_reuslt;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Tells your app's SearchView to use this activity's searchable configuration
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        AutoCompleteTextView searchText = (AutoCompleteTextView)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchText.setHintTextColor(getResources().getColor(R.color.text_hint_color));
        searchText.setTextColor(getResources().getColor(R.color.text_black_color));
        searchText.setHint("请输入关键字");
        //hide icon
        ImageView searchIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchIcon.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
         searchView.setQuery(getIntent().getStringExtra(
                "keyword"), false);
        searchView.clearFocus();
        return true;
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
            fragment.refreshResult(query);
            searchView.setQuery(query,false);
            searchView.clearFocus();
        }
    }

}
