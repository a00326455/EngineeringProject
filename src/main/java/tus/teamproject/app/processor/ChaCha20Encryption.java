package tus.teamproject.app.processor;

import tus.teamproject.app.domain.EncryptionInterface;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.ChaCha20ParameterSpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/*
 ChaCha20 is a stream cipher designed as an alternative to AES.
 It offers high performance and security, especially on devices with limited processing power.
 */
public class ChaCha20Encryption implements EncryptionInterface {
    private static final String ALGORITHM = "ChaCha20";
    private static final int KEY_SIZE = 256;
    private byte[] nonce;
    private int counter;
    private SecretKey key;

    public ChaCha20Encryption() {
        // Generate a secret key
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(KEY_SIZE);
            key = keyGen.generateKey();

            // Create a nonce (12 bytes)
            nonce = new byte[12];
            SecureRandom random = new SecureRandom();
            random.nextBytes(nonce);

            counter = 5;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void encryptFile(String inputFilePath, String outputFilePath) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            ChaCha20ParameterSpec paramSpec = new ChaCha20ParameterSpec(nonce, counter);
            cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

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

    @Override
    public void decryptFile(String inputFilePath, String outputFilePath) {
        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            ChaCha20ParameterSpec paramSpec = new ChaCha20ParameterSpec(nonce, counter);
            cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

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
