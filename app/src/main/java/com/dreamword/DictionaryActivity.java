package com.dreamword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dreamword.data.DictionaryManager;
import com.dreamword.data.DictionaryManager.Grade;
import com.dreamword.data.DictionaryManager.WordItem;
import com.dreamword.data.WordStore;

import java.util.ArrayList;
import java.util.List;

public class DictionaryActivity extends AppCompatActivity {
    private DictionaryManager dictionaryManager;
    private WordStore wordStore;
    private Spinner stageSpinner;
    private Spinner bookSpinner;
    private ListView wordListView;
    private ArrayAdapter<String> wordAdapter;
    private List<String> wordList;
    private List<WordItem> currentWords;
    private Grade currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        dictionaryManager = DictionaryManager.getInstance();
        dictionaryManager.loadDictionary(this);
        wordStore = new WordStore(this);

        stageSpinner = findViewById(R.id.spinnerStage);
        bookSpinner = findViewById(R.id.spinnerBook);
        wordListView = findViewById(R.id.lvWords);
        TextView tvEmpty = findViewById(R.id.tvEmpty);
        CardView btnAddSelected = findViewById(R.id.btnAddSelected);
        CardView btnStartReview = findViewById(R.id.btnStartReview);
        TextView btnBack = findViewById(R.id.btnBack);

        wordList = new ArrayList<>();
        wordAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, wordList);
        wordListView.setAdapter(wordAdapter);
        wordListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        loadStages();

        stageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadBooks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        bookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadWords();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnBack.setOnClickListener(v -> finish());

        btnAddSelected.setOnClickListener(v -> {
            if (currentWords == null || currentWords.isEmpty()) {
                Toast.makeText(this, "请先选择词库", Toast.LENGTH_SHORT).show();
                return;
            }

            int count = 0;
            for (int i = 0; i < wordListView.getCount(); i++) {
                if (wordListView.isItemChecked(i)) {
                    WordItem item = currentWords.get(i);
                    wordStore.addWord(item.english, item.chinese);
                    count++;
                }
            }

            if (count > 0) {
                Toast.makeText(this, "已添加 " + count + " 个单词", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "请选择要添加的单词", Toast.LENGTH_SHORT).show();
            }
        });

        btnStartReview.setOnClickListener(v -> {
            if (currentWords == null || currentWords.isEmpty()) {
                Toast.makeText(this, "请先选择词库", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<String> selectedWords = new ArrayList<>();
            for (int i = 0; i < wordListView.getCount(); i++) {
                if (wordListView.isItemChecked(i)) {
                    WordItem item = currentWords.get(i);
                    wordStore.addWord(item.english, item.chinese);
                    selectedWords.add(item.english);
                }
            }

            if (selectedWords.isEmpty()) {
                Toast.makeText(this, "请选择要复习的单词", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putStringArrayListExtra("selectedWords", selectedWords);
            startActivity(intent);
        });
    }

    private void loadStages() {
        List<String> stages = dictionaryManager.getStages();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stageSpinner.setAdapter(adapter);
    }

    private void loadBooks() {
        String stage = stageSpinner.getSelectedItem().toString();
        List<Grade> books = dictionaryManager.getBooksForStage(stage);
        
        List<String> bookNames = new ArrayList<>();
        for (Grade book : books) {
            bookNames.add(book.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookSpinner.setAdapter(adapter);
    }

    private void loadWords() {
        String stage = stageSpinner.getSelectedItem().toString();
        String bookName = bookSpinner.getSelectedItem().toString();
        
        List<Grade> books = dictionaryManager.getBooksForStage(stage);
        for (Grade book : books) {
            if (book.name.equals(bookName)) {
                currentBook = book;
                currentWords = book.words;
                break;
            }
        }

        wordList.clear();
        if (currentWords != null) {
            for (WordItem item : currentWords) {
                wordList.add(item.english + " - " + item.chinese);
            }
        }
        wordAdapter.notifyDataSetChanged();

        wordListView.clearChoices();
        for (int i = 0; i < wordListView.getCount(); i++) {
            wordListView.setItemChecked(i, true);
        }
    }
}