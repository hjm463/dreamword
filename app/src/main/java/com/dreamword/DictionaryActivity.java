package com.dreamword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private Spinner versionSpinner;
    private Spinner stageSpinner;
    private Spinner bookSpinner;
    private ListView wordListView;
    private ArrayAdapter<String> wordAdapter;
    private List<String> wordList;
    private List<WordItem> currentWords;
    private Grade currentBook;
    private TextView tvPageInfo;
    private Button btnPrevPage;
    private Button btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        dictionaryManager = DictionaryManager.getInstance();
        dictionaryManager.loadDictionary();
        wordStore = new WordStore(this);

        versionSpinner = findViewById(R.id.spinnerVersion);
        stageSpinner = findViewById(R.id.spinnerStage);
        bookSpinner = findViewById(R.id.spinnerBook);
        wordListView = findViewById(R.id.lvWords);
        tvPageInfo = findViewById(R.id.tvPageInfo);
        btnPrevPage = findViewById(R.id.btnPrevPage);
        btnNextPage = findViewById(R.id.btnNextPage);
        CardView btnAddSelected = findViewById(R.id.btnAddSelected);
        CardView btnStartReview = findViewById(R.id.btnStartReview);
        TextView btnBack = findViewById(R.id.btnBack);

        wordList = new ArrayList<>();
        wordAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, wordList);
        wordListView.setAdapter(wordAdapter);
        wordListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        loadVersions();

        versionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadStages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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
                currentPage = 0;
                loadWords();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnPrevPage.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                loadWords();
            }
        });

        btnNextPage.setOnClickListener(v -> {
            if (currentWords != null && (currentPage + 1) * PAGE_SIZE < currentWords.size()) {
                currentPage++;
                loadWords();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnAddSelected.setOnClickListener(v -> {
            if (currentWords == null || currentWords.isEmpty()) {
                Toast.makeText(this, "请先选择词库", Toast.LENGTH_SHORT).show();
                return;
            }

            int count = 0;
            List<WordItem> displayedWords = getCurrentPageWords();
            
            for (int i = 0; i < wordListView.getCount(); i++) {
                if (wordListView.isItemChecked(i)) {
                    WordItem item = displayedWords.get(i);
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
            
            for (int i = 0; i < currentWords.size(); i++) {
                int globalIndex = i;
                int page = globalIndex / PAGE_SIZE;
                int localIndex = globalIndex % PAGE_SIZE;
                
                if (page == currentPage && wordListView.isItemChecked(localIndex)) {
                    WordItem item = currentWords.get(globalIndex);
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

    private List<WordItem> getCurrentPageWords() {
        if (currentWords == null) return new ArrayList<>();
        
        List<WordItem> pageWords = new ArrayList<>();
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, currentWords.size());
        
        for (int i = start; i < end; i++) {
            pageWords.add(currentWords.get(i));
        }
        
        return pageWords;
    }

    private void loadVersions() {
        List<String> versions = dictionaryManager.getVersions();
        if (versions.isEmpty()) {
            versions.add("人教版");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, versions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        versionSpinner.setAdapter(adapter);
    }

    private void loadStages() {
        List<String> stages = dictionaryManager.getStages();
        if (stages.isEmpty()) {
            stages.add("小学");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stageSpinner.setAdapter(adapter);
    }

    private void loadBooks() {
        String version = versionSpinner.getSelectedItem().toString();
        String stage = stageSpinner.getSelectedItem().toString();
        List<Grade> books = dictionaryManager.getBooksForVersionAndStage(version, stage);
        
        List<String> bookNames = new ArrayList<>();
        for (Grade book : books) {
            bookNames.add(book.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookSpinner.setAdapter(adapter);
    }

    private void loadWords() {
        String version = versionSpinner.getSelectedItem().toString();
        String stage = stageSpinner.getSelectedItem().toString();
        String bookName = bookSpinner.getSelectedItem().toString();
        
        List<Grade> books = dictionaryManager.getBooksForVersionAndStage(version, stage);
        for (Grade book : books) {
            if (book.name.equals(bookName)) {
                currentBook = book;
                currentWords = book.words;
                break;
            }
        }

        wordList.clear();
        List<WordItem> displayedWords = getCurrentPageWords();
        
        for (WordItem item : displayedWords) {
            wordList.add(item.english + " - " + item.chinese);
        }
        wordAdapter.notifyDataSetChanged();

        updatePageInfo();
        updatePageButtons();

        wordListView.clearChoices();
        for (int i = 0; i < wordListView.getCount(); i++) {
            wordListView.setItemChecked(i, true);
        }
    }

    private void updatePageInfo() {
        if (currentWords == null || currentWords.isEmpty()) {
            tvPageInfo.setText("第 0 / 0 页");
            return;
        }
        
        int totalPages = (int) Math.ceil((double) currentWords.size() / PAGE_SIZE);
        tvPageInfo.setText(String.format("第 %d / %d 页", currentPage + 1, totalPages));
    }

    private void updatePageButtons() {
        if (currentWords == null || currentWords.isEmpty()) {
            btnPrevPage.setEnabled(false);
            btnNextPage.setEnabled(false);
            return;
        }
        
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled((currentPage + 1) * PAGE_SIZE < currentWords.size());
    }
}
