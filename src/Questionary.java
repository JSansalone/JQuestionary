import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Calendar;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
public class Questionary extends JFrame{
	private QuestionaryListener listener;
	// Menu bar fields
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem menuItemNewQuestion;
	private JMenuItem menuItemViewQuestions;
	private JMenuItem menuItemExit;
	// -----------------------------------
	// Question area fields
	private JTextArea textAreaQuestion;
	private JPanel answersPanel;
	// -----------------------------------
	// Navigation fields
	private JButton btnNext;
	private JButton btnPrevious;
	private JButton btnDone;
	// -----------------------------------
	private Question activeQuestion;
	private ArrayList<QuestionAnswered> questionsAnswered;
	private Set<Question> alreadyShowed;
	private int questionsCounter;
	private boolean answered;
	public Questionary(){
		alreadyShowed = new TreeSet<Question>();
		questionsAnswered = new ArrayList<QuestionAnswered>();
		// Window settings
		setSize(700,800);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int heigth = screenSize.height;
		int contWidth = getWidth();
		int contHeidth = getHeight();
		setLocation((width - contWidth) / 2, (heigth - contHeidth) / 2);
		setTitle("Question�rio SCJP");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		// --------------------------------
		// Call method to create components
		initComponents();
		// --------------------------------
		nextQuestion();
	}
	public void initComponents(){
		listener = new QuestionaryListener();
		// Creating menu bar
		menuBar = new JMenuBar();
		menuFile = new JMenu("Arquivo");
		menuFile.setMnemonic('A');
		menuItemNewQuestion = new JMenuItem("Nova quest�o");
		menuItemNewQuestion.setMnemonic('N');
		menuItemNewQuestion.addActionListener(listener);
		menuItemViewQuestions = new JMenuItem("Ver quest�es");
		menuItemViewQuestions.setMnemonic('V');
		menuItemViewQuestions.addActionListener(listener);
		menuItemExit = new JMenuItem("Sair");
		menuItemExit.setMnemonic('S');
		menuItemExit.addActionListener(listener);
		menuFile.add(menuItemNewQuestion);
		menuFile.add(menuItemViewQuestions);
		menuFile.addSeparator();
		menuFile.add(menuItemExit);
		menuBar.add(menuFile);
		setJMenuBar(menuBar);
		// --------------------------------
		// Creating question text area
		textAreaQuestion = new JTextArea();
		JScrollPane scrPane = new JScrollPane(textAreaQuestion);
		scrPane.setBounds(10,10,675,270);
		textAreaQuestion.setLineWrap(true);
		textAreaQuestion.setWrapStyleWord(true);
		textAreaQuestion.setEditable(false);
		textAreaQuestion.setFont(new Font("Courier New",Font.PLAIN,14));
		answersPanel = new JPanel(null);
		answersPanel.setBounds(10,290,675,300);
		add(scrPane);
		add(answersPanel);
		// --------------------------------
		// Creating navigation buttons
		btnNext = new JButton("Pr�xima");
		btnNext.setBounds(590,710,90,25);
		btnNext.addActionListener(listener);
		btnPrevious = new JButton("Anterior");
		btnPrevious.setBounds(480,710,90,25);
		btnPrevious.addActionListener(listener);
		btnPrevious.setVisible(false);
		btnDone = new JButton("Concluir");
	//	btnDone.setBounds(370,710,90,25);
		btnDone.setBounds(480,710,90,25);
		btnDone.addActionListener(listener);
		add(btnNext);
		add(btnPrevious);
		add(btnDone);
		// --------------------------------
	}
	private void showQuestion(Question q){
		textAreaQuestion.setText(q.getQuestion());
		HashMap<String, String> answers = q.getAnswers();
		Set<String> letters = answers.keySet();
		String[] it = letters.toArray(new String[]{});
		Arrays.sort(it);
		ButtonGroup btg = new ButtonGroup();
		int xBound = -30;
		answersPanel.removeAll();
		answersPanel.setVisible(false);
		for(String s : it){
			JRadioButton rdb = new JRadioButton("<html><p>"+s+") "+answers.get(s)+"</p></html>");
			rdb.setBounds(10,(xBound+=40),655,40);
			rdb.addActionListener(listener);
			answersPanel.add(rdb);
			btg.add(rdb);
		}
		answersPanel.setVisible(true);
	}
	private void saveQuestion(){
		QuestionController controller = new QuestionController();
		Question q = null;
		boolean hasNoMore = false;
		hasNoMore = alreadyShowed.size() > controller.getQuestionsCount();
		if(!hasNoMore){
			Component[] components = answersPanel.getComponents();
			String letter = "";
			for(Component c : components){
				if(c instanceof JRadioButton && ((JRadioButton)c).isSelected()){
					letter = ((JRadioButton)c).getText().substring(9,10);
					break;
				}
			}
			QuestionAnswered qa = new QuestionAnswered(activeQuestion,letter);
			if(!questionsAnswered.contains(qa))questionsAnswered.add(qa);
		}
	}
	private void nextQuestion(){
		QuestionController controller = new QuestionController();
		Question q = null;
		boolean hasNoMore = false;
		answered = false;
		do{
			hasNoMore = alreadyShowed.size() >= controller.getQuestionsCount();
			if(!hasNoMore){
				q = controller.randomQuestion();
			}
		}while(!hasNoMore && alreadyShowed.contains(q));
		if(hasNoMore){
			JOptionPane.showMessageDialog(this,"N�o h� mais quest�es!","Aviso",JOptionPane.WARNING_MESSAGE);
		}else{
			showQuestion(q);
			alreadyShowed.add(q);
			questionsCounter++;
			activeQuestion = q;
		}
	}
	private void previousQuestion(){}
	private class QuestionaryListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			if(ae.getSource() == menuItemNewQuestion){
				new QuestionWizard(true).setVisible(true);
			}else if(ae.getSource() == menuItemViewQuestions){
				QuestionViewer qw = new QuestionViewer(new QuestionWizard(false));
				QuestionController controller = new QuestionController();
				qw.loadQuestions(controller.getQuestions());
				qw.setVisible(true);
			}else if(ae.getSource() == menuItemExit){
				System.exit(0);
			}else if(ae.getSource() == btnNext){
				if(answered){
					Questionary.this.saveQuestion();
					Questionary.this.nextQuestion();
				}else
					JOptionPane.showMessageDialog(Questionary.this,"Responda a pergunta!","Aviso",JOptionPane.WARNING_MESSAGE);
			}else if(ae.getSource() == btnPrevious){
			}else if(ae.getSource() == btnDone){
				if(answered){
					saveQuestion();
					int total = questionsAnswered.size();
					int correct = 0;
					int wrong = 0;
					Question q = null;
					String answered = "";
					for(QuestionAnswered qa : questionsAnswered){
						q = qa.getQuestion();
						answered = qa.getAnswer();
						if(q.getCorrectAnswer().equals(answered))
							correct++;
						else
							wrong++;
					}
					QuestionaryUtil.saveAnswers(questionsAnswered);
					QuestionaryResult qr = new QuestionaryResult();
					qr.setInformation(total,correct,wrong);
					qr.setVisible(true);
				}else
					JOptionPane.showMessageDialog(Questionary.this,"Responda a pergunta!","Aviso",JOptionPane.WARNING_MESSAGE);
			}else if(ae.getSource() instanceof JRadioButton){
				answered = true;
			}
		}
	}
	private static class QuestionaryUtil{
		public static void saveAnswers(ArrayList<QuestionAnswered> aqa){
			File root = new File("answers");
			if(!(root.exists() && root.mkdir()))
				root.mkdir();
			Calendar c = Calendar.getInstance();
			String d = ""+(c.get(Calendar.DAY_OF_MONTH) < 10 ? "0"+c.get(Calendar.DAY_OF_MONTH) : c.get(Calendar.DAY_OF_MONTH));
			String m = ""+(c.get(Calendar.MONTH)+1 < 10 ? "0"+(c.get(Calendar.MONTH)+1) : c.get(Calendar.MONTH)+1);
			String y = ""+(c.get(Calendar.YEAR));
			int count = 0;
			String fileName = "";
			File file = null;
			do{
				fileName = "answers"+y+m+d+"_"+(++count)+".txt";
				file = new File(root,fileName);
			}while(file.exists());
			try{
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				Question q = null;
				String answer = null;
				String correctAnswer = null;
				boolean correct = false;
				for(QuestionAnswered qa : aqa){
					q = qa.getQuestion();
					answer = qa.getAnswer();
					correctAnswer = q.getCorrectAnswer();
					correct = answer.equals(correctAnswer);
					bw.write(correct ? "[correta]" : "[incorreta]");
					bw.newLine();
					bw.write(q.getCode()+"- "+q.getQuestion());
					bw.newLine();
					bw.write("Resposta:"+q.getAnswers().get(answer));
					bw.newLine();
					if(!correct) bw.write("Resposta correta:"+q.getAnswers().get(correctAnswer));
					bw.newLine();
					bw.newLine();
					bw.newLine();
				}
				bw.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public static void main(String... args){
		new Questionary().setVisible(true);
	}
}