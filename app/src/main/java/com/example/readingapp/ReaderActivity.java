package com.example.readingapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReaderActivity extends AppCompatActivity {

    RecyclerView recyclerPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        recyclerPages = findViewById(R.id.recyclerPages);
        recyclerPages.setLayoutManager(new LinearLayoutManager(this));

        // gets the chapterURL from the detail activity
        String chapterUrl = getIntent().getStringExtra("CHAPTER_URL");

        if (chapterUrl != null) {
            loadPages(chapterUrl);
        }
    }

    private void loadPages(String url) {
        MangaSource.getMangaImages(url, new MangaSource.PageListener() {
            @Override
            public void onSuccess(List<String> imageUrls) {
                runOnUiThread(() -> {
                    PageAdapter adapter = new PageAdapter(ReaderActivity.this, imageUrls);
                    recyclerPages.setAdapter(adapter);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(ReaderActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show());
            }
        });
    }
}