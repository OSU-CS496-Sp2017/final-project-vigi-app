package com.example.android.vigi;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.vigi.utils.NewsUtils;
import com.example.android.vigi.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;




public class MainActivity extends AppCompatActivity implements NewsAdapter.OnSearchResultClickListener,
        LoaderManager.LoaderCallbacks<String>{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SEARCH_RESULTS_LIST_KEY = "searchResultsList";
    private static final String SEARCH_URL_KEY = "NewsURL";
    private static final int NEWS_SEARCH_LOADER_ID = 0;


    private RecyclerView mSearchResultsRV;
    private EditText mSearchBoxET;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private Toast mSearchResultToast;
    private NewsAdapter mNewsAdapter;

    private ArrayList<NewsUtils.SearchResult> mSearchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchResultsList = null;

        mSearchBoxET = (EditText)findViewById(R.id.et_search_box);
        mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mNewsAdapter = new NewsAdapter(this);
        mSearchResultsRV.setAdapter(mNewsAdapter);

        getSupportLoaderManager().initLoader(NEWS_SEARCH_LOADER_ID, null, this);

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String searchQuery = mSearchBoxET.getText().toString();
                if(!TextUtils.isEmpty(searchQuery)){
                    doNews(searchQuery);
                }
            }
        });
    }
    private void doNews (String searchQuery){
        String newsSearchUrl = NewsUtils.buildNewsSearchURL(searchQuery);

        Bundle argsBundle = new Bundle();
        argsBundle.putString(SEARCH_URL_KEY, newsSearchUrl);
        getSupportLoaderManager().restartLoader(NEWS_SEARCH_LOADER_ID, argsBundle, this);
    }

    @Override
    public void onSearchResultClick(NewsUtils.SearchResult searchResult) {
        Intent intent = new Intent(this, SearchResultDetailActivity.class);
        intent.putExtra(NewsUtils.SearchResult.EXTRA_SEARCH_RESULT, searchResult);
        startActivity(intent);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String> (this) {

            String mSearchResultsJSON;

            @Override
            protected void onStartLoading() {
                if (args != null) {
                    if (mSearchResultsJSON != null) {
                        Log.d(TAG, "AsyncTaskLoader delivering cached results");
                        deliverResult(mSearchResultsJSON);
                    } else {
                        mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }
            }

            @Override
            public String loadInBackground() {
                if (args != null) {
                    String newsSearchUrl = args.getString(SEARCH_URL_KEY);
                    Log.d(TAG, "AsyncTaskLoader making network call: " + newsSearchUrl);
                    String searchResults = null;
                    try {
                        searchResults = NetworkUtils.doHTTPGet(newsSearchUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return searchResults;
                } else {
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mSearchResultsJSON = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "AsyncTaskLoader's onLoadFinished called");
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if (data != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mSearchResultsRV.setVisibility(View.VISIBLE);
            mSearchResultsList = NewsUtils.parseNewsSearchResultsJSON(data);
            mNewsAdapter.updateSearchResults(mSearchResultsList);
        } else {
            mSearchResultsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }
}
