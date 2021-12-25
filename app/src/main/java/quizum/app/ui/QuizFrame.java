package quizum.app.ui;

import quizum.CsvQuestionService;
import quizum.JsonQuestionService;
import quizum.QuizumRuntimeException;
import quizum.QuizumUtils;
import quizum.beans.Question;
import quizum.beans.UserInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuizFrame extends JFrame {
    private List<Question> questionList;
    private JLabel imageLabel, label;
    private UserInfo userInfo;
    private int iterator;
    private List<QuestionPanel> questionPanelList;
    private JButton buttonNext;
    private JButton buttonPrev;
    private final JsonQuestionService jsonQuestionService = new JsonQuestionService();
    private final CsvQuestionService csvQuestionService = new CsvQuestionService();

    public QuizFrame(String fileName, UserInfo userInfo) {
        if (fileName == null || fileName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nie można odczytać konfiguracji", "Błąd konfiguracji", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        fileName = "./resources/quizes/" + fileName;
        try {
            if (fileName.endsWith("json")) {
                questionList = jsonQuestionService.loadQuestionList(new File(fileName));
            } else {
                questionList = csvQuestionService.loadQuestionList(new File(fileName));
            }
        } catch (QuizumRuntimeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Błąd wczytywania pliku", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        this.userInfo = userInfo;
        initialize();
    }

    private void initialize() {
        setTitle("Quizum");
        setLocation(100, 100);
        setMinimumSize(new Dimension(1200, 700));
        setMaximumSize(new Dimension(1400, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(10, 10));

        iterator = -1;

        JPanel imagePanel = new JPanel();
        imagePanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        imageLabel = new JLabel();
        imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel.add(imageLabel);

        questionPanelList = new ArrayList<>();
        questionList.forEach(question -> questionPanelList.add(new QuestionPanel(question)));
        Collections.shuffle(questionPanelList);

        int width = questionPanelList.stream().max(Comparator.comparingInt(qPanel -> qPanel.getSize().width)).get().getWidth();
        int height = questionPanelList.stream().max(Comparator.comparingInt(qPanel -> qPanel.getSize().height)).get().getHeight();
        setSize(width, height);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(new GridLayout(1, 3, 0, 5));

        panel_1.add(new JPanel());

        JPanel panel_2 = new JPanel();
        panel_1.add(panel_2);

        buttonPrev = new JButton("Poprzednie");
        buttonPrev.addActionListener(arg0 -> {
            QuestionPanel panel = getPrevQuestionPanel();

            if (panel != null) {
                changePanel(panel);
            }
        });
        panel_2.add(buttonPrev);

        label = new JLabel("");
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        panel_2.add(label);

        buttonNext = new JButton("Następne");
        buttonNext.addActionListener(arg0 -> {
            QuestionPanel panel = getNextQuestionPanel();

            if (panel != null) {
                changePanel(panel);
            }
        });
        panel_2.add(buttonNext);

        changePanel(getNextQuestionPanel());

        JPanel panel_3 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
        flowLayout.setHgap(20);
        flowLayout.setAlignment(FlowLayout.RIGHT);
        panel_1.add(panel_3);

        JButton btnNewButton_1 = new JButton("Zakończ");
        btnNewButton_1.setHorizontalAlignment(SwingConstants.RIGHT);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                userInfo.setFinishTime();
                questionPanelList.forEach(questionPanel -> {
                    userInfo.resolveQuestion(questionPanel.question,
                            questionPanel.getAnswer() != null ? questionPanel.getAnswer() : null);
                });

                QuizumUtils.generateReport(userInfo);
                JOptionPane.showMessageDialog(null, "Poprawnych odpowiedzi: " + userInfo.getScore() + " z " + questionPanelList.size() + " (" + (int) ((double) userInfo.getScore() / questionPanelList.size() * 100) + "%)", "Wynik", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });
        panel_3.add(btnNewButton_1);


        getContentPane().add(panel_1, BorderLayout.SOUTH);

        getContentPane().add(imagePanel, BorderLayout.EAST);
        setVisible(true);
    }

    public void changePanel(QuestionPanel panel) {
        if (panel == null)
            return;

        int state = this.getExtendedState();
        Dimension size = this.getSize();

        if (getContentPane().getComponents().length > 0)
            getContentPane().remove(0);
        getContentPane().add(panel, 0);

        if (panel.question.getPictureFileName() != null) {
            setQuestionImage(QuizumUtils.getImageByFilename(panel.question.getPictureFileName()));
        } else {
            setQuestionImage(null);
        }

        label.setText((iterator + 1) + " / " + questionPanelList.size());

        if (iterator > 0) {
            revalidateButton(buttonPrev, true);
        } else {
            revalidateButton(buttonPrev, false);
        }

        if (iterator + 1 < questionPanelList.size()) {
            revalidateButton(buttonNext, true);
        } else {
            revalidateButton(buttonNext, false);
        }

        revalidate();
        repaint();
        pack();

        // this line keeps frame from resizing by itself after changing active panel
        if (state == JFrame.MAXIMIZED_BOTH) this.setExtendedState(state);
        else this.setSize(size);
    }

    public QuestionPanel getNextQuestionPanel() {
        if (questionPanelList.size() - 1 > iterator) {
            iterator++;
            return questionPanelList.get(iterator);
        }

        return null;
    }

    public QuestionPanel getPrevQuestionPanel() {
        if (iterator > 0) {
            iterator--;
            return questionPanelList.get(iterator);
        }
        return null;
    }

    private void revalidateButton(JButton button, boolean toEnable) {
        if ((button.isEnabled() && !toEnable) || (!button.isEnabled() && toEnable)) {
            button.setEnabled(toEnable);
        }
    }

    private void setQuestionImage(ImageIcon icon) {
        if (icon != null) {
            icon = QuizumUtils.fitIcon(icon, 600, getHeight());
        }
        imageLabel.setIcon(icon);
        imageLabel.repaint();
    }

}
