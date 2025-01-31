package tus.teamproject.app.domain;

public interface EncryptionInterface {
    public byte[] encrypt(byte[] data);
    public byte[] decrypt(byte[] data);
}
