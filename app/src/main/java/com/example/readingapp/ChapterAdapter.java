package com.example.readingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private Context context;
    private List<Chapter> chapters;
    private OnChapterClickListener listener;

    public interface OnChapterClickListener {
        void onChapterClick(Chapter chapter);
    }

    public ChapterAdapter(Context context, List<Chapter> chapters, OnChapterClickListener listener) {
        this.context = context;
        this.chapters = chapters;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapters.get(position);
        holder.txtName.setText(chapter.getName());
        holder.itemView.setOnClickListener(v -> listener.onChapterClick(chapter));
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(android.R.id.text1);
        }
    }
}