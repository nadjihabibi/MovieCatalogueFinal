package com.nadji.moviecatalogue.ui.tvshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nadji.moviecatalogue.R;
import com.nadji.moviecatalogue.adapter.TvshowAdapter;
import com.nadji.moviecatalogue.entity.TvShow;
import com.nadji.moviecatalogue.ui.detail.TvshowDetailActivity;

import java.util.ArrayList;

public class TvShowFragment extends Fragment {
    public ArrayList<TvShow> listTvshow = new ArrayList<>();
    private TvShowViewModel tvShowViewModel;
    private ProgressBar progressBar;
    private TvshowAdapter tvshowAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progress_tvshow);
        RecyclerView recyclerView = view.findViewById(R.id.rv_tvshow);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        tvshowAdapter = new TvshowAdapter(listTvshow);
        tvshowAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(tvshowAdapter);

        tvShowViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(TvShowViewModel.class);

        tvShowViewModel.setTvShow();
        showLoading(true);

        tvShowViewModel.getTvShow().observe(getViewLifecycleOwner(), new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> items) {
                if (items != null) {
                    tvshowAdapter.setList(items);
                    showLoading(false);
                }
            }
        });

        tvshowAdapter.setOnItemTvShowClickCallback(new TvshowAdapter.OnItemTvShowClickCallback() {
            @Override
            public void onitemTvshowClicked(TvShow data) {
                Intent toDetail = new Intent(getContext(), TvshowDetailActivity.class);
                toDetail.putExtra(TvshowDetailActivity.EXTRA_TV, data);
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