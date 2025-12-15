package com.example.readingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText etSearch;
    Button btnSearch;
    RecyclerView recyclerSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerSearch = findViewById(R.id.recyclerSearch);

        recyclerSearch.setLayoutManager(new GridLayoutManager(this, 3)); // Grid of 3

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                performSearch(query);
            }
        });
    }

    private void performSearch(String query) {
        Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();

        MangaSource.searchManga(query, new MangaSource.MangaListener() {
            @Override
            public void onSuccess(List<Manga> mangaList) {
                runOnUiThread(() -> {
                    if (mangaList.isEmpty()) {
                        Toast.makeText(SearchActivity.this, "No results found.", Toast.LENGTH_SHORT).show();
                    }

                    // REUSE the adapter from the main screen!
                    MangaAdapter adapter = new MangaAdapter(SearchActivity.this, mangaList, manga -> {
                        Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                        intent.putExtra("MANGA_TITLE", manga.getTitle());
                        intent.putExtra("MANGA_URL", manga.getLink());
                        intent.putExtra("MANGA_IMG", manga.getImageURL());
                        startActivity(intent);
                    });
                    recyclerSearch.setAdapter(adapter);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(SearchActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show());
            }
        });
    }
}