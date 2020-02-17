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
import com.nadji.moviecatalogue.R;
import com.nadji.moviecatalogue.entity.Movie;
import com.nadji.moviecatalogue.helper.MappingHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<Bitmap> mWidgetItems = new ArrayList<>();
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
        if (cursor != null) {
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        // querying ke database
        cursor = mContext.getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);
        ArrayList<Movie> movieList = MappingHelper.mapCursorToArrayListMovie(Objects.requireNonNull(cursor));
        Binder.restoreCallingIdentity(identityToken);
        for (Movie list : movieList){
            try{
                Bitmap bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w500" + list.getPoster())
                        .submit()
                        .get();
                mWidgetItems.add(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (cursor!= null){
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.image_view_widget, mWidgetItems.get(position));

        Bundle extras = new Bundle();
        extras.putInt(FavoriteMovieWidget.EXTRA_ITEM, position);
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
