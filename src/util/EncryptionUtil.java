package util;

public class EncryptionUtil {
    private static final int SHIFT = 2;

    public static String encrypt(String text) {
        if (text == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                char encrypted = (char) ((c - base + SHIFT) % 26 + base);
                result.append(encrypted);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String decrypt(String text) {
        if (text == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                char decrypted = (char) ((c - base - SHIFT + 26) % 26 + base);
                result.append(decrypted);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
