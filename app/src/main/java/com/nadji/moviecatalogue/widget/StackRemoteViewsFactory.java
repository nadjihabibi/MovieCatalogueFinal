package com.nadji.moviecatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.nadji.moviecatalogue.BuildConfig;
import com.nadji.moviecatalogue.R;
import com.nadji.moviecatalogue.entity.Movie;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String URL_POSTER_W185 = BuildConfig.URL_POSTER_W185;
    private final ArrayList<Movie> moviesItems = new ArrayList<>();
    private Context mContext;
    private Cursor cursor;

    public StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        if (moviesItems.size() != 0) {
            moviesItems.clear();
        }

        final long identityToken = Binder.clearCallingIdentity();

        // querying ke database
        cursor = mContext.getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                Movie movie = new Movie(cursor);
                moviesItems.add(movie);
            }
        }
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return moviesItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        if (moviesItems.size() != 0) {
            String urlPoster = moviesItems.get(position).getPoster();
            if (urlPoster != null) {
                String url = URL_POSTER_W185 + urlPoster;
                try {
                    Bitmap bitmap = Glide.with(mContext)
                            .asBitmap()
                            .load(url)
                            .submit()
                            .get();
                    rv.setImageViewBitmap(R.id.image_view_widget, bitmap);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        Bundle extras = new Bundle();
        extras.putString(FavoriteMovieWidget.EXTRA_TITLE, moviesItems.get(position).getTitle());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.image_view_widget, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

