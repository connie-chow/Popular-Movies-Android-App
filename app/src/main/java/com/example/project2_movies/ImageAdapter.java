package com.example.project2_movies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


// change to Base Recycler Adapter
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MovieViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;

    private String[] imageUrls;
    private ArrayList<MovieRoom> mMovies; // use cursor

    public ImageAdapter(Context context, ArrayList<MovieRoom> movies) {
        //super(context, R.layout.grid_item_movie, R.id.list_item_movie_imageview, imageUrls);
        super();
        this.mContext = context;
        //this.imageUrls = imageUrls;
        this.mMovies = new ArrayList<MovieRoom>(movies);

        // what does this inflater do?
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

    public void setList(List<MovieRoom> movies)
    {
        //mMovies.addAll(movies);
        if(movies != null){
            for(MovieRoom movieObj : movies) {
                mMovies.add(movieObj);
            }
            //mMovieAdapter.notifyDataSetChanged();
            this.notifyDataSetChanged();
        }
    }


    public void add(MovieRoom m) {

        String poster_path = m.getPoster_path();
        String adult = m.getAdult();
        String overview = m.getOverview();
        String release_date = m.getRelease_date();
        String genre_ids = m.getGenre_ids();
        String m_id = m.getM_id();
        String original_title = m.getOriginal_title();
        String original_language = m.getOriginal_language();
        String title = m.getTitle();
        String backdrop_path = m.getBackdrop_path();
        String popularity = m.getPopularity();
        String vote_count = m.getVote_count();
        String vote_average = m.getVote_average();
        String video = m.getVideo();
        boolean favorite = m.getFavorite();

        mMovies.add(m);
        notifyDataSetChanged();
    }


    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }


    /*
    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.full_image, parent, false);
        }

        MovieRoom m = mMovies.get(position);
        String url = m.getPoster_path();
        url = "http://image.tmdb.org/t/p/w185/" + url;

        Picasso
                .get()
                .load(url)
                //.fit() // will explain later
                .into((ImageView) convertView);

        return convertView;
    }

*/

    /////////////////////////////////////////////////////////////////////
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Initialize the specific view for this MovieViewHolder
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.full_image;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        //Bind each item to the correspond views
        holder.bind(mMovies.get(position));
    }


    @Override
    public int getItemCount()
    {
        //return mMovies.size();
        return mMovies != null ? mMovies.size(): 0;
    }


    //Create a public interface to handle movie item clicks
    public interface MovieItemClicked {

        void onMovieItemClicked(Movie movie, MovieViewHolder holder);

    }




    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mMovieThumbnail;

        // Constructor: Assign reference to grid element and set a click listener to it
        MovieViewHolder(View itemView) {
            super(itemView);
            mMovieThumbnail = itemView.findViewById(R.id.full_image_view);
            mMovieThumbnail.setOnClickListener(this);
        }


        // Bind the ViewHolder to Adapter content by assigning adapter movie data to the movie
        // thumbnail UI element reference
        void bind(MovieRoom movie) {
            //Use Picasso to load each image
            // load from adapter variable the particular movie
            Picasso.get().setLoggingEnabled(true);
            // when i changed http to https, then the image loaded
            //Picasso.get().load("https://i.imgur.com/DvpvklR.png").into(mMovieThumbnail);
            String movie_url = "https://image.tmdb.org/t/p/w185/" + movie.getPoster_path();
            Picasso.get().load(movie_url).placeholder(R.drawable.ic_action_name)
                    .noFade()
                    .into(mMovieThumbnail);
            //https://github.com/square/picasso/issues/427
        }


        @Override
        public void onClick(View v) {
            //https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
            //We need to send the proper url for the image so that when the DetailActivity opens it has the proper image url
            //setProperMoviePosterUrl(getAdapterPosition());

            //Send now the movie object with the proper movie poster url
            //mMovieItemClicked.onMovieItemClicked(mMovies.get(getAdapterPosition()), this);
            Intent i = new Intent(mContext, MovieDetailActivity.class);
            // passing array index
            MovieRoom m = mMovies.get(getAdapterPosition());
            Intent id = i.putExtra("id", m.getM_id());
            mContext.startActivity(i);
        }
    }
}
