import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
public class QuestionWizard extends JDialog implements Loadable{
	private JTextArea areaQuestion;
	private JList<String> answersList;
	private JComboBox<String> cmbLetter;
	private JTextField txtAnswer;
	private JComboBox<String> cmbCorrectAnswer;
	private JButton btnDone;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnRemoveQuestion;
	private boolean mode;
	private QuestionWizardListener listener;
	private Question activeQuestion;
	public QuestionWizard(boolean modeNew){
		this.mode = modeNew;
		setSize(600,400);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int heigth = screenSize.height;
		int contWidth = getWidth();
		int contHeidth = getHeight();
		setLocation((width - contWidth) / 2, (heigth - contHeidth) / 2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setLayout(null);
		initComponents();
		if(mode){
			setTitle("Assistente para criação de questões");
		}else{
			setTitle("Assistente para edição de questões");
		}
	}
	private void initComponents(){
		listener = new QuestionWizardListener();
		JLabel lblQuestion = new JLabel("Texto da questão");
		lblQuestion.setBounds(10,10,580,20);
		add(lblQuestion);
		areaQuestion = new JTextArea();
		JScrollPane scr = new JScrollPane(areaQuestion);
		scr.setBounds(10,30,575,70);
		areaQuestion.setLineWrap(true);
		areaQuestion.setWrapStyleWord(true);
		add(scr);
		JLabel lblAnswer = new JLabel("Alternativas");
		lblAnswer.setBounds(10,130,150,20);
		add(lblAnswer);
		cmbLetter = new JComboBox<String>();
		cmbLetter.setModel(new DefaultComboBoxModel<String>(new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"}));
		cmbLetter.setBounds(10,150,50,25);
		add(cmbLetter);
		txtAnswer = new JTextField();
		txtAnswer.setBounds(70,150,515,25);
		add(txtAnswer);
		btnAdd = new JButton("+");
		btnAdd.setBounds(535,120,50,25);
		btnAdd.addActionListener(listener);
		add(btnAdd);
		btnRemove = new JButton("-");
		btnRemove.setBounds(475,120,50,25);
		btnRemove.addActionListener(listener);
		add(btnRemove);
		answersList = new JList<String>();
		answersList.setModel(new DefaultListModel<String>());
		JScrollPane scr2 = new JScrollPane(answersList);
		scr2.setBounds(10,180,575,70);
		add(scr2);
		JLabel lblCorrect = new JLabel("Alternativa correta");
		lblCorrect.setBounds(10,250,150,20);
		add(lblCorrect);
		cmbCorrectAnswer = new JComboBox<String>();
		cmbCorrectAnswer.setBounds(10,270,50,25);
		add(cmbCorrectAnswer);
		btnDone = new JButton();
		if(this.mode)
			btnDone.setText("Criar");
		else
			btnDone.setText("Atualizar");
		btnDone.setBounds(485,340,100,25);
		btnDone.addActionListener(listener);
		add(btnDone);
		btnRemoveQuestion = new JButton("Remover");
		btnRemoveQuestion.setBounds(375,340,100,25);
		btnRemoveQuestion.setVisible(!this.mode);
		btnRemoveQuestion.addActionListener(listener);
		add(btnRemoveQuestion);
	}
	@Override
	public void loadQuestion(Question q){
		activeQuestion = q;
		areaQuestion.setText(q.getQuestion());
		HashMap<String, String> answers = q.getAnswers();
		Set<String> letters = answers.keySet();
		Iterator<String> iterator = letters.iterator();
		String letter = "";
		((DefaultListModel<String>)answersList.getModel()).clear();
		cmbLetter.setModel(new DefaultComboBoxModel<String>(new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"}));
		cmbCorrectAnswer.removeAllItems();
		while(iterator.hasNext()){
			letter = iterator.next();
			addAnswer(letter, answers.get(letter));
		}
		cmbCorrectAnswer.setSelectedItem(q.getCorrectAnswer());
		setVisible(true);
	}
	private void addAnswer(){
		String letter = (String)cmbLetter.getSelectedItem();
		cmbLetter.removeItem(letter);
		((DefaultListModel<String>)answersList.getModel()).addElement(letter+") "+txtAnswer.getText());
		cmbCorrectAnswer.addItem(letter);
		sort(cmbCorrectAnswer);
		sort(answersList);
	}
	private void addAnswer(String letter, String answer){
		cmbLetter.removeItem(letter);
		((DefaultListModel<String>)answersList.getModel()).addElement(letter+") "+answer);
		cmbCorrectAnswer.addItem(letter);
		sort(cmbCorrectAnswer);
		sort(answersList);
	}
	private void sort(JComboBox<String> cmb){
		int total = cmb.getItemCount();
		String[] str = new String[total];
		for(int i=0; i<total; i++)
			str[i] = cmb.getItemAt(i);
		Arrays.sort(str);
		cmb.removeAllItems();
		for(String s : str)
			cmb.addItem(s);
	}
	private void sort(JList<String> l){
		int total = ((DefaultListModel<String>)l.getModel()).size();
		String[] str = new String[total];
		for(int i=0; i<total; i++)
			str[i] = ((DefaultListModel<String>)l.getModel()).getElementAt(i);
		Arrays.sort(str);
		((DefaultListModel<String>)l.getModel()).removeAllElements();
		for(String s : str)
			((DefaultListModel<String>)l.getModel()).addElement(s);
	}
	private class QuestionWizardListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ae){
			if(ae.getSource() == btnDone){
				if(((DefaultListModel<String>)answersList.getModel()).size() == 0){
					JOptionPane.showMessageDialog(QuestionWizard.this, "Adicione alternativas à questão!", "Aviso", JOptionPane.WARNING_MESSAGE);
				}else if(areaQuestion.getText().trim().equals("")){
					JOptionPane.showMessageDialog(QuestionWizard.this, "Digite o enunciado da questão!", "Aviso", JOptionPane.WARNING_MESSAGE);
				}else{
					QuestionController controller = new QuestionController();
					Question question = new Question();
					if(!mode){
						question = activeQuestion;
						question.clearAnswers();
					}
					question.setQuestion(areaQuestion.getText());
					int total = ((DefaultListModel<String>)answersList.getModel()).size();
					String aux = "";
					String answer = "";
					String letter = "";
					for(int i=0; i<total; i++){
						aux = ((DefaultListModel<String>)answersList.getModel()).getElementAt(i);
						answer = aux.substring(3,aux.length());
						letter = aux.substring(0,1);
						question.setAnswer(letter,answer);
					}
					question.setCorrectAnswer((String)cmbCorrectAnswer.getSelectedItem());
					if(mode ? controller.saveQuestion(question) : controller.updateQuestion(question)){
						JOptionPane.showMessageDialog(QuestionWizard.this, mode ? "Questão salva!" : "Questão atualizada!", "Concluído", JOptionPane.INFORMATION_MESSAGE);
						controller.update();
					}else
						JOptionPane.showMessageDialog(QuestionWizard.this, mode ? "Erro ao salvar a questão!" : "Erro ao atualizar a questão!", "Erro", JOptionPane.ERROR_MESSAGE);
				}
			}else if(ae.getSource() == btnRemoveQuestion){
				QuestionController controller = new QuestionController();
				if(controller.deleteQuestion(activeQuestion.getCode())){
					JOptionPane.showMessageDialog(QuestionWizard.this, "Questão removida!", "Concluído", JOptionPane.INFORMATION_MESSAGE);
					controller.update();
				}else
					JOptionPane.showMessageDialog(QuestionWizard.this, "Erro ao remover a questão!", "Erro", JOptionPane.ERROR_MESSAGE);
				activeQuestion = null;
			}else if(ae.getSource() == btnAdd){
				if(txtAnswer.getText().trim().equals("")){	
					JOptionPane.showMessageDialog(QuestionWizard.this, "Digite o texto da alternativa!", "Aviso", JOptionPane.WARNING_MESSAGE);
				}else{
					QuestionWizard.this.addAnswer();
				}
			}else if(ae.getSource() == btnRemove){
				if(answersList.getSelectedIndex() == -1){
					JOptionPane.showMessageDialog(QuestionWizard.this, "Selecione uma alternativa!", "Aviso", JOptionPane.WARNING_MESSAGE);
				}else{
					String letter = ((DefaultListModel<String>)answersList.getModel()).getElementAt(answersList.getSelectedIndex());
					letter = letter.substring(0,1);
					cmbCorrectAnswer.removeItem(letter);
					((DefaultListModel<String>)answersList.getModel()).removeElementAt(answersList.getSelectedIndex());
					cmbLetter.addItem(letter);
					QuestionWizard.this.sort(cmbLetter);
				}
			}
		}
	}
}