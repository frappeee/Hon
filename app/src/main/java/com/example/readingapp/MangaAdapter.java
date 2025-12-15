package com.example.readingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {
    private Context context;
    private List<Manga> mangaList;
    private onMangaClickListener listener;

    public interface onMangaClickListener {
        void onMangaClick(Manga manga);
    }

    public MangaAdapter(Context context, List<Manga> mangaList, onMangaClickListener listener) {
        this.context = context;
        this.mangaList = mangaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MangaAdapter.MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manga, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        Manga manga = mangaList.get(position);
        holder.txtTitle.setText(manga.getTitle());

        // created a fake id so we can web scrape it safely without the web site blocking us
        GlideUrl glideUrl = new GlideUrl(manga.getImageURL(), new LazyHeaders.Builder()
                .addHeader("Referer", "https://coffeemanga.io/")
                .addHeader("User-Agent", "Mozilla/5.0")
                .build());

        Glide.with(context)
                .load(glideUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imgCover);

        holder.itemView.setOnClickListener(v -> listener.onMangaClick(manga));
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    public static class MangaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView txtTitle;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgCover);
            txtTitle = itemView.findViewById(R.id.txtTitle);
        }
    }
}
