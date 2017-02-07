package com.pcap;

import com.pcap.data.Dictionary;
import com.pcap.methods.BruteForce;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author JamesDavies
 * @date 05/02/2017
 * PCAP
 */
class PasswordCracker {

    private String encryptedData;
    private Dictionary dictionary;

    PasswordCracker(PcapFile file, Dictionary dictionary) {
        this.encryptedData = file.read();
        this.dictionary = dictionary;
    }

    public void crack() {

        // Run each of the crack methods in their own threads
        // TODO: Crack the password here

        System.out.println("Max chunks: " + this.dictionary.getMaxChunks());
        System.out.println("First chunk length: " + this.dictionary.getChunk(0).length);

        ArrayList<Thread> threads = new ArrayList<>();

        // Loop over every chunk and pass the string array for that chunk
        for (int i = 0; i < this.dictionary.getMaxChunks(); i++) {

            String[] words = this.dictionary.getChunk(i);

            // Pass the words to the constructor and create a new instance
            Thread thread = new Thread(new BruteForce(this.encryptedData, words, i));

            threads.add(thread);
            thread.start();

        }

        // Write the result to file
        writeOutput("No Matches found");
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