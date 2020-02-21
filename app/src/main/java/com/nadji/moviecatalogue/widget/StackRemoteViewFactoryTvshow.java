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
import com.nadji.moviecatalogue.entity.TvShow;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.CONTENT_URI_TVSHOW;

class StackRemoteViewFactoryTvshow implements RemoteViewsService.RemoteViewsFactory {
    private static final String URL_POSTER_W185 = BuildConfig.URL_POSTER_W185;
    private final ArrayList<TvShow> tvshowItems = new ArrayList<>();
    private Context mContext;
    private Cursor cursor;


    public StackRemoteViewFactoryTvshow(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (tvshowItems.size() != 0) {
            tvshowItems.clear();
        }

        final long identityToken = Binder.clearCallingIdentity();

        // querying ke database
        cursor = mContext.getContentResolver().query(CONTENT_URI_TVSHOW, null, null, null, null);
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                TvShow movie = new TvShow(cursor);
                tvshowItems.add(movie);
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
        return tvshowItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item_tvshow);

        if (tvshowItems.size() != 0) {
            String urlPoster = tvshowItems.get(position).getPoster();
            if (urlPoster != null) {
                String url = URL_POSTER_W185 + urlPoster;
                try {
                    Bitmap bitmap = Glide.with(mContext)
                            .asBitmap()
                            .load(url)
                            .submit()
                            .get();
                    rv.setImageViewBitmap(R.id.image_view_widget_tvshow, bitmap);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        Bundle extras = new Bundle();
        extras.putString(FavoriteTvShowWidget.EXTRA_TITLE_TVSHOW, tvshowItems.get(position).getName());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.image_view_widget_tvshow, fillInIntent);
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
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
