package com.example.project2_movies;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieRoom {


    //@PrimaryKey(autoGenerate = true)
    //public int id;
    int getId;

    @PrimaryKey
    @NonNull
    private String m_id;

    private String poster_path;
    private String adult;
    private String overview;
    private String release_date;
    private String genre_ids;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private String popularity;
    private String vote_count;
    private String video;
    private String vote_average;
    private boolean favorite;

    public MovieRoom(
                 String poster_path,
                 String adult,
                 String overview,
                 String release_date,
                 String genre_ids,
                 String m_id,
                 String original_title,
                 String original_language,
                 String title,
                 String backdrop_path,
                 String popularity,
                 String vote_count,
                 String video,
                 String vote_average){

        this.poster_path = poster_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
        this.genre_ids = genre_ids;
        this.m_id = m_id;
        this.original_title = original_title;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.video = video;
        this.vote_average = vote_average;
        this.favorite = false;
    }


    //public int getId() { return id; }
    public String getPoster_path() {
        return poster_path;
    }
    public String getAdult() { return adult; }
    public String getOverview() { return overview; }
    public String getRelease_date() { return release_date; }
    public String getGenre_ids() { return genre_ids; }
    public String getM_id() { return m_id; }
    public String getOriginal_title() { return original_title; }
    public String getOriginal_language() { return original_language; }
    public String getTitle() { return title; }
    public String getBackdrop_path() { return backdrop_path; }
    public String getPopularity() { return popularity; }
    public String getVote_count() { return vote_count; }
    public String getVideo() { return video; }
    public String getVote_average() { return vote_average; }
    public boolean getFavorite() { return favorite; }


    public void setPosterPath(String s) { this.poster_path = s; }
    public void setAdult(String s) { this.adult = s; }
    public void setOverview(String s) { this.overview = s; }
    public void setReleaseDate(String s) { this.release_date = s; }
    public void setGenreIds(String s) { this.genre_ids = s; }
    public void setM_id(String m_id) { this.m_id = m_id; }
    public void setOriginalTitle(String s) { this.original_title = s; }
    public void setOriginalLanguage(String s) { this.original_language = s; }
    public void setTitle(String s) { this.title = s; }
    public void setBackdropPath(String s) { this.backdrop_path = s; }
    public void setPopularity(String s) { this.popularity = s; }
    public void setVoteCount(String s) { this.vote_count = s; }
    public void setVideo(String s) { this.video = s; }
    public void setVoteAverage(String s) { this.vote_average = s; }
    public void setFavorite(boolean f) { this.favorite = f; }

}
