package com.dreamword.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryManager {
    private static DictionaryManager instance;
    private final Gson gson = new Gson();
    private DictionaryData dictionaryData;
    private boolean loaded = false;

    public static synchronized DictionaryManager getInstance() {
        if (instance == null) {
            instance = new DictionaryManager();
        }
        return instance;
    }

    public void loadDictionary(Context context) {
        if (loaded) return;
        
        try {
            String json = DictionaryData.getDictionaryJson(context);
            if (json != null) {
                Type type = new TypeToken<DictionaryData>() {}.getType();
                dictionaryData = gson.fromJson(json, type);
                loaded = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getStages() {
        if (!loaded || dictionaryData == null || dictionaryData.grades == null) {
            return new ArrayList<>();
        }
        List<String> stages = new ArrayList<>();
        for (Grade grade : dictionaryData.grades) {
            if (!stages.contains(grade.stage)) {
                stages.add(grade.stage);
            }
        }
        return stages;
    }

    public List<Grade> getBooksForStage(String stage) {
        if (!loaded || dictionaryData == null || dictionaryData.grades == null) {
            return new ArrayList<>();
        }
        List<Grade> books = new ArrayList<>();
        for (Grade grade : dictionaryData.grades) {
            if (grade.stage.equals(stage)) {
                books.add(grade);
            }
        }
        return books;
    }

    public Grade getBook(String bookId) {
        if (!loaded || dictionaryData == null || dictionaryData.grades == null) {
            return null;
        }
        for (Grade grade : dictionaryData.grades) {
            if (grade.id.equals(bookId)) {
                return grade;
            }
        }
        return null;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public static class DictionaryData {
        public String title;
        public List<Grade> grades;
    }

    public static class Grade {
        public String id;
        public String name;
        public String stage;
        public List<WordItem> words;
    }

    public static class WordItem {
        public String english;
        public String chinese;
    }
}