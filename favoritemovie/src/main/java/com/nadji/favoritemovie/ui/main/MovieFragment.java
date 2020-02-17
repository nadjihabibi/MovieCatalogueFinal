package com.nadji.favoritemovie.ui.main;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nadji.favoritemovie.R;
import com.nadji.favoritemovie.adapter.MovieAdapter;
import com.nadji.favoritemovie.detail.MovieDetailActivity;
import com.nadji.favoritemovie.entity.Movie;
import com.nadji.favoritemovie.helper.DatabaseContract;
import com.nadji.favoritemovie.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements LoadMovieCallback {
    private MovieAdapter adapterMovie;
    private RecyclerView rvMovie;
    private ArrayList<Movie> listMovie = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView tv;


    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onStart() {
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovie.setHasFixedSize(true);
        adapterMovie = new MovieAdapter(getContext());
        adapterMovie.notifyDataSetChanged();
        rvMovie.setAdapter(adapterMovie);
        tv = getView().findViewById(R.id.tv_moviefav);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, getContext());
        getContext().getContentResolver().registerContentObserver(DatabaseContract
                .MovieColumns.CONTENT_URI_MOVIE, true, myObserver);

        new LoadMovieAsync(getContext(), this).execute();
        adapterMovie.setFavMovie(listMovie);

        adapterMovie.setOnItemMovieClickCallback(new MovieAdapter.OnItemMovieClickCallback() {
            @Override
            public void onItemMovieClickCallback(Movie data) {
                Intent toDetail = new Intent(getContext(), MovieDetailActivity.class);
                toDetail.putExtra(MovieDetailActivity.EXTRA_MOVIE, data);
                startActivity(toDetail);
            }
        });

        super.onStart();
//        showLoading(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMovie = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progressBar_movie);
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {
        progressBar.setVisibility(View.INVISIBLE);
        if (movies.size() > 0) {
            adapterMovie.setFavMovie(movies);
            tv.setText("");
        } else {
            tv.setText(getString(R.string.empty_movie_data));
        }
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieCallback> weakCallback;

        private LoadMovieAsync(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.MovieColumns
                    .CONTENT_URI_MOVIE, null, null, null, null);
            return MappingHelper.mapCursorToArrayListMovie(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    private static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    }
}

interface LoadMovieCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movies);
}
