package com.example.readingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MangaAdapter adapter;
    private List<Manga> allManga = new ArrayList<>();
    private int currentPage = 1;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MangaAdapter(this, allManga, manga -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("MANGA_TITLE", manga.getTitle());
            intent.putExtra("MANGA_URL", manga.getLink());
            intent.putExtra("MANGA_IMG", manga.getImageURL());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        loadMangaData(currentPage);

        // infinite scroll feature
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // checks if we can scroll down further
                // if it's false it means we reached the bottom of the screen
                if (!recyclerView.canScrollVertically(1)) {

                    if (!isLoading) {
                        currentPage++;
                        loadMangaData(currentPage);

                        Toast.makeText(MainActivity.this, "Loading more...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void loadMangaData(int page) {
        isLoading = true;

        MangaSource.getPopularManga(page, new MangaSource.MangaListener() {
            @Override
            public void onSuccess(List<Manga> newMangaList) {
                runOnUiThread(() -> {
                    if (newMangaList != null && !newMangaList.isEmpty()) {
                        allManga.addAll(newMangaList);
                        adapter.notifyDataSetChanged();
                    }
                    isLoading = false;
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    isLoading = false;
                });
            }
        });
    }

    // --- Menu Code ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        } else if (id == R.id.action_favorites) {
            startActivity(new Intent(this, FavoriteActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}