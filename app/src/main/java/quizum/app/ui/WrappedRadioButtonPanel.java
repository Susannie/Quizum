package quizum.app.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class WrappedRadioButtonPanel extends JPanel{
	private JRadioButton rButton;

	public WrappedRadioButtonPanel(String content) {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 0.0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0};
		setLayout(gridBagLayout);
		
		createJRadioButton();
		setWrappedLabel(content);
	}

	private void createJRadioButton() {		
		rButton = new JRadioButton();
		GridBagConstraints gbc_rButton = new GridBagConstraints();
		gbc_rButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_rButton.insets = new Insets(0, 0, 0, 10);
		gbc_rButton.gridx = 0;
		gbc_rButton.gridy = 0;
		add(rButton, gbc_rButton);		
	}

	private void setWrappedLabel(String content) {		
		WrappedLabel wLabel = new WrappedLabel(content);
		GridBagConstraints gbc_wLabel = new GridBagConstraints();
		gbc_wLabel.fill = GridBagConstraints.BOTH;		
		gbc_wLabel.gridx = 1;
		gbc_wLabel.gridy = 0;
		add(wLabel, gbc_wLabel);
	}
	
	public JRadioButton getButton(){
		return rButton;
	}
}
