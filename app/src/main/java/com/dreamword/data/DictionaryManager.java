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

        addPrimarySchoolBooks();
        addJuniorHighBooks();
        addSeniorHighBooks();
    }

    private void addPrimarySchoolBooks() {
        String[][] primary3Words = {
            {"apple", "苹果"}, {"ant", "蚂蚁"}, {"boy", "男孩"}, {"book", "书"}, {"cake", "蛋糕"},
            {"cat", "猫"}, {"dog", "狗"}, {"desk", "书桌"}, {"egg", "鸡蛋"}, {"fish", "鱼"},
            {"girl", "女孩"}, {"hand", "手"}, {"ice", "冰"}, {"juice", "果汁"}, {"kite", "风筝"},
            {"lion", "狮子"}, {"milk", "牛奶"}, {"nose", "鼻子"}, {"orange", "橙子"}, {"pig", "猪"},
            {"queen", "女王"}, {"rabbit", "兔子"}, {"sun", "太阳"}, {"tree", "树"}, {"umbrella", "雨伞"}
        };

        String[][] primary4Words = {
            {"beautiful", "美丽的"}, {"because", "因为"}, {"before", "在...之前"}, {"begin", "开始"},
            {"between", "在...之间"}, {"big", "大的"}, {"black", "黑色的"}, {"blue", "蓝色的"},
            {"bread", "面包"}, {"brother", "兄弟"}, {"car", "汽车"}, {"chair", "椅子"},
            {"children", "孩子们"}, {"class", "班级"}, {"clock", "时钟"}, {"cold", "冷的"},
            {"come", "来"}, {"day", "天"}, {"door", "门"}, {"drink", "喝"}, {"eat", "吃"},
            {"eight", "八"}, {"eleven", "十一"}, {"family", "家庭"}, {"father", "父亲"}, {"five", "五"}
        };

        String[][] primary5Words = {
            {"after", "在...之后"}, {"again", "再一次"}, {"animal", "动物"}, {"answer", "回答"},
            {"ask", "问"}, {"at", "在"}, {"away", "离开"}, {"back", "回来"},
            {"ball", "球"}, {"be", "是"}, {"bed", "床"}, {"better", "更好的"},
            {"best", "最好的"}, {"bring", "带来"}, {"buy", "买"}, {"call", "打电话"},
            {"can", "能"}, {"carry", "携带"}, {"clean", "打扫"}, {"close", "关闭"}, {"come", "来"}
        };

        String[][] primary6Words = {
            {"about", "关于"}, {"above", "在...上面"}, {"across", "穿过"}, {"act", "行动"},
            {"add", "添加"}, {"age", "年龄"}, {"air", "空气"}, {"also", "也"},
            {"always", "总是"}, {"another", "另一个"}, {"any", "任何"}, {"area", "区域"},
            {"arm", "手臂"}, {"around", "围绕"}, {"arrive", "到达"}, {"art", "艺术"},
            {"as", "作为"}, {"ask", "问"}, {"at", "在"}, {"away", "离开"}, {"baby", "婴儿"}
        };

        String[] primaryBooks = {"三年级上册", "三年级下册", "四年级上册", "四年级下册", "五年级上册", "五年级下册", "六年级上册", "六年级下册"};
        String[][][] primaryWordSets = {primary3Words, primary3Words, primary4Words, primary4Words, primary5Words, primary5Words, primary6Words, primary6Words};

        for (String version : versions) {
            for (int i = 0; i < primaryBooks.length; i++) {
                Grade grade = new Grade();
                grade.id = version + "_primary_" + (i + 1);
                grade.name = primaryBooks[i];
                grade.stage = "小学";
                grade.version = version;
                grade.words = new ArrayList<>();
                
                for (String[] pair : primaryWordSets[i]) {
                    grade.words.add(new WordItem(pair[0], pair[1]));
                }
                grades.add(grade);
            }
        }
    }

    private void addJuniorHighBooks() {
        String[][] junior7Words = {
            {"ability", "能力"}, {"accept", "接受"}, {"achieve", "实现"}, {"action", "行动"}, {"advice", "建议"},
            {"afford", "负担得起"}, {"agree", "同意"}, {"air", "空气"}, {"all", "所有"}, {"allow", "允许"},
            {"alone", "独自"}, {"along", "沿着"}, {"already", "已经"}, {"also", "也"}, {"although", "虽然"},
            {"always", "总是"}, {"among", "在...之中"}, {"amount", "数量"}, {"and", "和"}, {"angry", "生气的"},
            {"animal", "动物"}, {"another", "另一个"}, {"answer", "回答"}, {"any", "任何"}, {"anyone", "任何人"},
            {"anything", "任何事"}, {"appear", "出现"}, {"area", "区域"}, {"argue", "争论"}, {"around", "周围"}
        };

        String[][] junior8Words = {
            {"beautiful", "美丽的"}, {"because", "因为"}, {"become", "成为"}, {"before", "之前"}, {"begin", "开始"},
            {"believe", "相信"}, {"between", "在...之间"}, {"big", "大的"}, {"black", "黑色的"}, {"blue", "蓝色的"},
            {"book", "书"}, {"both", "两者都"}, {"break", "打破"}, {"bring", "带来"}, {"build", "建造"},
            {"business", "商业"}, {"busy", "忙碌的"}, {"but", "但是"}, {"buy", "买"}, {"by", "通过"},
            {"call", "打电话"}, {"can", "能"}, {"care", "关心"}, {"carry", "携带"}, {"change", "改变"},
            {"child", "孩子"}, {"city", "城市"}, {"class", "班级"}, {"come", "来"}, {"company", "公司"}
        };

        String[][] junior9Words = {
            {"accident", "事故"}, {"according", "根据"}, {"achieve", "实现"}, {"across", "穿过"}, {"action", "行动"},
            {"active", "积极的"}, {"address", "地址"}, {"admit", "承认"}, {"advance", "前进"}, {"advantage", "优势"},
            {"advice", "建议"}, {"affect", "影响"}, {"afford", "负担"}, {"afraid", "害怕"}, {"after", "之后"},
            {"against", "反对"}, {"age", "年龄"}, {"agree", "同意"}, {"ahead", "向前"}, {"air", "空气"},
            {"alive", "活着的"}, {"allow", "允许"}, {"almost", "几乎"}, {"alone", "独自"}, {"along", "沿着"},
            {"already", "已经"}, {"also", "也"}, {"although", "虽然"}, {"always", "总是"}, {"among", "在...之中"}
        };

        String[] juniorBooks = {"七年级上册", "七年级下册", "八年级上册", "八年级下册", "九年级全一册"};
        String[][][] juniorWordSets = {junior7Words, junior7Words, junior8Words, junior8Words, junior9Words};

        for (String version : versions) {
            for (int i = 0; i < juniorBooks.length; i++) {
                Grade grade = new Grade();
                grade.id = version + "_junior_" + (i + 1);
                grade.name = juniorBooks[i];
                grade.stage = "初中";
                grade.version = version;
                grade.words = new ArrayList<>();
                
                for (String[] pair : juniorWordSets[i]) {
                    grade.words.add(new WordItem(pair[0], pair[1]));
                }
                grades.add(grade);
            }
        }
    }

    private void addSeniorHighBooks() {
        String[][] senior1Words = {
            {"abandon", "放弃"}, {"ability", "能力"}, {"absence", "缺席"}, {"absolute", "绝对的"}, {"absorb", "吸收"},
            {"abstract", "抽象的"}, {"academic", "学术的"}, {"accept", "接受"}, {"access", "访问"}, {"accident", "事故"},
            {"accompany", "陪伴"}, {"accomplish", "完成"}, {"according", "根据"}, {"account", "账户"}, {"accurate", "准确的"},
            {"achieve", "实现"}, {"acquire", "获得"}, {"across", "穿过"}, {"action", "行动"}, {"active", "积极的"},
            {"adapt", "适应"}, {"add", "添加"}, {"address", "地址"}, {"adjust", "调整"}, {"admit", "承认"},
            {"advance", "前进"}, {"advantage", "优势"}, {"adventure", "冒险"}, {"advice", "建议"}, {"affair", "事务"}
        };

        String[][] senior2Words = {
            {"bake", "烘烤"}, {"balance", "平衡"}, {"ban", "禁止"}, {"base", "基础"}, {"basic", "基本的"},
            {"battle", "战斗"}, {"beauty", "美丽"}, {"because", "因为"}, {"become", "成为"}, {"bedroom", "卧室"},
            {"behave", "表现"}, {"behavior", "行为"}, {"behind", "在...后面"}, {"believe", "相信"}, {"bell", "铃"},
            {"belong", "属于"}, {"below", "在...下面"}, {"benefit", "利益"}, {"bent", "弯曲的"}, {"beside", "在...旁边"},
            {"best", "最好的"}, {"betray", "背叛"}, {"beyond", "超过"}, {"bill", "账单"}, {"biology", "生物学"},
            {"birth", "出生"}, {"blame", "责备"}, {"blank", "空白的"}, {"blind", "盲的"}, {"blood", "血液"}
        };

        String[][] senior3Words = {
            {"camp", "露营"}, {"capable", "有能力的"}, {"capital", "首都"}, {"capture", "捕获"}, {"careful", "小心的"},
            {"carry", "携带"}, {"case", "案例"}, {"cause", "原因"}, {"celebrate", "庆祝"}, {"center", "中心"},
            {"challenge", "挑战"}, {"champion", "冠军"}, {"change", "改变"}, {"character", "性格"}, {"charge", "充电"},
            {"chart", "图表"}, {"cheat", "欺骗"}, {"cheer", "欢呼"}, {"chemical", "化学的"}, {"chest", "胸部"},
            {"chief", "主要的"}, {"choice", "选择"}, {"circle", "圆圈"}, {"citizen", "公民"}, {"civil", "公民的"},
            {"claim", "声称"}, {"classify", "分类"}, {"clean", "干净的"}, {"clear", "清楚的"}, {"climate", "气候"}
        };

        String[][] seniorElective1Words = {
            {"damage", "损害"}, {"dance", "跳舞"}, {"danger", "危险"}, {"dare", "敢"}, {"dark", "黑暗的"},
            {"date", "日期"}, {"dead", "死的"}, {"deal", "处理"}, {"dear", "亲爱的"}, {"death", "死亡"},
            {"debate", "辩论"}, {"decide", "决定"}, {"declare", "宣布"}, {"decorate", "装饰"}, {"decrease", "减少"},
            {"deep", "深的"}, {"defeat", "击败"}, {"defend", "保卫"}, {"degree", "程度"}, {"delay", "延迟"},
            {"deliver", "传递"}, {"demand", "要求"}, {"depend", "依赖"}, {"describe", "描述"}, {"deserve", "值得"},
            {"design", "设计"}, {"desire", "渴望"}, {"destroy", "破坏"}, {"develop", "发展"}, {"devote", "奉献"}
        };

        String[][] seniorElective2Words = {
            {"effect", "效果"}, {"effort", "努力"}, {"elder", "年长的"}, {"electric", "电的"}, {"element", "元素"},
            {"elephant", "大象"}, {"else", "其他"}, {"empty", "空的"}, {"enable", "使能够"}, {"encourage", "鼓励"},
            {"end", "结束"}, {"enemy", "敌人"}, {"energy", "能量"}, {"engine", "引擎"}, {"enjoy", "享受"},
            {"enough", "足够的"}, {"enter", "进入"}, {"equal", "平等的"}, {"escape", "逃跑"}, {"especially", "特别"},
            {"even", "甚至"}, {"event", "事件"}, {"ever", "曾经"}, {"every", "每个"}, {"evidence", "证据"},
            {"exact", "精确的"}, {"examine", "检查"}, {"example", "例子"}, {"excellent", "优秀的"}, {"except", "除了"}
        };

        String[][] seniorElective3Words = {
            {"fable", "寓言"}, {"face", "脸"}, {"fact", "事实"}, {"factor", "因素"}, {"fail", "失败"},
            {"fair", "公平的"}, {"faith", "信仰"}, {"fall", "落下"}, {"false", "错误的"}, {"fame", "名声"},
            {"family", "家庭"}, {"familiar", "熟悉的"}, {"famous", "著名的"}, {"fan", "粉丝"}, {"far", "远的"},
            {"fast", "快的"}, {"fat", "胖的"}, {"fear", "害怕"}, {"feed", "喂养"}, {"feel", "感觉"},
            {"female", "女性的"}, {"fence", "栅栏"}, {"fetch", "取"}, {"few", "很少"}, {"field", "领域"},
            {"fight", "战斗"}, {"figure", "数字"}, {"fill", "填充"}, {"final", "最后的"}, {"find", "找到"}
        };

        String[] seniorBooks = {"必修第一册", "必修第二册", "必修第三册", "选择性必修第一册", "选择性必修第二册", "选择性必修第三册"};
        String[][][] seniorWordSets = {senior1Words, senior2Words, senior3Words, seniorElective1Words, seniorElective2Words, seniorElective3Words};

        for (String version : versions) {
            for (int i = 0; i < seniorBooks.length; i++) {
                Grade grade = new Grade();
                grade.id = version + "_senior_" + (i + 1);
                grade.name = seniorBooks[i];
                grade.stage = "高中";
                grade.version = version;
                grade.words = new ArrayList<>();
                
                for (String[] pair : seniorWordSets[i]) {
                    grade.words.add(new WordItem(pair[0], pair[1]));
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
