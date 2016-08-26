package quizum.app.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import quizum.QuizumUtils;
import quizum.beans.Question;
import quizum.beans.UserInfo;

public class QuizFrame extends JFrame{
	private List<Question> questionList;
	private JLabel imageLabel;
	private UserInfo userInfo;
	private int iterator;
	private List<QuestionPanel> questionPanelList;

	public QuizFrame(String fileName, UserInfo userInfo) {
		if(fileName == null || fileName.isEmpty()){
			JOptionPane.showMessageDialog(null, "Nie można odczytać konfiguracji", "Błąd konfiguracji", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		questionList=QuizumUtils.loadQuestionList(new File(fileName));
		this.userInfo = userInfo;
		initialize();
	}

	private void initialize() {
		setTitle("Quizum");
		setBounds(100, 100, 1200, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		iterator = -1;
		
		JPanel imagePanel = new JPanel();
		imagePanel.setAutoscrolls(true);
		
		
		imageLabel = new JLabel();		
		imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		imagePanel.add(imageLabel);

		questionPanelList = new ArrayList<QuestionPanel>();		
		questionList.forEach(question -> questionPanelList.add(new QuestionPanel(question)));
		Collections.shuffle(questionPanelList);
		
		changePanel(getNextQuestionPanel());
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Poprzednie");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				QuestionPanel panel = getPrevQuestionPanel();
				
				if(panel != null){
					changePanel(panel);
				}
			}
		});
		panel_1.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Zakończ");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				questionPanelList.forEach(questionPanel -> {
					userInfo.resolveQuestion(questionPanel.question, questionPanel.getAnswer()!=null ? questionPanel.getAnswer() : null);
				});

				QuizumUtils.generateReport(userInfo);
				JOptionPane.showMessageDialog(null, "Poprawnych odpowiedzi: "+userInfo.getTotal()+" z "+questionPanelList.size()+" ("+(int)((double)userInfo.getTotal()/questionPanelList.size()*100)+"%)", "Wynik", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		});
		panel_1.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Następne");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				QuestionPanel panel = getNextQuestionPanel();
				
				if(panel != null){
					changePanel(panel);
				}
			}
		});
		panel_1.add(btnNewButton_2);
		
		getContentPane().add(imagePanel, BorderLayout.EAST);
		setVisible(true);
	}

	public void changePanel(QuestionPanel panel) {
		if (panel == null)
			return;
		if (getContentPane().getComponents().length > 0)
			getContentPane().remove(0);
		getContentPane().add(panel, 0);
		repaint();
		revalidate();
		
		if(panel.question.getPictureFileName()!=null) {
			setQuestionImage(QuizumUtils.getImageByFilename(panel.question.getPictureFileName()));
		}else{
			setQuestionImage(null);
		}
	}
	
	public QuestionPanel getNextQuestionPanel(){
		if(questionPanelList.size()-1 > iterator){
			iterator++;
			return questionPanelList.get(iterator);
		}
		return null;
	}
	
	public QuestionPanel getPrevQuestionPanel(){
		if(iterator > 0){
			iterator--;
			return questionPanelList.get(iterator);
		}
		return null;
	}
	
	private void setQuestionImage(ImageIcon icon){
		if(icon!=null){
			icon = QuizumUtils.fitIcon(icon, 600, getHeight());		
		}
		imageLabel.setIcon(icon);
		imageLabel.repaint();
	}	

}
