package quizum;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.apache.commons.text.StringEscapeUtils;
import org.joda.time.DateTime;
import quizum.beans.Question;
import quizum.beans.UserInfo;
import quizum.dto.QuestionDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

public class QuizumUtils {
	
	public static void generateReport(UserInfo userInfo){		
		String datePath;
		
		DateTime dt = new DateTime();
		datePath=dt.getYear()+"/"+dt.getMonthOfYear()+"/"+dt.getDayOfMonth()+"/";
		

        Map<Question, Integer> answeredQuestions = userInfo.getAnsweredQuestions();
        List<QuestionDTO> questionDtoList = new ArrayList<QuestionDTO>();

        MustacheFactory mf = new DefaultMustacheFactory();
        try {
        	HashMap<String, Object> scopes = new HashMap<String, Object>();
            scopes.put("username", userInfo.getUsername());
            scopes.put("score", userInfo.getTotal());
            scopes.put("total", userInfo.getAnsweredQuestions().size());
            
            answeredQuestions.forEach((question, key) -> {
            	QuestionDTO quest = new QuestionDTO();
            	String[] rightAnswer = question.getAnswers().stream().filter(elem -> elem[0].equals("0")).findAny().get();
            	
            	quest.setAnswer(escape(rightAnswer[1]));
            	
            	if(key == null){
            		quest.setWrongAnswer("Brak");
            	}
            	else if(key != 0){
            		try{
            			String[] badAnswer = question.getAnswers().stream().filter(elem -> elem[0].equals(key.toString())).findFirst().orElse(null);
            			quest.setWrongAnswer(escape(badAnswer[1]));
            		} catch (NoSuchElementException e){ 
            			quest.setWrongAnswer("Wystąpił błąd");
            		}
            	}
            	
            	quest.setContent(escape(question.getContent()));
            	quest.setPictureFileName(question.getPictureFileName());   
            	ImageIcon tempIcon = fitIcon(getImageByFilename(question.getPictureFileName()), 600, 600);
            	quest.setPictureWidth(tempIcon.getIconWidth());
            	quest.setPictureHeight(tempIcon.getIconHeight());
            	
            	questionDtoList.add(quest);
            });
        	
            scopes.put("questions", questionDtoList);
            
        	File file = new File("results/"+datePath);
        	if(!file.exists()) file.mkdirs();
        	
            Writer writer =	new OutputStreamWriter(new FileOutputStream("results/"+datePath+userInfo.getUsername()+".html"), StandardCharsets.UTF_8);
			Mustache mustache = mf.compile(new InputStreamReader(new FileInputStream("resources/templates/"+Configs.getInstance().getProperty("result.template")), StandardCharsets.UTF_8),"wynik");
			mustache.execute(writer, scopes);
			writer.flush();
			writer.close();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Nie udało się wygenerować raportu", "Błąd zapisu do pliku", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public static ImageIcon getImageByFilename(String filename){
		return new ImageIcon("resources/images/"+filename);
	}
	
	public static ImageIcon fitIcon(ImageIcon icon, int containerWidth , int containerHeight){
		if(icon.getIconWidth()>containerWidth){
			double rate = icon.getIconWidth()/(double)containerWidth;
			int height = (int) (icon.getIconHeight()/rate);
			int width = (int) (icon.getIconWidth()/rate);
			icon = new ImageIcon(getScaledImage(icon, width, height));
		}else if(icon.getIconHeight()>containerHeight){
			double rate = icon.getIconHeight()/(double)containerHeight;
			int height = (int) (icon.getIconHeight()/rate);
			int width = (int) (icon.getIconWidth()/rate);
			icon = new ImageIcon(getScaledImage(icon, width, height));
		}	
		
		return icon;
	}
	
	public static Image getScaledImage(ImageIcon icon, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(icon.getImage(), 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}

	private static String escape(String stringToEscape) {
		return StringEscapeUtils.escapeHtml3(stringToEscape);
	}
	
}
