package com.nadji.favoritemovie.helper;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.nadji.moviecatalogue";
    public static final String SCHEME_MOVIE = "content";

    public static String TABLE_FAVMOVIE = "movie";
    public static String TABLE_FAVTVSHOW = "tvshow";

    public static final class MovieColumns implements BaseColumns {
        public static final String IDM = "idM";
        public static final String NAME = "name";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String POSTER = "poster";
        public static final String USER_SCORE = "userScore";

        public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME_MOVIE)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVMOVIE)
                .build();

        public static final Uri CONTENT_URI_TVSHOW = new Uri.Builder().scheme(SCHEME_MOVIE)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVTVSHOW)
                .build();

    }

}
