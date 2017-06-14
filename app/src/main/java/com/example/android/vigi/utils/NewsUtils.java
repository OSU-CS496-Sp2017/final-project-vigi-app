package com.example.android.vigi.utils;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by Jacky on 4/29/17.
 */

public class NewsUtils {
    private final static String OWM_NEWS_BASE_URL = "https://newsapi.org/v1/articles";
    private final static String OWM_NEWS_QUERY_PARAM = "source";
    private final static String OWM_NEWS_APP_ID = "apiKey";

    private final static String OWM_NEWS_APIKEY = "8adf9b7e456c41819410537af7cce783";


    public static class SearchResult implements Serializable {
        public static final String EXTRA_SEARCH_RESULT = "com.example.android.vigi.utils.NewsUtils.SearchResult";
        public String title;
        public String date;
        public String author;
        public String description;
        public String image;
        public String url;
    }

    public static String buildNewsSearchURL(String searchQuery) {
        return Uri.parse(OWM_NEWS_BASE_URL).buildUpon()
                .appendQueryParameter(OWM_NEWS_QUERY_PARAM, searchQuery)
                .appendQueryParameter(OWM_NEWS_APP_ID, OWM_NEWS_APIKEY)
                .build()
                .toString();
    }

    public static ArrayList<SearchResult> parseNewsSearchResultsJSON(String searchResultsJSON) {
        try {
            JSONObject searchResultsObj = new JSONObject(searchResultsJSON);
            JSONArray searchResultsItems = searchResultsObj.getJSONArray("articles");

            ArrayList<SearchResult> searchResultsList = new ArrayList<SearchResult>();
            for (int i = 0; i < searchResultsItems.length(); i++) {
                SearchResult searchResult = new SearchResult();
                JSONObject searchResultItem = searchResultsItems.getJSONObject(i);


                searchResult.title = searchResultItem.getString("title");

                searchResult.date = searchResultItem.getString("publishedAt");

                searchResult.author = searchResultItem.getString("author");

                searchResult.description = searchResultItem.getString("description");

                searchResult.image = searchResultItem.getString("urlToImage");

                searchResult.url = searchResultItem.getString("url");


                searchResultsList.add(searchResult);
            }
            return searchResultsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }



}

