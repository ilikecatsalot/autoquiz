package com.example.autoquiz;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ArticlesActivity extends AppCompatActivity {

    ScraperHelper db;
    ArrayList<Article> articles;
    RecyclerView recyclerView;
    ArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        db = new ScraperHelper(this);
        articles = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadArticles();
        adapter = new ArticleAdapter(this, articles);
        recyclerView.setAdapter(adapter);
    }

    private void loadArticles() {
        Cursor cursor = db.getAllData();
        if (cursor.getCount() == 0) return;

        try {
            while (cursor.moveToNext()) {
                // Adjust the indexes based on the query that fetches title and date only
                String link = cursor.getString(1);
                String title = cursor.getString(2);  // Title is now the first column
                String date = cursor.getString(4);   // Date is now the second column

                // Add articles with only title and date
                articles.add(new Article(link, title, date)); // Updated constructor (no content)
            }
        } finally {
            cursor.close();
        }
    }
}
