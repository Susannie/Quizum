package quizum;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import quizum.beans.Question;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class CsvQuestionService implements QuestionService {
    private static final String[] HEADERS = new String[]{"Pytanie", "Nazwa pliku z obrazkiem", "Prawidłowa odpowiedź", "Pozostałe odpowiedzi"};

    @Override
    public List<Question> loadQuestionList(File file) throws QuizumRuntimeException {
        try (Reader in = new FileReader(file)) {
            return CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(in)
                    .getRecords()
                    .stream()
                    .map(this::map)
                    .collect(Collectors.toList());
        } catch (IOException | NoSuchElementException e) {
            System.out.println("Failed to load file\n" + e.getLocalizedMessage());
            throw new QuizumRuntimeException("Nie udało się odczytać danych z pliku.");
        }
    }

    @Override
    public void writeQuestionList(File file, List<Question> questionList) throws QuizumRuntimeException {
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                int option = new JOptionPane().showConfirmDialog(null, "Jesteś pewien że chcesz nadpisać zawartość pliku: " + file.getName() + "?", "Nadpisać?", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            FileWriter out = new FileWriter(file);
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS));
            for (Question question : questionList) {
                ArrayList<String> record = new ArrayList<>();
                record.add(question.getContent());
                record.add(question.getPictureFileName());
                question.getAnswers().stream().filter(a -> a[0].equals("0")).map(a -> a[1]).forEach(record::add);
                question.getAnswers().stream().filter(a -> !a[0].equals("0")).map(a -> a[1]).forEach(record::add);
                printer.printRecord(record.toArray());
            }
            printer.flush();
            printer.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Failed to save question to file \n" + e.getLocalizedMessage());
            throw new QuizumRuntimeException("Nie udało się zapisać do pliku.");
        }
    }

    private Question map(CSVRecord record) throws NoSuchElementException {
        Question question = new Question();
        Iterator<String> iterator = record.iterator();
        question.setContent(iterator.next());
        question.setPictureFileName(iterator.next());
        List<String[]> answers = new ArrayList<>();
        while (iterator.hasNext()) {
            answers.add(new String[]{String.valueOf(answers.size()), iterator.next()});
        }
        question.setAnswers(answers);
        return question;
    }
}
