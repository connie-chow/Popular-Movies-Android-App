package com.example.project2_movies;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.OnConflictStrategy;
import androidx.room.*;


import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY m_id ASC")
    LiveData<List<MovieRoom>> getAllMovies();

    @Delete
    void deleteMovie(MovieRoom movie);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertMovie(MovieRoom movie);

    //@Query("SELECT * FROM movies WHERE id = :id")
    //MovieRoom loadMovieById(int id);

    @Query("SELECT * from movies WHERE m_id = :m_id")
    MovieRoom loadMovieByMId(String m_id);

    @Query("UPDATE movies SET favorite = :favorite WHERE m_id = :m_id")
    int updateMovie(String m_id, boolean favorite);

    @Query("SELECT * FROM movies WHERE favorite = :favorite")
    LiveData<List<MovieRoom>> loadFavoriteMovies(boolean favorite);

    @Update(onConflict = REPLACE)
    void updateMovie(MovieRoom movie);

    @Query("DELETE FROM movies")
    void deleteAll();

}
