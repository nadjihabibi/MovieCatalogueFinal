package com.nadji.moviecatalogue.ui.detail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nadji.moviecatalogue.BuildConfig;
import com.nadji.moviecatalogue.R;
import com.nadji.moviecatalogue.db.FavoriteHelper;
import com.nadji.moviecatalogue.entity.TvShow;
import com.nadji.moviecatalogue.widget.FavoriteTvShowWidget;

import java.util.Objects;

import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.BACKDROP;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.CONTENT_URI_TVSHOW;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.IDT;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.NAME;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.POSTER;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.USER_SCORE;

public class TvshowDetailActivity extends AppCompatActivity {
    private static final String URL_POSTER_W500 = BuildConfig.URL_POSTER_W500;
    public static final String EXTRA_TV = "extra_tvshow";
    private FavoriteHelper favoriteHelper;
    private TvShow tvShow;
    ProgressBar progressBar;
    private Uri uriWithId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_detail);
        tvShow = getIntent().getParcelableExtra(EXTRA_TV);
        progressBar = findViewById(R.id.progressBar_detail_tvshow);
        setTitle(getResources().getString(R.string.title_tvshow));

        ImageView imgPoster = findViewById(R.id.img_poster_tvshow);
        TextView tvJudul = findViewById(R.id.tv_title_tvshow);
        TextView tvScore = findViewById(R.id.tv_score_tvshow);
        TextView tvRilis = findViewById(R.id.tv_releasedate_tvshow);
        TextView tvDesc = findViewById(R.id.tv_desc_tvshow);
        Glide.with(this).load(URL_POSTER_W500 + tvShow.getBackdrop()).into(imgPoster);
        tvJudul.setText(tvShow.getName());
        tvScore.setText(tvShow.getUserScore());
        tvRilis.setText(tvShow.getReleaseDate());
        tvDesc.setText(tvShow.getOverview());

        uriWithId = Uri.parse(CONTENT_URI_TVSHOW + "/" + tvShow.getIdT());

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        showLoading(true);
        if (tvShow != null) {
            showLoading(false);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (favoriteHelper.isExistFavTvshow(tvShow)) {
            getMenuInflater().inflate(R.menu.already_favorite, menu);
        } else {
            getMenuInflater().inflate(R.menu.favorit_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        ContentValues values = new ContentValues();
        values.put(IDT, tvShow.getIdT());
        values.put(NAME, tvShow.getName());
        values.put(POSTER, tvShow.getPoster());
        values.put(BACKDROP, tvShow.getBackdrop());
        values.put(OVERVIEW, tvShow.getOverview());
        values.put(USER_SCORE, tvShow.getUserScore());
        values.put(RELEASE_DATE, tvShow.getReleaseDate());

        if (item.getItemId() == R.id.action_add_fav) {
            getContentResolver().insert(CONTENT_URI_TVSHOW, values);
            item.setIcon(R.drawable.ic_favorite);
            updateFavoriteTvShowStackWidget();
            Toast.makeText(TvshowDetailActivity.this, getResources().getString(R.string.success_add_favorite), Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_delete_fav) {
            getContentResolver().delete(uriWithId, null, null);
            item.setIcon(R.drawable.ic_favorite_border);
            updateFavoriteTvShowStackWidget();
            Toast.makeText(TvshowDetailActivity.this, getResources().getString(R.string.success_delete_favorite), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void updateFavoriteTvShowStackWidget() {
        Context context = getApplicationContext();
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, FavoriteTvShowWidget.class);
        int[] idAppWidget = widgetManager.getAppWidgetIds(componentName);
        widgetManager.notifyAppWidgetViewDataChanged(idAppWidget, R.id.stack_view_tvshow);
    }
}
