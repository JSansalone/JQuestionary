public class QuestionAnswered{
	private int order;
	private Question question;
	private String answer;
	public QuestionAnswered(int order, Question question, String answer){
		this.question = question;
		this.answer = answer;
	}
	public int getOrder(){
		return this.order;
	}
	public Question getQuestion(){
		return this.question;
	}
	public String getAnswer(){
		return this.answer;
	}
}