package com.example.project2_movies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    public ArrayList<Pair<String, String>> mReviews;


    // TrailerAdapter constructor
    public ReviewsAdapter(Context context, ArrayList<Pair<String, String>> reviews) {
        super();
        this.mContext = context;
        this.mReviews = new ArrayList<Pair<String, String>>(reviews);
        inflater = LayoutInflater.from(context);
    }


    // Construct
    //https://stackoverflow.com/questions/33784369/recyclerview-get-view-at-particular-position
    public int getCount() {
        return mReviews.size();
    }
    public void clear() {
        mReviews.clear();
    }
    public Pair<String, String> getItem(int position) {
        return mReviews.get(position);
    }
    public long getItemId(int position) {
        return 0;
    }
    public void setList(ArrayList<Pair<String, String>> reviews) {
        mReviews.addAll(reviews);
    }

    public void add(String author, String content) {
        mReviews.add(new Pair<>(author, content));
        notifyDataSetChanged();
    }


    // Constructor
    public ReviewsAdapter(Context c) {
        mContext = c;
    }


    /////////////////////////////////////////////////////////////////////
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Initialize the specific view for this MovieViewHolder

        // Reference the trailer item and then inflate it
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        // Assign reference to this view to the ViewHolder
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ReviewsAdapter.ReviewViewHolder viewHolder = new ReviewsAdapter.ReviewViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        //Bind each item to the correspond views
        // ReviewsAdapter. preceded
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return mReviews.size();
    }



    public interface MovieItemClicked {

        void onMovieItemClicked(Movie movie, ImageAdapter.MovieViewHolder holder);

    }




    // TRAILER VIEW HOLDER/////////////////////////////////////////////////////////////////////
    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView mTrailerItemView;
        public TextView mTrailerItemContentView;

        // Use TrailerViewHolder constructor to assign reference to the trailer item
        public ReviewViewHolder(View itemView) {
            super(itemView);
            mTrailerItemView = itemView.findViewById(R.id.review_tv);
            mTrailerItemContentView = itemView.findViewById(R.id.review_content_tv);
        }



        void bind(int listIndex) {
            // update here to bind the author + content
            // get it from Room DB
            //String trailer_desc = "Trailer " + String.valueOf(listIndex);
            Pair<String, String> review = mReviews.get(listIndex);
            String author = review.first;
            String content = review.second;
            String text = author + "\n" + content;
            //review.toString()
            mTrailerItemView.setText(author);
            mTrailerItemContentView.setText(content);
            mTrailerItemView.setTypeface(null, Typeface.BOLD);
            mTrailerItemView.setTextSize(15);
            mTrailerItemView.setPadding(0, 0, 0, 7);
            mTrailerItemContentView.setPadding(0, 0, 0, 50);
        }
    }
}

