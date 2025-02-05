package tus.teamproject.app.factory;

import tus.teamproject.app.domain.Algorithms;
import tus.teamproject.app.domain.EncryptionInterface;
import tus.teamproject.app.processor.AESGCMEncryption;
import tus.teamproject.app.processor.TripleDESEncryption;

import java.util.Objects;

public class EncryptionAlgorithmFactory {
    public static EncryptionInterface getEncryptionProcessor(Algorithms algorithm) {
        if (Objects.requireNonNull(algorithm) == Algorithms.AES_GCM) {
            return new AESGCMEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.TRIPLE_DES) {
            return new TripleDESEncryption();
        }
        return null;
    }
}
