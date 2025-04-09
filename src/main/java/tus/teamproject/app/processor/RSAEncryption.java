package tus.teamproject.app.processor;

import tus.teamproject.app.domain.EncryptionInterface;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;

public class RSAEncryption implements EncryptionInterface {

    private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final int KEY_SIZE = 1024;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public RSAEncryption(){
        // Generate RSA key pair
        try {
            KeyPair keyPair = generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(KEY_SIZE);
        return keyGen.generateKeyPair();
    }

    public void encryptFile(String inputFilePath, String outputFilePath) {
        try (FileInputStream fis = new FileInputStream(new File(inputFilePath));
             FileOutputStream fos = new FileOutputStream(new File(outputFilePath))) {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] buffer = new byte[117];
            int bytesRead;
            byte[] output;
            while ((bytesRead = fis.read(buffer)) != -1) {
                output = cipher.doFinal(buffer);
                if (output != null) {
                    fos.write(output);
                }
            }
            fos.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decryptFile(String inputFilePath, String outputFilePath) {
        try (FileInputStream fis = new FileInputStream(new File(inputFilePath));
             FileOutputStream fos = new FileOutputStream(new File(outputFilePath))) {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // need to use buffer of size public key length - 11 (117) for encryption
            // and public key size (128) for decryption.
            byte[] buffer = new byte[128];
            int bytesRead;
            byte[] output;
            while ((bytesRead = fis.read(buffer)) != -1) {
                output = cipher.doFinal(buffer); // Refer: https://security.stackexchange.com/questions/57205/why-is-rsa-decryption-slow
                if (output != null) {
                    fos.write(output);
                }
            }
            fos.close();
            fis.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getKeyLength() {
        return KEY_SIZE;
    }
}
