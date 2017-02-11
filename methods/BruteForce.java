package com.pcap.methods;

import com.pcap.data.Dictionary;

import java.util.ArrayList;
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
        this.charset = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm!@£$";
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
        for (int i = 0; i < 999999; i++) {
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
//
//            // Second character
//            for (int j = 0; j < this.charset.length(); j++) {
//                String secondWord = firstWord.concat(String.valueOf(this.charset.charAt(j)));
//                if (checkPassword(this.encryptedData, secondWord)) return true;
//            }
        }

        return false;
    }

    /**
     * Prepend integer to a string.
     * e.g. pass0 -> pass1 -> pass2 ...
     */
    private String[] prependOrAppendNumber(String word) {

        //System.out.println("Prepend number method called.");

        ArrayList<String> words = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            words.add(String.valueOf(i).concat(word));
            words.add(word.concat(String.valueOf(i)));
            System.out.println(String.valueOf(i).concat(word));
            System.out.println(word.concat(String.valueOf(i)));
        }

        return words.toArray(new String[0]);
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


    private void crack(String[] words) {

        for (String word : words) {

            // For each of the words in the dictionary, apply the filters
            // Try to crack the word on it's own here.
            checkFound(checkPassword(this.encryptedData, word));

            // Filters
            //checkFound(prependNumber(word));
            checkFound(duplicateString(word));
            checkFound(capitalize(word));
            //checkFound(appendNumber(word));
            //checkFound(appendCharacter(word));
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