package tus.teamproject.app.processor;

import tus.teamproject.app.domain.EncryptionInterface;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.util.Base64;

public class DHAESEncryption implements EncryptionInterface {
    private static final int KEY_SIZE = 16;

    private final SecretKey secretKey1;
    private final SecretKey secretKey2;

    public DHAESEncryption() {
        DHParameterSpec dhSpec = generateParams();

        // Generate a DH key pair  (using DHParameterSpec object)
        KeyPair keyPair1 = generateKeyPair(dhSpec);
        PublicKey publicKey1 = keyPair1.getPublic();
        PrivateKey privateKey1 = keyPair1.getPrivate();

        KeyPair keyPair2 = generateKeyPair(dhSpec);
        PublicKey publicKey2 = keyPair2.getPublic();
        PrivateKey privateKey2 = keyPair2.getPrivate();

        // generate symmetric key
        secretKey1 = generateSymKey(privateKey1, publicKey2);
        secretKey2 = generateSymKey(privateKey2, publicKey1);

    }

    public static DHParameterSpec generateParams() {

        try {
            // Create the parameter generator for a 1024-bit DH key pair
            AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
            paramGen.init(1024);

            // Generate the parameters
            AlgorithmParameters params = paramGen.generateParameters();

            return params.getParameterSpec(DHParameterSpec.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyPair generateKeyPair(DHParameterSpec dhSpec ){
        KeyPairGenerator keyGen;
        KeyPair keypair;
        try {
            keyGen = KeyPairGenerator.getInstance("DH");
            keyGen.initialize(dhSpec);
            keypair = keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
        return keypair;
    }

    public static SecretKey generateSymKey(Key privateKey, Key publicKey){
        SecretKey secretKey;
        try {
            KeyAgreement ka = KeyAgreement.getInstance("DH");
            ka.init(privateKey);
            ka.doPhase(publicKey, true);
            byte[] rawValue = ka.generateSecret();
            secretKey = new SecretKeySpec(rawValue, 0, KEY_SIZE, "AES");
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            //System.out.println("Secret key is: " + encodedKey);

        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return secretKey;
    }

    public static byte[] encryptBytes(byte[] objectBytes, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(objectBytes);
    }

    public static byte[] decryptBytes(byte[] encryptedBytes, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return decryptedBytes;
    }

    @Override
    public void encryptFile(String inputFilePath, String outputFilePath) {
        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey1);

            FileInputStream fis = new FileInputStream(inputFilePath);
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            byte[] buffer = new byte[1024];
            int bytesRead;
            byte[] output;
            while ((bytesRead = fis.read(buffer)) != -1) {
                output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                fos.write(outputBytes);
            }
            fis.close();
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void decryptFile(String inputFilePath, String outputFilePath) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey2);

            FileInputStream fis = new FileInputStream(inputFilePath);
            FileOutputStream fos = new FileOutputStream(outputFilePath);

            byte[] buffer = new byte[1024];
            int bytesRead;
            byte[] output;
            while ((bytesRead = fis.read(buffer)) != -1) {
                output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                fos.write(outputBytes);
            }
            fis.close();
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getKeyLength() {
        return KEY_SIZE;
    }
}
