package quizum.app.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import quizum.beans.Question;

public class QuestionPanel extends JLayeredPane {
	public Question question;
	private List<JRadioButton> buttonList;
	
	public QuestionPanel(Question question) {

		setMinimumSize(new Dimension(600, 800));
		setAutoscrolls(true);

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 534 };
		gbl_panel.rowHeights = new int[] { 300, 100, 0 };
		gbl_panel.columnWeights = new double[] { 1.0 };
		gbl_panel.rowWeights = new double[] { 1.0, 0.0, 0.0 };
		setLayout(gbl_panel);

		this.question = question;
		init(question);
	}

	public void init(Question question) {
		JLabel lblNewLabel = new JLabel(question.getContent());
		lblNewLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblNewLabel.insets = new Insets(20, 20, 20, 20);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setMinimumSize(new Dimension(10, 100));
		GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
		gbc_buttonPanel.insets = new Insets(20, 20, 20, 20);
		gbc_buttonPanel.fill = GridBagConstraints.BOTH;
		gbc_buttonPanel.gridx = 0;
		gbc_buttonPanel.gridy = 1;
		add(buttonPanel, gbc_buttonPanel);
		buttonPanel.setLayout(new GridLayout(0, 1, 0, 0));

		ButtonGroup radioButtonGroup = new ButtonGroup();

		buttonList = new ArrayList<JRadioButton>();
		
		question.shuffleAnswers();
		question.getAnswers().forEach(answer -> {
			JRadioButton button = new JRadioButton(answer[1]);
			button.setName(answer[0]);
			buttonPanel.add(button);
			radioButtonGroup.add(button);
			buttonList.add(button);
		});

		setVisible(true);
		repaint();
	}
	
	public Integer getAnswer(){		
		JRadioButton chosenButton = buttonList.stream().filter(button -> button.isSelected()).findAny().orElse(null);
		return chosenButton!=null ? Integer.valueOf(chosenButton.getName()) : null;
	}
}
