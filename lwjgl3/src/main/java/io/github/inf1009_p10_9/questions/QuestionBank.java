package io.github.inf1009_p10_9.questions;

import java.util.List;
import java.util.ArrayList;

public class QuestionBank {
    private String subject;    // e.g. "Math", "English"
    private String difficulty; // e.g. "Easy", "Hard"
    private List<Question> questions = new ArrayList<>();

    public QuestionBank(String subject, String difficulty) {
        this.subject = subject;
        this.difficulty = difficulty;
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public int getSize() {
        return questions.size();
    }

    public List<Question> getAllQuestions() {
        return questions;
    }

    public String getSubject() {
        return subject;
    }

    public String getDifficulty() {
        return difficulty;
    }
}