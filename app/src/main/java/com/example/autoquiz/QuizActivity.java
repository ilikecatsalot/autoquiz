package com.example.autoquiz;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



public class QuizActivity extends AppCompatActivity {
    private String articleTitle;
    private String articleText;
    private QuestionHelper questionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize QuestionHelper
        questionHelper = new QuestionHelper(this);

        // Get the article title and content from the intent
        articleTitle = getIntent().getStringExtra("articleTitle");
        articleText = getIntent().getStringExtra("articleText");

        // Set the article title as a placeholder
        TextView textView = findViewById(R.id.tvArticleTitle);
        textView.setText("Generate quiz for: " + articleTitle);

        // Setup a button to open the number of questions dialog
        Button btnGenerate = findViewById(R.id.btnGenerateQuiz);
        btnGenerate.setOnClickListener(v -> showNumberOfQuestionsDialog());
    }

    private void showNumberOfQuestionsDialog() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this)
                .setTitle("Enter number of questions")
                .setView(input)
                .setPositiveButton("OK", (dialog, whichButton) -> {
                    String numQuestions = input.getText().toString();
                    if (!numQuestions.isEmpty()) {
                        generateQuiz(Integer.parseInt(numQuestions));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void generateQuiz(int numQuestions) {
        String url = "https://uptight-kathy-jorhateng-bd7401db.koyeb.app/generate-questions";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text", articleText);
            jsonObject.put("num_questions", numQuestions);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        JSONObject questions = response.getJSONObject("questions");
                        displayQuiz(questions);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error generating quiz: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }

    private void displayQuiz(JSONObject questions) {
        try {
            List<Question> questionList = new ArrayList<>();

            Iterator<String> keys = questions.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject questionData = questions.getJSONObject(key);
                String questionText = questionData.getString("question");
                String answer = questionData.getString("answer");
                String entity = questionData.getString("entity");

                // Fetch distractors for the entity from SQLite using QuestionHelper
                List<String> distractors = questionHelper.getDistractors(entity);
                distractors.add(answer);

                // Shuffle options
                Collections.shuffle(distractors);

                Question question = new Question(questionText, answer, entity, distractors);
                questionList.add(question);
            }

            QuestionAdapter questionAdapter = new QuestionAdapter(this, questionList);
            RecyclerView recyclerView = findViewById(R.id.recyclerViewQuiz);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(questionAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
