package com.nadji.favoritemovie.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private int id;
    private int idM;
    private String name;
    private String overview;
    private String releaseDate;
    private String poster;
    private String userScore;

    public Movie() {
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        idM = in.readInt();
        name = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        poster = in.readString();
        userScore = in.readString();
    }

    public Movie(int id, int idM, String name, String overview, String releaseDate, String poster, String userScore) {
        this.id = id;
        this.idM = idM;
        this.name = name;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.poster = poster;
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
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
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
        parcel.writeString(name);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(poster);
        parcel.writeString(userScore);
    }
}
