package com.pcap.methods;

import com.pcap.data.Dictionary;


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

    public BruteForce(String encryptedData, String[] words, int chunkId) {

        // Call constructor on parent
        super();

        // Constructor
        this.words = words;
        this.chunkId = chunkId;
        this.encryptedData = encryptedData;
        this.charset = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm!?@£$";
    }

    /**
     * Duplicate a string.
     * e.g. passpass, P4SSP4SS ...
     */
    private boolean duplicateString(String word) {
        return checkPassword(this.encryptedData, word + word);
    }

    /**
     * Password could just be a plain number
     *
     * @return boolean
     */
    private boolean checkForNumber() {
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
        // First Random character
        for (int i = 0; i < this.charset.length(); i++) {
            String firstWord = word.concat(String.valueOf(this.charset.charAt(i)));
            if (checkPassword(this.encryptedData, firstWord)) return true;
        }

        return false;
    }

    /**
     * Append or prepend a number to the string,
     * e.g. pass0, 0pass, pass1, 1pass etc ...
     */
    private boolean appendOrPrependNumber(String word) {
        // First Random character
        for (int i = 0; i < 99; i++) {
            if (checkPassword(this.encryptedData, word.concat(String.valueOf(i)))) return true;
            if (checkPassword(this.encryptedData, String.valueOf(i).concat(word))) return true;
        }

        return false;
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


    private void crack(String[] words) {

        for (String word : words) {

            // For each of the words in the dictionary, apply the filters
            // Try to crack the word on it's own here.
            checkPassword(this.encryptedData, word);
            // Filters
            appendCharacter(word);
            duplicateString(word);
            capitalize(word);
            appendOrPrependNumber(word);
        }
    }


    public void run() {

        // Check if this is the last thread to be ran, this is where you check the number
        if (this.chunkId >= Dictionary.chunkAmount) {
            checkForNumber();
            return;
        }

        crack(words);
    }

}