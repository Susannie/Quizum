package quizum.app;

import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.joda.time.DateTime;

import quizum.Configs;
import quizum.app.ui.QuizFrame;
import quizum.beans.UserInfo;

public class Quizum {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
				UIManager.getLookAndFeelDefaults().put("defaultFont", new Font(Configs.getInstance().getProperty("quizum.font.family","SansSerif"), Integer.parseInt(Configs.getInstance().getProperty("quizum.font.style","0")), Integer.parseInt(Configs.getInstance().getProperty("quizum.font.size","12"))));
				new QuizFrame(Configs.getInstance().getProperty("question.base"), new UserInfo(userNameDialog()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}	

	public static String userNameDialog(){
		String name = (String)JOptionPane.showInputDialog("Jak się nazywasz?");
		if(name == null) System.exit(0);
		name=name.trim();
		
		DateTime dt = new DateTime();
		String datePath=dt.getYear()+"/"+dt.getMonthOfYear()+"/"+dt.getDayOfMonth()+"/";
		
		while (!name.matches("^[a-zA-Z\\s]{5,30}$") || new File("results/"+datePath+name+".html").exists()){
			if(new File("results/"+datePath+name+".html").exists()){
				JOptionPane.showMessageDialog(null, "Użytkownik: "+name+" już istnieje.", "Wynik", JOptionPane.WARNING_MESSAGE);
			}
			name = (String)JOptionPane.showInputDialog("Jak się nazywasz? \nNazwa może składać się jedynie z liter (bez polskich znaków) oraz spacji, i mieć długość 5-30 znaków");
			if(name == null) System.exit(0);
			name=name.trim();
		}
		return name;
	}
	
	

}
