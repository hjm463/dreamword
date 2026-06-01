package com.dreamword.data;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DictionaryManager {
    private static DictionaryManager instance;
    private boolean loaded = false;
    private List<String> stages = new ArrayList<>();
    private List<Grade> grades = new ArrayList<>();

    public interface LoadCallback {
        void onSuccess();
        void onError(String error);
    }

    public static synchronized DictionaryManager getInstance() {
        if (instance == null) {
            instance = new DictionaryManager();
        }
        return instance;
    }

    public void loadDictionary(LoadCallback callback) {
        if (loaded) {
            callback.onSuccess();
            return;
        }
        
        new FetchWordTask(callback).execute();
    }

    private class FetchWordTask extends AsyncTask<Void, Void, Boolean> {
        private LoadCallback callback;
        private String errorMessage;

        FetchWordTask(LoadCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                fetchWordsFromYoudao();
                if (grades.isEmpty()) {
                    fetchWordsFromBaidu();
                }
                if (grades.isEmpty()) {
                    generateMockWords();
                }
                return true;
            } catch (Exception e) {
                errorMessage = e.getMessage();
                generateMockWords();
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            loaded = true;
            if (success) {
                callback.onSuccess();
            } else {
                callback.onError(errorMessage);
            }
        }
    }

    private void fetchWordsFromYoudao() throws IOException {
        String[] stagesArray = {"小学", "初中", "高中"};
        String[][] booksArray = {
            {"三年级上册", "三年级下册"},
            {"七年级上册", "七年级下册"},
            {"高一上册", "高一册下"}
        };

        for (int i = 0; i < stagesArray.length; i++) {
            String stage = stagesArray[i];
            stages.add(stage);
            
            for (String bookName : booksArray[i]) {
                Grade grade = new Grade();
                grade.id = stage + "_" + bookName;
                grade.name = bookName;
                grade.stage = stage;
                grade.words = fetchWordList(bookName);
                grades.add(grade);
            }
        }
    }

    private List<WordItem> fetchWordList(String keyword) throws IOException {
        List<WordItem> words = new ArrayList<>();
        String urlStr = "https://dict.youdao.com/suggest?q=" + URLEncoder.encode(keyword + "英语单词", "UTF-8") + "&num=20&doctype=json";
        
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        if (conn.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
            JsonObject data = json.getAsJsonObject("data");
            if (data != null) {
                JsonArray entries = data.getAsJsonArray("entries");
                if (entries != null) {
                    for (JsonElement element : entries) {
                        JsonObject entry = element.getAsJsonObject();
                        String word = entry.get("word").getAsString();
                        String explain = entry.get("explain").getAsString();
                        if (word.matches("^[A-Za-z]+$") && word.length() > 2) {
                            explain = explain.replaceAll("^\\[[^\\]]+\\]", "").trim();
                            explain = explain.replaceAll("^[a-z]+\\.\\s*", "").trim();
                            String[] parts = explain.split("[;；]");
                            String meaning = parts[0].trim();
                            words.add(new WordItem(word, meaning));
                        }
                    }
                }
            }
        }
        return words;
    }

    private void fetchWordsFromBaidu() throws IOException {
        String[] basicWords = {"apple", "banana", "cat", "dog", "elephant", "fish", "girl", "happy", "idea", "jump"};
        
        for (String word : basicWords) {
            String urlStr = "https://fanyi.baidu.com/sug?kw=" + URLEncoder.encode(word, "UTF-8");
            
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
                if (json.get("errno").getAsInt() == 0) {
                    JsonArray data = json.getAsJsonArray("data");
                    if (data != null && !data.isEmpty()) {
                        JsonObject item = data.get(0).getAsJsonObject();
                        String w = item.get("k").getAsString();
                        String v = item.get("v").getAsString();
                        if (!grades.isEmpty()) {
                            grades.get(0).words.add(new WordItem(w, v));
                        }
                    }
                }
            }
        }
    }

    private void generateMockWords() {
        stages.clear();
        grades.clear();

        stages.add("推荐词库");

        Grade recommended = new Grade();
        recommended.id = "recommended";
        recommended.name = "常用单词";
        recommended.stage = "推荐词库";
        recommended.words = new ArrayList<>();

        String[][] wordList = {
            {"apple", "苹果"}, {"banana", "香蕉"}, {"cat", "猫"},
            {"dog", "狗"}, {"elephant", "大象"}, {"fish", "鱼"},
            {"garden", "花园"}, {"happy", "快乐的"}, {"island", "岛屿"},
            {"jungle", "丛林"}, {"knowledge", "知识"}, {"library", "图书馆"},
            {"mountain", "山"}, {"nature", "自然"}, {"orange", "橙子"},
            {"people", "人们"}, {"question", "问题"}, {"rainbow", "彩虹"},
            {"sunshine", "阳光"}, {"teacher", "老师"}, {"umbrella", "雨伞"},
            {"vegetable", "蔬菜"}, {"water", "水"}, {"yellow", "黄色"},
            {"zebra", "斑马"}
        };

        for (String[] pair : wordList) {
            recommended.words.add(new WordItem(pair[0], pair[1]));
        }

        grades.add(recommended);
    }

    public List<String> getStages() {
        return stages;
    }

    public List<Grade> getBooksForStage(String stage) {
        List<Grade> result = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.stage.equals(stage)) {
                result.add(grade);
            }
        }
        return result;
    }

    public Grade getBook(String bookId) {
        for (Grade grade : grades) {
            if (grade.id.equals(bookId)) {
                return grade;
            }
        }
        return null;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void reset() {
        loaded = false;
        stages.clear();
        grades.clear();
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

        public WordItem() {}

        public WordItem(String english, String chinese) {
            this.english = english;
            this.chinese = chinese;
        }
    }
}
