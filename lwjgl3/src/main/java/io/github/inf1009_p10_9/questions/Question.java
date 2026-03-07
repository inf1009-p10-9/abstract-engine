package io.github.inf1009_p10_9.questions;

public class Question {
	private String question;
	private String optionA;
	private String optionB;
	private String correctAnswer;
	
	public Question(String q, String optA, String optB, String c) {
		this.question = q;
		this.optionA = optA;
		this.optionB = optB;
		this.correctAnswer = c;
	}
	
	public boolean isCorrect(String answer) {
        return correctAnswer.equalsIgnoreCase(answer);
    }
	
	public String getQuestion() {
		return question;
	}
	
	public String getOptionA() {
		return optionA;
	}
	
	public String getOptionB() {
		return optionB;
	}
	
	public String getCorrectAnswer() {
        return correctAnswer;
    }
	
	public void setQuestion(String q) {
		this.question = q;
	}
	
	public void setOptionA(String a) {
		this.optionA = a;
	}
	
	public void setOptionB(String b) {
		this.optionB = b;
	}
	
	public void setCorrectAnswer(String correct) {
        this.correctAnswer = correct;
    }
	
}
