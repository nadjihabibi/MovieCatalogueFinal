package com.nadji.favoritemovie.detail;

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
import com.nadji.favoritemovie.R;
import com.nadji.favoritemovie.entity.Movie;

import java.util.Objects;

import static com.nadji.favoritemovie.helper.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";
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
        Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + movie.getPoster()).into(imgPoster);
        tvJudul.setText(movie.getTitle());
        tvScore.setText(movie.getUserScore());
        tvRilis.setText(movie.getReleaseDate());
        tvDesc.setText(movie.getOverview());

        uriWithId = Uri.parse(CONTENT_URI_MOVIE + "/" + movie.getIdM());

        showLoading(true);
        if (movie != null) {
            showLoading(false);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.already_favorite, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        if (item.getItemId() == R.id.action_delete_fav) {
            item.setIcon(R.drawable.ic_favorite_border);
            getContentResolver().delete(uriWithId, null, null);
            Toast.makeText(MovieDetailActivity.this, getResources().getString(R.string.success_delete_favorite), Toast.LENGTH_SHORT).show();
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
}
