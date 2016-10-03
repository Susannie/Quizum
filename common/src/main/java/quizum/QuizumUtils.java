package quizum;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import quizum.beans.Question;
import quizum.beans.UserInfo;
import quizum.dto.QuestionDTO;

public class QuizumUtils {

	public static List<Question> loadQuestionList(File file) {
		ObjectMapper mapper = new ObjectMapper();
		List<Question> list = new ArrayList<Question>();

		try {
			list = mapper.readValue(file, new TypeReference<List<Question>>() {
			});
		} catch (IOException e) {
			System.out.println("Failed to load file\n" + e.getLocalizedMessage());
		}

		return list;
	}
	
	public static void writeQuestionList(File file, List<Question> questionList){
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}else{
				int option=new JOptionPane().showConfirmDialog(null, "Jesteś pewien że chcesz nadpisać zawartość pliku: "+file.getName()+"?", "Nadpisać?", JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.NO_OPTION){
					return;
				}
			}
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.writeValue(file, questionList);
			System.out.println("Saved");
		} catch (IOException e) {
			System.out.println("Failed to save question to file \n" + e.getLocalizedMessage());
		}
	}
	
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
            	
            	quest.setAnswer(rightAnswer[1]);          	
            	
            	if(key == null){
            		quest.setWrongAnswer("Brak");
            	}          	
            	else if(key != 0){
            		try{
            			String[] badAnswer = question.getAnswers().stream().filter(elem -> elem[0].equals(key.toString())).findFirst().orElse(null);
            			quest.setWrongAnswer(badAnswer[1].replaceAll("\n", "<br>"));  
            		} catch (NoSuchElementException e){ 
            			quest.setWrongAnswer("Wystąpił błąd");  
            		}
            	}
            	
            	quest.setContent(question.getContent().replaceAll("\n", "<br>"));
            	quest.setPictureFileName(question.getPictureFileName());   
            	ImageIcon tempIcon = fitIcon(getImageByFilename(question.getPictureFileName()), 600, 600);
            	quest.setPictureWidth(tempIcon.getIconWidth());
            	quest.setPictureHeight(tempIcon.getIconHeight());
            	
            	questionDtoList.add(quest);
            });
        	
            scopes.put("questions", questionDtoList);
            
        	File file = new File("results/"+datePath);
        	if(!file.exists()) file.mkdirs();
        	
            Writer writer =	new OutputStreamWriter(new FileOutputStream("results/"+datePath+userInfo.getUsername()+".html"), "UTF-8");
			Mustache mustache = mf.compile(new InputStreamReader(new FileInputStream("resources/templates/"+Configs.getInstance().getProperty("result.template")), "UTF-8"),"wynik");
			mustache.execute(writer, scopes);
			writer.flush();
			writer.close();			
			
			
		} catch (Exception e) {
			e.printStackTrace();
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
	
}
