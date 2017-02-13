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
    public static final int chunkAmount = 95;
    // The number of chunks for the concatenation threads
    public static final int concatChunks = 32;

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
            this.chunkSize = (int) Math.ceil(list.size() / chunkAmount);
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
     * Get the dictionary fragment between 2 chunks specifying the chunk size.
     *
     * @param chunk
     * @return
     */
    public String[] getChunk(int chunk, int size) {
        return Arrays.copyOfRange(this.getWords(), size * chunk, (size * chunk) + size);
    }

    public String[] getWordsBetween(int start, int end) {
        return Arrays.copyOfRange(this.getWords(), start, end);
    }

    /**
     * Return the maximum amount of chunks for this dictionary.
     *
     * @return
     */
    public int getMaxChunks() {
        return this.totalChunks;
    }

    public int getChunkSize(int maxChunks) {
        return (int) Math.ceil(this.words.length / maxChunks);
    }

    /**
     * Return the maximum amount of chunks for this dictionary.
     *
     * @return
     */
    public int getMaxChunks(int size) {
        int newsize = (int) Math.ceil(this.words.length / size);
        return (int) Math.ceil(this.words.length / newsize);
    }
}
