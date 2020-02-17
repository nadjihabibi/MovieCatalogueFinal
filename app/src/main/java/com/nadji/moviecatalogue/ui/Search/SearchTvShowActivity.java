package com.nadji.moviecatalogue.ui.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nadji.moviecatalogue.BuildConfig;
import com.nadji.moviecatalogue.R;
import com.nadji.moviecatalogue.adapter.TvshowAdapter;
import com.nadji.moviecatalogue.entity.TvShow;
import com.nadji.moviecatalogue.ui.detail.TvshowDetailActivity;

import java.util.ArrayList;
import java.util.Objects;

public class SearchTvShowActivity extends AppCompatActivity {
    public static final String EXTRA_SEARCH = "query";
    public ArrayList<TvShow> listMovies = new ArrayList<>();
    private TvshowAdapter adapter;
    private ProgressBar progressBar;
    private TvShowSearchViewModel tvShowSearchViewModel;
    public String querySearch;
    private static final String URL_SEARCH_TVSHOW = BuildConfig.URL_SEARCH_TVSHOW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tvshow);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.tvshow_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        querySearch = getIntent().getStringExtra(EXTRA_SEARCH);
        String url = URL_SEARCH_TVSHOW + querySearch;

        progressBar = findViewById(R.id.progress_tvshow);
        RecyclerView rvTvshow = findViewById(R.id.rv_tvshow);
        rvTvshow.setLayoutManager(new LinearLayoutManager(this));
        rvTvshow.setHasFixedSize(true);
        adapter = new TvshowAdapter(listMovies);
        adapter.notifyDataSetChanged();
        rvTvshow.setAdapter(adapter);

        tvShowSearchViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(TvShowSearchViewModel.class);
        tvShowSearchViewModel.setTvShow(url);

        showLoading(true);

        tvShowSearchViewModel.getTvShow().observe(this, new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> tvShows) {
                if (tvShows != null) {
                    adapter.setList(tvShows);
                    showLoading(false);
                }
            }
        });
//
//        movieSearchViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
//                .get(MovieSearchViewModel.class);
//        movieSearchViewModel.setMovie(url);
//        showLoading(true);
//
//        movieSearchViewModel.getMovies().observe(this, new Observer<ArrayList<Movie>>() {
//            @Override
//            public void onChanged(ArrayList<Movie> movies) {
//                if (movies != null) {
//                    adapterMovie.setList(movies);
//                    showLoading(false);
//                }
//            }
//        }
        adapter.setOnItemTvShowClickCallback(new TvshowAdapter.OnItemTvShowClickCallback() {
            @Override
            public void onitemTvshowClicked(TvShow data) {
                Intent toDetail = new Intent(SearchTvShowActivity.this, TvshowDetailActivity.class);
                toDetail.putExtra(TvshowDetailActivity.EXTRA_TV, data);
                startActivity(toDetail);
            }
        });

//        adapterMovie.setOnItemMovieClickCallback(new MovieAdapter.OnItemMovieClickCallback() {
//            @Override
//            public void onItemMovieClickCallback(Movie data) {
//                Toast.makeText(SearchMovieActivity.this, "" + data, Toast.LENGTH_SHORT).show();
//                Intent toDetail = new Intent(SearchMovieActivity.this, MovieDetailActivity.class);
//                toDetail.putExtra(MovieDetailActivity.EXTRA_MOVIE, data);
//                startActivity(toDetail);
//            }
//        });
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
