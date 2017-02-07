package com.pcap.methods;

/**
 * @author JamesDavies
 * @date 06/02/2017
 * PCAP
 */
public class BruteForce implements Runnable {

    private String data;
    private Thread thread;
    private int chunkId;
    private String[] words;

    public BruteForce(String data, String[] words, int chunkId) {
        // Constructor
        this.words = words;
        this.chunkId = chunkId;
        this.data = data;
    }

    /**
     * Reverse a string.
     * e.g. drowssnap -> olleh ...
     */
    private void reverseString(String word) {

    }

    /**
     * Duplicate a string.
     * e.g. passpass, P4SSP4SS ...
     */
    private void duplicateString(String word) {

    }

    /**
     * Append character to a string.
     * e.g. pass0 -> pass1 -> pass@ ...
     */
    private void appendCharacter(String word) {

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

            /*
             * a -> b -> c -> aa ab a
             */
        }

        return "";

    }

    public void run() {
        System.out.println("Starting Cracking");

        //crack(words);
    }
}
