package com.dreamword.data;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DictionaryManager {
    private static DictionaryManager instance;
    private boolean loaded = false;
    private List<String> versions = new ArrayList<>();
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
                buildFullDictionary();
                return true;
            } catch (Exception e) {
                errorMessage = e.getMessage();
                buildMockDictionary();
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

    private void buildFullDictionary() throws IOException {
        versions.add("人教版");
        versions.add("外研版");
        
        stages.add("小学");
        stages.add("初中");
        stages.add("高中");

        String[] primaryBooks = {"三年级上册", "三年级下册", "四年级上册", "四年级下册", "五年级上册", "五年级下册", "六年级上册", "六年级下册"};
        String[] juniorBooks = {"七年级上册", "七年级下册", "八年级上册", "八年级下册", "九年级全一册"};
        String[] seniorBooks = {"必修第一册", "必修第二册", "必修第三册", "选择性必修第一册", "选择性必修第二册", "选择性必修第三册"};

        for (String version : versions) {
            for (int i = 0; i < primaryBooks.length; i++) {
                Grade grade = new Grade();
                grade.id = version + "_primary_" + (i + 1);
                grade.name = primaryBooks[i];
                grade.stage = "小学";
                grade.version = version;
                grade.words = fetchWordsForBook(version, "小学", primaryBooks[i]);
                grades.add(grade);
            }

            for (int i = 0; i < juniorBooks.length; i++) {
                Grade grade = new Grade();
                grade.id = version + "_junior_" + (i + 1);
                grade.name = juniorBooks[i];
                grade.stage = "初中";
                grade.version = version;
                grade.words = fetchWordsForBook(version, "初中", juniorBooks[i]);
                grades.add(grade);
            }

            for (int i = 0; i < seniorBooks.length; i++) {
                Grade grade = new Grade();
                grade.id = version + "_senior_" + (i + 1);
                grade.name = seniorBooks[i];
                grade.stage = "高中";
                grade.version = version;
                grade.words = fetchWordsForBook(version, "高中", seniorBooks[i]);
                grades.add(grade);
            }
        }
    }

    private List<WordItem> fetchWordsForBook(String version, String stage, String book) throws IOException {
        List<WordItem> words = new ArrayList<>();
        
        String keyword = version + stage + book + "英语单词";
        String urlStr = "https://dict.youdao.com/suggest?q=" + URLEncoder.encode(keyword, "UTF-8") + "&num=30&doctype=json";
        
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
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
                conn.disconnect();

                String json = sb.toString();
                if (json.contains("\"entries\"")) {
                    int entriesStart = json.indexOf("\"entries\":[");
                    int entriesEnd = json.indexOf("]", entriesStart);
                    
                    if (entriesStart > 0 && entriesEnd > entriesStart) {
                        String entriesStr = json.substring(entriesStart + 11, entriesEnd);
                        String[] items = entriesStr.split("\\},\\{");
                        
                        for (String item : items) {
                            String word = extractValue(item, "\"word\":\"", "\"");
                            String explain = extractValue(item, "\"explain\":\"", "\"");
                            
                            if (word != null && word.matches("^[A-Za-z]+$") && word.length() > 1) {
                                if (explain != null) {
                                    explain = explain.replaceAll("^\\[[^\\]]+\\]", "").trim();
                                    explain = explain.replaceAll("^[a-z]+\\.\\s*", "").trim();
                                    String[] parts = explain.split("[;；]");
                                    if (parts.length > 0) {
                                        explain = parts[0].trim();
                                    }
                                } else {
                                    explain = "暂无释义";
                                }
                                words.add(new WordItem(word, explain));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            words.addAll(generateDefaultWords(book));
        }

        if (words.isEmpty()) {
            words.addAll(generateDefaultWords(book));
        }

        return words;
    }

    private String extractValue(String json, String start, String end) {
        int startIdx = json.indexOf(start);
        if (startIdx == -1) return null;
        
        int valueStart = startIdx + start.length();
        int endIdx = json.indexOf(end, valueStart);
        if (endIdx == -1) return null;
        
        String value = json.substring(valueStart, endIdx);
        return value.replace("\\u002F", "/").replace("\\\"", "\"");
    }

    private List<WordItem> generateDefaultWords(String book) {
        List<WordItem> words = new ArrayList<>();
        String[][] wordPool = {
            {"apple", "苹果"}, {"banana", "香蕉"}, {"cat", "猫"}, {"dog", "狗"},
            {"elephant", "大象"}, {"fish", "鱼"}, {"garden", "花园"}, {"happy", "快乐的"},
            {"island", "岛屿"}, {"jungle", "丛林"}, {"knowledge", "知识"}, {"library", "图书馆"},
            {"mountain", "山"}, {"nature", "自然"}, {"orange", "橙子"}, {"people", "人们"},
            {"question", "问题"}, {"rainbow", "彩虹"}, {"sunshine", "阳光"}, {"teacher", "老师"},
            {"umbrella", "雨伞"}, {"vegetable", "蔬菜"}, {"water", "水"}, {"yellow", "黄色"},
            {"zebra", "斑马"}, {"beautiful", "美丽的"}, {"computer", "电脑"}, {"different", "不同的"},
            {"example", "例子"}, {"favorite", "最喜欢的"}, {"general", "一般的"}, {"history", "历史"},
            {"important", "重要的"}, {"journey", "旅程"}, {"kitchen", "厨房"}, {"language", "语言"}
        };

        int start = book.hashCode() % 10;
        for (int i = start; i < Math.min(start + 15, wordPool.length); i++) {
            words.add(new WordItem(wordPool[i][0], wordPool[i][1]));
        }

        return words;
    }

    private void buildMockDictionary() {
        versions.clear();
        stages.clear();
        grades.clear();

        versions.add("人教版");
        versions.add("外研版");
        
        stages.add("小学");
        stages.add("初中");
        stages.add("高中");

        String[] primaryBooks = {"三年级上册", "三年级下册", "四年级上册", "四年级下册", "五年级上册", "五年级下册", "六年级上册", "六年级下册"};
        String[] juniorBooks = {"七年级上册", "七年级下册", "八年级上册", "八年级下册", "九年级全一册"};
        String[] seniorBooks = {"必修第一册", "必修第二册", "必修第三册", "选择性必修第一册", "选择性必修第二册", "选择性必修第三册"};

        String[][] wordPool = {
            {"apple", "苹果"}, {"banana", "香蕉"}, {"cat", "猫"}, {"dog", "狗"}, {"elephant", "大象"},
            {"fish", "鱼"}, {"garden", "花园"}, {"happy", "快乐的"}, {"island", "岛屿"}, {"jungle", "丛林"},
            {"knowledge", "知识"}, {"library", "图书馆"}, {"mountain", "山"}, {"nature", "自然"}, {"orange", "橙子"},
            {"people", "人们"}, {"question", "问题"}, {"rainbow", "彩虹"}, {"sunshine", "阳光"}, {"teacher", "老师"},
            {"umbrella", "雨伞"}, {"vegetable", "蔬菜"}, {"water", "水"}, {"yellow", "黄色"}, {"zebra", "斑马"},
            {"beautiful", "美丽的"}, {"computer", "电脑"}, {"different", "不同的"}, {"example", "例子"}, {"favorite", "最喜欢的"},
            {"general", "一般的"}, {"history", "历史"}, {"important", "重要的"}, {"journey", "旅程"}, {"kitchen", "厨房"}
        };

        for (String version : versions) {
            for (int i = 0; i < primaryBooks.length; i++) {
                Grade grade = new Grade();
                grade.id = version + "_primary_" + (i + 1);
                grade.name = primaryBooks[i];
                grade.stage = "小学";
                grade.version = version;
                grade.words = new ArrayList<>();
                
                int start = (version.hashCode() + i) % wordPool.length;
                for (int j = 0; j < 10 && start + j < wordPool.length; j++) {
                    grade.words.add(new WordItem(wordPool[start + j][0], wordPool[start + j][1]));
                }
                grades.add(grade);
            }

            for (int i = 0; i < juniorBooks.length; i++) {
                Grade grade = new Grade();
                grade.id = version + "_junior_" + (i + 1);
                grade.name = juniorBooks[i];
                grade.stage = "初中";
                grade.version = version;
                grade.words = new ArrayList<>();
                
                int start = (version.hashCode() + primaryBooks.length + i) % wordPool.length;
                for (int j = 0; j < 12 && start + j < wordPool.length; j++) {
                    grade.words.add(new WordItem(wordPool[start + j][0], wordPool[start + j][1]));
                }
                grades.add(grade);
            }

            for (int i = 0; i < seniorBooks.length; i++) {
                Grade grade = new Grade();
                grade.id = version + "_senior_" + (i + 1);
                grade.name = seniorBooks[i];
                grade.stage = "高中";
                grade.version = version;
                grade.words = new ArrayList<>();
                
                int start = (version.hashCode() + primaryBooks.length + juniorBooks.length + i) % wordPool.length;
                for (int j = 0; j < 15 && start + j < wordPool.length; j++) {
                    grade.words.add(new WordItem(wordPool[start + j][0], wordPool[start + j][1]));
                }
                grades.add(grade);
            }
        }
    }

    public List<String> getVersions() {
        return versions;
    }

    public List<String> getStages() {
        return stages;
    }

    public List<Grade> getBooksForVersionAndStage(String version, String stage) {
        List<Grade> result = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.version.equals(version) && grade.stage.equals(stage)) {
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
        versions.clear();
        stages.clear();
        grades.clear();
    }

    public static class Grade {
        public String id;
        public String name;
        public String stage;
        public String version;
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
