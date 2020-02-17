package com.nadji.moviecatalogue.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nadji.moviecatalogue.R;
import com.nadji.moviecatalogue.adapter.PagerAdapter;

public class FavoriteFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        setViewPager(viewPager);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return view;
    }

    private void setViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MovieFavFragment(), getResources().getString(R.string.title_movies));
        adapter.addFragment(new TvShowFavFragment(), getResources().getString(R.string.title_tvshow));
        viewPager.setAdapter(adapter);
    }
}