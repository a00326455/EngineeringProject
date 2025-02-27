package tus.teamproject.app.processor;

import tus.teamproject.app.domain.EncryptionInterface;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;

public class IDEAEncryption implements EncryptionInterface {

    private SecretKey secretKey;
    private static final int KEY_SIZE = 256;

    public IDEAEncryption(){
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("IDEA");
            keyGen.init(KEY_SIZE);
            secretKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void encryptFile(String inputFilePath, String outputFilePath) {
        try {
            Cipher cipher = Cipher.getInstance("IDEA/ECB/PKCS5Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            FileInputStream fis = new FileInputStream(inputFilePath);
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                fos.write(outputBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decryptFile(String inputFilePath, String outputFilePath) {
        try{
            Cipher cipher = Cipher.getInstance("IDEA/ECB/PKCS5Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            FileInputStream fis = new FileInputStream(inputFilePath);
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                fos.write(outputBytes);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getKeyLength() {
        return KEY_SIZE;
    }
}
