package tus.teamproject.app.factory;

import tus.teamproject.app.domain.Algorithms;
import tus.teamproject.app.domain.EncryptionInterface;
import tus.teamproject.app.processor.TwoFishEncryption;

import java.util.Objects;

public class EncryptionAlgorithmFactory {
    public static EncryptionInterface getEncryptionProcessor(Algorithms algorithm) {
        if (Objects.requireNonNull(algorithm) == Algorithms.TWOFISH) {
            return new TwoFishEncryption();
        }
        return null;
    }
}
