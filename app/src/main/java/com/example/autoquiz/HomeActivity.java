package com.example.autoquiz;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    ScraperHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new ScraperHelper(this);

        EditText etDuration = findViewById(R.id.etDuration);
        Button btnFetchArticles = findViewById(R.id.btnFetchArticles);
        Button btnViewArticles = findViewById(R.id.btnViewArticles);

        btnFetchArticles.setOnClickListener(view -> {
            String durationText = etDuration.getText().toString().trim();

            // Validate duration input
            if (durationText.isEmpty()) {
                Toast.makeText(this, "Please enter a duration in minutes", Toast.LENGTH_SHORT).show();
                return;
            }

            int durationMinutes;
            try {
                durationMinutes = Integer.parseInt(durationText);
                if (durationMinutes <= 0) {
                    Toast.makeText(this, "Duration must be greater than zero", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid duration entered", Toast.LENGTH_SHORT).show();
                return;
            }

            // Start the scraper in a background thread with the specified duration
            new Thread(() -> scrapeArticles(durationMinutes)).start();
        });

        btnViewArticles.setOnClickListener(view -> {
            // Navigate to ArticlesActivity to view articles
            Intent intent = new Intent(HomeActivity.this, ArticlesActivity.class);
            startActivity(intent);
        });
    }

    private void scrapeArticles(int durationMinutes) {
        String url = "https://forumias.com/blog/mustread/";
        long startTime = System.currentTimeMillis();
        long maxDuration = durationMinutes * 60 * 1000; // Convert minutes to milliseconds

        try {
            // Fetch and parse the main page
            Document mainDoc = Jsoup.connect(url).get();
            Elements forumLinks = mainDoc.select("a[href]");

            for (Element forumLink : forumLinks) {
                if (!forumLink.attr("href").contains("https://forumias.com/blog/must-read-")) continue;

                // Check time limit
                if (System.currentTimeMillis() - startTime > maxDuration) {
                    Log.d("Scraper", "Time limit reached. Stopping scrape.");
                    break;
                }

                String forumUrl = forumLink.attr("href");
                String date = extractDateFromUrl(forumUrl);

                if (date == null) {
                    Log.d("Scraper", "No date found in URL: " + forumUrl);
                    continue;
                }

                try {
                    // Fetch and parse the ForumIAS page
                    Document forumDoc = Jsoup.connect(forumUrl).get();
                    Elements hinduLinks = forumDoc.select("a[href]");

                    for (Element hinduLink : hinduLinks) {
                        String hinduUrl = hinduLink.attr("href");
                        String title = hinduLink.text(); // Extract the link text as the title

                        if (!(hinduUrl.startsWith("https://www.thehindu.com/") || hinduUrl.startsWith("https://epaper.thehindu.com/"))) {
                            continue;
                        }

                        try {
                            // Fetch and parse The Hindu article
                            Document hinduDoc = Jsoup.connect(hinduUrl).get();
                            Elements paragraphs = hinduDoc.select("p");

                            StringBuilder articleContent = new StringBuilder();
                            for (Element paragraph : paragraphs) {
                                articleContent.append(paragraph.text()).append("\n");
                            }

                            // Clean the article content
                            String cleanedContent = cleanContent(articleContent.toString());

                            // Store the title, content, and date
                            db.insertData(hinduUrl, title, cleanedContent, date);
                            Log.d("Scraper", "Data stored for: " + hinduUrl);

                            // Respectful scraping with a small delay
                            Thread.sleep(1000);

                        } catch (Exception e) {
                            Log.e("Scraper", "Failed to fetch Hindu link: " + hinduUrl, e);
                        }
                    }

                } catch (Exception e) {
                    Log.e("Scraper", "Failed to fetch ForumIAS link: " + forumUrl, e);
                }
            }

            runOnUiThread(() -> Toast.makeText(this, "Articles fetched successfully!", Toast.LENGTH_SHORT).show());

        } catch (IOException e) {
            Log.e("Scraper", "Failed to fetch main page: " + url, e);
            runOnUiThread(() -> Toast.makeText(this, "Failed to fetch articles", Toast.LENGTH_SHORT).show());
        }
    }


    private String cleanContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        // Remove everything before "Photo Credit: The Hindu"
        String photoCreditKeyword = "Photo Credit: The Hindu";
        if (content.contains(photoCreditKeyword)) {
            content = content.substring(content.indexOf(photoCreditKeyword) + photoCreditKeyword.length()).trim();
        }

        // Remove everything after "Published"
        String publishedKeyword = "BACK TO TOP";
        if (content.contains(publishedKeyword)) {
            content = content.substring(0, content.indexOf(publishedKeyword)).trim();
        }

        // Remove common placeholder texts
        String[] placeholders = {
                "To enjoy additional benefits",
                "CONNECT WITH US",
                "Updated -",
                "BACK TO TOP",
                "Terms & conditions",
                "Institutional Subscriber",
                "Comments have to be in English, and in full sentences.",
                "They cannot be abusive or personal.",
                "Please abide by our community guidelines for posting your comments.",
                "We have migrated to a new commenting platform.",
                "Copyright©"
        };

        for (String placeholder : placeholders) {
            content = content.replace(placeholder, "").trim();
        }

        return content;
    }

    private String extractDateFromUrl(String url) {
        // Regular expression to match dates in the format: 7th-march-2024
        String regex = "(\\d{1,2}(?:st|nd|rd|th)-[a-zA-Z]+-\\d{4})";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            String rawDate = matcher.group(0);

            try {
                // Remove ordinal suffixes (st, nd, rd, th)
                String cleanedDate = rawDate.replaceAll("(st|nd|rd|th)", "");

                // Convert raw date string to a Date object
                java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("d-MMMM-yyyy", java.util.Locale.ENGLISH);
                inputFormat.setLenient(false); // Use strict parsing to avoid unexpected formats
                java.util.Date date = inputFormat.parse(cleanedDate);

                // Format the Date object into "dd-MM-yy"
                java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd-MM-yy");
                return outputFormat.format(date); // Return the formatted date string

            } catch (java.text.ParseException e) {
                e.printStackTrace();
                return null; // Return null if parsing fails
            }
        }

        return null; // Return null if no match is found
    }

}
