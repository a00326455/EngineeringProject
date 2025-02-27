package tus.teamproject.app.processor;

import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;
import org.bouncycastle.jce.spec.IEKeySpec;
import tus.teamproject.app.domain.EncryptionInterface;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;

/*
ECC (Elliptic Curve Cryptography) is an asymmetric encryption algorithm that provides strong security with
smaller key sizes compared to RSA. It is widely used in modern cryptographic protocols.
 */
public class ECCEncryption implements EncryptionInterface {

    private static final String ALGORITHM = "EC";
    private static final int KEY_SIZE = 2048;
    private KeyPair keyPair;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public ECCEncryption(){
        // Generate ECC key pair
        try {
            keyPair = generateECKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static KeyPair generateECKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, "BC");
        keyPairGenerator.initialize(new ECNamedCurveGenParameterSpec("secp256r1"));
        return keyPairGenerator.generateKeyPair();
    }

    public void encryptFile(String inputFilePath, String outputFilePath) {
        try {
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            IEKeySpec spec = new IEKeySpec(keyPair.getPrivate(), keyPair.getPublic());
            cipher.init(Cipher.ENCRYPT_MODE, spec);

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
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            IEKeySpec spec = new IEKeySpec(keyPair.getPrivate(), keyPair.getPublic());
            cipher.init(Cipher.DECRYPT_MODE, spec);

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
