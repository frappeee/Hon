package com.example.readingapp;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrefManager {
    private static final String PREF_NAME = "MangaAppPrefs";
    private static final String KEY_FAVORITES = "Favorites";
    private SharedPreferences pref;
    private Gson gson;

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // adding to favorites feature
    public void addFavorite(Manga manga) {
        List<Manga> list = getFavorites();

        // this will check if there are any duplicate links
        for (Manga m : list) {
            if (m.getLink().equals(manga.getLink())) {
                return;
            }
        }

        list.add(manga);
        saveList(list);
    }

    // removing from favorites feature
    public void removeFavorite(Manga manga) {
        List<Manga> list = getFavorites();

        list.removeIf(m -> m.getLink().equals(manga.getLink()));

        saveList(list);
    }

    public boolean isFavorite(String url) {
        List<Manga> list = getFavorites();
        for (Manga m : list) {
            if (m.getLink().equals(url)) return true;
        }
        return false;
    }

    public List<Manga> getFavorites() {
        String json = pref.getString(KEY_FAVORITES, "");
        if (json.isEmpty()) return new ArrayList<>();

        Type type = new TypeToken<List<Manga>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void saveList(List<Manga> list) {
        String json = gson.toJson(list);
        pref.edit().putString(KEY_FAVORITES, json).apply();
    }
}