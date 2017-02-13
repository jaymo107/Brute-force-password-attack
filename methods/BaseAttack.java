package com.pcap.methods;

import com.pcap.misc.events.PasswordCrackedEventListener;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @author JamesDavies
 * @date 07/02/2017
 * PCAP
 */
public class BaseAttack {

    private Cipher cipher;
    private IvParameterSpec ivParameterSpec;
    private SecretKeySpec secretKeySpec;
    private MessageDigest digest;
    private ArrayList<Thread> threads;
    private List<PasswordCrackedEventListener> listeners;

    public BaseAttack() {

        String iv = "1234567891011121";

        try {
            this.listeners = new ArrayList<>();
            this.ivParameterSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            this.digest = MessageDigest.getInstance("SHA-256");

        } catch (Exception e) {
            System.err.println("Failed to initialise the cipher.");
        }
    }

    /**
     * Add a new event listener for the base attack.
     *
     * @param toAdd The listener class.
     */
    public void addListener(PasswordCrackedEventListener toAdd) {
        listeners.add(toAdd);
    }

    /**
     * Check if the password can crack the hash
     *
     * @param encrypted
     * @param password
     * @return
     */
    protected boolean checkPassword(String encrypted, String password) {

        // Convert the password to hash
        byte[] passwordBytes = getHash(password);

        // Decode the data to decrypt
        byte[] encryptedData = Base64.getDecoder().decode(encrypted);

        try {

            // Get the new secret key with the password to check
            this.secretKeySpec = new SecretKeySpec(passwordBytes, "AES");

            // Init the cipher with the new secret key
            cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec, this.ivParameterSpec);

            // Decrypt the file and BASe64 decode it
            byte[] original = cipher.doFinal(encryptedData);

            // Store the result to a string
            String result = new String(original, "UTF-8");

            boolean hasFound = (result.contains("DECRYPTED:"));

            if (hasFound) {
                String output = result.split(":")[1];

                // Write the output to the text file
                writeOutput(output);

                System.err.println("Password found '" + password + "'");

                for (PasswordCrackedEventListener hl : listeners)
                    hl.onPasswordCracked();
            }

            // Check if it contains the words DECRYPTED
            return hasFound;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the hash of the password and retrieve the first 16 bytes
     *
     * @param password
     * @return
     */
    private byte[] getHash(String password) {

        byte[] newBytes = this.digest.digest(password.getBytes());

        return Arrays.copyOfRange(newBytes, 0, 16);
    }

    /**
     * Write the output of the decrypted file to output.txt
     *
     * @param output The message that was decrypted
     */
    private void writeOutput(String output) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write(output);
            writer.close();
        } catch (Exception e) {
            System.err.println("Couldn't write the output to file.");
        }
    }

}
