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

//    public ArrayList<Movie> getAllFavMovie() {
//        ArrayList<Movie> arrayList = new ArrayList<>();
//        Cursor cursor = database.query(DATABASE_TABLE_MOVIE,
//                null,
//                null,
//                null,
//                null,
//                null,
//                _ID + " ASC",
//                null);
//        cursor.moveToFirst();
//        Movie movie;
//        if (cursor.getCount() > 0) {
//            do {
//                movie = new Movie();
//                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
//                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
//                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
//                movie.setUserScore(cursor.getString(cursor.getColumnIndexOrThrow(USER_SCORE)));
//                movie.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
//                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
//                arrayList.add(movie);
//                cursor.moveToNext();
//            } while (!cursor.isAfterLast());
//        }
//        cursor.close();
//        return arrayList;
//    }

//    public ArrayList<TvShow> getAllFavTvshow() {
//        ArrayList<TvShow> arrayList = new ArrayList<>();
//        Cursor cursor = database.query(DATABASE_TABLE_TVSHOW,
//                null,
//                null,
//                null,
//                null,
//                null,
//                _ID + " ASC",
//                null);
//        cursor.moveToFirst();
//        TvShow tvShow;
//        if (cursor.getCount() > 0) {
//            do {
//                tvShow = new TvShow();
//                tvShow.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
//                tvShow.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
//                tvShow.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
//                tvShow.setUserScore(cursor.getString(cursor.getColumnIndexOrThrow(USER_SCORE)));
//                tvShow.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
//                tvShow.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
//                arrayList.add(tvShow);
//                cursor.moveToNext();
//            } while (!cursor.isAfterLast());
//        }
//        cursor.close();
//        return arrayList;
//    }
//
//    public Movie getFavoriteById(String id) {
//        Cursor cursor = database.query(DATABASE_TABLE_MOVIE,
//                null,
//                _ID + " = ?",
//                new String[]{id},
//                null,
//                null,
//                null,
//                null);
//        cursor.moveToFirst();
//        Movie movie = new Movie();
//        if (cursor.getCount() > 0) {
//            do {
//                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
//                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
//                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
//                movie.setUserScore(cursor.getString(cursor.getColumnIndexOrThrow(USER_SCORE)));
//                movie.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
//                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
//
//                cursor.moveToNext();
//            } while (!cursor.isAfterLast());
//        }
//        cursor.close();
//        return movie;
//    }

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

//    public long insertFavMovie(Movie movie) {
//        ContentValues conten = new ContentValues();
//        conten.put(_ID, movie.getId());
//        conten.put(ID, movie.getId());
//        conten.put(TITLE, movie.getTitle());
//        conten.put(RELEASE_DATE, movie.getReleaseDate());
//        conten.put(OVERVIEW, movie.getOverview());
//        conten.put(POSTER, movie.getPoster());
//        conten.put(USER_SCORE, movie.getUserScore());
//        return database.insert(DATABASE_TABLE_MOVIE, null, conten);
//    }

    public long insertMovie(ContentValues values) {
        return database.insert(DATABASE_TABLE_MOVIE, null, values);
    }

    public long insertTvShow(ContentValues values) {
        return database.insert(DATABASE_TABLE_TVSHOW, null, values);
    }

//    public long insertFavTvshow(TvShow tvShow) {
//        ContentValues conten = new ContentValues();
//        conten.put(_ID, tvShow.getId());
//        conten.put(NAME, tvShow.getName());
//        conten.put(RELEASE_DATE, tvShow.getReleaseDate());
//        conten.put(OVERVIEW, tvShow.getOverview());
//        conten.put(POSTER, tvShow.getPoster());
//        conten.put(USER_SCORE, tvShow.getUserScore());
//        return database.insert(DATABASE_TABLE_TVSHOW, null, conten);
//    }

//    public int deleteFavMovie(int id) {
//        return database.delete(TABLE_FAVMOVIE, _ID + " = " + id, null);
//    }

    public int deleteByIdMovie(String id) {
        return database.delete(TABLE_FAVMOVIE, IDM + " = ?", new String[]{id});
    }

    public int deleteByIdTvShow(String id) {
        return database.delete(TABLE_FAVTVSHOW, IDT + " = ?", new String[]{id});
    }

//    public int deleteFavTvshow(int id) {
//        return database.delete(TABLE_FAVTVSHOW, _ID + " = " + id, null);
//    }
}
