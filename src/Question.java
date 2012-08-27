import java.io.Serializable;
import java.util.HashMap;
public class Question implements Serializable{
	private Integer code;
	private String question;
	private HashMap<String,String> answers;
	private String correctAnswer;
	public Question(){
		answers = new HashMap<String, String>();
	}
	public void setQuestion(String text){
		this.question = text;
	}
	public void setAnswer(String letter, String answer){
		answers.put(letter, answer);
	}
	public void clearAnswers(){
		answers.clear();
	}
	public void setCorrectAnswer(String letter){
		correctAnswer = letter;
	}
	public void setCode(int code){
		this.code = code;
	}
	public int getCode(){
		return this.code;
	}
	public String getQuestion(){
		return this.question;
	}
	public String getCorrectAnswer(){
		return this.correctAnswer;
	}
	public HashMap<String, String> getAnswers(){
		return this.answers;
	}
}