package com.example.readingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import java.util.List;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageViewHolder> {

    private Context context;
    private List<String> imageUrls;

    public PageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_page, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        String url = imageUrls.get(position);

        // Create the Fake URL
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Referer", "https://coffeemanga.io/")
                .addHeader("User-Agent", "Mozilla/5.0")
                .build());

        Glide.with(context)
                .load(glideUrl)
                .override(Target.SIZE_ORIGINAL)
                .into(holder.photoView);
    }

    @Override
    public int getItemCount() { return imageUrls.size(); }

    public static class PageViewHolder extends RecyclerView.ViewHolder {
        PhotoView photoView;
        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.photoView);
        }
    }
}