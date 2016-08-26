package quizum.beans;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {
	final private String username;
	private Map<Question, Integer> questionsResolved;

	public UserInfo(String username) {
		this.username = username;		
		questionsResolved = new HashMap<Question, Integer>();
	}
	
	public void resolveQuestion(Question question, Integer answer){
		questionsResolved.put(question, answer);
	}
	
	public String getUsername(){
		return username;
	}
	
	public int getTotal(){
		return (int) questionsResolved.entrySet().parallelStream().filter(entry -> entry!=null && entry.getValue()!=null && entry.getValue().equals(0)).count();
	}
	
	public Map<Question, Integer> getAnsweredQuestions(){
		return questionsResolved;
	}
}
