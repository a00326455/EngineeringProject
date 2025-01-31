package tus.teamproject.app.utils;

public class EncodingUtils {
    public static String base64Encode(byte[] data) {
        return java.util.Base64.getEncoder().encodeToString(data);
    }

    public static byte[] base64Decode(String data) {
        return java.util.Base64.getDecoder().decode(data);
    }
}
