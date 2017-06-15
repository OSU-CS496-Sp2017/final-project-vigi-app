package com.example.android.vigi.data;

/**
 * Created by Jacky on 6/10/17.
 */

public class SourcesPreference {
    private static String DEFAULT_NEWS_CATEGORY = "business";
    private static String DEFAULT_NEWS_SORTBY = "latest";

    public static void change_category(String category){
        DEFAULT_NEWS_CATEGORY = category;
    }

    public static void change_unit(String sort) {
            DEFAULT_NEWS_SORTBY = sort;
    }

    public static String getDefaultNewsSource() {
        return DEFAULT_NEWS_CATEGORY;
    }

    public static String getDefaultNewsSortby() {
        return DEFAULT_NEWS_SORTBY;
    }
}
