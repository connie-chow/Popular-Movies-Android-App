package com.example.project2_movies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// Adapter for Movie Trailers
// Takes from RecyclerView's Adapter which takes TrailerViewHolders
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<MovieRoom> mMovies;
    private ArrayList<String> mTrailerURLs;


    // TrailerAdapter constructor
    public TrailerAdapter(Context context, ArrayList<MovieRoom> movies) {
        super();
        this.mContext = context;
        this.mMovies = new ArrayList<MovieRoom>(movies);
        this.mTrailerURLs = new ArrayList<String>();
        inflater = LayoutInflater.from(context);
    }


    // Construct
    public int getCount() {
        return mMovies.size();
    }
    public void clear() {
        mMovies.clear();
    }
    public MovieRoom getItem(int position) {
        return mMovies.get(position);
    }
    public long getItemId(int position) {
        return 0;
    }
    public void setList(ArrayList<MovieRoom> movies) {
        mMovies.addAll(movies);
    }

    public void add(String url) {
        mTrailerURLs.add(url);
        notifyDataSetChanged();
    }


    // Constructor
    public TrailerAdapter(Context c) {
        mContext = c;
    }


    /////////////////////////////////////////////////////////////////////
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Initialize the specific view for this MovieViewHolder

        // Reference the trailer item and then inflate it
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        // Assign reference to this view to the ViewHolder
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TrailerAdapter.TrailerViewHolder viewHolder = new TrailerAdapter.TrailerViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position) {
        //Bind each item to the correspond views
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return mTrailerURLs.size();
    }



    //Create a public interface to handle movie item clicks

    public interface MovieItemClicked {

        void onMovieItemClicked(Movie movie, ImageAdapter.MovieViewHolder holder);

    }




    // TRAILER VIEW HOLDER/////////////////////////////////////////////////////////////////////
    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTrailerItemView;

        // Use TrailerViewHolder constructor to assign reference to the trailer item
        public TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerItemView = itemView.findViewById(R.id.trailer_tv);
            mTrailerItemView.setOnClickListener(this);
        }



        void bind(int listIndex) {
            String trailer_desc = "Trailer " + String.valueOf(listIndex);
            mTrailerItemView.setText(trailer_desc);
        }


        @Override
        public void onClick(View v) {

            //https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
            String m = mTrailerURLs.get(getAdapterPosition());
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(m));
            Intent id = i.putExtra("id", m);
            mContext.startActivity(i);

        }
    }
}
