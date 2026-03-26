package io.github.inf1009_p10_9.game.questions;

import java.util.List;
import java.util.ArrayList;

// holds a collection of questions for a specific subject and difficulty pairing
public class QuestionBank {
    private String subject;
    private String difficulty;
    private List<Question> questions = new ArrayList<>();

    public QuestionBank(String subject, String difficulty) {
        this.subject = subject;
        this.difficulty = difficulty;
    }

    // adding and retrieving questions
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

    // getters
    public String getSubject() {
        return subject;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
