package com.nadji.moviecatalogue.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.nadji.moviecatalogue.db.DatabaseContract;

import static com.nadji.moviecatalogue.db.DatabaseContract.getColumnInt;
import static com.nadji.moviecatalogue.db.DatabaseContract.getColumnString;

public class Movie implements Parcelable {
    private int id;
    private int idM;
    private String title;
    private String overview;
    private String releaseDate;
    private String poster;
    private String backdrop;
    private String userScore;

    public Movie() {
    }

    public Movie(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.MovieColumns._ID);
        this.idM = getColumnInt(cursor, DatabaseContract.MovieColumns.IDM);
        this.title = getColumnString(cursor, DatabaseContract.MovieColumns.TITLE);
        this.overview = getColumnString(cursor, DatabaseContract.MovieColumns.OVERVIEW);
        this.releaseDate = getColumnString(cursor, DatabaseContract.MovieColumns.RELEASE_DATE);
        this.poster = getColumnString(cursor, DatabaseContract.MovieColumns.POSTER);
        this.backdrop = getColumnString(cursor, DatabaseContract.MovieColumns.BACKDROP);
        this.overview = getColumnString(cursor, DatabaseContract.MovieColumns.OVERVIEW);
    }

    public Movie(Parcel in) {
        id = in.readInt();
        idM = in.readInt();
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        poster = in.readString();
        backdrop = in.readString();
        userScore = in.readString();
    }

    public Movie(int id, int idM, String title, String overview, String releaseDate, String poster, String backdrop, String userScore) {
        this.id = id;
        this.idM = idM;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.backdrop = backdrop;
        this.userScore = userScore;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdM() {
        return idM;
    }

    public void setIdM(int idM) {
        this.idM = idM;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(idM);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(poster);
        parcel.writeString(backdrop);
        parcel.writeString(userScore);
    }
}
