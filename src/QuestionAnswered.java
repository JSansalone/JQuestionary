public class QuestionAnswered{
	private Question question;
	private String answer;
	public QuestionAnswered(Question question, String answer){
		this.question = question;
		this.answer = answer;
	}
	public Question getQuestion(){
		return this.question;
	}
	public String getAnswer(){
		return this.answer;
	}
	public String toString(){
		return this.question.getCode()+":"+this.answer;
	}
	public int hashCode(){
		return this.question.getCode();
	}
	public boolean equals(Object o){
		return this.question.getCode() == ((QuestionAnswered)o).getQuestion().getCode();
	}
}