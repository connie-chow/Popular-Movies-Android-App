package com.example.project2_movies;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-4-saving-user-data/lesson-10-storing-data-with-room/10-1-c-room-livedata-viewmodel/10-1-c-room-livedata-viewmodel.html#chapterstart
//https://codelabs.developers.google.com/codelabs/android-training-livedata-viewmodel/#0
//https://google-developer-training.github.io/android-developer-advanced-course-practicals/unit-6-working-with-architecture-components/lesson-14-room,-livedata,-viewmodel/14-1-b-room-delete-data/14-1-b-room-delete-data.html#task2intro


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

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

public class MainActivity extends AppCompatActivity {

    private ImageAdapter mMovieAdapter;
    private AppDatabase mDb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // what is it doing here?
        //https://stackoverflow.com/questions/25477860/error-inflating-class-android-support-v7-widget-recyclerview
        setContentView(R.layout.grid_layout);

        // get reference to the recycler view (grid layout)
        //https://stackoverflow.com/questions/25477860/error-inflating-class-android-support-v7-widget-recyclerview
        RecyclerView gridView = (RecyclerView) findViewById(R.id.rv_images);

        // tell layout manager we want it to layout contents of recycler view as grid
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);

        // create new adapter object which will translate the database movies json to the recycler view
        mMovieAdapter = new ImageAdapter(this, new ArrayList<MovieRoom>());

        // Instance of ImageAdapter Class
        // has to be recycler view so that gridView is treated as the returned object type
        // connect this adapter to the gridview where data will be fed to the recycler view
        gridView.setAdapter(mMovieAdapter);

        // get sort type
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMovies("top rated");
            }
        });


        Button b_pop = (Button) findViewById(R.id.button_popular);
        b_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMovies("popular");
            }
        });


        // Favorites Button
        Button b_favorites = (Button) findViewById(R.id.button_favorite);
        fetch_favorites();

        b_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetch_favorites();
            }
        });



        updateMovies("popular");

        mDb = AppDatabase.getInstance(getApplicationContext());



        /**
         * On Click event for Single Gridview Item
         * */
        /*
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                // passing array index
                i.putExtra("id", position);
                startActivity(i);
            }
        });
        */

    }



    @Override
    public void onStart() {
        super.onStart();
        //updateMovies();
    }


    private void fetch_favorites() {
        //final LiveData<List<MovieRoom>> movies = mDb.movieDao().loadFavoriteMovies(true);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getFavoriteMovies().observe(this, new Observer<List<MovieRoom>>() {
            @Override
            public void onChanged(@Nullable List<MovieRoom> movieEntries) {
                //Log.d(TAG, "Receiving database update from LiveData");
                mMovieAdapter.clear();
                mMovieAdapter.setList(movieEntries);
            }
        });
    }



    private void updateMovies(String sortType) {
        FetchMovieTask weatherTask = new FetchMovieTask();
        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));*/
        //weatherTask.execute("filler");
        weatherTask.execute(sortType);
    }


    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieRoom>> {

        //youtube api key AIzaSyBQeCJPjHjtqKyExEEEi0vQT6V0Zsbf3_o, key=API_KEY

        @Override
        protected ArrayList<MovieRoom> doInBackground(String... params)
        {

            //https://stackoverflow.com/questions/20239386/how-to-parse-data-from-2-different-urls-by-asynctask-method
            //7ce66efa6f9aed94ee0c073d4e188678 API KEY from movie db
            //http://api.themoviedb.org/3/movie/popular?api_key=7ce66efa6f9aed94ee0c073d4e188678
            //http://api.themoviedb.org/3/movie/top_rated?api_key=7ce66efa6f9aed94ee0c073d4e188678
            //android.os.Debug.waitForDebugger();
            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0)
            {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            Uri builtUri;

            try
            {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                // why did adding an s to http work? to get connection
                final String MOVIE_TOPRATED_BASE_URL =
                        "https://api.themoviedb.org/3/movie/top_rated?";
                final String MOVIE_POPULAR_BASE_URL =
                        "https://api.themoviedb.org/3/movie/popular?";
                final String MOVIE_REVIEWS_BASE_URL =
                        "https://api.themoviedb.org/3/movie/popular?";
                final String MOVIE_TRAILER_BASE_URL =
                        "https://api.themoviedb.org/3/movie/popular?";
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=cxLG2wtE7TM")));
                //use movie KEY field from ID drill down
                final String APPID_PARAM = "api_key";
                final String APPID_PAGE = "page";



                    if (params[0].equals("popular"))
                    {
                        // .appendQueryParameter(APPID_PAGE, params[1])
                        builtUri = Uri.parse(MOVIE_POPULAR_BASE_URL).buildUpon()
                                .appendQueryParameter(APPID_PARAM, "d8504d1b057d88ce702a34af8c445832")
                                .build();
                    }
                    else
                    {
                        //MOVIE_TOPRATED_BASE_URL
                        builtUri = Uri.parse(MOVIE_TOPRATED_BASE_URL).buildUpon()
                                .appendQueryParameter(APPID_PARAM, "d8504d1b057d88ce702a34af8c445832")
                                .build();
                    }

                    URL url = new URL(builtUri.toString());

                    //Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                    // Create the request to OpenWeatherMap, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    // why did connect() work after uninstalling app in emulator and then reinstalling
                    // and then adding network security to androidmanifest?
                    //https://developer.android.com/training/basics/network-ops/connecting
                    // https://stackoverflow.com/questions/56266801/java-net-socketexception-socket-failed-eperm-operation-not-permitted
                    // https://stackoverflow.com/questions/31762955/asynctask-permission-denied-missing-internet-permission
                    //https://stackoverflow.com/questions/45940861/android-8-cleartext-http-traffic-not-permitted
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    int s = inputStream.available();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null)
                    {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0)
                    {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    movieJsonStr = buffer.toString();
                    //Log.v(LOG_TAG, "Forecast string: " + movieJsonStr);


            }
            catch(IOException e)
            {
                //Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }
            finally
            {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                }
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch (final IOException e)
                    {
                       // Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try
            {
                return getMovieDataFromJson(movieJsonStr);
            }
            catch (JSONException e)
            {
                // Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }



        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private ArrayList<MovieRoom> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            // done in background
            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_ADULT = "adult";
            final String OWM_OVERVIEW = "overview";
            final String OWM_RELEASE_DATE = "release_date";
            final String OWM_GENRE_IDS = "genre_ids";
            final String OWM_ID = "id";
            final String OWM_ORIGINAL_TITLE = "original_title";
            final String OWM_ORIGINAL_LANGUAGE = "original_language";
            final String OWM_TITLE = "title";
            final String OWM_BACKDROP_PATH = "backdrop_path";
            final String OWM_POPULARITY = "popularity";
            final String OWM_VOTE_COUNT = "vote_count";
            final String OWM_VIDEO = "video";
            final String OWM_VOTE_AVERAGE = "vote_average";

            ArrayList<MovieRoom> resultList = new ArrayList<MovieRoom>();
            String poster_path, adult, overview, release_date, genre_ids, id, original_title, original_language, title, backdrop_path, popularity, vote_count, video, vote_average;

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);

            //List<MovieRoom> movies_before = mDb.movieDao().getAllMovies(); //320
            //mDb.movieDao().deleteAll();
            //List<MovieRoom> movies_after_delete = mDb.movieDao().getAllMovies(); //320

            for (int i = 0; i < movieArray.length(); i++) {

                // Get the JSON object representing the movie
                JSONObject movie = movieArray.getJSONObject(i);

                // description is in a child array called "weather", which is 1 element long.
                poster_path = movie.getString(OWM_POSTER_PATH); //.getJSONObject(0);
                adult = movie.getString(OWM_ADULT);
                overview = movie.getString(OWM_OVERVIEW);
                release_date = movie.getString(OWM_RELEASE_DATE);
                genre_ids = movie.getString(OWM_GENRE_IDS);
                id = movie.getString(OWM_ID);
                original_title = movie.getString(OWM_ORIGINAL_TITLE);
                original_language = movie.getString(OWM_ORIGINAL_LANGUAGE);
                title = movie.getString(OWM_TITLE);
                backdrop_path = movie.getString(OWM_BACKDROP_PATH);
                popularity = movie.getString(OWM_POPULARITY);
                vote_count = movie.getString(OWM_VOTE_COUNT);
                video = movie.getString(OWM_VIDEO);
                vote_average = movie.getString(OWM_VOTE_AVERAGE);

                // create movie object, stuff in the attributes and then
                Movie mObj = new Movie(
                        poster_path,
                        adult,
                        overview,
                        release_date,
                        genre_ids,
                        id,
                        original_title,
                        original_language,
                        title,
                        backdrop_path,
                        popularity,
                        vote_count,
                        video,
                        vote_average);



                // Write JSON data to Movie Room DB
                MovieRoom m = new MovieRoom(
                        poster_path,
                        adult,
                        overview,
                        release_date,
                        genre_ids,
                        id,
                        original_title,
                        original_language,
                        title,
                        backdrop_path,
                        popularity,
                        vote_count,
                        video,
                        vote_average);

                // check if already in db (preserve favorite flag), else insert
                //MovieRoom db_entry = mDb.movieDao().loadMovieByMId(m.getM_id());
                //if(db_entry.)
                long room_id = mDb.movieDao().insertMovie(m);

                //finish();


                // pass to adapter as String array....in onPostExecute
                //resultList.add(mObj);
                resultList.add(m);
            }
            //List<MovieRoom> movies_after = mDb.movieDao().getAllMovies();  //340
            return resultList;

        }


        @Override
        protected void onPostExecute(ArrayList<MovieRoom> result) {
            // string result which came from the doinbackground()
            // take result and add each movie to the adapter
            //android.os.Debug.waitForDebugger();
            if (result != null) {
                mMovieAdapter.clear();
                for(MovieRoom movieObj : result) {
                    mMovieAdapter.add(movieObj);
                }
                // New data is back from the server.  Hooray!
            }

            // New data is back from the server. Hooray!
            mMovieAdapter.notifyDataSetChanged();
        }
    } //asynctask
}
