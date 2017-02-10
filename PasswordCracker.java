package com.pcap;

import com.pcap.data.Dictionary;
import com.pcap.methods.BruteForce;
import com.pcap.misc.DisplayElapsedTime;

import java.util.ArrayList;
import java.util.Timer;

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
        System.out.println(this.encryptedData);
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

        // Thread for calculating the number
        Thread numberGeneratorThread = new Thread(new BruteForce(this.encryptedData, null, Dictionary.chunkAmount));
        threads.add(numberGeneratorThread);
        numberGeneratorThread.start();

        // Start timer to show elapsed time
        //new Timer().scheduleAtFixedRate(new DisplayElapsedTime(), 0, 1000);

    }
}