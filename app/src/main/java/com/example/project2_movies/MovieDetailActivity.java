package com.example.project2_movies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "error:";
    private AppDatabase mDb;
    ImageButton imgButton;
    private TrailerAdapter mMovieTrailersAdapter;
    private ReviewsAdapter mMovieReviewsAdapter;
    private int movie_id;
    private Movie m;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        RecyclerView trailersView = (RecyclerView) findViewById(R.id.rv_trailers);
        // tell layout manager we want it to layout contents of recycler view as Linear Layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        trailersView.setLayoutManager(layoutManager);
        mMovieTrailersAdapter = new TrailerAdapter(this, new ArrayList<MovieRoom>());


        RecyclerView reviewsView = (RecyclerView) findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(this);
        reviewsView.setLayoutManager(layoutManagerReviews);
        mMovieReviewsAdapter = new ReviewsAdapter(this, new ArrayList<Pair<String, String>>());
        reviewsView.setAdapter(mMovieReviewsAdapter);



        // Instance of ImageAdapter Class
        // has to be recycler view so that gridView is treated as the returned object type
        // connect this adapter to the gridview where data will be fed to the recycler view
        trailersView.setAdapter(mMovieTrailersAdapter);



        mDb = AppDatabase.getInstance(getApplicationContext());


        // get intent data
        Intent i = getIntent();
        //Movie m = (Movie) i.getParcelableExtra("id");
        //m = (Movie) i.getParcelableExtra("id");

        // Selected image id
        Bundle extras = i.getExtras();
        final String m_id = i.getExtras().getString("id");



        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final MovieRoom m = mDb.movieDao().loadMovieByMId(m_id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // to do
                        populateUI(m);
                    }

                    // New data is back from the server. Hooray!
                    //mMovieAdapter.notifyDataSetChanged();
                });
            }
        });



        /*
        ImageAdapter imageAdapter = new ImageAdapter(this);

        ImageView imageView = (ImageView) findViewById(R.id.iv_movie_poster);
        //Movie m = imageAdapter.getItem(position);
        String movie_url = "https://image.tmdb.org/t/p/w185/" + m.getPoster_path();
        Picasso.get().load(movie_url).into(imageView);

        String overview = m.getOverview();
        String release_date = m.getRelease_date();
        String original_title = m.getOriginal_title();
        String vote_average = m.getVote_average();

        TextView overview_tv = (TextView) findViewById(R.id.tv_overview);
        TextView release_date_tv = (TextView) findViewById((R.id.release_date_tv));
        TextView original_title_tv = (TextView) findViewById((R.id.tv_original_title));
        TextView vote_average_tv = (TextView) findViewById((R.id.vote_avg_tv));

        overview_tv.setText(overview);
        release_date_tv.setText(release_date);
        original_title_tv.setText(original_title);
        vote_average_tv.setText(vote_average);

        fetch_trailers(m.getM_id());
        fetch_reviews(m.getM_id());

        TextView favButton =(TextView)findViewById(R.id.tv_favorite);



        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Marked as favorite",Toast.LENGTH_LONG).show();
                // write to Room DB with user ID and movie ID
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int i = Integer.parseInt(m.getM_id());
                        //final MovieRoom movie = mDb.movieDao().loadMovieByMId(m.id);
                        //List<MovieRoom> moviesgetall = mDb.movieDao().getAllMovies();
                        final MovieRoom movie = mDb.movieDao().loadMovieByMId(m.getM_id());
                        //movie.setFavorite(true);
                        //mDb.movieDao().updateMovie(m.id, true);
                        MovieRoom movie2 = mDb.movieDao().loadMovieByMId(m.getM_id());
                        List<MovieRoom> movieslist = mDb.movieDao().loadFavoriteMovies(true);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                update_favorite();
                                //final MovieRoom movie = mDb.movieDao().loadMovieByMId(m.id);
                                //movie.setFavorite(true);
                                //mDb.movieDao().updateMovie(movie.getM_id(), true);
                                //MovieRoom movie2 = mDb.movieDao().loadMovieByMId(m.id);
                            }
                        });
                    }
                });
            }
        });

*/
    }



    public void populateUI(final MovieRoom m) {
        ImageAdapter imageAdapter = new ImageAdapter(this);

        ImageView imageView = (ImageView) findViewById(R.id.iv_movie_poster);
        //Movie m = imageAdapter.getItem(position);
        String movie_url = "https://image.tmdb.org/t/p/w185/" + m.getPoster_path();
        Picasso.get().load(movie_url).into(imageView);

        String overview = m.getOverview();
        String release_date = m.getRelease_date();
        String original_title = m.getOriginal_title();
        String vote_average = m.getVote_average();

        TextView overview_tv = (TextView) findViewById(R.id.tv_overview);
        TextView release_date_tv = (TextView) findViewById((R.id.release_date_tv));
        TextView original_title_tv = (TextView) findViewById((R.id.tv_original_title));
        TextView vote_average_tv = (TextView) findViewById((R.id.vote_avg_tv));

        overview_tv.setText(overview);
        release_date_tv.setText(release_date);
        original_title_tv.setText(original_title);
        vote_average_tv.setText(vote_average);

        fetch_trailers(m.getM_id());
        fetch_reviews(m.getM_id());

        TextView favButton =(TextView)findViewById(R.id.tv_favorite);

        if(m.getFavorite() == true) {
            favButton.setText("Favorite *");
        } else {
            favButton.setText("Mark as Favorite");
        }





        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Marked as favorite",Toast.LENGTH_LONG).show();
                // write to Room DB with user ID and movie ID


                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int i = Integer.parseInt(m.getM_id());
                        //final MovieRoom movie = mDb.movieDao().loadMovieByMId(m.id);
                        //List<MovieRoom> moviesgetall = mDb.movieDao().getAllMovies();
                        final MovieRoom movie = mDb.movieDao().loadMovieByMId(m.getM_id());
                        //movie.setFavorite(true);
                        if(movie.getFavorite() == false) {
                            mDb.movieDao().updateMovie(m.getM_id(), true);
                        } else {
                            mDb.movieDao().updateMovie(m.getM_id(), false);
                        }

                        final boolean movie_favorited = mDb.movieDao().loadMovieByMId(m.getM_id()).getFavorite();
                        //List<MovieRoom> movieslist = mDb.movieDao().loadFavoriteMovies(true);
                        //List<MovieRoom> movies_all = mDb.movieDao().getAllMovies();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                update_favorite(movie_favorited);
                                //final MovieRoom movie = mDb.movieDao().loadMovieByMId(m.id);
                                //movie.setFavorite(true);
                                //mDb.movieDao().updateMovie(movie.getM_id(), true);
                                //MovieRoom movie2 = mDb.movieDao().loadMovieByMId(m.id);
                            }
                        });
                    }
                });
            }

        });

    }

    public void update_favorite(boolean favorite) {
        // toggle favorites button to "Marked as Favorite"
        TextView favButton =(TextView)findViewById(R.id.tv_favorite);

        if(favorite == true) {
            favButton.setText("Favorite *");
        } else {
            favButton.setText("Mark as Favorite");
        }
    }


    public void fetch_trailers(String id) {
        MovieDetailActivity.FetchMovieTrailersTask trailersTask = new MovieDetailActivity.FetchMovieTrailersTask();
        trailersTask.execute(id);
    }


    public void fetch_reviews(String id) {
        MovieDetailActivity.FetchMovieReviewsTask detailsTask = new MovieDetailActivity.FetchMovieReviewsTask();
        detailsTask.execute(id);
    }


    ////////////////////////////////////////////////////////////////////////////////////
    // MOVIE REVIEWS FROM JSON
    ////////////////////////////////////////////////////////////////////////////////////
    private ArrayList<Pair<String, String>> getMovieReviewsFromJson(String movieJsonStr)
            throws JSONException {

        // done in background
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_KEY = "key";
        final String OWM_RESULTS = "results";
        final String OWM_AUTHOR = "author";
        final String OWM_CONTENT = "content";


        ArrayList<Pair<String, String>> resultList = new ArrayList<Pair<String, String>>();
        //ArrayList<String> resultList = new ArrayList<>();
        String author, content, poster_path, adult, overview, release_date, genre_ids, id, original_title, original_language, title, backdrop_path, popularity, vote_count, video, vote_average;

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray trailerArray = movieJson.getJSONArray(OWM_RESULTS);

        for (int i = 0; i < trailerArray.length(); i++) {

            // Get the JSON object representing the movie review entry
            JSONObject review = trailerArray.getJSONObject(i);
            author = review.getString(OWM_AUTHOR); //.getJSONObject(0);
            content = review.getString(OWM_CONTENT);
            resultList.add(new Pair<>(author, content));

            // update movie entry in Room DB
        }
        return resultList;

    }


    ////////////////////////////////////////////////////////////////////////////////////
    // MOVIE TRAILERS FROM JSON
    ////////////////////////////////////////////////////////////////////////////////////
    private ArrayList<String> getMovieTrailersFromJson(String movieJsonStr)
            throws JSONException {

        // done in background
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_KEY = "key";
        final String OWM_RESULTS = "results";
        String youtube_url = "";

        ArrayList<String> resultList = new ArrayList<>();
        String poster_path, adult, overview, release_date, genre_ids, id, original_title, original_language, title, backdrop_path, popularity, vote_count, video, vote_average;

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray trailerArray = movieJson.getJSONArray(OWM_RESULTS);

        for (int i = 0; i < trailerArray.length(); i++) {

            // Get the JSON object representing the movie trailer entry
            youtube_url = "";
            JSONObject trailer = trailerArray.getJSONObject(i);
            poster_path = trailer.getString(OWM_KEY); //.getJSONObject(0);
            youtube_url = "http://www.youtube.com/watch?v=" + poster_path;
            resultList.add(youtube_url);

            // update movie entry in Room DB
        }
        return resultList;

    }


    ////////////////////////////////////////////////////////////////////////////////////
    // MOVIE TRAILERS ASYNC TASK
    ////////////////////////////////////////////////////////////////////////////////////
    public class FetchMovieTrailersTask extends AsyncTask<String, Void, ArrayList<String>> {

        //youtube api key AIzaSyBQeCJPjHjtqKyExEEEi0vQT6V0Zsbf3_o, key=API_KEY


        @Override
        protected ArrayList<String> doInBackground(String... params) {

            //7ce66efa6f9aed94ee0c073d4e188678 API KEY from movie db
            //http://api.themoviedb.org/3/movie/popular?api_key=7ce66efa6f9aed94ee0c073d4e188678
            //http://api.themoviedb.org/3/movie/top_rated?api_key=7ce66efa6f9aed94ee0c073d4e188678
            //android.os.Debug.waitForDebugger();
            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            String youtube_url = "";
            Uri builtUri;

            try {
                String movie_id = params[0].toString();
                final String MOVIE_REVIEWS_BASE_URL =
                        "https://api.themoviedb.org/3/movie/" + movie_id + "/reviews?";
                final String MOVIE_TRAILER_BASE_URL =
                        "https://api.themoviedb.org/3/movie/" + movie_id + "/videos?";
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=cxLG2wtE7TM")));
                //use movie KEY field from ID drill down
                final String APPID_PARAM = "api_key";

                builtUri = Uri.parse(MOVIE_TRAILER_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, "d8504d1b057d88ce702a34af8c445832")
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                int code = urlConnection.getResponseCode();
                if (code != 200) {
                    throw new IOException("Invalid response from server: " + code);
                }

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                try { // why does connect() not skip to finally block when I put in this try?
                    // Read the input stream into a String
                    //https://developer.android.com/reference/java/net/HttpURLConnection
                    //https://stackoverflow.com/questions/13812051/httpsurlconnectionimpl-getinputstream-error-on-android-platform
                    InputStream inputStream = urlConnection.getInputStream();
                    int s = inputStream.available();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.

                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        Log.i("data", line);
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.

                    }
                    movieJsonStr = buffer.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Log.v(LOG_TAG, "Forecast string: " + movieJsonStr);
            } catch (IOException e) {
                //Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        // Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            try {
                return getMovieTrailersFromJson(movieJsonStr);
            } catch (JSONException e) {
                // Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {

            if (result != null) {
                mMovieTrailersAdapter.clear();
                for(String trailer : result) {
                    mMovieTrailersAdapter.add(trailer);
                }
                // New data is back from the server.  Hooray!
            }

            mMovieTrailersAdapter.notifyDataSetChanged();

        }
    }




    ///////////////////////////////////////////////////////////////////////////////////////
    // FETCH REVIEWS
    ///////////////////////////////////////////////////////////////////////////////////////
    public class FetchMovieReviewsTask extends AsyncTask<String, Void, ArrayList<Pair<String, String>>> {

        //youtube api key AIzaSyBQeCJPjHjtqKyExEEEi0vQT6V0Zsbf3_o, key=API_KEY


        @Override
        protected ArrayList<Pair<String, String>> doInBackground(String... params) {

            //7ce66efa6f9aed94ee0c073d4e188678 API KEY from movie db
            //http://api.themoviedb.org/3/movie/popular?api_key=7ce66efa6f9aed94ee0c073d4e188678
            //http://api.themoviedb.org/3/movie/top_rated?api_key=7ce66efa6f9aed94ee0c073d4e188678
            //android.os.Debug.waitForDebugger();
            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            String youtube_url = "";
            Uri builtUri;

            try {
                String movie_id = params[0].toString();
                final String MOVIE_REVIEWS_BASE_URL =
                        "https://api.themoviedb.org/3/movie/" + movie_id + "/reviews?";

                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=cxLG2wtE7TM")));
                //use movie KEY field from ID drill down
                final String APPID_PARAM = "api_key";

                builtUri = Uri.parse(MOVIE_REVIEWS_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, "d8504d1b057d88ce702a34af8c445832")
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                int code = urlConnection.getResponseCode();
                if (code != 200) {
                    throw new IOException("Invalid response from server: " + code);
                }

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                try { // why does connect() not skip to finally block when I put in this try?
                    // Read the input stream into a String
                    //https://developer.android.com/reference/java/net/HttpURLConnection
                    //https://stackoverflow.com/questions/13812051/httpsurlconnectionimpl-getinputstream-error-on-android-platform
                    InputStream inputStream = urlConnection.getInputStream();
                    int s = inputStream.available();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.

                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        Log.i("data", line);
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.

                    }
                    movieJsonStr = buffer.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Log.v(LOG_TAG, "Forecast string: " + movieJsonStr);
            } catch (IOException e) {
                //Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        // Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            try {
                return getMovieReviewsFromJson(movieJsonStr);
            } catch (JSONException e) {
                // Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<Pair<String, String>> result) {

            if (result != null) {
                mMovieReviewsAdapter.clear();

                for (int i = 0; i < result.size() - 1; i++) {
                    Pair<String, String> entry = result.get(i);
                    String author = entry.first;
                    String content = entry.second;
                    mMovieReviewsAdapter.add(author, content);
                }

            }

            mMovieReviewsAdapter.notifyDataSetChanged();

        }
    }
}
