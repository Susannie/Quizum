package quizum.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    final private String username;
    final private String filename;
    final private DateTimeWrapper startTime;
    private DateTimeWrapper finishTime;
    private final Map<Question, Integer> questionsResolved;
    private final List<SolvedQuestion> questionsSolved;

    public UserInfo() {
        this.username = null;
        this.filename = null;
        this.startTime = DateTimeWrapper.now();
        this.questionsResolved = new HashMap<>();
        this.questionsSolved = new ArrayList<>();
    }

    public UserInfo(String username, String filename) {
        this.username = username;
        this.filename = filename;
        this.startTime = DateTimeWrapper.now();
        this.questionsResolved = new HashMap<>();
        this.questionsSolved = new ArrayList<>();
    }

    public void resolveQuestion(Question question, Integer answer) {
        questionsResolved.put(question, answer);
        if (answer != null) {
            questionsSolved.add(new SolvedQuestion(question.getContent(), question.getAnswerbyIndex(answer), answer == 0 ? SolvedQuestion.Status.CORRECT : SolvedQuestion.Status.INCORRECT));
        } else {
            questionsSolved.add(new SolvedQuestion(question.getContent(), null, SolvedQuestion.Status.UNANSWERED));
        }
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return (int) questionsSolved.stream().filter(question -> SolvedQuestion.Status.CORRECT.equals(question.getStatus())).count();
    }

    public int getTotalQuestions() {
        return questionsSolved.size();
    }

    @JsonIgnore
    public Map<Question, Integer> getAnsweredQuestions() {
        return questionsResolved;
    }

    public DateTimeWrapper getStartTime() {
        return startTime;
    }

    public DateTimeWrapper getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(DateTimeWrapper finishTime) {
        this.finishTime = finishTime;
    }

    public void setFinishTime() {
        this.finishTime = DateTimeWrapper.now();
    }

    public Duration getDuration() {
        return Duration.between(startTime.getDateTime(), finishTime.getDateTime());
    }

    public String getFilename() {
        return filename;
    }

    public List<SolvedQuestion> getQuestionsSolved() {
        return questionsSolved;
    }

}
