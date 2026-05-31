package com.dreamword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dreamword.data.WordStore;

public class MainActivity extends AppCompatActivity {
    private WordStore wordStore;
    private TextView tvTotal, tvMastered, tvReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordStore = new WordStore(this);

        tvTotal = findViewById(R.id.tvTotal);
        tvMastered = findViewById(R.id.tvMastered);
        tvReview = findViewById(R.id.tvReview);

        CardView btnAdd = findViewById(R.id.btnAdd);
        CardView btnReview = findViewById(R.id.btnReview);
        CardView btnBook = findViewById(R.id.btnBook);

        btnAdd.setOnClickListener(v -> { try { startActivity(new Intent(this, AddActivity.class)); } catch (Exception e) { e.printStackTrace(); } });
        btnReview.setOnClickListener(v -> { try { startActivity(new Intent(this, ReviewActivity.class)); } catch (Exception e) { e.printStackTrace(); } });
        btnBook.setOnClickListener(v -> { try { startActivity(new Intent(this, BookActivity.class)); } catch (Exception e) { e.printStackTrace(); } });
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
