package quizum.app.ui;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.UIManager;

public class WrappedLabel extends JTextArea {
	
	WrappedLabel(String string) {
        super(string);
        setEditable(false);  
        setCursor(null);  
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setFont(UIManager.getFont("Label.font"));
        setBorder(UIManager.getBorder("Label.border"));
        setFocusable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
    }
     
    
}
