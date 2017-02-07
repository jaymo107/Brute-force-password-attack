package com.pcap.methods;

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

    public BruteForce(String encryptedData, String[] words, int chunkId) {

        // Call constructor on parent
        super();

        // Constructor
        this.words = words;
        this.chunkId = chunkId;
        this.encryptedData = encryptedData;
        this.charset = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789!@£$%^&*()?<>+{}";
        this.maxDepth = 3;
    }

    /**
     * Reverse a string.
     * e.g. drowssnap -> olleh ...
     */
    private boolean reverseString(String word) {

        String newString = new StringBuilder(word).reverse().toString();

        return checkPassword(this.encryptedData, newString);
    }

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
    private void lowercase(String word) {

    }

    /**
     * Convert letter in the word to uppercase.
     * e.g. pAssword -> paSsword -> ... PASSword -> PASSWord ...
     */
    private void uppercase(String word) {

    }

    /**
     * Replace certain letters with numbers.
     * e.g. P4SS -> P45S -> P455 ...
     */
    private void numberSubstitution(String word) {

    }


    private String crack(String[] words) {

        for (String word : words) {

            // For each of the words in the dictionary, apply the filters
            // Try to crack the word on it's own here.
            /*
             * a -> b -> c -> aa ab a
             */
        }

        return "";

    }

    public void run() {
        System.out.println("Starting Cracking on chunk #" + this.chunkId);

        //crack(words);
    }
}
