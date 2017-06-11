package com.example.android.vigi;

/**
 * Created by Jacky on 5/7/17.
 */
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.android.vigi.utils.NewsUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.os.AsyncTask;

import java.io.InputStream;

public class SearchResultDetailActivity extends AppCompatActivity {
    private TextView mSearchResultTitleTV;
    private TextView mSearchResultDateTV;
    private TextView mSearchResultAuthorTV;
    private TextView mSearchResultDescriptionTV;
    private ImageView mSearchResultImageTV;
    private NewsUtils.SearchResult mSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultTitleTV = (TextView)findViewById(R.id.tv_search_title);
        mSearchResultDateTV = (TextView)findViewById(R.id.tv_search_date);
        mSearchResultAuthorTV = (TextView)findViewById(R.id.tv_search_author);
        mSearchResultDescriptionTV = (TextView)findViewById(R.id.tv_search_description);
        mSearchResultImageTV = (ImageView)findViewById(R.id.tv_search_image);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(NewsUtils.SearchResult.EXTRA_SEARCH_RESULT)) {
            mSearchResult = (NewsUtils.SearchResult) intent.getSerializableExtra(NewsUtils.SearchResult.EXTRA_SEARCH_RESULT);
            mSearchResultTitleTV.setText(mSearchResult.title);
            mSearchResultDateTV.setText(mSearchResult.date);
            mSearchResultAuthorTV.setText("by " + mSearchResult.author);
            mSearchResultDescriptionTV.setText(mSearchResult.description);
            new DownloadImageTask(mSearchResultImageTV).execute(mSearchResult.image);

        }
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

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_in_map:
                viewRepoOnInMap();
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

    public void viewRepoOnInMap() {
        if (mSearchResult != null) {
            Uri repoUri = Uri.parse("http://maps.google.com/maps?q=" + "corvallis");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, repoUri);
            if (webIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(webIntent);
            }
        }
    }

    public void shareRepo() {
        if (mSearchResult != null) {
            String shareText = mSearchResult.date + mSearchResult.temperature + mSearchResult.weather;
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }
*/
}
