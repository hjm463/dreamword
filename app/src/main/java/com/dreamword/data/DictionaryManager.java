package com.dreamword.data;

import java.util.ArrayList;
import java.util.List;

public class DictionaryManager {
    private static DictionaryManager instance;
    private boolean loaded = false;
    private List<String> versions = new ArrayList<>();
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
        
        buildFullDictionary();
        loaded = true;
    }

    private void buildFullDictionary() {
        versions.add("人教版");
        versions.add("外研版");
        
        stages.add("小学");
        stages.add("初中");
        stages.add("高中");

        String[][] primaryWords = {
            {"apple", "苹果"}, {"ant", "蚂蚁"}, {"boy", "男孩"}, {"book", "书"}, {"cake", "蛋糕"},
            {"cat", "猫"}, {"dog", "狗"}, {"desk", "书桌"}, {"egg", "鸡蛋"}, {"fish", "鱼"},
            {"girl", "女孩"}, {"hand", "手"}, {"ice", "冰"}, {"juice", "果汁"}, {"kite", "风筝"},
            {"lion", "狮子"}, {"milk", "牛奶"}, {"nose", "鼻子"}, {"orange", "橙子"}, {"pig", "猪"},
            {"queen", "女王"}, {"rabbit", "兔子"}, {"sun", "太阳"}, {"tree", "树"}, {"umbrella", "雨伞"},
            {"van", "货车"}, {"water", "水"}, {"yellow", "黄色"}, {"zebra", "斑马"}, {"bird", "鸟"},
            {"car", "汽车"}, {"door", "门"}, {"flower", "花"}, {"garden", "花园"}, {"home", "家"},
            {"island", "岛屿"}, {"jump", "跳"}, {"king", "国王"}, {"love", "爱"}, {"money", "钱"}
        };

        String[][] juniorWords = {
            {"ability", "能力"}, {"accept", "接受"}, {"achieve", "实现"}, {"action", "行动"}, {"advice", "建议"},
            {"afford", "负担得起"}, {"agree", "同意"}, {"alive", "活着的"}, {"allow", "允许"}, {"alone", "独自"},
            {"amazing", "令人惊讶的"}, {"appear", "出现"}, {"area", "区域"}, {"argue", "争论"}, {"arrive", "到达"},
            {"article", "文章"}, {"asleep", "睡着的"}, {"attention", "注意"}, {"attract", "吸引"}, {"avoid", "避免"},
            {"balance", "平衡"}, {"beautiful", "美丽的"}, {"because", "因为"}, {"become", "成为"}, {"before", "之前"},
            {"begin", "开始"}, {"believe", "相信"}, {"between", "在...之间"}, {"beyond", "超过"}, {"blind", "盲的"},
            {"blood", "血液"}, {"blue", "蓝色"}, {"board", "木板"}, {"borrow", "借"}, {"break", "打破"},
            {"bring", "带来"}, {"build", "建造"}, {"business", "商业"}, {"busy", "忙碌的"}, {"butterfly", "蝴蝶"},
            {"camera", "相机"}, {"capital", "首都"}, {"careful", "小心的"}, {"carry", "携带"}, {"celebrate", "庆祝"},
            {"change", "改变"}, {"cheer", "欢呼"}, {"choose", "选择"}, {"collect", "收集"}, {"comfortable", "舒适的"},
            {"common", "普通的"}, {"compare", "比较"}, {"complete", "完成"}, {"consider", "考虑"}, {"continue", "继续"},
            {"control", "控制"}, {"cost", "花费"}, {"create", "创造"}, {"culture", "文化"}, {"daily", "日常的"}
        };

        String[][] seniorWords = {
            {"abandon", "放弃"}, {"ability", "能力"}, {"aboard", "在船上"}, {"absence", "缺席"}, {"absolute", "绝对的"},
            {"abstract", "抽象的"}, {"academic", "学术的"}, {"accelerate", "加速"}, {"access", "通道，访问"}, {"accommodate", "容纳"},
            {"accompany", "陪伴"}, {"accomplish", "完成"}, {"accordance", "按照"}, {"account", "账户，描述"}, {"accumulate", "积累"},
            {"accurate", "准确的"}, {"acknowledge", "承认"}, {"acquire", "获得"}, {"adapt", "适应"}, {"adequate", "足够的"},
            {"adjust", "调整"}, {"administer", "管理"}, {"admirable", "令人钦佩的"}, {"admission", "录取"}, {"adopt", "采用，收养"},
            {"advocate", "提倡"}, {"affirm", "确认"}, {"affluent", "富裕的"}, {"agenda", "议程"}, {"aggression", "侵略"},
            {"aggressive", "侵略性的"}, {"agony", "痛苦"}, {"agriculture", "农业"}, {"aircraft", "飞机"}, {"alarm", "警报"},
            {"album", "相册"}, {"alcohol", "酒精"}, {"alert", "警觉的"}, {"alliance", "联盟"}, {"allocate", "分配"},
            {"alter", "改变"}, {"alternative", "替代的"}, {"ambassador", "大使"}, {"ambiguous", "模糊的"}, {"ambitious", "有野心的"},
            {"ample", "充足的"}, {"amplify", "放大"}, {"analyze", "分析"}, {"ancestor", "祖先"}, {"anchor", "锚"},
            {"ancient", "古代的"}, {"anniversary", "周年"}, {"anonymous", "匿名的"}, {"anticipate", "预期"}, {"apparent", "明显的"},
            {"appreciate", "欣赏"}, {"apprehension", "理解，忧虑"}, {"appropriate", "适当的"}, {"approval", "批准"}, {"approximate", "近似的"}
        };

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
                grade.words = getWordsForBook(primaryWords, i, 8);
                grades.add(grade);
            }

            for (int i = 0; i < juniorBooks.length; i++) {
                Grade grade = new Grade();
                grade.id = version + "_junior_" + (i + 1);
                grade.name = juniorBooks[i];
                grade.stage = "初中";
                grade.version = version;
                grade.words = getWordsForBook(juniorWords, i, 12);
                grades.add(grade);
            }

            for (int i = 0; i < seniorBooks.length; i++) {
                Grade grade = new Grade();
                grade.id = version + "_senior_" + (i + 1);
                grade.name = seniorBooks[i];
                grade.stage = "高中";
                grade.version = version;
                grade.words = getWordsForBook(seniorWords, i, 15);
                grades.add(grade);
            }
        }
    }

    private List<WordItem> getWordsForBook(String[][] wordPool, int bookIndex, int count) {
        List<WordItem> words = new ArrayList<>();
        int start = bookIndex * count;
        
        for (int i = 0; i < count && start + i < wordPool.length; i++) {
            words.add(new WordItem(wordPool[start + i][0], wordPool[start + i][1]));
        }
        
        return words;
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
