package com.example.project2_movies;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.util.Log;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    //private static long String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<MovieRoom>> movies;
    private LiveData<List<MovieRoom>> favorite_movies;


    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        movies = database.movieDao().getAllMovies();
        favorite_movies = database.movieDao().loadFavoriteMovies(true);
    }

    public LiveData<List<MovieRoom>> getMovies() {
        return movies;
    }

    public LiveData<List<MovieRoom>> getFavoriteMovies() {
        return favorite_movies;
    }

}
