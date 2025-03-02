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
        } else if (Objects.requireNonNull(algorithm) == Algorithms.BLOWFISH) {
            return new BlowfishEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.IDEA) {
            return new IDEAEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.CHACHA20) {
            return new ChaCha20Encryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.SERPENT) {
            return new SerpentEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.CAMELLIA) {
            return new CamelliaEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.RSA) {
            return new RSAEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.ECC) {
            return new ECCEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.DH_AES) {
            return new DHAESEncryption();
        } else if (Objects.requireNonNull(algorithm) == Algorithms.RSA_AES) {
            return new RSAAESEncryption();
        }
        return null;
    }
}
