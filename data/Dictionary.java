package com.pcap.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author JamesDavies
 * @date 06/02/2017
 * PCAP
 */
public class Dictionary {

    private String[] words;
    private int chunkSize;
    public static final int chunkAmount = 127;
    private int totalChunks;

    /**
     * Load in the text file
     *
     * @param path The path to the dictionary.txt file
     */
    public Dictionary(String path) {

        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str;

            List<String> list = new ArrayList<>();

            // Limit the amount of passwords to read in
            while ((str = in.readLine()) != null) {
                // Add password to the dictionary.txt
                list.add(str);
            }

            this.words = list.toArray(new String[0]);
            this.chunkSize = (int) Math.ceil(list.size() / this.chunkAmount);
            this.totalChunks = (int) Math.ceil(this.words.length / this.chunkSize);

        } catch (Exception e) {
            System.err.println("Couldn't load in dictionary.txt file.");
        }
    }

    /**
     * Return the words in the dictionary.txt
     *
     * @return String[]
     */
    public String[] getWords() {
        return this.words;
    }

    /**
     * Get the dictionary fragment between 2 chunks
     *
     * @param chunk
     * @return
     */
    public String[] getChunk(int chunk) {
        return Arrays.copyOfRange(this.getWords(), this.chunkSize * chunk, (this.chunkSize * chunk) + chunkSize);
    }

    /**
     * Check if there is a next chunk available
     *
     * @param chunk The chunk to check
     * @return boolean
     */
    public boolean hasNext(int chunk) {
        return (getChunk(chunk).length <= 0);
    }

    /**
     * Return the maximum amount of chunks for this dictionary.
     *
     * @return
     */
    public int getMaxChunks() {
        return this.totalChunks;
    }
}
