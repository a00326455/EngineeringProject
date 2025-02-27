package tus.teamproject.app.processor;

import tus.teamproject.app.domain.EncryptionInterface;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;

/*
 Twofish is a symmetric encryption algorithm and a successor to Blowfish.
 It offers strong security and is suitable for both hardware and software implementations.
 */
public class TwofishEncryption implements EncryptionInterface {

    private SecretKey secretKey;
    private static final int KEY_SIZE = 256;

    public TwofishEncryption(){
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("Twofish");
            keyGen.init(KEY_SIZE);
            secretKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void encryptFile(String inputFilePath, String outputFilePath) {
        try {
            Cipher cipher = Cipher.getInstance("Twofish/ECB/PKCS5Padding", "BC");
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
            Cipher cipher = Cipher.getInstance("Twofish/ECB/PKCS5Padding", "BC");
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
