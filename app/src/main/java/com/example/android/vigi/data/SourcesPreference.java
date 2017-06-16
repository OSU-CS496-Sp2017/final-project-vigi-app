package com.example.android.vigi.data;

/**
 * Created by Jacky on 6/10/17.
 */

public class SourcesPreference {
    private static String DEFAULT_NEWS_SORTBY = "time";

    public static void change_sort(String sort) {
            DEFAULT_NEWS_SORTBY = sort;
    }

    public static String getDefaultNewsSortby() {
        return DEFAULT_NEWS_SORTBY;
    }
}
