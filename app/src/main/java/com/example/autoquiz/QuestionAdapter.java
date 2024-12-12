package com.example.autoquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private Context context;
    private List<Question> questionList;

    public QuestionAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.tvQuestion.setText(question.getQuestionText());
        holder.tvAnswer.setText("Answer: " + question.getCorrectAnswer());
        holder.tvOptions.setText("Options:\n" + question.getOptions());
    }

//    public void onBindViewHolder(QuestionViewHolder holder, int position) {
//        Question question = questionList.get(position);
//        holder.tvQuestion.setText(question.getQuestionText());
//        holder.tvAnswer.setText("Answer: " + question.getAnswer());
//        holder.tvEntity.setText("Entity: " + question.getEntity());
//        holder.tvLabel.setText("Label: " + question.getLabel());
//    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion, tvAnswer, tvOptions; // Ensure tvOptions is declared here

        public QuestionViewHolder(View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            tvOptions = itemView.findViewById(R.id.tvOptions); // Linking tvOptions
        }
    }

//    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
//        TextView tvQuestion, tvAnswer, tvEntity, tvLabel;
//
//        public QuestionViewHolder(View itemView) {
//            super(itemView);
//            tvQuestion = itemView.findViewById(R.id.tvQuestion);
//            tvAnswer = itemView.findViewById(R.id.tvAnswer);
//            tvEntity = itemView.findViewById(R.id.tvEntity);
//            tvLabel = itemView.findViewById(R.id.tvLabel);
//        }
//    }
}
