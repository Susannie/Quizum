package quizum;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import quizum.beans.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CsvQuestionService implements QuestionService {
    private static final String[] HEADERS = new String[]{"Pytanie","Nazwa pliku z obrazkiem","Prawidłowa odpowiedź","Pozostałe odpowiedzi"};

    @Override
    public List<Question> loadQuestionList(File file) {
        try {
            Reader in = new FileReader(file);
            return CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(in)
                    .getRecords()
                    .stream()
                    .map(this::map)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Failed to load file\n" + e.getLocalizedMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void writeQuestionList(File file, List<Question> questionList) {
        try {

            FileWriter out = new FileWriter(file);
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS));
            for (Question question : questionList) {
                ArrayList<String> record = new ArrayList<>();
                record.add(question.getContent());
                record.add(question.getPictureFileName());
                record.add(question.getContent());
                question.getAnswers().stream().map(a -> a[1]).forEach(record::add);
                printer.printRecord(record.toArray());
            }
            printer.flush();
            printer.close();
        } catch (IOException e) {
            System.out.println("Failed to save question to file \n" + e.getLocalizedMessage());
        }
    }

    private Question map(CSVRecord record) {
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
