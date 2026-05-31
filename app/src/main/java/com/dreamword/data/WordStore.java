package com.dreamword.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordStore {
    private static final String PREFS_NAME = "dreamword_data";
    private static final String KEY_WORDS = "words";
    private final SharedPreferences prefs;
    private final Gson gson;
    private List<WordData> words;

    public WordStore(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadWords();
    }

    private void loadWords() {
        String json = prefs.getString(KEY_WORDS, "[]");
        Type type = new TypeToken<List<WordData>>(){}.getType();
        words = gson.fromJson(json, type);
        if (words == null) words = new ArrayList<>();
    }

    public void saveWords() {
        String json = gson.toJson(words);
        prefs.edit().putString(KEY_WORDS, json).apply();
    }

    public void addWord(String english, String chinese) {
        for (WordData w : words) {
            if (w.getEnglish().equalsIgnoreCase(english)) {
                return;
            }
        }
        words.add(new WordData(english, chinese));
        saveWords();
    }

    public void removeWord(String english) {
        words.removeIf(w -> w.getEnglish().equalsIgnoreCase(english));
        saveWords();
    }

    public void clearWords() {
        words.clear();
        saveWords();
    }

    public List<WordData> getWords() {
        return new ArrayList<>(words);
    }

    public List<WordData> getUnmasteredWords() {
        List<WordData> unmastered = new ArrayList<>();
        for (WordData w : words) {
            if (!w.isMastered()) {
                unmastered.add(w);
            }
        }
        Collections.shuffle(unmastered);
        return unmastered;
    }

    public void markMastered(String english) {
        for (WordData w : words) {
            if (w.getEnglish().equalsIgnoreCase(english)) {
                w.setMastered(true);
                saveWords();
                return;
            }
        }
    }

    public int getTotalCount() { return words.size(); }

    public int getMasteredCount() {
        int count = 0;
        for (WordData w : words) {
            if (w.isMastered()) count++;
        }
        return count;
    }

    public int getUnmasteredCount() {
        return getTotalCount() - getMasteredCount();
    }
}
