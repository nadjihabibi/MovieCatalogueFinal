package com.nadji.moviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import com.nadji.moviecatalogue.db.FavoriteHelper;
import com.nadji.moviecatalogue.entity.Movie;
import com.nadji.moviecatalogue.entity.TvShow;

import static com.nadji.moviecatalogue.db.DatabaseContract.AUTHORITY;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.CONTENT_URI_TVSHOW;
import static com.nadji.moviecatalogue.db.DatabaseContract.TABLE_FAVMOVIE;
import static com.nadji.moviecatalogue.db.DatabaseContract.TABLE_FAVTVSHOW;

public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TVSHOW = 3;
    private static final int TVSHOW_ID = 4;
    private FavoriteHelper favoriteHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVMOVIE, MOVIE);
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVMOVIE + "/#", MOVIE_ID);
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVTVSHOW, TVSHOW);
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVTVSHOW + "/#", TVSHOW_ID);
    }

    public MovieProvider() {
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        favoriteHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = favoriteHelper.queryAllMovie();
                break;
            case MOVIE_ID:
                cursor = favoriteHelper.queryByIdMovie(uri.getLastPathSegment());
                break;
            case TVSHOW:
                cursor = favoriteHelper.queryAllTvShow();
                break;
            case TVSHOW_ID:
                cursor = favoriteHelper.queryByIdTvShow(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Movie movie = new Movie();
        TvShow tvShow = new TvShow();
        Uri uriId;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                favoriteHelper.insertMovie(values);
                uriId = Uri.parse(CONTENT_URI_MOVIE + "/" + movie.getIdM());
                getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE, null);
                break;
            case TVSHOW:
                favoriteHelper.insertTvShow(values);
                uriId = Uri.parse(CONTENT_URI_TVSHOW + "/" + tvShow.getIdT());
                getContext().getContentResolver().notifyChange(CONTENT_URI_TVSHOW, null);
                break;
            default:
                throw new SQLException("failed to insert row into " + uri);
        }
        return uriId;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = favoriteHelper.deleteByIdMovie(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE, null);
                break;
            case TVSHOW_ID:
                deleted = favoriteHelper.deleteByIdTvShow(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(CONTENT_URI_TVSHOW, null);
                break;
            default:
                deleted = 0;
                break;
        }
        return deleted;
    }
}
