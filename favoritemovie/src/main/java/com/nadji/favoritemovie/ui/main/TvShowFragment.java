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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nadji.favoritemovie.R;
import com.nadji.favoritemovie.adapter.TvshowAdapter;
import com.nadji.favoritemovie.detail.TvshowDetailActivity;
import com.nadji.favoritemovie.entity.TvShow;
import com.nadji.favoritemovie.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.nadji.favoritemovie.helper.DatabaseContract.MovieColumns.CONTENT_URI_TVSHOW;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment implements LoadTvShowCallback {
    private TvshowAdapter adapter;
    public ArrayList<TvShow> listFavTvshow = new ArrayList<>();
    private RecyclerView rvFavTvshow;
    private ProgressBar progressBar;
    private TextView tv;


    public TvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        rvFavTvshow.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavTvshow.setHasFixedSize(true);
        adapter = new TvshowAdapter(getContext());
        adapter.notifyDataSetChanged();
//        rvFavTvshow.setAdapter(adapter);
//        tv = getView().findViewById(R.id.tv_tvshowfav);
//
//        HandlerThread handlerThread = new HandlerThread("DataObserver");
//        handlerThread.start();
//        Handler handler = new Handler(handlerThread.getLooper());
//
//        DataObserver myObserver = new DataObserver(handler, getContext());
//        getContext().getContentResolver().registerContentObserver(CONTENT_URI_TVSHOW, true, myObserver);
//
//        new LoadMovieAsync(getContext(), this).execute();
//        adapter.setFavTvShow(listFavTvshow);

        adapter.setOnItemTvShowClickCallback(new TvshowAdapter.OnItemTvShowClickCallback() {
            @Override
            public void onitemTvshowClicked(TvShow data) {
                Intent toDetail = new Intent(getContext(), TvshowDetailActivity.class);
                toDetail.putExtra(TvshowDetailActivity.EXTRA_TV, data);
                startActivity(toDetail);
            }
        });
//        showLoading(false);
        super.onStart();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavTvshow = view.findViewById(R.id.rv_tvshow);
        progressBar = view.findViewById(R.id.progress_tvshow);
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(ArrayList<TvShow> tvShows) {
        progressBar.setVisibility(View.INVISIBLE);
        if (tvShows.size()>0){
            adapter.setFavTvShow(tvShows);
            tv.setText("");
        }else {
            tv.setText(getString(R.string.empty_tvshow_data));
        }
    }

    private static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, ArrayList<TvShow>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadTvShowCallback> weakCallback;

        public LoadMovieAsync(Context context, LoadTvShowCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<TvShow> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(CONTENT_URI_TVSHOW, null, null, null, null);
            return MappingHelper.mapCursorToArrayListTvShow(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<TvShow> tvShows) {
            super.onPostExecute(tvShows);
            weakCallback.get().postExecute(tvShows);
        }
    }
}

interface LoadTvShowCallback {
    void preExecute();

    void postExecute(ArrayList<TvShow> tvShows);
}


