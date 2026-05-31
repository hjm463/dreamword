package com.dreamword;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamword.data.WordData;
import com.dreamword.data.WordStore;

import java.util.List;

public class BookActivity extends AppCompatActivity {
    private WordStore wordStore;
    private RecyclerView rvWords;
    private TextView tvEmpty;
    private WordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        wordStore = new WordStore(this);
        rvWords = findViewById(R.id.rvWords);
        tvEmpty = findViewById(R.id.tvEmpty);

        TextView btnBack = findViewById(R.id.btnBack);
        CardView btnRefresh = findViewById(R.id.btnRefresh);
        CardView btnClear = findViewById(R.id.btnClear);

        btnBack.setOnClickListener(v -> finish());
        btnRefresh.setOnClickListener(v -> refreshList());
        btnClear.setOnClickListener(v -> showClearConfirm());

        rvWords.setLayoutManager(new LinearLayoutManager(this));
        refreshList();
    }

    private void refreshList() {
        List<WordData> words = wordStore.getWords();
        if (words.isEmpty()) {
            rvWords.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvWords.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            adapter = new WordAdapter(words);
            rvWords.setAdapter(adapter);
        }
    }

    private void showClearConfirm() {
        new AlertDialog.Builder(this)
            .setTitle("确认清空")
            .setMessage("确定要清空所有单词吗？此操作不可撤销。")
            .setPositiveButton("清空", (d, w) -> {
                wordStore.clearWords();
                refreshList();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    static class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {
        private final List<WordData> words;

        WordAdapter(List<WordData> words) {
            this.words = words;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            WordData word = words.get(position);
            holder.tvEnglish.setText(word.getEnglish());
            holder.tvChinese.setText(word.getChinese());
            holder.vStatus.setBackgroundResource(
                word.isMastered() ? R.drawable.card_bg_success : R.drawable.card_bg_danger
            );
        }

        @Override
        public int getItemCount() {
            return words.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvEnglish, tvChinese;
            View vStatus;

            ViewHolder(View itemView) {
                super(itemView);
                tvEnglish = itemView.findViewById(R.id.tvEnglish);
                tvChinese = itemView.findViewById(R.id.tvChinese);
                vStatus = itemView.findViewById(R.id.vStatus);
            }
        }
    }
}
