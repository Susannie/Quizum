package quizum;

import quizum.beans.Question;

import java.io.File;
import java.util.List;

public interface QuestionService {
    List<Question> loadQuestionList(File file);
    void writeQuestionList(File file, List<Question> questionList);
}
