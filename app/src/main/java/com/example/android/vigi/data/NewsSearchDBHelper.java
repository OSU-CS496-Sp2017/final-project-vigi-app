package com.example.android.vigi.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Jacky on 6/14/17.
 */

public class NewsSearchDBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 1;

    public NewsSearchDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_REPOS_TABLE =
                "CREATE TABLE " + NewsSearchContract.FavoriteNews.TABLE_NAME + " (" +
                        NewsSearchContract.FavoriteNews._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NewsSearchContract.FavoriteNews.COLUMN_TITLE + " TEXT NOT NULL, " +
                        NewsSearchContract.FavoriteNews.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        NewsSearchContract.FavoriteNews.COLUMN_URL + " TEXT NOT NULL, " +
                        NewsSearchContract.FavoriteNews.COLUMN_AUTHOR + " TEXT, " +
                        NewsSearchContract.FavoriteNews.COLUMN_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                        ");";
        db.execSQL(SQL_CREATE_FAVORITE_REPOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NewsSearchContract.FavoriteNews.TABLE_NAME);
        onCreate(db);
    }
}
