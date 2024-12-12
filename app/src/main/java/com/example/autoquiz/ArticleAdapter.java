package com.example.autoquiz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
        holder.tvTitle.setText(article.getTitle());  // Set the title
        holder.tvDate.setText(article.getDate());    // Set the date

        holder.itemView.setOnClickListener(v -> {
            // Open the article link in a browser when clicked
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getLink()));
            context.startActivity(browserIntent);
        });
    }


    @Override
    public int getItemCount() {
        return articles.size();  // Return the size of the articles list
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle); // Title TextView
            tvDate = itemView.findViewById(R.id.tvDate);   // Date TextView
        }
    }
}
