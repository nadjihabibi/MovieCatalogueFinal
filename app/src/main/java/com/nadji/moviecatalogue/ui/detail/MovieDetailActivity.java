package com.nadji.moviecatalogue.ui.detail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nadji.moviecatalogue.BuildConfig;
import com.nadji.moviecatalogue.R;
import com.nadji.moviecatalogue.db.FavoriteHelper;
import com.nadji.moviecatalogue.entity.Movie;
import com.nadji.moviecatalogue.widget.FavoriteMovieWidget;

import java.util.Objects;

import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.BACKDROP;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.IDM;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.POSTER;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.TITLE;
import static com.nadji.moviecatalogue.db.DatabaseContract.MovieColumns.USER_SCORE;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String URL_POSTER_W500 = BuildConfig.URL_POSTER_W500;
    public static final String EXTRA_MOVIE = "extra_movie";
    private FavoriteHelper favoriteHelper;
    private Movie movie;
    private ProgressBar progressBar;
    private Uri uriWithId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        setTitle(getResources().getString(R.string.title_movies));

        progressBar = findViewById(R.id.progressBar_detail_movie);
        ImageView imgPoster = findViewById(R.id.img_poster);
        TextView tvJudul = findViewById(R.id.tv_title);
        TextView tvScore = findViewById(R.id.tv_score);
        TextView tvRilis = findViewById(R.id.tv_release_date);
        TextView tvDesc = findViewById(R.id.tv_desc);
        Glide.with(this).load(URL_POSTER_W500 + movie.getBackdrop()).into(imgPoster);
        tvJudul.setText(movie.getTitle());
        tvScore.setText(movie.getUserScore());
        tvRilis.setText(movie.getReleaseDate());
        tvDesc.setText(movie.getOverview());

        uriWithId = Uri.parse(CONTENT_URI_MOVIE + "/" + movie.getIdM());

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        showLoading(true);
        if (movie != null) {
            showLoading(false);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (favoriteHelper.isExistFavMovie(movie)) {
            getMenuInflater().inflate(R.menu.already_favorite, menu);
        } else {
            getMenuInflater().inflate(R.menu.favorit_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        ContentValues values = new ContentValues();
        values.put(IDM, movie.getIdM());
        values.put(TITLE, movie.getTitle());
        values.put(POSTER, movie.getPoster());
        values.put(BACKDROP, movie.getBackdrop());
        values.put(OVERVIEW, movie.getOverview());
        values.put(USER_SCORE, movie.getUserScore());
        values.put(RELEASE_DATE, movie.getReleaseDate());

        if (item.getItemId() == R.id.action_add_fav) {
            getContentResolver().insert(CONTENT_URI_MOVIE, values);
            item.setIcon(R.drawable.ic_favorite);
            updateFavoriteMoviesStackWidget();
            Toast.makeText(MovieDetailActivity.this, getResources().getString(R.string.success_add_favorite), Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_delete_fav) {
            getContentResolver().delete(uriWithId, null, null);
            item.setIcon(R.drawable.ic_favorite_border);
            Toast.makeText(MovieDetailActivity.this, getResources().getString(R.string.success_delete_favorite), Toast.LENGTH_SHORT).show();
            updateFavoriteMoviesStackWidget();
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

    private void updateFavoriteMoviesStackWidget() {
        Context context = getApplicationContext();
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, FavoriteMovieWidget.class);
        int[] idAppWidget = widgetManager.getAppWidgetIds(componentName);
        widgetManager.notifyAppWidgetViewDataChanged(idAppWidget, R.id.stack_view);
    }
}
