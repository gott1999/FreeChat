package edu.xww.urchat.tools;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Encode {

    public static String hash(String inputs) {
        return hash(inputs, "SHA-256");
    }

    public static String hash(String inputs, String algorithm) {
        StringBuilder res = new StringBuilder();

        try {
            MessageDigest instance = MessageDigest.getInstance(algorithm);
            byte[] newBytes = instance.digest(inputs.getBytes(StandardCharsets.UTF_8));

            String temp;
            for (byte b: newBytes) {
                temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) res.append('0');
                res.append(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res.toString();
    }

}
