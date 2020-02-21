package com.nadji.moviecatalogue.ui.movies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nadji.moviecatalogue.R;
import com.nadji.moviecatalogue.adapter.MovieAdapter;
import com.nadji.moviecatalogue.entity.Movie;
import com.nadji.moviecatalogue.ui.detail.MovieDetailActivity;

import java.util.ArrayList;

public class MoviesFragment extends Fragment {
    private ArrayList<Movie> listMovies = new ArrayList<>();
//    public ArrayList<Movie> listMovies = new ArrayList<>();
//    Variable yang hanya digunakan pada kelas yang sama sebaiknya dijadikan private.
    private MoviesViewModel moviesViewModel;
    private MovieAdapter adapterMovie;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar_movie);
        final RecyclerView rvMovie = view.findViewById(R.id.rv_movie);
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovie.setHasFixedSize(true);
        adapterMovie = new MovieAdapter(listMovies);
        adapterMovie.notifyDataSetChanged();
        rvMovie.setAdapter(adapterMovie);

        moviesViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(MoviesViewModel.class);
        moviesViewModel.setMovie();
        showLoading(true);

        moviesViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
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
                Intent toDetail = new Intent(getContext(), MovieDetailActivity.class);
                toDetail.putExtra(MovieDetailActivity.EXTRA_MOVIE, data);
                startActivity(toDetail);
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}