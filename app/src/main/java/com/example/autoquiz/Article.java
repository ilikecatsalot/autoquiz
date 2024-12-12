package com.example.autoquiz;

public class Article {
    String link;
    String title;  // Use title directly
    String date;

    String content;

    public Article(String link, String title, String date) {
        this.link = link;
        this.title = title;  // Initialize title
        this.date = date;
        this.content = content;
    }

    // Getters
    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;  // Return the title
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}