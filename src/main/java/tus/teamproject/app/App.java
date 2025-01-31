package tus.teamproject.app;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Main Application Class
 */
public class App {
    private final Logger logger = Logger.getLogger(App.class.getName());
    private Properties prop;

    public static void main(String[] args) {
        App app = new App();
        app.process();
    }


    public App() {
        try {
            logger.info("Started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void process() {
        logger.info("Started");
        try {
            int maxKeySize = javax.crypto.Cipher.getMaxAllowedKeyLength("AES");
            System.out.println("Max Key Size for AES : " + maxKeySize);
            if (maxKeySize < 2147483647) {
                logger.warning("Can't proceed, please configure Java first. Refer - https://www.baeldung.com/java-bouncy-castle#setup-unlimited-strength-jurisdiction-policy-files");
                System.exit(1);
            }
            // Open the properties file
            prop = new Properties();
            Reader reader = new FileReader("/Users/maheshkanse/Documents/GitHub/EngineeringProject/target/classes/config.properties");
            prop.load(reader);
            reader.close();

            logger.info("Input: " + prop.getProperty("input_dir"));
            logger.info("Output: " + prop.getProperty("output_dir"));

            perform_tests();

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Done");
    }

    private void walkFiles() {
        try (Stream<Path> paths = Files.walk(Paths.get(prop.getProperty("input_dir")))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(perform_tests);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void perform_tests() {
        // TODO
    }
}
