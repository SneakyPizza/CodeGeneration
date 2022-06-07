package io.swagger.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.io.UnsupportedEncodingException;

public class AES {
    private static byte[] key;
    private static SecretKeySpec secret_Key;
    private static final String Known_Key ="DIT_is-de KeY!1!";

    public static void set_Key() {
        MessageDigest sha_1 = null;
        try {
            key = Known_Key.getBytes(StandardCharsets.UTF_8);
            sha_1 = MessageDigest.getInstance("SHA-1");
            key = sha_1.digest(key);
            key = Arrays.copyOf(key, 16);
            secret_Key = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String decrypt(String ToDecrypt) {
        try {
            set_Key();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secret_Key);
            return new String(cipher.doFinal(Base64.getDecoder()
                    .decode(ToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return null;
    }

    public static String encrypt(String ToEncrypt) {
        try {
            set_Key();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret_Key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(ToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return null;
    }

}
