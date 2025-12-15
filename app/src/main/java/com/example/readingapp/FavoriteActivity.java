package com.example.readingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerFavorites);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // loads data from the list of favorite manga that is saved using a json file
        try {
            PrefManager prefManager = new PrefManager(this);
            List<Manga> favList = prefManager.getFavorites();

            if (favList != null && !favList.isEmpty()) {
                MangaAdapter adapter = new MangaAdapter(this, favList, manga -> {
                    Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
                    intent.putExtra("MANGA_TITLE", manga.getTitle());
                    intent.putExtra("MANGA_URL", manga.getLink());
                    intent.putExtra("MANGA_IMG", manga.getImageURL());
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "No favorites yet!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading favorites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return true;
    }
}