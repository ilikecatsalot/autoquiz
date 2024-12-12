package com.example.autoquiz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    Context context;
    ArrayList<Article> articles;

    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvDate.setText(article.getDate());

        // Open article link in a browser
        holder.btnViewArticle.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getLink()));
            context.startActivity(browserIntent);
        });

        // Navigate to the quiz generation activity
        holder.btnGenerateQuiz.setOnClickListener(v -> {
            Intent quizIntent = new Intent(context, QuizActivity.class);
            quizIntent.putExtra("articleTitle", article.getTitle());
            quizIntent.putExtra("articleText", article.getContent()); // Add the article content here
            context.startActivity(quizIntent);
        });
    }



    @Override
    public int getItemCount() {
        return articles.size();  // Return the size of the articles list
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate;
        Button btnViewArticle, btnGenerateQuiz;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle); // Title TextView
            tvDate = itemView.findViewById(R.id.tvDate);   // Date TextView
            btnViewArticle = itemView.findViewById(R.id.btnViewArticle); // View Article button
            btnGenerateQuiz = itemView.findViewById(R.id.btnGenerateQuiz); // Generate Quiz button
        }
    }

}
