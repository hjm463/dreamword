package com.dreamword;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dreamword.data.WordStore;

import java.util.regex.Pattern;

public class AddActivity extends AppCompatActivity {
    private WordStore wordStore;
    private EditText etInput;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        wordStore = new WordStore(this);
        etInput = findViewById(R.id.etInput);
        tvResult = findViewById(R.id.tvResult);

        TextView btnBack = findViewById(R.id.btnBack);
        CardView btnAddWord = findViewById(R.id.btnAddWord);

        btnBack.setOnClickListener(v -> { try { finish(); } catch (Exception e) { e.printStackTrace(); } });
        btnAddWord.setOnClickListener(v -> { try { doAddWord(); } catch (Exception e) { e.printStackTrace(); } });
    }

    private void doAddWord() {
        String text = etInput.getText().toString().trim();
        if (text.isEmpty()) {
            tvResult.setText("请输入单词！");
            tvResult.setTextColor(getColor(R.color.danger_red));
            return;
        }

        String[] parts = parseWord(text);
        if (parts == null) {
            tvResult.setText("格式错误，请用\"英文 中文\"或\"英文|中文\"");
            tvResult.setTextColor(getColor(R.color.danger_red));
            return;
        }

        wordStore.addWord(parts[0], parts[1]);
        tvResult.setText("已添加：" + parts[0] + " - " + parts[1]);
        tvResult.setTextColor(getColor(R.color.success_green));
        etInput.setText("");
    }

    private String[] parseWord(String text) {
        if (text.contains("|")) {
            String[] p = text.split("\\|", 2);
            if (p.length == 2) return new String[]{p[0].trim(), p[1].trim()};
        }

        Pattern chinesePattern = Pattern.compile("[\\u4e00-\\u9fff]");
        java.util.regex.Matcher matcher = chinesePattern.matcher(text);
        if (matcher.find()) {
            int idx = matcher.start();
            String eng = text.substring(0, idx).trim();
            String chn = text.substring(idx).trim();
            if (!eng.isEmpty() && !chn.isEmpty()) {
                return new String[]{eng, chn};
            }
        }

        String[] p = text.split("\\s+", 2);
        if (p.length == 2) return new String[]{p[0].trim(), p[1].trim()};

        return null;
    }
}
