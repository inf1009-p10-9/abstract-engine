package io.github.inf1009_p10_9.game.questions;

import java.util.Collections;
import java.util.HashMap;
import io.github.inf1009_p10_9.engine.interfaces.IManagerMinimal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

// singleton that manages all question banks and tracks progress through the active one
public class QuestionManager implements IManagerMinimal {
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
        try {
            String jsonText = Gdx.files.internal("questions/questions.json").readString();
            JsonReader jsonReader = new JsonReader();
            JsonValue root = jsonReader.parse(jsonText);

            for (JsonValue bankData : root.get("banks")) {
                String subject    = bankData.getString("subject");
                String difficulty = bankData.getString("difficulty");
                QuestionBank bank = new QuestionBank(subject, difficulty);

                for (JsonValue q : bankData.get("questions")) {
                    String text    = q.getString("text");
                    String optionA = q.getString("optionA");
                    String optionB = q.getString("optionB");
                    String answer  = q.getString("answer");
                    bank.addQuestion(new Question(text, optionA, optionB, answer));
                }

                
                allBanks.put(subject + "_" + difficulty, bank);
            }

            System.out.println("Questions loaded from file");

        } catch (Exception e) {
            System.err.println("Failed to load questions.json: " + e.getMessage());
        }
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
