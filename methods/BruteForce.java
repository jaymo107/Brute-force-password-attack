package com.pcap.methods;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JamesDavies
 * @date 06/02/2017
 * PCAP
 */
public class BruteForce extends BaseAttack implements Runnable {

    private String encryptedData;
    private Thread thread;
    private int chunkId;
    private String[] words;
    private String charset;
    private int maxDepth;
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
        this.maxDepth = 3;

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

        String newString = new StringBuilder(word).reverse().toString();

        return checkPassword(this.encryptedData, newString);
    }

//    private boolean randomNumber() {
//
//    }

    /**
     * Duplicate a string.
     * e.g. passpass, P4SSP4SS ...
     */
    private boolean duplicateString(String word) {
        return checkPassword(this.encryptedData, word + word);
    }

    /**
     * Append character to a string.
     * e.g. pass0 -> pass1 -> pass@ ...
     */
    private boolean appendCharacter(String word, int depth) {

        String newWord = word + this.charset.toCharArray()[0];

        return checkPassword(this.encryptedData, newWord);


    }


    /**
     * Convert letter in the word to lowercase.
     * e.g. pAssword -> paSsword -> ... PASSword -> PASSWord ...
     */
    private boolean capitalize(String word) {

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

            if(checkPassword(this.encryptedData, word))
                return true;

        }

        return checkPassword(this.encryptedData, word);
    }


    private String crack(String[] words) {

        for (String word : words) {

            // For each of the words in the dictionary, apply the filters
            // Try to crack the word on it's own here.

            checkFound(reverseString(word));

            checkFound(duplicateString(word));

            checkFound(capitalize(word));

            checkFound(numberSubstitution(word));

            /*
             * a -> b -> c -> aa ab a
             */
        }

        return "";

    }

    private void checkFound(boolean isFound) {
        if (isFound) {
            System.err.println("PASSWORD WAS FOUND BROTHERRRR");
            System.exit(0);
        }
    }

    public void run() {
        //System.out.println("Starting Cracking on chunk #" + this.chunkId);

        //permuteString("", "james");
        //
        crack(words);
    }
}
