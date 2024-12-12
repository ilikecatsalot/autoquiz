package com.example.autoquiz;

import java.util.List;

public class Question {
    private String questionText;
    private String correctAnswer;
    private String options;

    // Constructor to initialize question with text, correct answer, and options
    public Question(String questionText, String correctAnswer, String options, List<String> distractors) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    // Getter methods for the properties
    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getOptions() {
        return options;
    }
}


//public class Question {
//    private String questionText;
//    private String answer;
//    private String entity;
//    private String label;
//
//    public Question(String questionText, String answer, String entity, String label) {
//        this.questionText = questionText;
//        this.answer = answer;
//        this.entity = entity;
//        this.label = label;
//    }
//
//    public String getQuestionText() {
//        return questionText;
//    }
//
//    public String getAnswer() {
//        return answer;
//    }
//
//    public String getEntity() {
//        return entity;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//}
