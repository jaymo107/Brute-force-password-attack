package com.pcap;

import com.pcap.data.Dictionary;
import com.pcap.methods.BruteForce;
import com.pcap.misc.DisplayElapsedTime;
import com.pcap.misc.events.PasswordCrackedEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * @author JamesDavies
 * @date 05/02/2017
 * PCAP
 */
class PasswordCracker implements PasswordCrackedEventListener {

    private String encryptedData;
    private Dictionary dictionary;
    private ArrayList<Thread> threads;
    private long startTime;

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

        this.threads = new ArrayList<>();
        this.startTime = System.currentTimeMillis();

        // Loop over every chunk and pass the string array for that chunk
        for (int i = 0; i < this.dictionary.getMaxChunks(); i++) {
            String[] words = this.dictionary.getChunk(i);
            // Create new BruteForce object and set the event listener to this class.
            BruteForce attack = new BruteForce(this.encryptedData, words, i);
            attack.addListener(this);
            // Pass the words to the constructor and create a new instance
            Thread thread = new Thread(attack);
            // Add the threads to the arraylist
            threads.add(thread);
        }

        // Thread for calculating the number
        BruteForce numberAttack = new BruteForce(this.encryptedData, null, Dictionary.chunkAmount);
        numberAttack.addListener(this);
        Thread numberGeneratorThread = new Thread(numberAttack);
        threads.add(numberGeneratorThread);

        for (Thread thread : threads) {
            // Start every thread together
            thread.start();
        }

        // Start timer to show elapsed time
        new Timer().scheduleAtFixedRate(new DisplayElapsedTime(), 0, 10000);

    }

    @Override
    public synchronized void onPasswordCracked() {

        // Calculate time difference
        long endTime = System.currentTimeMillis() - startTime;

        // Stop all threads
        for (Thread thread : this.threads) {
            thread.interrupt();
        }

        System.err.println("=============================================================================");
        System.err.println("Password cracked successfully in " + TimeUnit.MILLISECONDS.toMinutes(endTime) + " mins " + (TimeUnit.MILLISECONDS.toSeconds(endTime) - (TimeUnit.MILLISECONDS.toMinutes(endTime) * 60)) + " seconds. Output sent to 'output.txt'");
        System.err.println("=============================================================================");


        System.exit(0);
    }
}