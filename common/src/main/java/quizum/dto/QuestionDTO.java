package quizum.dto;

public class QuestionDTO{
	private String content;
	private String pictureFileName;
	private int pictureWidth;
	private int pictureHeight;
	private String answer;
	private String wrongAnswer;

	public QuestionDTO(){
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPictureFileName() {
		return pictureFileName;
	}

	public void setPictureFileName(String pictureFileName) {
		this.pictureFileName = pictureFileName;
	}

	public int getPictureWidth() {
		return pictureWidth;
	}

	public void setPictureWidth(int pictureWidth) {
		this.pictureWidth = pictureWidth;
	}

	public int getPictureHeight() {
		return pictureHeight;
	}

	public void setPictureHeight(int pictureHeight) {
		this.pictureHeight = pictureHeight;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getWrongAnswer() {
		return wrongAnswer;
	}

	public void setWrongAnswer(String wrongAnswer) {
		this.wrongAnswer = wrongAnswer;
	}	
}
