package com.nadji.moviecatalogue.ui.Search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nadji.moviecatalogue.BuildConfig;
import com.nadji.moviecatalogue.entity.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class MovieSearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Movie>> listMovie = new MutableLiveData<>();

    public void setMovie(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String list = new String(responseBody);
                    JSONObject responseObject = new JSONObject(list);
                    JSONArray result = responseObject.getJSONArray("results");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject movie = result.getJSONObject(i);
                        Movie movieItems = new Movie();

//                        movieItems.setId(movie.getInt("id"));
                        movieItems.setTitle(movie.getString("title"));
                        movieItems.setPoster(movie.getString("poster_path"));
                        movieItems.setOverview(movie.getString("overview"));
                        movieItems.setReleaseDate(movie.getString("release_date"));
                        movieItems.setUserScore(movie.getString("vote_average"));
                        listItems.add(movieItems);
                    }
                    listMovie.postValue(listItems);

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


    public LiveData<ArrayList<Movie>> getMovies() {
        return listMovie;
    }
}
