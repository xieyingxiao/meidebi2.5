package com.meidebi.app.ui.provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by mdb-ii on 14-12-31.
 */
public class PlacesSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static int MODE = DATABASE_MODE_QUERIES;
    public final static String AUTHORITY = "com.meidebi.app.search_suggestion_provider";

    public PlacesSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}