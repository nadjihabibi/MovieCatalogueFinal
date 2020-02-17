package com.nadji.moviecatalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nadji.moviecatalogue.entity.Movie;
import com.nadji.moviecatalogue.entity.TvShow;

import static android.provider.BaseColumns._ID;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.IDM;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.IDT;
import static com.nadji.moviecatalogue.db.DatabaseContract.TABLE_FAVMOVIE;
import static com.nadji.moviecatalogue.db.DatabaseContract.TABLE_FAVTVSHOW;


public class FavoriteHelper {
    private static final String DATABASE_TABLE_MOVIE = TABLE_FAVMOVIE;
    private static final String DATABASE_TABLE_TVSHOW = TABLE_FAVTVSHOW;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper INSTANCE;
    private static SQLiteDatabase database;

    public FavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    // metode untuk membuka koneksi ke database
    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    // metode untuk menutup koneksi ke database
    public void close() {
        databaseHelper.close();
        if (database.isOpen()) ;
        database.close();
    }

    public Cursor queryAllMovie() {
        return database.query(DATABASE_TABLE_MOVIE,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC");
    }

    public Cursor queryByIdMovie(String id) {
        return database.query(DATABASE_TABLE_MOVIE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public Cursor queryAllTvShow() {
        return database.query(DATABASE_TABLE_TVSHOW,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC");
    }

    public Cursor queryByIdTvShow(String id) {
        return database.query(DATABASE_TABLE_TVSHOW,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public boolean isExistFavMovie(Movie movie) {
        database = databaseHelper.getReadableDatabase();
        String QUERY = "SELECT * FROM " + TABLE_FAVMOVIE + " WHERE " + IDM + "=" + movie.getIdM();
        Cursor cursor = database.rawQuery(QUERY, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean isExistFavTvshow(TvShow tvShow) {
        database = databaseHelper.getReadableDatabase();
        String QUERY = "SELECT * FROM " + TABLE_FAVTVSHOW + " WHERE " + IDT + "=" + tvShow.getIdT();
        Cursor cursor = database.rawQuery(QUERY, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public long insertMovie(ContentValues values) {
        return database.insert(DATABASE_TABLE_MOVIE, null, values);
    }

    public long insertTvShow(ContentValues values) {
        return database.insert(DATABASE_TABLE_TVSHOW, null, values);
    }

    public int deleteByIdMovie(String id) {
        return database.delete(TABLE_FAVMOVIE, IDM + " = ?", new String[]{id});
    }

    public int deleteByIdTvShow(String id) {
        return database.delete(TABLE_FAVTVSHOW, IDT + " = ?", new String[]{id});
    }
}
