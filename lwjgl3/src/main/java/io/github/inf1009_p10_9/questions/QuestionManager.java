package io.github.inf1009_p10_9.questions;

import java.util.Collections;
import java.util.HashMap;
import io.github.inf1009_p10_9.interfaces.IManager;

// singleton that manages all question banks and tracks progress through the active one
public class QuestionManager implements IManager {
    private static QuestionManager instance;

    // all loaded banks keyed by "Subject_Difficulty", e.g. "Math_Easy"
    private HashMap<String, QuestionBank> allBanks;

    // state for the current session
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

    // sets up the bank map and loads all questions
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

    // resets all state, called on shutdown or full restart
    @Override
    public void clear() {
        allBanks = null;
        activeBank = null;
        currentIndex = 0;
        score = 0;
        bankFinished = false;
    }

    // populates allBanks with hardcoded questions for each subject and difficulty
    private void loadAllBanks() {
        // math easy
        QuestionBank mathEasy = new QuestionBank("Math", "Easy");
        mathEasy.addQuestion(new Question("1 + 1 = ?", "2", "3", "A"));
        mathEasy.addQuestion(new Question("2 + 2 = ?", "5", "4", "B"));
        mathEasy.addQuestion(new Question("3 + 1 = ?", "4", "5", "A"));
        mathEasy.addQuestion(new Question("5 - 2 = ?", "2", "3", "B"));
        allBanks.put("Math_Easy", mathEasy);

        // math hard
        QuestionBank mathHard = new QuestionBank("Math", "Hard");
        mathHard.addQuestion(new Question("12 + 15 = ?", "27", "25", "A"));
        mathHard.addQuestion(new Question("6 x 7 = ?", "42", "48", "A"));
        mathHard.addQuestion(new Question("81 / 9 = ?", "8", "9", "B"));
        mathHard.addQuestion(new Question("15 - 8 = ?", "6", "7", "B"));
        allBanks.put("Math_Hard", mathHard);

        // english easy
        QuestionBank englishEasy = new QuestionBank("English", "Easy");
        englishEasy.addQuestion(new Question("Opposite of 'hot'?", "Cold", "Warm", "A"));
        englishEasy.addQuestion(new Question("One mouse, two __?", "Mouses", "Mice", "B"));
        englishEasy.addQuestion(new Question("Which is correct?", "Freind", "Friend", "B"));
        englishEasy.addQuestion(new Question("Opposite of 'big'?", "Small", "Tall", "A"));
        allBanks.put("English_Easy", englishEasy);

        // english hard
        QuestionBank englishHard = new QuestionBank("English", "Hard");
        englishHard.addQuestion(new Question("She ___ to school", "Go", "Went", "B"));
        englishHard.addQuestion(new Question("Synonym of 'happy'?", "Joyful", "Angry", "A"));
        englishHard.addQuestion(new Question("Plural of 'child'?", "Childs", "Children", "B"));
        englishHard.addQuestion(new Question("Opposite of 'ancient'?", "Modern", "Old", "A"));
        allBanks.put("English_Hard", englishHard);
    }

    // sets the active bank by subject and difficulty, resets progress, and shuffles the questions
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

    // returns the current question, or null if there is no active bank or the bank is finished
    public Question getCurrentQuestion() {
        if (activeBank == null || bankFinished) {
            return null;
        }
        return activeBank.getQuestion(currentIndex);
    }

    // advances to the next question, or marks the bank as finished if we are on the last one
    public void nextQuestion() {
        if (activeBank == null) {
            return;
        }

        if (currentIndex < activeBank.getSize() - 1) {
            currentIndex++;
        } else {
            bankFinished = true;
            System.out.println("Bank finished! Score: " + score + "/" + activeBank.getSize());
        }
    }

    // checks the given answer against the current question and increments score if correct
    public boolean checkAnswer(String answer) {
        boolean isCorrect = getCurrentQuestion().isCorrect(answer);
        if (isCorrect) {
            score++;
        }
        return isCorrect;
    }

    // resets progress on the current bank so the player can try it again
    public void replayCurrentBank() {
        if (activeBank != null) {
            currentIndex = 0;
            score = 0;
            bankFinished = false;
            Collections.shuffle(activeBank.getAllQuestions());
        }
    }

    // getters
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
