package quizum;

import quizum.beans.Question;

import java.io.File;
import java.util.List;

public interface QuestionService {
    List<Question> loadQuestionList(File file) throws QuizumRuntimeException;
    void writeQuestionList(File file, List<Question> questionList) throws QuizumRuntimeException;
}
