package com.example.android.vigi.data;

import android.provider.BaseColumns;
/**
 * Created by Jacky on 6/14/17.
 */

public class NewsSearchContract {
    private NewsSearchContract() {}

    public static class FavoriteNews implements BaseColumns {
        public static final String TABLE_NAME = "favoriteNews";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_IMAGE = "IMAGE";
    }

}
