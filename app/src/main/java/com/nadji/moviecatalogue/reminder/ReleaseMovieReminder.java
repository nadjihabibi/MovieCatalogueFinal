package com.nadji.moviecatalogue.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nadji.moviecatalogue.BuildConfig;
import com.nadji.moviecatalogue.MainActivity;
import com.nadji.moviecatalogue.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class ReleaseMovieReminder extends BroadcastReceiver {
    private static final String URL_MOVIE_RELEASE = BuildConfig.URL_MOVIE_RELEASE;
    private final static int RELEASE_ALARM_ID = 102;
    private final static int NOTIF_RELEASE_ID = 2;
    private static final String CHANNEL_RELEASE_ID = "Channel_2";
    private static final String CHANNEL_RELEASE_NAME = "DailyRelease channel";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(calendar.getTime());

        AsyncHttpClient client = new AsyncHttpClient();
        String url = URL_MOVIE_RELEASE + currentDate + "&primary_release_date.lte=" + currentDate;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String list = new String(responseBody);
                    JSONObject responseObject = new JSONObject(list);
                    JSONArray result = responseObject.getJSONArray("results");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject movie = result.getJSONObject(i);
                        String title = movie.getString("title");
                        String message = movie.getString("release_date");
                        showReleaseNotification(context, title, message, NOTIF_RELEASE_ID + i);
                    }
                } catch (JSONException e) {
                    Log.e("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void setReleaseToday(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseMovieReminder.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_ALARM_ID, intent, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, context.getResources().getString(R.string.release_today_reminder), Toast.LENGTH_SHORT).show();
    }

    public void cancelReleaseToday(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseMovieReminder.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_ALARM_ID, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, context.getResources().getString(R.string.release_today_cancel), Toast.LENGTH_SHORT).show();
    }

    private void showReleaseNotification(Context context, String title, String message, int NOTIF_RELEASE_ID) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIF_RELEASE_ID, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_RELEASE_ID)
                .setSmallIcon(R.drawable.ic_movie_filter)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_RELEASE_ID,
                    CHANNEL_RELEASE_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_RELEASE_ID);

            if (mNM != null) {
                mNM.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (mNM != null) {
            mNM.notify(NOTIF_RELEASE_ID, notification);
        }
    }
}
