package quizum;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configs extends Properties {
    private static Configs instance = null;

    protected Configs() {
    }

    public static Configs getInstance() {
        if (instance == null) {
            instance = new Configs();
            InputStream input = null;

            try {
                input = new FileInputStream("properties.properties");
                instance.load(input);
            } catch (IOException e) {
                System.out.println("Failed to load properties file\n" + e.getLocalizedMessage());
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return instance;
    }

    public void changeDefaultFile(String filename) throws IOException {
        instance.setProperty("question.base", filename);
        FileOutputStream out = new FileOutputStream("properties.properties");
        instance.store(out, null);
        out.close();
    }

    public void changeDefaultTemplate(String filename) throws IOException {
        instance.setProperty("result.template", filename);
        FileOutputStream out = new FileOutputStream("properties.properties");
        instance.store(out, null);
        out.close();
    }
}
