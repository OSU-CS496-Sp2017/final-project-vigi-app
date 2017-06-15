package com.example.android.vigi;

/**
 * Created by Jacky on 5/7/17.
 */
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.os.AsyncTask;
import android.view.View;


import java.io.InputStream;

import com.example.android.vigi.utils.NewsUtils;
import com.example.android.vigi.data.NewsSearchContract;
import com.example.android.vigi.data.NewsSearchDBHelper;

public class SearchResultDetailActivity extends AppCompatActivity {

    private ImageView mSearchResultBookmarkIV;
    private TextView mSearchResultTitleTV;
    private TextView mSearchResultDateTV;
    private TextView mSearchResultAuthorTV;
    private TextView mSearchResultDescriptionTV;
    private ImageView mSearchResultImageTV;
    private TextView mSearchResultUrlTV;
    private NewsUtils.SearchResult mSearchResult;
    private SQLiteDatabase mDB;
    private boolean mIsBookmarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultBookmarkIV = (ImageView)findViewById(R.id.iv_search_result_bookmark);
        mSearchResultTitleTV = (TextView)findViewById(R.id.tv_search_title);
        mSearchResultDateTV = (TextView)findViewById(R.id.tv_search_date);
        mSearchResultAuthorTV = (TextView)findViewById(R.id.tv_search_author);
        mSearchResultDescriptionTV = (TextView)findViewById(R.id.tv_search_description);
        mSearchResultImageTV = (ImageView)findViewById(R.id.tv_search_image);
        mSearchResultUrlTV = (TextView)findViewById(R.id.tv_search_url);

        NewsSearchDBHelper dbHelper = new NewsSearchDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(NewsUtils.SearchResult.EXTRA_SEARCH_RESULT)) {
            mSearchResult = (NewsUtils.SearchResult) intent.getSerializableExtra(NewsUtils.SearchResult.EXTRA_SEARCH_RESULT);
            mSearchResultTitleTV.setText(mSearchResult.title);
            mSearchResultDateTV.setText(mSearchResult.date);
            mSearchResultAuthorTV.setText("by " + mSearchResult.author);
            mSearchResultDescriptionTV.setText(mSearchResult.description);
            new DownloadImageTask(mSearchResultImageTV).execute(mSearchResult.image);
            mSearchResultUrlTV.setText("Link: " + mSearchResult.url);

            mIsBookmarked = checkSearchResultIsInDB();
            updateBookmarkIconState();

            //updateStarCountInDB();

        }

        mSearchResultBookmarkIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsBookmarked = !mIsBookmarked;
                updateSearchResultInDB();
                updateBookmarkIconState();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mDB.close();
        super.onDestroy();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_new:
                viewNewOnWeb();
                return true;
            case R.id.action_share:
                shareRepo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_result_detail, menu);
        return true;
    }

    public void viewNewOnWeb() {
        if (mSearchResult != null) {
            Uri repoUri = Uri.parse(mSearchResult.url);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, repoUri);
            if (webIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(webIntent);
            }
        }
    }


    public void shareRepo() {
        if (mSearchResult != null) {
            String shareText = mSearchResult.description;
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }

    private void updateBookmarkIconState() {
        if (mIsBookmarked) {
            mSearchResultBookmarkIV.setImageResource(R.drawable.ic_bookmark_black_48dp);
        } else {
            mSearchResultBookmarkIV.setImageResource(R.drawable.ic_bookmark_border_black_48dp);
        }
    }

    private void updateSearchResultInDB() {
        if (mIsBookmarked) {
            addSearchResultToDB();
        } else {
            deleteSearchResultFromDB();
        }
    }

    private long addSearchResultToDB() {
        if (mSearchResult != null) {
            ContentValues values = new ContentValues();
            values.put(NewsSearchContract.FavoriteNews.COLUMN_TITLE, mSearchResult.title);
            values.put(NewsSearchContract.FavoriteNews.COLUMN_DESCRIPTION, mSearchResult.description);
            values.put(NewsSearchContract.FavoriteNews.COLUMN_URL, mSearchResult.url);
            values.put(NewsSearchContract.FavoriteNews.COLUMN_AUTHOR, mSearchResult.author);
            values.put(NewsSearchContract.FavoriteNews.COLUMN_DATE, mSearchResult.date);
            return mDB.insert(NewsSearchContract.FavoriteNews.TABLE_NAME, null, values);
        } else {
            return -1;
        }
    }
    private void deleteSearchResultFromDB() {
        if (mSearchResult != null) {
            String sqlSelection = NewsSearchContract.FavoriteNews.COLUMN_TITLE + " = ?";
            String[] sqlSelectionArgs = { mSearchResult.title };
            mDB.delete(NewsSearchContract.FavoriteNews.TABLE_NAME, sqlSelection, sqlSelectionArgs);
        }
    }

    private boolean checkSearchResultIsInDB() {
        boolean isInDB = false;
        if (mSearchResult != null) {
            String sqlSelection = NewsSearchContract.FavoriteNews.COLUMN_TITLE + " = ?";
            String[] sqlSelectionArgs = { mSearchResult.title };
            Cursor cursor = mDB.query(
                    NewsSearchContract.FavoriteNews.TABLE_NAME,
                    null,
                    sqlSelection,
                    sqlSelectionArgs,
                    null,
                    null,
                    null
            );
            isInDB = cursor.getCount() > 0;
            cursor.close();
        }
        return isInDB;
    }

    /*private void updateStarCountInDB() {
        if (mSearchResult != null) {
            String sqlSelection = NewsSearchContract.FavoriteNews.COLUMN_TITLE + " = ?";
            String[] sqlSelectionArgs = { mSearchResult.title };
            ContentValues values = new ContentValues();
            values.put(NewsSearchContract.FavoriteNews.COLUMN_AUTHOR, mSearchResult.author);
            mDB.update(NewsSearchContract.FavoriteNews.TABLE_NAME, values, sqlSelection, sqlSelectionArgs);
        }
    }*/

}
