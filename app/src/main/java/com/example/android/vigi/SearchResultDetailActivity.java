package com.example.android.vigi;

/**
 * Created by Jacky on 5/7/17.
 */
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.os.AsyncTask;
import com.example.android.vigi.utils.NewsUtils;
import android.support.v7.widget.RecyclerView;

import java.io.InputStream;


public class SearchResultDetailActivity extends AppCompatActivity implements NewsResultItemAdapter{
    private RecyclerView mSearchResultItemRV;
    private NewsResultItemAdapter mNewsItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultItemRV = (RecyclerView)findViewById(R.id.rv_search_item);

        mSearchResultItemRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultItemRV.setHasFixedSize(true);

        mNewsItemAdapter = new NewsResultItemAdapter(this);
        mSearchResultItemRV.setAdapter(mNewsItemAdapter);

    }

    /*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
