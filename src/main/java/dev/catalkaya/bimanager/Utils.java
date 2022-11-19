package dev.catalkaya.bimanager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Utils {
    public static final String ADMINISTRATOR_PERSON_ID = "c893a566-4470-4e94-a6f3-607c16753c3a";

    public static String hashSHA3(String s) throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA3-512").digest(s.getBytes(StandardCharsets.UTF_8)));
    }
}
