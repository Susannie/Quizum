package quizum.beans;

public class SolvedQuestion {
    public enum Status {
        UNANSWERED, INCORRECT, CORRECT
    }

    private String question;
    private String selectedAnswer;
    private Status status;

    public SolvedQuestion() {
    }

    public SolvedQuestion(String question, String selectedAnswer, Status status) {
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.status = status;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
