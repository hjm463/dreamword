package com.dreamword.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordData {
    private String english;
    private String chinese;
    private boolean mastered;

    public WordData(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
        this.mastered = false;
    }

    public String getEnglish() { return english; }
    public String getChinese() { return chinese; }
    public boolean isMastered() { return mastered; }
    public void setMastered(boolean mastered) { this.mastered = mastered; }
}
