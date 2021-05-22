package quizum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import quizum.beans.Question;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonQuestionService implements QuestionService {
    @Override
    public List<Question> loadQuestionList(File file) {
        ObjectMapper mapper = new ObjectMapper();
        List<Question> list = new ArrayList<>();

        try {
            list = mapper.readValue(file, new TypeReference<List<Question>>() {
            });
        } catch (IOException e) {
            System.out.println("Failed to load file\n" + e.getLocalizedMessage());
        }

        return list;
    }

    @Override
    public void writeQuestionList(File file, List<Question> questionList) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                int option = new JOptionPane().showConfirmDialog(null, "Jesteś pewien że chcesz nadpisać zawartość pliku: " + file.getName() + "?", "Nadpisać?", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.NO_OPTION) {
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
}
