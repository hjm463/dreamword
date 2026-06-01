package com.dreamword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dreamword.data.MusicManager;
import com.dreamword.data.WordStore;

public class MainActivity extends AppCompatActivity {
    private WordStore wordStore;
    private TextView tvTotal, tvMastered, tvReview;
    private CardView btnMusic;
    private MusicManager musicManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordStore = new WordStore(this);
        musicManager = MusicManager.getInstance();
        musicManager.init(this);

        tvTotal = findViewById(R.id.tvTotal);
        tvMastered = findViewById(R.id.tvMastered);
        tvReview = findViewById(R.id.tvReview);

        CardView btnAdd = findViewById(R.id.btnAdd);
        CardView btnReview = findViewById(R.id.btnReview);
        CardView btnBook = findViewById(R.id.btnBook);
        CardView btnDictionary = findViewById(R.id.btnDictionary);
        btnMusic = findViewById(R.id.btnMusic);

        btnAdd.setOnClickListener(v -> startActivity(new Intent(this, AddActivity.class)));
        btnReview.setOnClickListener(v -> startActivity(new Intent(this, ReviewActivity.class)));
        btnBook.setOnClickListener(v -> startActivity(new Intent(this, BookActivity.class)));
        btnDictionary.setOnClickListener(v -> startActivity(new Intent(this, DictionaryActivity.class)));
        btnMusic.setOnClickListener(v -> toggleMusic());
        CardView btnTranslate = findViewById(R.id.btnTranslate);
        btnTranslate.setOnClickListener(v -> startActivity(new Intent(this, TranslateActivity.class)));
    }

    private void toggleMusic() {
        if (musicManager.isPlaying()) {
            musicManager.stop();
            btnMusic.setCardBackgroundColor(getColor(R.color.card_bg));
        } else {
            musicManager.play();
            btnMusic.setCardBackgroundColor(getColor(R.color.primary_teal));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }

    private void updateStats() {
        tvTotal.setText(String.valueOf(wordStore.getTotalCount()));
        tvMastered.setText(String.valueOf(wordStore.getMasteredCount()));
        tvReview.setText(String.valueOf(wordStore.getUnmasteredCount()));
    }
}
