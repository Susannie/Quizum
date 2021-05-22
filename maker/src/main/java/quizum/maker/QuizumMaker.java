package quizum.maker;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import quizum.Configs;
import quizum.CsvQuestionService;
import quizum.JsonQuestionService;
import quizum.QuizumUtils;
import quizum.beans.Question;

public class QuizumMaker {
    private JFrame frmQuizumMaker;
    private DefaultListModel<Question> listmodel;
    private final JsonQuestionService jsonQuestionService = new JsonQuestionService();
    private final CsvQuestionService csvQuestionService = new CsvQuestionService();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                UIManager.getLookAndFeelDefaults().put("defaultFont", new Font(Configs.getInstance().getProperty("maker.font.family", "SansSerif"), Integer.parseInt(Configs.getInstance().getProperty("maker.font.style", "0")), Integer.parseInt(Configs.getInstance().getProperty("maker.font.size", "12"))));
                QuizumMaker window = new QuizumMaker();
                window.frmQuizumMaker.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public QuizumMaker() {
        initialize();
    }

    private void initialize() {
        frmQuizumMaker = new JFrame();
        frmQuizumMaker.setTitle("Quizum Maker");
        frmQuizumMaker.setBounds(100, 100, 805, 440);
        frmQuizumMaker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmQuizumMaker.getContentPane().setLayout(null);

        final JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Data Files", "json", "csv");
        fc.setFileFilter(filter);

        listmodel = new DefaultListModel<>();
        JList<Question> list = new JList<>(listmodel);
        list.setBounds(10, 36, 518, 334);
        frmQuizumMaker.getContentPane().add(list);

        JLabel lblListaPyta = new JLabel("Lista pyta\u0144");
        lblListaPyta.setBounds(10, 11, 114, 14);
        frmQuizumMaker.getContentPane().add(lblListaPyta);

        JButton btnNewButton = new JButton("Nowe pytanie");
        btnNewButton.addActionListener(e -> {
            CreateEditDialog newQuestionDialog = new CreateEditDialog();
            Question newQuestion = newQuestionDialog.showDialog();
            if (newQuestion != null)
                listmodel.addElement(newQuestion);
        });
        btnNewButton.setBounds(567, 33, 191, 23);
        frmQuizumMaker.getContentPane().add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Edytuj");
        btnNewButton_1.addActionListener(e -> {
            Question selectedQuestion = list.getSelectedValue();
            if (selectedQuestion != null) {
                CreateEditDialog newQuestionDialog = new CreateEditDialog(selectedQuestion);
                Question editedQuestion = newQuestionDialog.showDialog();
                if (editedQuestion != null)
                    listmodel.setElementAt(editedQuestion, list.getSelectedIndex());
            }
        });
        btnNewButton_1.setBounds(567, 101, 191, 23);
        frmQuizumMaker.getContentPane().add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Usuń");
        btnNewButton_2.addActionListener(e -> listmodel.removeElementAt(list.getSelectedIndex()));
        btnNewButton_2.setBounds(567, 135, 191, 23);
        frmQuizumMaker.getContentPane().add(btnNewButton_2);

        JMenuBar menuBar = new JMenuBar();
        frmQuizumMaker.setJMenuBar(menuBar);

        JMenu mnPlik = new JMenu("Plik");
        menuBar.add(mnPlik);

        JMenuItem mntmNowyZestaw = new JMenuItem("Nowy");
        mntmNowyZestaw.addActionListener(listener -> listmodel.clear());
        mnPlik.add(mntmNowyZestaw);

        JMenuItem mntmWczytajZestaw = new JMenuItem("Wczytaj");
        mntmWczytajZestaw.addActionListener(listener -> {
            listmodel.clear();

            fc.setCurrentDirectory(new File("."));
            int returnVal = fc.showOpenDialog(frmQuizumMaker);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                File selectedFile = fc.getSelectedFile();
                if (selectedFile.getName().endsWith(".json")) {
                    jsonQuestionService.loadQuestionList(selectedFile)
                            .forEach(question -> listmodel.addElement(question));
                } else {
                    csvQuestionService.loadQuestionList(selectedFile)
                            .forEach(question -> listmodel.addElement(question));
                }
            }
        });
        mnPlik.add(mntmWczytajZestaw);

        JMenuItem mntmZapiszJSON = new JMenuItem("Zapisz jako JSON");
        mntmZapiszJSON.addActionListener(listener -> {
            String filename = "";

            fc.setCurrentDirectory(new File("."));
            fc.setFileFilter(new FileNameExtensionFilter("JSON", "json"));
            fc.setSelectedFile(new File("pytania.json"));
            int returnVal = fc.showSaveDialog(frmQuizumMaker);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                filename = fc.getSelectedFile().getPath();
            }

            if (filename.isEmpty())
                return;

            csvQuestionService.writeQuestionList(new File(filename.endsWith(".json") ? filename : filename + ".json"), Collections.list(listmodel.elements()));
        });
        mnPlik.add(mntmZapiszJSON);

        JMenuItem mntmZapiszCSV = new JMenuItem("Zapisz jako CSV");
        mntmZapiszCSV.addActionListener(listener -> {
            String filename = "";

            fc.setCurrentDirectory(new File("."));
            fc.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
            fc.setSelectedFile(new File("pytania.csv"));
            int returnVal = fc.showSaveDialog(frmQuizumMaker);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                filename = fc.getSelectedFile().getPath();
            }

            if (filename.isEmpty())
                return;

            csvQuestionService.writeQuestionList(new File(filename.endsWith(".csv") ? filename : filename + ".csv"), Collections.list(listmodel.elements()));
        });
        mnPlik.add(mntmZapiszCSV);
    }
}
