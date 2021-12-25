package quizum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class QuizumUtils {

    public static void generateReport(UserInfo userInfo) {
        generateRawReport(userInfo);
        generateHtmlReport(userInfo);
    }

    private static void generateRawReport(UserInfo userInfo) {
        Writer writer = null;
        try {
            DateTime dt = new DateTime();
            String filename = dt.getYear() + "_" + dt.getMonthOfYear() + "_" + dt.getDayOfMonth() + "_" + userInfo.getFilename().split("\\.")[0] + ".json";
            File file = new File("results/raw/");
            if (!file.exists()) file.mkdirs();
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(WRITE_DATES_AS_TIMESTAMPS);
            String reportFilename = "results/raw/" + filename;
            try{
                List<UserInfo> previous = new ArrayList<>();
                File reportFile = new File(reportFilename);
                if (reportFile.exists() && reportFile.canRead())
                    previous = mapper.readValue(reportFile, new TypeReference<List<UserInfo>>(){});
                previous.add(userInfo);
                writer = new OutputStreamWriter(new FileOutputStream(reportFilename), StandardCharsets.UTF_8);
                mapper.writeValue(writer, previous);
                writer.flush();
            } catch (JsonProcessingException e) {
                if (writer != null) writer.close();
                writer = new OutputStreamWriter(new FileOutputStream(reportFilename+"_bck_"+System.currentTimeMillis()), StandardCharsets.UTF_8);
                mapper.writeValue(writer, userInfo);
                writer.flush();
            }
        } catch (Exception e) {
            //no op
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    //no op
                }
            }
        }
    }

    private static void generateHtmlReport(UserInfo userInfo) {
        String datePath;

        DateTime dt = new DateTime();
        datePath = dt.getYear() + "/" + dt.getMonthOfYear() + "/" + dt.getDayOfMonth() + "/";

        Map<Question, Integer> answeredQuestions = userInfo.getAnsweredQuestions();
        List<QuestionDTO> questionDtoList = new ArrayList<>();

        MustacheFactory mf = new DefaultMustacheFactory();
        try {
            HashMap<String, Object> scopes = new HashMap<>();
            scopes.put("userInfo", userInfo);
            scopes.put("filenameWithoutExtension", userInfo.getFilename().split("\\.(csv|json)")[0]);
            scopes.put("username", userInfo.getUsername());
            scopes.put("score", userInfo.getScore());
            scopes.put("total", userInfo.getAnsweredQuestions().size());

            answeredQuestions.forEach((question, key) -> {
                QuestionDTO quest = new QuestionDTO();
                String[] rightAnswer = question.getAnswers().stream().filter(elem -> elem[0].equals("0")).findAny().get();

                quest.setAnswer(escape(rightAnswer[1]));

                if (key == null) {
                    quest.setWrongAnswer("Brak");
                } else if (key != 0) {
                    try {
                        question.getAnswers().stream()
                                .filter(elem -> elem[0].equals(key.toString()))
                                .findFirst()
                                .ifPresent(wrongAnswer -> quest.setWrongAnswer(escape(wrongAnswer[1])));
                    } catch (NoSuchElementException e) {
                        quest.setWrongAnswer("Wystąpił błąd");
                    }
                }
                List<String> otherAnswers = question.getAnswers().stream()
                        .filter(elem -> !elem[0].equals("0"))
                        .filter(elem -> key != null && !elem[0].equals(key.toString()))
                        .map(elem -> elem[1])
                        .collect(Collectors.toList());

                quest.setOtherAnswers(otherAnswers);


                quest.setContent(escape(question.getContent()));
                quest.setPictureFileName(question.getPictureFileName());
                ImageIcon tempIcon = fitIcon(getImageByFilename(question.getPictureFileName()), 600, 600);
                quest.setPictureWidth(tempIcon.getIconWidth());
                quest.setPictureHeight(tempIcon.getIconHeight());

                questionDtoList.add(quest);
            });

            scopes.put("questions", questionDtoList);

            File file = new File("results/" + datePath);
            if (!file.exists()) file.mkdirs();

            Writer writer = new OutputStreamWriter(new FileOutputStream("results/" + datePath + getReportName(userInfo) + ".html"), StandardCharsets.UTF_8);
            Mustache mustache = mf.compile(new InputStreamReader(new FileInputStream("resources/templates/" + Configs.getInstance().getProperty("result.template")), StandardCharsets.UTF_8), "wynik");
            mustache.execute(writer, scopes);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nie udało się wygenerować raportu", "Błąd zapisu do pliku", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public static ImageIcon getImageByFilename(String filename) {
        return new ImageIcon("resources/images/" + filename);
    }

    public static ImageIcon fitIcon(ImageIcon icon, int containerWidth, int containerHeight) {
        if (icon.getIconWidth() > containerWidth) {
            double rate = icon.getIconWidth() / (double) containerWidth;
            int height = (int) (icon.getIconHeight() / rate);
            int width = (int) (icon.getIconWidth() / rate);
            icon = new ImageIcon(getScaledImage(icon, width, height));
        } else if (icon.getIconHeight() > containerHeight) {
            double rate = icon.getIconHeight() / (double) containerHeight;
            int height = (int) (icon.getIconHeight() / rate);
            int width = (int) (icon.getIconWidth() / rate);
            icon = new ImageIcon(getScaledImage(icon, width, height));
        }

        return icon;
    }

    public static Image getScaledImage(ImageIcon icon, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(icon.getImage(), 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    public static String getReportName(String username, String filename) {
        return username + "_" + filename;
    }

    public static String getReportName(UserInfo userInfo) {
        return getReportName(userInfo.getUsername(), userInfo.getFilename());
    }

    private static String escape(String stringToEscape) {
        return StringEscapeUtils.escapeHtml3(stringToEscape);
    }

}
