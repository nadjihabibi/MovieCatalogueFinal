package com.nadji.moviecatalogue.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.nadji.moviecatalogue.R;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteTvShowWidget extends AppWidgetProvider {

    private static final String TOAST_ACTION = "com.nadji.moviecatalogue.TOAST_ACTION";
    public static final String EXTRA_TITLE_TVSHOW = "com.nadji.moviecatalogue.EXTRA_TITLE";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, StackWidgetServicesTvshow.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_tv_show_widget);
        views.setRemoteAdapter(R.id.stack_view_tvshow, intent);
        views.setEmptyView(R.id.stack_view_tvshow, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoriteTvShowWidget.class);
        toastIntent.setAction(FavoriteTvShowWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view_tvshow, toastPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                String title = intent.getStringExtra(EXTRA_TITLE_TVSHOW);
                Toast.makeText(context, title + " Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

