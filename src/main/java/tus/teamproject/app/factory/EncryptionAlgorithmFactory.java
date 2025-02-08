package tus.teamproject.app.factory;

import tus.teamproject.app.domain.Algorithms;
import tus.teamproject.app.domain.EncryptionInterface;
import tus.teamproject.app.processor.*;

import java.util.Objects;

public class EncryptionAlgorithmFactory {
    public static EncryptionInterface getEncryptionProcessor(Algorithms algorithm) {
        if (Objects.requireNonNull(algorithm) == Algorithms.AES_GCM) {
            return new AESGCMEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.TRIPLE_DES) {
            return new TripleDESEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.PBE_DES) {
            return new PBEDESEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.TWOFISH) {
            return new TwofishEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.IDEA) {
            return new IDEAEncryption();
        }
        return null;
    }
}
