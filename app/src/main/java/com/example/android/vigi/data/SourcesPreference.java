package com.example.android.vigi.data;

/**
 * Created by Jacky on 6/10/17.
 */

public class SourcesPreference {
    private static final String DEFAULT_NEWS_SOURCE = "cnn";
    private static final String DEFAULT_NEWS_SORTBY = "latest";


    public static String getDefaultNewsSource() {
        return DEFAULT_NEWS_SOURCE;
    }

    public static String getDefaultNewsSortby() {
        return DEFAULT_NEWS_SORTBY;
    }
}
