package com.nadji.moviecatalogue.ui.tvshow;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nadji.moviecatalogue.BuildConfig;
import com.nadji.moviecatalogue.entity.TvShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class TvShowViewModel extends ViewModel {

    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String URL_TVSHOW = BuildConfig.URL_TVSHOW;
    private MutableLiveData<ArrayList<TvShow>> listTvShow = new MutableLiveData<>();

    public void setTvShow() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listItems = new ArrayList<>();
        String url = URL_TVSHOW + API_KEY + "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String list = new String(responseBody);
                    JSONObject responseObject = new JSONObject(list);
                    JSONArray result = responseObject.getJSONArray("results");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject tvShow = result.getJSONObject(i);
                        TvShow tvShowItems = new TvShow();
                        tvShowItems.setIdT(tvShow.getInt("id"));
                        tvShowItems.setName(tvShow.getString("name"));
                        tvShowItems.setReleaseDate(tvShow.getString("first_air_date"));
                        tvShowItems.setOverview(tvShow.getString("overview"));
                        tvShowItems.setPoster(tvShow.getString("poster_path"));
                        tvShowItems.setUserScore(tvShow.getString("vote_average"));
                        listItems.add(tvShowItems);
                    }
                    listTvShow.postValue(listItems);
                } catch (JSONException e) {
                    Log.e("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public LiveData<ArrayList<TvShow>> getTvShow() {
        return listTvShow;
    }
}