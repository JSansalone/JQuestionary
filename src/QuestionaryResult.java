import javax.swing.JDialog;
import javax.swing.JLabel;
import java.text.NumberFormat;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Dimension;
public class QuestionaryResult extends JDialog{
	private int totalQuestions;
	private int correctQuestions;
	private int wrongQuestions;
	private JLabel lblTotal;
	private JLabel lblCorrect;
	private JLabel lblWrong;
	private JLabel lblScore;
	private NumberFormat nf;
	public QuestionaryResult(){
		setSize(400,200);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int heigth = screenSize.height;
		int contWidth = getWidth();
		int contHeidth = getHeight();
		setLocation((width - contWidth) / 2, (heigth - contHeidth) / 2);
		setTitle("Resultado");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setLayout(null);
		initComponents();
	}
	private void initComponents(){
		lblTotal = new JLabel();
		lblTotal.setBounds(120,40,200,20);
		add(lblTotal);
		lblCorrect = new JLabel();
		lblCorrect.setBounds(120,60,200,20);
		add(lblCorrect);
		lblWrong = new JLabel();
		lblWrong.setBounds(120,80,200,20);
		add(lblWrong);
		lblScore = new JLabel();
		lblScore.setBounds(120,100,200,20);
		add(lblScore);
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
	}
	public void setInformation(int totalQuestions, int correct, int wrong){
		lblTotal.setText(totalQuestions+" questões respondidas");
		lblCorrect.setText("<html><font color = blue>"+correct+" acertos</font></html>");
		lblWrong.setText("<html><font color = red>"+wrong+" erros</font></html>");
		lblScore.setText((nf.format(((double)correct/totalQuestions)*100))+"% de acerto");
	}
}