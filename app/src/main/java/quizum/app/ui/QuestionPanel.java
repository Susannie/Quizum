package quizum.app.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import quizum.beans.Question;

public class QuestionPanel extends JPanel {
	public Question question;
	private List<JRadioButton> buttonList;
	
	public QuestionPanel(Question question) {
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 534 };
		gbl_panel.rowHeights = new int[] {100, 10};
		gbl_panel.columnWeights = new double[] { 1.0 };
		gbl_panel.rowWeights = new double[] { 1.0, 1.0 };
		setLayout(gbl_panel);

		this.question = question;
		init(question);
		
		repaint();
	}

	public void init(Question question) {
		WrappedLabel lblNewLabel = new WrappedLabel(question.getContent());
		lblNewLabel.setAlignmentX(RIGHT_ALIGNMENT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(20, 20, 20, 20);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		JPanel buttonPanel = new JPanel();
		GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
		gbc_buttonPanel.anchor = GridBagConstraints.SOUTH;
		gbc_buttonPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonPanel.gridx = 0;
		gbc_buttonPanel.gridy = 1;
		add(buttonPanel, gbc_buttonPanel);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		ButtonGroup radioButtonGroup = new ButtonGroup();

		buttonList = new ArrayList<JRadioButton>();
		
		question.shuffleAnswers();
		question.getAnswers().forEach(answer -> {
			WrappedRadioButtonPanel wbPanel = new WrappedRadioButtonPanel(answer[1]);
			wbPanel.setBorder(BorderFactory.createEmptyBorder(0,5,5,0));
			buttonPanel.add(wbPanel);
			
			JRadioButton button = wbPanel.getButton();
			button.setName(answer[0]);			
			radioButtonGroup.add(button);
			buttonList.add(button);
		});
	}
	
	public Integer getAnswer(){		
		JRadioButton chosenButton = buttonList.stream().filter(button -> button.isSelected()).findAny().orElse(null);
		return chosenButton!=null ? Integer.valueOf(chosenButton.getName()) : null;
	}
}
