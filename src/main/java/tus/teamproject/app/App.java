package tus.teamproject.app;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import tus.teamproject.app.domain.Algorithms;
import tus.teamproject.app.domain.EncryptionInterface;
import tus.teamproject.app.factory.EncryptionAlgorithmFactory;

import java.io.*;
import java.nio.file.Files;
import java.security.Security;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Main Application Class
 */
public class App {
    private final Logger logger = Logger.getLogger(App.class.getName());
    private Properties prop;
    private BufferedWriter resultWriter;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        App app = new App();
        app.process();
    }


    public App() {
        try {
            logger.info("Started");
            resultWriter = new BufferedWriter(new FileWriter("report.csv", false));
            resultWriter.write("algo,filesize,time,path\n");
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

            resultWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Done");
    }

    private void walkFiles() {
        File f = new File(prop.getProperty("input_dir"));
        File[] files = f.listFiles();
        if(files != null) {
            try {
                for (File file : files) {
                    perform_tests(file);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void perform_tests(File path) {

        try{
            long fileSize = Files.size(path.toPath());

            for(Algorithms algo : Algorithms.values()){
                String encryptedFilePath = prop.getProperty("output_dir") + "/encrypted_" + algo + "_" + path.getName();
                String decryptedFilePath = prop.getProperty("output_dir") + "/decrypted_" + algo + "_" + path.getName();

                EncryptionInterface processor = EncryptionAlgorithmFactory.getEncryptionProcessor(algo);
                if(processor != null) {
                    Long startTime = System.currentTimeMillis();
                    logger.info("Encrypting: " + path);
                    processor.encryptFile(path.toString(), encryptedFilePath);

                    logger.info("Decrypting: " + encryptedFilePath);
                    processor.decryptFile(encryptedFilePath, decryptedFilePath);
                    Long endTime = System.currentTimeMillis();

                    String resultLine = algo + "," + fileSize + "," + (endTime - startTime) + "," + path + "\n";
                    logger.info(resultLine);
                    resultWriter.write(resultLine);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            try {
                resultWriter.write("Error,Error,Error" + path + "\n");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
