package com.example.android.vigi;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.vigi.utils.NewsUtils;

import java.util.ArrayList;
/**
 * Created by Jason on 6/10/17.
 */

public class NewsResultItemAdapter extends RecyclerView.Adapter<NewsResultItemAdapter.NewsResultItemViewHolder> {
    private ArrayList<NewsUtils.SearchResult> mSearchResultsList;
    private NewsResultItemAdapter.OnSearchResultClickListener mSearchResultClickListener;

    public NewsResultItemAdapter(NewsResultItemAdapter.OnSearchResultClickListener clickListener) {
        mSearchResultClickListener = clickListener;
    }

    public void updateSearchResults(ArrayList<NewsUtils.SearchResult> searchResultsList) {
        mSearchResultsList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mSearchResultsList != null) {
            return mSearchResultsList.size();
        } else {
            return 0;
        }
    }

    @Override
    public NewsResultItemAdapter.NewsResultItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news_list_item, parent, false);
        return new NewsResultItemAdapter.NewsResultItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsResultItemAdapter.NewsResultItemViewHolder holder, int position) {
        holder.bind(mSearchResultsList.get(position));
    }

    public interface OnSearchResultClickListener {
        void onSearchResultClick(NewsUtils.SearchResult searchResult);
    }

    class NewsResultItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSearchResultTitleTV;
        private TextView mSearchResultDateTV;
        private TextView mSearchResultAuthorTV;
        private TextView mSearchResultDescriptionTV;
        private ImageView mSearchResultImageTV;


        public NewsResultItemViewHolder(View itemView) {
            super(itemView);
            mSearchResultTitleTV = (TextView)findViewById(R.id.tv_search_title);
            mSearchResultDateTV = (TextView)findViewById(R.id.tv_search_date);
            mSearchResultAuthorTV = (TextView)findViewById(R.id.tv_search_author);
            mSearchResultDescriptionTV = (TextView)findViewById(R.id.tv_search_description);
            mSearchResultImageTV = (ImageView) findViewById(R.id.tv_search_image);
        }

        public void bind(NewsUtils.SearchResult searchResult) {
            mSearchResultTitleTV.setText(searchResult.title);
            mSearchResultDateTV.setText(searchResult.date);
            mSearchResultAuthorTV.setText("by " + searchResult.author);
            mSearchResultDescriptionTV.setText(searchResult.description);
        }

        @Override
        public void onClick(View v) {
            NewsUtils.SearchResult searchResult = mSearchResultsList.get(getAdapterPosition());
            mSearchResultClickListener.onSearchResultClick(searchResult);
        }
    }
}
