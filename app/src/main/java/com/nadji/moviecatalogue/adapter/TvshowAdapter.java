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
import com.nadji.moviecatalogue.entity.TvShow;

import java.util.ArrayList;

public class TvshowAdapter extends RecyclerView.Adapter<TvshowAdapter.ListViewHolder> {
    private ArrayList<TvShow> list;
    public Context context;
    private OnItemTvShowClickCallback onItemTvShowClickCallback;

    public TvshowAdapter(ArrayList<TvShow> list) {
        this.list = list;
    }

    public TvshowAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<TvShow> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    public void setFavTvShow(ArrayList<TvShow> listTv) {
        this.list = new ArrayList<>();
        this.list.addAll(listTv);
        notifyDataSetChanged();
    }

//    public ArrayList<Movie> getListTvshow() {
//        return list;
//    }

    public interface OnItemTvShowClickCallback {
        void onitemTvshowClicked(TvShow data);
    }

    public void setOnItemTvShowClickCallback(OnItemTvShowClickCallback onItemTvShowClickCallback) {
        this.onItemTvShowClickCallback = onItemTvShowClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv_show, parent, false);

        final ListViewHolder holder = new ListViewHolder(mView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemTvShowClickCallback.onitemTvshowClicked(list.get(holder.getAdapterPosition()));
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
        TextView tvJudul, tvRilis, tvDetail, tvScore;
        ImageView imgposter;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_title_tvshow);
            tvRilis = itemView.findViewById(R.id.tv_releasedate_tvshow);
            tvDetail = itemView.findViewById(R.id.tv_desc_tvshow);
            imgposter = itemView.findViewById(R.id.img_poster_tvshow);
            tvScore = itemView.findViewById(R.id.score_tvshow);
        }

        public void bind(TvShow tvShow) {
            tvJudul.setText(tvShow.getName());
            tvRilis.setText(tvShow.getReleaseDate());
            tvDetail.setText(tvShow.getOverview());
            tvScore.setText(tvShow.getUserScore());
            Glide.with(itemView).load("https://image.tmdb.org/t/p/w185/" + tvShow.getPoster()).into(imgposter);

        }
    }
}
