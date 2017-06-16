package com.example.android.vigi;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
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
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.graphics.Typeface


import com.example.android.vigi.utils.NewsUtils;
import com.example.android.vigi.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NewsAdapter.OnSearchResultClickListener,
        LoaderManager.LoaderCallbacks<String>, NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


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


        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mSearchResultsList = null;

        mSearchBoxET = (EditText)findViewById(R.id.et_search_box);
        mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mNewsAdapter = new NewsAdapter(this);
        mSearchResultsRV.setAdapter(mNewsAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

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

        NavigationView navigationView = (NavigationView)findViewById(R.id.nv_navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void doNews (String searchQuery){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String sort =
                sharedPreferences.getString(
                        getString(R.string.pref_sort_key),
                        getString(R.string.pref_sort_default)
                );
        String category =
                sharedPreferences.getString(
                        getString(R.string.pref_category_key),
                        getString(R.string.pref_category_default)
                );

        TextView text = (TextView) findViewById(R.id.tv_search_description);
        Typeface font = Typeface.createFromAsset(getAssets(), "yourfont.ttf");
        text.setTypeface(font);

        searchQuery = check_query(searchQuery);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_saved_search_results:
                mDrawerLayout.closeDrawers();
                Intent savedResultsIntent = new Intent(this, SavedSearchResultsActivity.class);
                startActivity(savedResultsIntent);
                return true;
            case R.id.nav_settings:
                mDrawerLayout.closeDrawers();
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return false;
        }
    }

    public String check_query(String searchQuery){
        if(searchQuery.equals("abc") || searchQuery.equals("ABC"))
                return "abc-news-au";
        else if(searchQuery.equals("bbc") || searchQuery.equals("BBC"))
                return "bbc-news";
        else if(searchQuery.equals("bbc") || searchQuery.equals("BBC"))
            return "bbc-news";
        else if(searchQuery.equals("cnn") || searchQuery.equals("CNN"))
            return "cnn";
        else if(searchQuery.equals("bbc") || searchQuery.equals("BBC"))
            return "bbc-news";
        else if(searchQuery.equals("bbc") || searchQuery.equals("BBC"))
            return "bbc-news";
        else if(searchQuery.equals("financial times") || searchQuery.equals("Finanical Times"))
            return "financial-times";
        else if(searchQuery.equals("google news") || searchQuery.equals("Google News"))
            return "google-news";
        else if(searchQuery.equals("usa today") || searchQuery.equals("USA Today"))
            return "usa-today";
        else if(searchQuery.equals("time") || searchQuery.equals("Time"))
            return "Time";
        else
            return searchQuery;
    }
}
