package com.nadji.moviecatalogue.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.nadji.moviecatalogue.R;
import com.nadji.moviecatalogue.reminder.DailyReminder;
import com.nadji.moviecatalogue.reminder.ReleaseMovieReminder;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity {
    private DailyReminder dailyReminder;
    private ReleaseMovieReminder releaseMovieReminder;
    private SwitchCompat dailySwitch, releasetodaySwitch;
    private SharedPreferences sharedpreferences;
    private String MY_PREFERENCES = "my_preference";
    private String DAILY_STATUS = "daily_status";
    private String RELEASE_STATUS = "release_status";
    Boolean daily = false;
    Boolean release = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.reminder_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dailyReminder = new DailyReminder();
        releaseMovieReminder = new ReleaseMovieReminder();
        dailySwitch = findViewById(R.id.daily_reminder);
        releasetodaySwitch = findViewById(R.id.release_today);

        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        daily = sharedpreferences.getBoolean(DAILY_STATUS, false);
        if (daily) {
            dailySwitch.setChecked(true);
        } else {
            dailySwitch.setChecked(false);
        }
        release = sharedpreferences.getBoolean(RELEASE_STATUS, false);
        if (release) {
            releasetodaySwitch.setChecked(true);
        } else {
            releasetodaySwitch.setChecked(false);
        }

        dailySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    setDailyReminder();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(DAILY_STATUS, isChecked);
                    editor.apply();
                } else {
                    cancelDailyReminder();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove(DAILY_STATUS);
                    editor.apply();
                }
            }
        });

        releasetodaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    setReleaseToday();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(RELEASE_STATUS, isChecked);
                    editor.apply();
                } else {
                    cancelReleaseToday();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove(RELEASE_STATUS);
                    editor.apply();
                }
            }
        });
    }

    private void setReleaseToday() {
        releaseMovieReminder.setReleaseToday(this);
    }

    private void cancelReleaseToday() {
        releaseMovieReminder.cancelReleaseToday(this);
    }

    private void setDailyReminder() {
        dailyReminder.setDailyReminder(this);
    }

    private void cancelDailyReminder() {
        dailyReminder.cancelDailyReminder(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}