package com.nadji.moviecatalogue.ui.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nadji.moviecatalogue.BuildConfig;
import com.nadji.moviecatalogue.R;
import com.nadji.moviecatalogue.adapter.MovieAdapter;
import com.nadji.moviecatalogue.entity.Movie;
import com.nadji.moviecatalogue.ui.detail.MovieDetailActivity;

import java.util.ArrayList;
import java.util.Objects;

public class SearchMovieActivity extends AppCompatActivity {
    public static final String EXTRA_SEARCH = "query";
    public ArrayList<Movie> listMovies = new ArrayList<>();
    private MovieAdapter adapterMovie;
    private ProgressBar progressBar;
    private MovieSearchViewModel movieSearchViewModel;
    public String querySearch;
    private static final String URL_SEARCH_MOVIE = BuildConfig.URL_SEARCH_MOVIE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movies);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.movie_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        querySearch = getIntent().getStringExtra(EXTRA_SEARCH);
        String url = URL_SEARCH_MOVIE + querySearch;

        progressBar = findViewById(R.id.progressBar_movie);
        RecyclerView rvMovie = findViewById(R.id.rv_movie);
        rvMovie.setLayoutManager(new LinearLayoutManager(this));
        rvMovie.setHasFixedSize(true);
        adapterMovie = new MovieAdapter(listMovies);
        adapterMovie.notifyDataSetChanged();
        rvMovie.setAdapter(adapterMovie);

        movieSearchViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(MovieSearchViewModel.class);
        movieSearchViewModel.setMovie(url);
        showLoading(true);

        movieSearchViewModel.getMovies().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies != null) {
                    adapterMovie.setList(movies);
                    showLoading(false);
                }
            }
        });

        adapterMovie.setOnItemMovieClickCallback(new MovieAdapter.OnItemMovieClickCallback() {
            @Override
            public void onItemMovieClickCallback(Movie data) {
                Toast.makeText(SearchMovieActivity.this, "" + data, Toast.LENGTH_SHORT).show();
                Intent toDetail = new Intent(SearchMovieActivity.this, MovieDetailActivity.class);
                toDetail.putExtra(MovieDetailActivity.EXTRA_MOVIE, data);
                startActivity(toDetail);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
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
