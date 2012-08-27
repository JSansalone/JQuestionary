import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
public class QuestionViewer extends JDialog{
	private JList<String> questionsList;
	private Loadable loadable;
	private HashMap<Integer, Question> questions;
	private QuestionViewerListener listener;
	public QuestionViewer(Loadable loadable){
		this.loadable = loadable;
		this.listener = new QuestionViewerListener();
		setTitle("Visualizador de questões");
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
	}
	private void initComponents(){
		JLabel lblQuestions = new JLabel("Questões");
		lblQuestions.setBounds(10,10,150,20);
		add(lblQuestions);
		questionsList = new JList<String>();
		questionsList.setModel(new DefaultListModel<String>());
		questionsList.addMouseListener(this.listener);
		JScrollPane scr = new JScrollPane(questionsList);
		scr.setBounds(10,30,575,335);
		add(scr);
	}
	public void loadQuestions(HashMap<Integer, Question> questions){
		this.questions = questions;
		Set<Integer> codes = questions.keySet();
		Iterator<Integer> iterator = codes.iterator();
		Question q = null;
		while(iterator.hasNext()){
			q = questions.get(iterator.next());
			((DefaultListModel<String>)questionsList.getModel()).addElement(q.getCode()+" - "+q.getQuestion());
		}
	}
	private class QuestionViewerListener implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent me){
			if(me.getSource() == questionsList){
				if(me.getClickCount() == 2){
					int questionCode = Integer.parseInt(((DefaultListModel<String>)questionsList.getModel()).getElementAt(questionsList.getSelectedIndex()).substring(0,1));
					Question q = questions.get(questionCode);
					QuestionViewer.this.loadable.loadQuestion(q);
				}
			}
		}
		@Override
		public void mouseEntered(MouseEvent me){
		}
		@Override
		public void mouseExited(MouseEvent me){
		}
		@Override
		public void mousePressed(MouseEvent me){
		}
		@Override
		public void mouseReleased(MouseEvent me){
		}
	}
}