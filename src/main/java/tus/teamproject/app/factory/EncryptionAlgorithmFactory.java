package tus.teamproject.app.factory;

import tus.teamproject.app.domain.Algorithms;
import tus.teamproject.app.domain.EncryptionInterface;
import tus.teamproject.app.processor.AESGCMEncryption;

import java.util.Objects;

public class EncryptionAlgorithmFactory {
    public static EncryptionInterface getEncryptionProcessor(Algorithms algorithm) {
        if (Objects.requireNonNull(algorithm) == Algorithms.AES_GCM) {
            return new AESGCMEncryption();
        }
        return null;
    }
}
