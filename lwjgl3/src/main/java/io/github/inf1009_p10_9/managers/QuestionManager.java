package io.github.inf1009_p10_9.managers;

import java.util.Collections;
import java.util.HashMap;
import io.github.inf1009_p10_9.interfaces.IManager;
import io.github.inf1009_p10_9.questions.*;

public class QuestionManager implements IManager {
    private static QuestionManager instance;

    private HashMap<String, QuestionBank> allBanks;
    private QuestionBank activeBank;
    private int currentIndex;
    private int score;
    private boolean bankFinished;

    protected QuestionManager() {}

    public static QuestionManager getInstance() {
        if (instance == null) {
            instance = new QuestionManager();
        }
        return instance;
    }

    @Override
    public void initialize() {
        allBanks = new HashMap<>();
        currentIndex = 0;
        score = 0;
        bankFinished = false;
        loadAllBanks();
        System.out.println("QuestionManager initialized");
    }

    @Override
    public void update() {}

    @Override
    public void clear() {
        allBanks = null;
        activeBank = null;
        currentIndex = 0;
        score = 0;
        bankFinished = false;
    }

    private void loadAllBanks() {
        // Math Easy
        QuestionBank mathEasy = new QuestionBank("Math", "Easy");
        mathEasy.addQuestion(new Question("1 + 1 = ?", "2", "3", "A"));
        mathEasy.addQuestion(new Question("2 + 2 = ?", "5", "4", "B"));
        mathEasy.addQuestion(new Question("3 + 1 = ?", "4", "5", "A"));
        mathEasy.addQuestion(new Question("5 - 2 = ?", "2", "3", "B"));
        allBanks.put("Math_Easy", mathEasy);

        // Math Hard
        QuestionBank mathHard = new QuestionBank("Math", "Hard");
        mathHard.addQuestion(new Question("12 + 15 = ?", "27", "25", "A"));
        mathHard.addQuestion(new Question("6 x 7 = ?", "42", "48", "A"));
        mathHard.addQuestion(new Question("81 / 9 = ?", "8", "9", "B"));
        mathHard.addQuestion(new Question("15 - 8 = ?", "6", "7", "B"));
        allBanks.put("Math_Hard", mathHard);

        // English Easy
        QuestionBank englishEasy = new QuestionBank("English", "Easy");
        englishEasy.addQuestion(new Question("Opposite of 'hot'?", "Cold", "Warm", "A"));
        englishEasy.addQuestion(new Question("One mouse, two __?", "Mouses", "Mice", "B"));
        englishEasy.addQuestion(new Question("Which is correct?", "Freind", "Friend", "B"));
        englishEasy.addQuestion(new Question("Opposite of 'big'?", "Small", "Tall", "A"));
        allBanks.put("English_Easy", englishEasy);

        // English Hard
        QuestionBank englishHard = new QuestionBank("English", "Hard");
        englishHard.addQuestion(new Question("She ___ to school", "Go", "Went", "B"));
        englishHard.addQuestion(new Question("Synonym of 'happy'?", "Joyful", "Angry", "A"));
        englishHard.addQuestion(new Question("Plural of 'child'?", "Childs", "Children", "B"));
        englishHard.addQuestion(new Question("Opposite of 'ancient'?", "Modern", "Old", "A"));
        allBanks.put("English_Hard", englishHard);
    }

    public void selectBank(String subject, String difficulty) {
        String bankKey = subject + "_" + difficulty;
        QuestionBank selectedBank = allBanks.get(bankKey);

        if (selectedBank != null) {
            activeBank = selectedBank;
            currentIndex = 0;
            score = 0;
            bankFinished = false;
            Collections.shuffle(activeBank.getAllQuestions());
            System.out.println("Selected bank: " + bankKey);
        } else {
            System.err.println("Bank not found: " + bankKey);
        }
    }

    public Question getCurrentQuestion() {
        if (activeBank == null || bankFinished) {
            return null;
        }
        return activeBank.getQuestion(currentIndex);
    }

    public void nextQuestion() {
        if (activeBank == null) {
            return;
        }

        if (currentIndex < activeBank.getSize() - 1) {
            currentIndex++;
        } else {
            // all questions in bank are done
            bankFinished = true;
            System.out.println("Bank finished! Score: " + score + "/" + activeBank.getSize());
        }
    }

    public boolean checkAnswer(String answer) {
        boolean isCorrect = getCurrentQuestion().isCorrect(answer);
        if (isCorrect) {
            score++;
        }
        return isCorrect;
    }

    public void replayCurrentBank() {
        if (activeBank != null) {
            currentIndex = 0;
            score = 0;
            bankFinished = false;
            Collections.shuffle(activeBank.getAllQuestions());
        }
    }

    public boolean isBankFinished() {
        return bankFinished;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        if (activeBank == null) {
            return 0;
        }
        return activeBank.getSize();
    }

    public String getActiveSubject() {
        if (activeBank == null) {
            return null;
        }
        return activeBank.getSubject();
    }

    public String getActiveDifficulty() {
        if (activeBank == null) {
            return null;
        }
        return activeBank.getDifficulty();
    }
}
