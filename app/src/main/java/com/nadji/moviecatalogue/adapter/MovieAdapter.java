package com.nadji.moviecatalogue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nadji.moviecatalogue.R;
import com.nadji.moviecatalogue.entity.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ListViewHolder> {
    private ArrayList<Movie> list;
    public Context context;
    private OnItemMovieClickCallback onItemMovieClickCallback;

    public MovieAdapter(ArrayList<Movie> list) {
        this.list = list;
    }

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Movie> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    public void setFavMovie(ArrayList<Movie> listMovie) {
        this.list = new ArrayList<>();
        this.list.addAll(listMovie);
        notifyDataSetChanged();
    }

    public interface OnItemMovieClickCallback {
        void onItemMovieClickCallback(Movie data);//int position
    }

    public void setOnItemMovieClickCallback(OnItemMovieClickCallback onItemMovieClickCallback) {
        this.onItemMovieClickCallback = onItemMovieClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);

        final ListViewHolder holder = new ListViewHolder(mView);
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemMovieClickCallback.onItemMovieClickCallback(list.get(holder.getAdapterPosition()));
                    }
                });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        final TextView tvJudul, tvRilis, tvDetail, tvScore;
        final ImageView imgposter;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_title_movie);
            tvRilis = itemView.findViewById(R.id.tv_releasedate_movie);
            tvDetail = itemView.findViewById(R.id.tv_desc_movie);
            tvScore = itemView.findViewById(R.id.score_movie);
            imgposter = itemView.findViewById(R.id.img_poster_movie);
        }

        void bind(Movie movieItems) {
            tvJudul.setText(movieItems.getTitle());
            tvRilis.setText(movieItems.getReleaseDate());
            tvDetail.setText(movieItems.getOverview());
            tvScore.setText(movieItems.getUserScore());
            Glide.with(itemView).load("https://image.tmdb.org/t/p/w185/" + movieItems.getPoster()).into(imgposter);
        }
    }
}
