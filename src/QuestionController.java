import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
public class QuestionController{
	private File root;
	public QuestionController(){
		root = new File("questions");
		if(!(root.exists() && root.isDirectory()))
			root.mkdir();
	}
	private boolean save(Question q, boolean isNew){
		try{
			if(isNew)
				q.setCode(getNextQuestionNumber());
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(root,"q"+q.getCode()+".ser")));
			os.writeObject(q);
			os.close();
			return true;
		}catch(IOException e){
			return false;
		}
	}
	public boolean saveQuestion(Question q){
		if(!(root.exists() && root.isDirectory()))
			root.mkdir();
		return save(q,true);
	}
	public boolean updateQuestion(Question q){
		if(!(root.exists() && root.isDirectory()))
			root.mkdir();
		return save(q,false);
	}
	public boolean deleteQuestion(int number){
		if(!(root.exists() && root.isDirectory()))
			return false;
		File f = new File(root,"q"+number+".ser");
		if(f.exists())
			return f.delete();
		return false;
	}
	public Question loadQuestion(int number){
		if(!(root.exists() && root.isDirectory()))
			return null;
		try{
			ObjectInputStream os = new ObjectInputStream(new FileInputStream("q"+number+".ser"));
			Question q = (Question)os.readObject();
			os.close();
			return q;
		}catch(Exception e){
			return null;
		}
	}
	public HashMap<Integer, Question> getQuestions(){
		if(!(root.exists() && root.isDirectory()))
			return new HashMap<Integer, Question>();
		File[] files = root.listFiles();
		Question q = null;
		HashMap<Integer, Question> questions = new HashMap<Integer, Question>();
		for(File f : files){
			try{
				ObjectInputStream os = new ObjectInputStream(new FileInputStream(f));
				q = (Question)os.readObject();
				os.close();
				questions.put(q.getCode(),q);
			}catch(Exception ex){
			}
		}
		return questions;
	}
	public final int getNextQuestionNumber(){
		HashMap<Integer, Question> questions = getQuestions();
		if(questions.size() == 0){
			return 1;
		}else{
			Set<Integer> codes = questions.keySet();
			Iterator<Integer> iterator = codes.iterator();
			int maximum = 0;
			int aux = 0;
			while(iterator.hasNext()){
				aux = iterator.next();
				if(aux > maximum)
					maximum = aux;
			}
			return maximum + 1;
		}
	}
}