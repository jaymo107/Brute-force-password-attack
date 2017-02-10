package com.pcap.methods;

import com.pcap.data.Dictionary;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;

/**
 * @author JamesDavies
 * @date 06/02/2017
 * PCAP
 */
public class BruteForce extends BaseAttack implements Runnable {

    private String encryptedData;
    private int chunkId;
    private String[] words;
    private String charset;
    private HashMap<String, Integer> numberSubstitutes;

    public BruteForce(String encryptedData, String[] words, int chunkId) {

        // Call constructor on parent
        super();

        // Constructor
        this.words = words;
        this.chunkId = chunkId;
        this.encryptedData = encryptedData;
        this.charset = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789!@£$%^&*()?<>+{}";
        this.numberSubstitutes = new HashMap<>();

        populateSubstitutes();
    }

    /**
     * Populate the hashmap with numbers and their key values
     */
    private void populateSubstitutes() {
        this.numberSubstitutes.put("o", 0);
        this.numberSubstitutes.put("a", 4);
        this.numberSubstitutes.put("e", 3);
        this.numberSubstitutes.put("i", 1);
        this.numberSubstitutes.put("s", 5);
    }

    /**
     * Reverse a string.
     * e.g. drowssnap -> olleh ...
     */
    private boolean reverseString(String word) {
        System.out.println("Reverse string method called");
        return checkPassword(this.encryptedData, new StringBuilder(word).reverse().toString());
    }

    /**
     * Duplicate a string.
     * e.g. passpass, P4SSP4SS ...
     */
    private boolean duplicateString(String word) {
        System.out.println("Duplicate string method called");
        return checkPassword(this.encryptedData, word + word);
    }

    /**
     * Password could just be a plain number
     *
     * @return boolean
     */
    private boolean checkForNumber() {
        System.out.println("Number method called.");
        for (int i = 0; i < 99999999; i++) {
            if (checkPassword(this.encryptedData, String.valueOf(i))) return true;
        }

        return false;
    }

    /**
     * Append character to a string.
     * e.g. pass0 -> pass1 -> pass@ ...
     */
    private boolean appendCharacter(String word) {

        System.out.println("Append character method called.");

        // First Random character
        for (int i = 0; i < this.charset.length(); i++) {
            String firstWord = word.concat(String.valueOf(this.charset.charAt(i)));
            if (checkPassword(this.encryptedData, firstWord)) return true;

            // Second character
            for (int j = 0; j < this.charset.length(); j++) {
                String secondWord = firstWord.concat(String.valueOf(this.charset.charAt(j)));
                if (checkPassword(this.encryptedData, secondWord)) return true;
            }
        }

        return checkPassword(this.encryptedData, word);
    }


    /**
     * Append integer to a string.
     * e.g. pass0 -> pass1 -> pass2 ...
     */
    private boolean appendNumber(String word) {

        System.out.println("Append number method called.");

        for (int i = 0; i < 99999; i++) {
            if (checkPassword(this.encryptedData, word.concat(String.valueOf(i))))
                return true;
        }

        return false;
    }

    /**
     * Prepend integer to a string.
     * e.g. pass0 -> pass1 -> pass2 ...
     */
    private boolean prependNumber(String word) {

        System.out.println("Prepend number method called.");

        for (int i = 0; i < 99999; i++) {
            if (checkPassword(this.encryptedData, String.valueOf(i).concat(word)))
                return true;
        }

        return false;
    }

    /**
     * Convert letter in the word to lowercase.
     * e.g. pAssword -> paSsword -> ... PASSword -> PASSWord ...
     */
    private boolean capitalize(String word) {

        System.out.println("Capitalize string method called");

        // Check full casings
        if (checkPassword(this.encryptedData, word.toUpperCase()) ||
                checkPassword(this.encryptedData, word.toLowerCase()))
            return true;

        // Initially set the word to lower case
        word = word.toLowerCase();

        for (int i = 0; i < word.length(); i++) {
            String newWord = word.substring(((i - 1 <= 0) ? 0 : i - 1), i).toUpperCase() + word.substring(i);

            if (checkPassword(this.encryptedData, newWord))
                return true;
        }

        return false;
    }

    /**
     * Replace certain letters with numbers.
     * e.g. P4SS -> P45S -> P455 ...
     */
    private boolean numberSubstitution(String word) {

        System.out.println("Number substitution string method called");

        for (int i = 0; i < word.length(); i++) {

            // Check if the current letter (either upper or lowercase) is in the hashmap
            String currentChar = String.valueOf(word.charAt(i));
            String lowercaseChar = currentChar.toLowerCase();
            String uppercaseChar = currentChar.toUpperCase();

            if (this.numberSubstitutes.containsKey(lowercaseChar)) {
                // We can replace it
                String replacement = String.valueOf(this.numberSubstitutes.get(lowercaseChar));
                word = word.replaceAll("[" + uppercaseChar + lowercaseChar + "]", replacement);
            }

            if (checkPassword(this.encryptedData, word))
                return true;
        }

        return checkPassword(this.encryptedData, word);
    }


    private void crack(String[] words) {

        for (String word : words) {

            // For each of the words in the dictionary, apply the filters
            // Try to crack the word on it's own here.
            checkFound(checkPassword(this.encryptedData, word));

            // Filters
//            checkFound(reverseString(word));
//            checkFound(prependNumber(word));
//            checkFound(duplicateString(word));
//            checkFound(capitalize(word));
//            checkFound(numberSubstitution(word));
//            checkFound(appendNumber(word));
//            checkFound(appendCharacter(word));
        }
    }

    private void checkFound(boolean isFound) {
        if (isFound) {
            System.err.println("PASSWORD WAS FOUND BROTHERRRR");
            // TODO: Return the output from the decryption
            Thread.yield();
            System.exit(0);
        }
    }


    public void run() {

        // Check if this is the last thread to be ran, this is where you check the number
        if (this.chunkId == Dictionary.chunkAmount) {
            checkFound(checkForNumber());
            return;
        }

        crack(words);
    }

}