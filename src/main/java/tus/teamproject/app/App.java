package tus.teamproject.app;

import tus.teamproject.app.domain.Algorithms;
import tus.teamproject.app.domain.EncryptionInterface;
import tus.teamproject.app.factory.EncryptionAlgorithmFactory;

import java.io.*;
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
//            Reader reader = new FileReader("/Users/maheshkanse/Documents/GitHub/EngineeringProject/target/classes/config.properties");
//            prop.load(reader);
//            reader.close();

            if(System.getenv("INPUT_DIR") != null) {
                prop.setProperty("input_dir", System.getenv("INPUT_DIR"));
            } else {
                prop.setProperty("input_dir", "/Users/maheshkanse/Documents/GitHub/EngineeringProject/test_dir");
            }
            if(System.getenv("OUTPUT_DIR") != null){
                prop.setProperty("output_dir", System.getenv("OUTPUT_DIR"));
            } else {
                prop.setProperty("output_dir", "/Users/maheshkanse/Documents/GitHub/EngineeringProject/results_dir");
            }

            logger.info("Input: " + prop.getProperty("input_dir"));
            logger.info("Output: " + prop.getProperty("output_dir"));

            walkFiles();

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Done");
    }

    private void walkFiles() {
        File f = new File(prop.getProperty("input_dir"));
        File[] files = f.listFiles();
        if(files != null) {
            for (File file : files) {
                perform_tests(file);
            }
        }
    }

    private void perform_tests(File path) {
        String encryptedFilePath = prop.getProperty("output_dir") + "/encrypted_" + path.getName();
        String decryptedFilePath = prop.getProperty("output_dir") + "/decrypted_" + path.getName();
        for(Algorithms algo : Algorithms.values()){
            EncryptionInterface processor = EncryptionAlgorithmFactory.getEncryptionProcessor(algo);
            if(processor != null) {
                logger.info("Encrypting: " + path.toString());
                processor.encryptFile(path.toString(), encryptedFilePath);

                logger.info("Decrypting: " + encryptedFilePath);
                processor.decryptFile(encryptedFilePath, decryptedFilePath);
            }
        }
    }

}
