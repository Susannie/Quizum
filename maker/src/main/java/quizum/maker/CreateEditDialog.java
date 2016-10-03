package quizum.maker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import quizum.QuizumUtils;
import quizum.beans.Question;

public class CreateEditDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextArea textArea;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JLabel filepath;
	private JButton btnNewButton_1;
	private JButton btnDoczObrazek;
	private String filename;
	private JLabel imagePreview;
	private Question processedQuestion;

	public static void main(String[] args) {
		try {
			CreateEditDialog dialog = new CreateEditDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CreateEditDialog() {
		initialize();
	}

	public CreateEditDialog(Question question) {
		processedQuestion = question;
		initialize();
		prepareEditMode();
	}

	private void initialize() {
		setModal(true);
		setBounds(100, 100, 850, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		final JFileChooser fc = new JFileChooser();

		JLabel lblTrePytania = new JLabel("Treść pytania");
		lblTrePytania.setBounds(10, 11, 78, 14);
		contentPanel.add(lblTrePytania);

		textArea = new JTextArea();
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textArea.setLineWrap(true);
		textArea.setBounds(10, 36, 438, 162);
		contentPanel.add(textArea);

		JLabel lblOdpowiedzipierwszaJest = new JLabel("Odpowiedzi: (pierwsza jest poprawna)");
		lblOdpowiedzipierwszaJest.setBounds(10, 207, 218, 14);
		contentPanel.add(lblOdpowiedzipierwszaJest);

		textField = new JTextField();
		textField.setBackground(new Color(204, 255, 204));
		textField.setBounds(10, 232, 438, 30);
		contentPanel.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(10, 263, 438, 30);
		contentPanel.add(textField_1);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(10, 293, 438, 30);
		contentPanel.add(textField_2);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(10, 324, 438, 30);
		contentPanel.add(textField_3);

		filepath = new JLabel("");
		filepath.setBounds(484, 41, 305, 20);
		contentPanel.add(filepath);

		btnNewButton_1 = new JButton("Usuń obrazek");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filename = null;
				btnDoczObrazek.setText("Dołącz obrazek");
				btnNewButton_1.setVisible(false);
				filepath.setText("");
				imagePreview.setIcon(null);
			}
		});
		btnNewButton_1.setVisible(false);
		btnNewButton_1.setBackground(Color.red);
		btnNewButton_1.setBounds(640, 10, 130, 25);
		contentPanel.add(btnNewButton_1);

		btnDoczObrazek = new JButton("Dołącz obrazek");
		btnDoczObrazek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fc.setCurrentDirectory(new File("resources/images/"));
				int returnVal = fc.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					filename = fc.getSelectedFile().getAbsolutePath().split("resources[\\\\]images[\\\\]")[1];
					filepath.setText(filename);
					btnDoczObrazek.setText("Zmień obrazek");
					btnNewButton_1.setVisible(true);
					imagePreview.setIcon(QuizumUtils.fitIcon(QuizumUtils.getImageByFilename(filename), 310, 260));
				}
			}
		});
		btnDoczObrazek.setBounds(500, 10, 130, 25);
		contentPanel.add(btnDoczObrazek);

		imagePreview = new JLabel("");
		imagePreview.setBounds(485, 95, 310, 260);
		contentPanel.add(imagePreview);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			JButton okButton = new JButton("Zapisz");
			okButton.setActionCommand("Save");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					processedQuestion = new Question(textArea.getText(), new String[] { textField.getText(),
							textField_1.getText(), textField_2.getText(), textField_3.getText() }, filename);
					setVisible(false);
					dispose();
				}
			});
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}
		{
			JButton cancelButton = new JButton("Anuluj");
			cancelButton.setActionCommand("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
					dispose();
				}
			});
			buttonPane.add(cancelButton);
		}
	}

	private void prepareEditMode() {
		textArea.setText(processedQuestion.getContent());
		textField.setText(processedQuestion.getAnswerbyIndex(0));//getAnswers().get(0)[1]);
		textField_1.setText(processedQuestion.getAnswerbyIndex(1));
		textField_2.setText(processedQuestion.getAnswerbyIndex(2));
		textField_3.setText(processedQuestion.getAnswerbyIndex(3));
		
		if (processedQuestion.getPictureFileName() != null) {
			btnNewButton_1.setVisible(true);
			btnDoczObrazek.setText("Zmień obrazek");
			filename = processedQuestion.getPictureFileName();
			filepath.setText(filename);
			imagePreview.setIcon(QuizumUtils.fitIcon(QuizumUtils.getImageByFilename(filename), 310, 260));
		}
	}

	public Question showDialog() {
		setVisible(true);
		return processedQuestion;
	}
}
