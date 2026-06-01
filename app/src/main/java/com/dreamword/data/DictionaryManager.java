package com.dreamword.data;

import java.util.ArrayList;
import java.util.List;

public class DictionaryManager {
    private static DictionaryManager instance;
    private boolean loaded = false;
    private List<String> stages = new ArrayList<>();
    private List<Grade> grades = new ArrayList<>();

    public static synchronized DictionaryManager getInstance() {
        if (instance == null) {
            instance = new DictionaryManager();
        }
        return instance;
    }

    public void loadDictionary() {
        if (loaded) return;
        
        // 创建模拟的内置词典数据
        stages.add("小学");
        stages.add("初中");
        stages.add("高中");

        // 小学词汇
        Grade primary1 = new Grade();
        primary1.id = "primary1";
        primary1.name = "三年级上册";
        primary1.stage = "小学";
        primary1.words = new ArrayList<>();
        primary1.words.add(new WordItem("apple", "苹果"));
        primary1.words.add(new WordItem("banana", "香蕉"));
        primary1.words.add(new WordItem("cat", "猫"));
        grades.add(primary1);

        Grade primary2 = new Grade();
        primary2.id = "primary2";
        primary2.name = "三年级下册";
        primary2.stage = "小学";
        primary2.words = new ArrayList<>();
        primary2.words.add(new WordItem("dog", "狗"));
        primary2.words.add(new WordItem("elephant", "大象"));
        primary2.words.add(new WordItem("fish", "鱼"));
        grades.add(primary2);

        loaded = true;
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
