package com.example.project2_movies;

import android.os.Parcelable;
import android.os.Parcel;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by chowchow on 12/6/2016.
 */

public class Movie implements Parcelable {

    public String poster_path;
    public String adult;
    public String overview;
    public String release_date;
    public String genre_ids;
    public String id;
    public String original_title;
    public String original_language;
    public String title;
    public String backdrop_path;
    public String popularity;
    public String vote_count;
    public String video;
    public String vote_average;
    public ArrayList<Pair<String, String>> reviews;

            //simplePair = new Pair<>(42, "Second");
    //Integer first = simplePair.first; // 42
    //String second = simplePair.second; // "Second"

    public Movie(String poster_path,
                 String adult,
                 String overview,
                 String release_date,
                 String genre_ids,
                 String id,
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
        this.id = id;
        this.original_title = original_title;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.video = video;
        this.vote_average = vote_average;
        this.reviews = new ArrayList<Pair<String, String>>();

    }

    public Movie() {
// do nothing
    }

    public void addReview(String key, String value) {
        this.reviews.add(new Pair<>(key, value));
    }

    public String getPosterPath() {
        return this.poster_path;
    }


    // Parceable methods required implemented
    // This is where you write the values you want to save to the `Parcel`.
    // The `Parcel` class has methods defined to help you save all of your values.
    // Note that there are only methods defined for simple values, lists, and other Parcelable objects.
    // You may need to make several classes Parcelable to send the data you want.
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(poster_path);
        out.writeString(adult);
        out.writeString(overview);
        out.writeString(release_date);
        out.writeString(genre_ids);
        out.writeString(id);
        out.writeString(original_title);
        out.writeString(original_language);
        out.writeString(title);
        out.writeString(backdrop_path);
        out.writeString(popularity);
        out.writeString(vote_count);
        out.writeString(video);
        out.writeString(vote_average);
        //out.writeParcelable(mInfo, flags);
    }

    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    private Movie(Parcel in) {
        this.poster_path = in.readString();
        this.adult = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.genre_ids = in.readString();
        this.id = in.readString();
        this.original_title = in.readString();
        this.original_language = in.readString();
        this.title = in.readString();
        this.backdrop_path = in.readString();
        this.popularity = in.readString();
        this.vote_count = in.readString();
        this.video = in.readString();
        this.vote_average = in.readString();
        //mInfo = in.readParcelable(MySubParcelable.class.getClassLoader());

    }


    // In the vast majority of cases you can simply return 0 for this.
    // There are cases where you need to use the constant `CONTENTS_FILE_DESCRIPTOR`
    // But this is out of scope of this tutorial
    @Override
    public int describeContents() {
        return 0;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}

