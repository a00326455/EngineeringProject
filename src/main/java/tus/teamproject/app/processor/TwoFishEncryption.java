package tus.teamproject.app.processor;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import tus.teamproject.app.domain.EncryptionInterface;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

public class TwoFishEncryption implements EncryptionInterface {
    private final String key = "MySecretKey";
    @Override
    public byte[] encrypt(byte[] data) {

        Security.addProvider(new BouncyCastleProvider());

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "Twofish");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("Twofish/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] encryptedData) {
        Security.addProvider(new BouncyCastleProvider());

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "Twofish");
        try {
            Cipher cipher = Cipher.getInstance("Twofish/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return cipher.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
