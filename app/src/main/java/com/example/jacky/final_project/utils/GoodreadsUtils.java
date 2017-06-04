package com.example.jacky.final_project.utils;

import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jacky on 6/4/17.
 */

public class GoodreadsUtils {
    private final static String OWN_GOODREADS_SEARCH_BASE_URL = "http://www.goodreads.com";
    private final static String OWM_GOODREADS_QUERY_PARAM = "q";



    private final static String OWM_GOODREADS_KEY = "YyKN0S5c46X8CvaIrjxDhQ";
    private final static String OWN_GOODREADS_SECRET = "1awpxQlh6cedLBVsMfCP4nCOH8fUEbYhM56hATnv0";

    public static class GoodreadsItem implements Serializable {
        public static final String EXTRA_GOODREADS_ITEM = "com.example.jacky.final_project.utils.GoodreadsItem.SearchResult";
        public int userId;
        public String authorsName;
        public String booksName;
        public boolean authorsFollow; //This is a Post
        public String reviewGet;
        public String reviewAdd; //This is a Post
        public String reviewEdit; //This is a Post
        public boolean reviewDestroy; //This is a Post
    }

}
