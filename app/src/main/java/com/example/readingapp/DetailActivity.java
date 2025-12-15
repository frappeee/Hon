package com.example.readingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView recyclerChapters;
    private FloatingActionButton btnFavorite;
    private ImageView imgDetailCover;
    private TextView txtDetailTitle;

    private PrefManager prefManager;
    private Manga currentManga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 1. Get Data from Intent
        String title = getIntent().getStringExtra("MANGA_TITLE");
        String url = getIntent().getStringExtra("MANGA_URL");
        String img = getIntent().getStringExtra("MANGA_IMG");

        // 2. Setup Views (Match IDs from activity_detail.xml)
        imgDetailCover = findViewById(R.id.imgDetailCover);
        txtDetailTitle = findViewById(R.id.txtDetailTitle);
        btnFavorite = findViewById(R.id.fabFavorite); // FIX: Match XML ID
        recyclerChapters = findViewById(R.id.recyclerChapters);

        // 3. Set Data to Views
        txtDetailTitle.setText(title);

        // Load Image (With Security Headers for CoffeeManga)
        if (img != null && !img.isEmpty()) {
            GlideUrl glideUrl = new GlideUrl(img, new LazyHeaders.Builder()
                    .addHeader("Referer", "https://coffeemanga.io/")
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .build());

            Glide.with(this)
                    .load(glideUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(imgDetailCover);
        }

        // 4. Setup Favorites
        prefManager = new PrefManager(this);
        currentManga = new Manga(title, img, url);

        // FIX: Pass the URL string, not the object
        updateFavoriteIcon(url);

        btnFavorite.setOnClickListener(v -> {
            // FIX: Pass the URL string to check
            if (prefManager.isFavorite(url)) {
                prefManager.removeFavorite(currentManga);
                Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
            } else {
                prefManager.addFavorite(currentManga);
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
            }
            updateFavoriteIcon(url);
        });

        // 5. Setup List
        recyclerChapters.setLayoutManager(new LinearLayoutManager(this));

        // 6. Load Chapters
        MangaSource.getChapterList(url, new MangaSource.ChapterListener() {
            @Override
            public void onSuccess(List<Chapter> chapters) {
                runOnUiThread(() -> {
                    ChapterAdapter adapter = new ChapterAdapter(DetailActivity.this, chapters, chapter -> {
                        Intent intent = new Intent(DetailActivity.this, ReaderActivity.class);
                        intent.putExtra("CHAPTER_URL", chapter.getLink());
                        startActivity(intent);
                    });
                    recyclerChapters.setAdapter(adapter);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(DetailActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void updateFavoriteIcon(String url) {
        // FIX: Check using URL
        if(prefManager.isFavorite(url)) {
            btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        }
        else {
            btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }
}