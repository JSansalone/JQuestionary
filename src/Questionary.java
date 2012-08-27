import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
	public Questionary(){
		// Window settings
		setSize(700,500);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int heigth = screenSize.height;
		int contWidth = getWidth();
		int contHeidth = getHeight();
		setLocation((width - contWidth) / 2, (heigth - contHeidth) / 2);
		setTitle("Questionário SCJP");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		// --------------------------------
		// Call method to create components
		initComponents();
		// --------------------------------
	}
	public void initComponents(){
		listener = new QuestionaryListener();
		// Creating menu bar
		menuBar = new JMenuBar();
		menuFile = new JMenu("Arquivo");
		menuFile.setMnemonic('A');
		menuItemNewQuestion = new JMenuItem("Nova questão");
		menuItemNewQuestion.setMnemonic('N');
		menuItemNewQuestion.addActionListener(listener);
		menuItemViewQuestions = new JMenuItem("Ver questões");
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
		textAreaQuestion.setBounds(10,10,675,70);
		textAreaQuestion.setLineWrap(true);
		textAreaQuestion.setWrapStyleWord(true);
		textAreaQuestion.setEditable(false);
		add(textAreaQuestion);
		// --------------------------------
		// Creating navigation buttons
		btnNext = new JButton("Próxima");
		btnNext.setBounds(590,410,90,25);
		btnNext.addActionListener(listener);
		btnPrevious = new JButton("Anterior");
		btnPrevious.setBounds(480,410,90,25);
		btnPrevious.addActionListener(listener);
		btnDone = new JButton("Concluir");
		btnDone.setBounds(370,410,90,25);
		btnDone.addActionListener(listener);
		add(btnNext);
		add(btnPrevious);
		add(btnDone);
		// --------------------------------
	}
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
			}else if(ae.getSource() == btnPrevious){
			}else if(ae.getSource() == btnDone){
			}
		}
	}
	public static void main(String... args){
		new Questionary().setVisible(true);
	}
}