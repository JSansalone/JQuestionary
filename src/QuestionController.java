import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Random;
import java.util.Calendar;
import java.util.ArrayList;
public class QuestionController{
	private File root;
	private static ArrayList<Integer> randomList;
	private static int randomCount;
	static{
		updateList();
	}
	private static void updateList(){
		randomList = new ArrayList<Integer>();
		QuestionController qc = new QuestionController();
		Calendar c = Calendar.getInstance();
		Random r = new Random(c.get(Calendar.HOUR)+c.get(Calendar.MINUTE)+c.get(Calendar.SECOND));
		int maximum = qc.getQuestionsCount();
		while(true){
			int n = r.nextInt(maximum+1);
			if(!randomList.contains(n) && n!=0){
				randomList.add(n);
			}
			if(randomList.size()==maximum)break;
		}
	}
	public void update(){
		updateList();
	}
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
	public Question randomQuestion(){
		boolean found = false;
		Question q = null;
		while(!found){
			q = getQuestion(randomList.get(randomCount++));
			if(q != null)
				found = true;
		}
		if(randomCount == randomList.size())
			randomCount = 0;
		if(found)
			return q;
		else
			return null;
	}
	public Question getQuestion(int code){
		if(!(root.exists() && root.isDirectory()))
			return null;
		Question q = null;
		try{
			ObjectInputStream os = new ObjectInputStream(new FileInputStream(new File(root,"q"+code+".ser")));
			q = (Question)os.readObject();
			os.close();
			return q;
		}catch(Exception ex){
			return null;
		}
	}
	public int getQuestionsCount(){
		if(!(root.exists() && root.isDirectory())){
			System.out.println("root não existe");
			return 0;
		}
		File[] files = root.listFiles();
		Question q = null;
		int count = 0;
		for(File f : files){
			try{
				ObjectInputStream os = new ObjectInputStream(new FileInputStream(f));
				q = (Question)os.readObject();
				os.close();
				count++;
			}catch(Exception ex){
			}
		}
		return count;
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