package tus.teamproject.app.domain;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public interface EncryptionInterface {
    public void encryptFile(String inputFilePath, String outputFilePath);

    public void decryptFile(String inputFilePath, String outputFilePath);
}
