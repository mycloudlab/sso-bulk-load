package org.acme;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PBKDF2 {


    public static String[] hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 27500;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
        String [] result = new String[2];
       
        KeySpec spec = new PBEKeySpec(chars, salt, iterations, 512);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash = skf.generateSecret(spec).getEncoded();

        result[0] = Base64.getEncoder().encodeToString(salt);
        result[1] = Base64.getEncoder().encodeToString(hash);
        
        return result;
        //return iterations + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }




    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);

        return salt;
    }
}
