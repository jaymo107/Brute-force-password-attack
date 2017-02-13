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
    private boolean concatThread;
    public static final boolean CONCAT_THREAD = true;
    public static final boolean TRANSFORMATION_THREAD = false;

    public BruteForce(String encryptedData, String[] words, int chunkId, boolean concatThread) {

        // Call constructor on parent
        super();

        // Constructor
        this.words = words;
        this.chunkId = chunkId;
        this.encryptedData = encryptedData;
        this.charset = "!%?@£$&#+-";
        this.concatThread = concatThread;
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
        for (int i = 0; i < 200; i++) {
            if (checkPassword(this.encryptedData, word.concat(String.valueOf(i))) ||
                    checkPassword(this.encryptedData, String.valueOf(i).concat(word))) return true;
        }
        return false;
    }

    /**
     * Concatenate 2 words together
     *
     * @param mainWord
     */
    private boolean concatenateWords(String mainWord) {
        //System.err.println("First word: " + mainWord);
        // Loop over every word that hasn't been modified
        for (String word : this.words) {
            // Check if word contains numbers
            if (!word.matches("[a-zA-Z]+") || mainWord.concat(word).length() > 15) continue;
            checkPassword(this.encryptedData, mainWord.concat(word));
        }
        return false;
    }


    /**
     * Convert letter in the word to lowercase.
     * e.g. pAssword -> paSsword -> ... PASSword -> PASSWord ...
     */
    private boolean capitalize(String word) {

        // Check full casings
        if (checkPassword(this.encryptedData, word.toUpperCase())) return true;

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
            capitalize(word);
            appendOrPrependNumber(word);
        }
    }


    public void run() {

        // Check if this thread is used for concatenation
        if (this.concatThread) {

            for (String word : words) {
                if (!word.matches("[a-zA-Z]+") || word.length() > 10) continue;
                if (concatenateWords(word)) return;
            }
            // If you reach the end of the for loop
            Thread.yield();
            return;
        }

        // Check if this is the last thread to be ran, this is where you check the number
        if (this.chunkId >= Dictionary.chunkAmount) {
            checkForNumber();
            Thread.yield();
            return;
        }

        crack(words);
    }

}