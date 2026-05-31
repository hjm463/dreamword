package com.dreamword;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dreamword.data.WordData;
import com.dreamword.data.WordStore;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    private WordStore wordStore;
    private List<WordData> reviewWords;
    private int currentIndex = 0;

    private TextView tvProgress, tvChinese, tvHint, tvResult;
    private EditText etAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        wordStore = new WordStore(this);

        tvProgress = findViewById(R.id.tvProgress);
        tvChinese = findViewById(R.id.tvChinese);
        tvHint = findViewById(R.id.tvHint);
        tvResult = findViewById(R.id.tvResult);
        etAnswer = findViewById(R.id.etAnswer);

        TextView btnBack = findViewById(R.id.btnBack);
        CardView btnPronounce = findViewById(R.id.btnPronounce);
        CardView btnShowHint = findViewById(R.id.btnShowHint);
        CardView btnCheck = findViewById(R.id.btnCheck);
        CardView btnNext = findViewById(R.id.btnNext);

        btnBack.setOnClickListener(v -> finish());
        btnPronounce.setOnClickListener(v -> doPronounce());
        btnShowHint.setOnClickListener(v -> doShowHint());
        btnCheck.setOnClickListener(v -> doCheckAnswer());
        btnNext.setOnClickListener(v -> doNextWord());

        reviewWords = wordStore.getUnmasteredWords();
        if (reviewWords.isEmpty()) {
            tvChinese.setText("所有单词已掌握！");
            tvProgress.setText("");
        } else {
            showWord();
        }
    }

    private void showWord() {
        if (currentIndex >= reviewWords.size()) {
            tvChinese.setText("本轮完成！");
            tvProgress.setText("");
            tvHint.setText("");
            tvResult.setText("");
            return;
        }
        WordData word = reviewWords.get(currentIndex);
        tvChinese.setText(word.getChinese());
        tvProgress.setText("进度: " + (currentIndex + 1) + "/" + reviewWords.size());
        etAnswer.setText("");
        tvHint.setText("");
        tvResult.setText("");
    }

    private void doPronounce() {
        if (reviewWords.isEmpty() || currentIndex >= reviewWords.size()) return;
        String word = reviewWords.get(currentIndex).getEnglish();
        new Thread(() -> {
            try {
                URL url = new URL("https://dict.youdao.com/dictvoice?type=0&audio=" + java.net.URLEncoder.encode(word, "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setConnectTimeout(10000);
                byte[] data = new byte[conn.getContentLength()];
                java.io.InputStream in = conn.getInputStream();
                int total = 0;
                while (total < data.length) {
                    int read = in.read(data, total, data.length - total);
                    if (read < 0) break;
                    total += read;
                }
                in.close();

                if (data.length > 100) {
                    File tempFile = new File(getCacheDir(), "pron_" + word.replaceAll("[^a-zA-Z0-9_]", "_") + ".mp3");
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    fos.write(data);
                    fos.close();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        try {
                            MediaPlayer mp = new MediaPlayer();
                            mp.setDataSource(tempFile.getAbsolutePath());
                            mp.prepare();
                            mp.start();
                            mp.setOnCompletionListener(mp2 -> {
                                mp2.release();
                                tempFile.delete();
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void doShowHint() {
        if (reviewWords.isEmpty() || currentIndex >= reviewWords.size()) return;
        String word = reviewWords.get(currentIndex).getEnglish();
        int n = word.length();
        String hint;
        if (n <= 1) hint = "?";
        else if (n == 2) hint = word.charAt(0) + "_ (2字母)";
        else if (n == 3) hint = word.charAt(0) + "__ (3字母)";
        else hint = word.substring(0, 2) + "_".repeat(n - 2) + " (" + n + "字母)";
        tvHint.setText("提示: " + hint);
    }

    private void doCheckAnswer() {
        if (reviewWords.isEmpty() || currentIndex >= reviewWords.size()) return;
        String answer = etAnswer.getText().toString().trim().toLowerCase();
        String correct = reviewWords.get(currentIndex).getEnglish().toLowerCase();

        if (answer.equals(correct)) {
            tvResult.setText("正确！太棒了！");
            tvResult.setTextColor(getColor(R.color.success_green));
            wordStore.markMastered(reviewWords.get(currentIndex).getEnglish());
            currentIndex++;
            new Handler(Looper.getMainLooper()).postDelayed(this::showWord, 800);
        } else {
            tvResult.setText("错误！正确答案：" + reviewWords.get(currentIndex).getEnglish());
            tvResult.setTextColor(getColor(R.color.danger_red));
            currentIndex++;
            new Handler(Looper.getMainLooper()).postDelayed(this::showWord, 1500);
        }
    }

    private void doNextWord() {
        if (reviewWords.isEmpty()) {
            reviewWords = wordStore.getUnmasteredWords();
            currentIndex = 0;
        }
        if (currentIndex >= reviewWords.size()) {
            reviewWords = wordStore.getUnmasteredWords();
            currentIndex = 0;
        }
        showWord();
    }
}
