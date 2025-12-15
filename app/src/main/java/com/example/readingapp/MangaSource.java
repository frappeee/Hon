package com.example.readingapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MangaSource {

    // --- Interfaces ---
    public interface MangaListener {
        void onSuccess(List<Manga> mangaList);
        void onError(String message);
    }

    public interface ChapterListener {
        void onSuccess(List<Chapter> chapters);
        void onError(String message);
    }

    public interface PageListener {
        void onSuccess(List<String> imageUrls);
        void onError(String message);
    }

    public static void getPopularManga(int page, final MangaListener listener) {
        new Thread(() -> {
            List<Manga> mangaList = new ArrayList<>();
            try {
                // web scraping for the manga title, link and images(cover and pages)
                String url = "https://coffeemanga.io/page/" + page + "/?s=&post_type=wp-manga&m_orderby=views";

                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .timeout(15000)
                        .get();

                Elements items = doc.select(".c-tabs-item__content");

                for (Element item : items) {
                    Element titleTag = item.select(".post-title h3 a").first();
                    if (titleTag != null) {
                        String title = titleTag.text();
                        String link = titleTag.attr("href");

                        Element imgTag = item.select(".tab-thumb img").first();
                        String imageUrl = "";

                        if (imgTag != null) {
                            imageUrl = imgTag.attr("data-src");
                            if (imageUrl.isEmpty()) imageUrl = imgTag.attr("src");
                        }

                        mangaList.add(new Manga(title, imageUrl, link));
                    }
                }
                listener.onSuccess(mangaList);

            } catch (IOException e) {
                listener.onError("Connection Error: " + e.getMessage());
            }
        }).start();
    }

    // gets the list of chapters a manga has
    public static void getChapterList(final String mangaUrl, final ChapterListener listener) {
        new Thread(() -> {
            List<Chapter> chapters = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(mangaUrl)
                        .userAgent("Mozilla/5.0")
                        .timeout(15000)
                        .get();

                Elements rows = doc.select("li.wp-manga-chapter a");

                for (Element row : rows) {
                    String name = row.text();
                    String link = row.attr("href");
                    chapters.add(new Chapter(name, link));
                }
                Collections.reverse(chapters);

                listener.onSuccess(chapters);

            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    public static void getMangaImages(final String chapterUrl, final PageListener listener) {
        new Thread(() -> {
            List<String> images = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(chapterUrl)
                        .userAgent("Mozilla/5.0")
                        .timeout(15000)
                        .get();

                Elements imgTags = doc.select(".reading-content img");

                for (Element img : imgTags) {
                    String url = img.attr("data-src");
                    if (url.isEmpty()) url = img.attr("src");

                    url = url.trim();

                    if (!url.isEmpty() && url.startsWith("http")) {
                        images.add(url);
                    }
                }
                listener.onSuccess(images);

            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    public static void searchManga(String query, final MangaListener listener) {
        new Thread(() -> {
            List<Manga> mangaList = new ArrayList<>();
            try {
                String cleanQuery = query.replace(" ", "+");

                String url = "https://coffeemanga.io/?s=" + cleanQuery + "&post_type=wp-manga";

                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .timeout(15000)
                        .get();

                Elements items = doc.select(".c-tabs-item__content");

                for (Element item : items) {
                    Element titleTag = item.select(".post-title h3 a").first();
                    if (titleTag != null) {
                        String title = titleTag.text();
                        String link = titleTag.attr("href");

                        Element imgTag = item.select(".tab-thumb img").first();
                        String imageUrl = "";
                        if (imgTag != null) {
                            imageUrl = imgTag.attr("data-src");
                            if (imageUrl.isEmpty()) imageUrl = imgTag.attr("src");
                        }

                        mangaList.add(new Manga(title, imageUrl, link));
                    }
                }
                listener.onSuccess(mangaList);

            } catch (IOException e) {
                listener.onError("Connection Error: " + e.getMessage());
            }
        }).start();
    }
}