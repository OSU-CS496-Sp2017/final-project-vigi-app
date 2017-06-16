package com.example.android.vigi;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

//import com.example.android.vigi.R;
import com.example.android.vigi.data.SourcesPreference;
import com.example.android.vigi.data.NewsSearchContract;
import com.example.android.vigi.data.NewsSearchDBHelper;
import com.example.android.vigi.utils.NewsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Jacky on 6/13/17.
 */

public class SavedSearchResultsActivity extends AppCompatActivity implements NewsAdapter.OnSearchResultClickListener{
    private RecyclerView mSavedSearchResultsRV;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_search_results);

        NewsSearchDBHelper dbHelper = new NewsSearchDBHelper(this);
        mDB = dbHelper.getReadableDatabase();

        ArrayList<NewsUtils.SearchResult> searchResultsList = getAllSavedSearchResults();
        NewsAdapter adapter = new NewsAdapter(this);
        adapter.updateSearchResults(searchResultsList);

        mSavedSearchResultsRV = (RecyclerView)findViewById(R.id.rv_saved_search_results);
        mSavedSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSavedSearchResultsRV.setHasFixedSize(true);
        mSavedSearchResultsRV.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        mDB.close();
        super.onDestroy();
    }

    @Override
    public void onSearchResultClick(NewsUtils.SearchResult searchResult) {
        Intent intent = new Intent(this, SearchResultDetailActivity.class);
        intent.putExtra(NewsUtils.SearchResult.EXTRA_SEARCH_RESULT, searchResult);
        startActivity(intent);
    }

    private ArrayList<NewsUtils.SearchResult> getAllSavedSearchResults() {
        Cursor cursor = mDB.query(
                NewsSearchContract.FavoriteNews.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
                //NewsSearchContract.FavoriteNews.COLUMN_DATE + " DESC"
        );

        ArrayList<NewsUtils.SearchResult> searchResultsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            NewsUtils.SearchResult searchResult = new NewsUtils.SearchResult();
            searchResult.title = cursor.getString(
                    cursor.getColumnIndex(NewsSearchContract.FavoriteNews.COLUMN_TITLE)
            );
            searchResult.description = cursor.getString(
                    cursor.getColumnIndex(NewsSearchContract.FavoriteNews.COLUMN_DESCRIPTION)
            );
            searchResult.url = cursor.getString(
                    cursor.getColumnIndex(NewsSearchContract.FavoriteNews.COLUMN_URL)
            );
            searchResult.author = cursor.getString(
                    cursor.getColumnIndex(NewsSearchContract.FavoriteNews.COLUMN_AUTHOR)
            );
            searchResult.date = cursor.getString(
                    cursor.getColumnIndex(NewsSearchContract.FavoriteNews.COLUMN_DATE)
            );
            searchResultsList.add(searchResult);
        }
        cursor.close();
        String a = "title";
        if (a.equals(SourcesPreference.getDefaultNewsSortby())){
            if (searchResultsList.size() > 0) {
                Collections.sort(searchResultsList, new Comparator<NewsUtils.SearchResult>() {
                    @Override
                    public int compare(final NewsUtils.SearchResult object1, final NewsUtils.SearchResult object2) {
                        return object1.title.compareTo(object2.title);
                    }
                });
            }
        }
        return searchResultsList;
    }
}
