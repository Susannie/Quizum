package quizum.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {
    private String content;
    private String pictureFileName;
    private List<String[]> answers;

    public Question() {

    }

    public Question(String content, String[] answers, String pictureFileName) {
        this.answers = constructAnswerList(answers);
        this.content = content;
        this.pictureFileName = pictureFileName;
    }

    public List<String[]> constructAnswerList(String[] answers) {
        List<String[]> answersList = new ArrayList<String[]>();
        for (int i = 0; i < answers.length; i++) {
            String[] answer = {String.valueOf(i), answers[i]};
            answersList.add(answer);
        }

        return answersList;
    }

    public String getContent() {
        return content;
    }

    public void shuffleAnswers() {
        Collections.shuffle(answers);
    }

    public String getAnswerbyIndex(int index) {
        return answers.stream().filter(answ -> answ[0].equals(String.valueOf(index))).findAny().map(answ -> answ[1]).orElse(null);
    }

    public List<String[]> getAnswers() {
        return answers;
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAnswers(List<String[]> answers) {
        this.answers = answers;
    }

    public String toString() {
        return content;
    }

}
