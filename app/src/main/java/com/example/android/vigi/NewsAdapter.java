package com.example.android.vigi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.vigi.utils.NewsUtils;

import java.util.ArrayList;

/**
 * Created by hessro on 4/25/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder> {

    private ArrayList<NewsUtils.SearchResult> mSearchResultsList;
    private OnSearchResultClickListener mSearchResultClickListener;

    public NewsAdapter(OnSearchResultClickListener clickListener) {
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
    public NewsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news_list_item, parent, false);
        return new NewsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsItemViewHolder holder, int position) {
        holder.bind(mSearchResultsList.get(position));
    }

    public interface OnSearchResultClickListener {
        void onSearchResultClick(NewsUtils.SearchResult searchResult);
    }

    class NewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNewsTextView;

        public NewsItemViewHolder(View itemView) {
            super(itemView);
            mNewsTextView = (TextView)itemView.findViewById(R.id.tv_news_text);
            itemView.setOnClickListener(this);
        }

        public void bind(NewsUtils.SearchResult searchResult) {
            mNewsTextView.setText(searchResult.title);
        }

        @Override
        public void onClick(View v) {
            NewsUtils.SearchResult searchResult = mSearchResultsList.get(getAdapterPosition());
            mSearchResultClickListener.onSearchResultClick(searchResult);
        }
    }
}
