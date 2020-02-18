package com.nadji.moviecatalogue.helper;

import android.database.Cursor;

import com.nadji.moviecatalogue.db.DatabaseContract;
import com.nadji.moviecatalogue.entity.Movie;
import com.nadji.moviecatalogue.entity.TvShow;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<Movie> mapCursorToArrayListMovie(Cursor movieCursor) {
        ArrayList<Movie> moviesList = new ArrayList<>();

        while (movieCursor.moveToNext()) {
            int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns._ID));
            int idm = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.IDM));
            String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE));
            String overview = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.OVERVIEW));
            String releaseDate = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE));
            String poster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER));
            String userScore = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.USER_SCORE));
            moviesList.add(new Movie(id, idm, title, overview, releaseDate, poster, userScore));
        }
        return moviesList;
    }

    public static ArrayList<TvShow> mapCursorToArrayListTvShow(Cursor tvshowCursor) {
        ArrayList<TvShow> tvShowsList = new ArrayList<>();

        while (tvshowCursor.moveToNext()) {
            int id = tvshowCursor.getInt(tvshowCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns._ID));
            int idT = tvshowCursor.getInt(tvshowCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.IDT));
            String title = tvshowCursor.getString(tvshowCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.NAME));
            String overview = tvshowCursor.getString(tvshowCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.OVERVIEW));
            String releaseDate = tvshowCursor.getString(tvshowCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE));
            String poster = tvshowCursor.getString(tvshowCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER));
            String userScore = tvshowCursor.getString(tvshowCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.USER_SCORE));
            tvShowsList.add(new TvShow(id, idT, title, overview, releaseDate, poster, userScore));
        }
        return tvShowsList;
    }
}
