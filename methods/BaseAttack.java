package com.pcap.methods;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author JamesDavies
 * @date 07/02/2017
 * PCAP
 */
public class BaseAttack {

    private Cipher cipher;
    private IvParameterSpec ivParameterSpec;
    private SecretKeySpec secretKeySpec;

    public BaseAttack() {

        String iv = "1234567891011121";

        try {
            this.ivParameterSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (Exception e) {
            System.err.println("Failed to initialise the cipher.");
        }
    }

    /**
     * Check if the password can crack the hash
     *
     * @param encrypted
     * @param password
     * @return
     */
    protected boolean checkPassword(String encrypted, String password) {

        try {
            // Get the new secret key with the password to check
            this.secretKeySpec = new SecretKeySpec(password.getBytes("UTF-8"), "AES");

            // Init the cipher with the new secret key
            cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec, this.ivParameterSpec);

            // Decrypt the file and BASe64 decode it
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            // Store the result to a string
            String result = new String(original, "UTF-8");

            // Check if it contains the words DECRYPTED
            return (result.contains("DECRYPTED:"));

        } catch (Exception e) {
            return false;
        }
    }

}
