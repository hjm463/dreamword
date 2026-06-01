package com.dreamword;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dreamword.data.TranslateManager;
import com.dreamword.data.WordStore;

public class TranslateActivity extends AppCompatActivity {
    private EditText etInput;
    private TextView tvResult;
    private WordStore wordStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        wordStore = new WordStore(this);

        etInput = findViewById(R.id.etInput);
        tvResult = findViewById(R.id.tvResult);

        TextView btnBack = findViewById(R.id.btnBack);
        CardView btnTranslate = findViewById(R.id.btnTranslate);
        CardView btnAddWord = findViewById(R.id.btnAddWord);

        btnBack.setOnClickListener(v -> finish());
        btnTranslate.setOnClickListener(v -> doTranslate());
        btnAddWord.setOnClickListener(v -> addToWordBook());
    }

    private void doTranslate() {
        String text = etInput.getText().toString().trim();
        if (text.isEmpty()) {
            tvResult.setText("请输入要翻译的内容");
            tvResult.setTextColor(getColor(R.color.danger_red));
            return;
        }

        tvResult.setText("正在翻译...");
        tvResult.setTextColor(getColor(R.color.accent_gold));

        TranslateManager.translate(text, new TranslateManager.TranslateCallback() {
            @Override
            public void onSuccess(String english, String chinese) {
                String result = String.format("英文：%s\n中文：%s", english, chinese);
                tvResult.setText(result);
                tvResult.setTextColor(getColor(R.color.success_green));
            }

            @Override
            public void onError(String error) {
                tvResult.setText("翻译失败：" + error);
                tvResult.setTextColor(getColor(R.color.danger_red));
            }
        });
    }

    private void addToWordBook() {
        String result = tvResult.getText().toString();
        if (!result.contains("英文：") || !result.contains("中文：")) {
            Toast.makeText(this, "请先翻译一个单词", Toast.LENGTH_SHORT).show();
            return;
        }

        String english = result.substring(result.indexOf("英文：") + 3, result.indexOf("\n中文："));
        String chinese = result.substring(result.indexOf("中文：") + 3);

        wordStore.addWord(english.trim(), chinese.trim());
        Toast.makeText(this, "已添加：" + english + " - " + chinese, Toast.LENGTH_SHORT).show();
    }
}