package com.nadji.moviecatalogue.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.nadji.moviecatalogue.db.DatabaseContract;

import static com.nadji.moviecatalogue.db.DatabaseContract.getColumnInt;
import static com.nadji.moviecatalogue.db.DatabaseContract.getColumnString;

public class TvShow implements Parcelable {
    private int id;
    private int idT;
    private String name;
    private String overview;
    private String releaseDate;
    private String poster;
    private String backdrop;
    private String userScore;

    public TvShow() {
    }

    public TvShow(Cursor cursor){
        this.id = getColumnInt(cursor, DatabaseContract.MovieColumns._ID);
        this.idT = getColumnInt(cursor, DatabaseContract.MovieColumns.IDT);
        this.name = getColumnString(cursor, DatabaseContract.MovieColumns.NAME);
        this.overview = getColumnString(cursor, DatabaseContract.MovieColumns.OVERVIEW);
        this.releaseDate = getColumnString(cursor, DatabaseContract.MovieColumns.RELEASE_DATE);
        this.poster = getColumnString(cursor, DatabaseContract.MovieColumns.POSTER);
        this.backdrop = getColumnString(cursor, DatabaseContract.MovieColumns.BACKDROP);
        this.overview = getColumnString(cursor, DatabaseContract.MovieColumns.OVERVIEW);
    }

    public TvShow(int id, int idT, String name, String overview, String releaseDate, String poster,String backdrop, String userScore) {
        this.id = id;
        this.idT = idT;
        this.name = name;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.backdrop = backdrop;
        this.userScore = userScore;
    }

    protected TvShow(Parcel in) {
        id = in.readInt();
        idT = in.readInt();
        name = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        poster = in.readString();
        backdrop = in.readString();
        userScore = in.readString();
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel in) {
            return new TvShow(in);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdT() {
        return idT;
    }

    public void setIdT(int idT) {
        this.idT = idT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        parcel.writeInt(idT);
        parcel.writeString(name);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(poster);
        parcel.writeString(backdrop);
        parcel.writeString(userScore);
    }
}
