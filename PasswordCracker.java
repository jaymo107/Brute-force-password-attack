package com.pcap;

import com.pcap.data.Dictionary;
import com.pcap.methods.BaseAttack;
import com.pcap.methods.BruteForce;
import com.pcap.misc.DisplayElapsedTime;
import com.pcap.misc.events.PasswordCrackedEventListener;

import java.util.ArrayList;
import java.util.Timer;

/**
 * @author JamesDavies
 * @date 05/02/2017
 * PCAP
 */
class PasswordCracker implements PasswordCrackedEventListener {

    private String encryptedData;
    private Dictionary dictionary;
    private ArrayList<Thread> threads;

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

        // Loop over every chunk and pass the string array for that chunk
        for (int i = 0; i < this.dictionary.getMaxChunks(); i++) {

            String[] words = this.dictionary.getChunk(i);


            BruteForce attack = new BruteForce(this.encryptedData, words, i);
            attack.addListener(this);


            // Pass the words to the constructor and create a new instance
            Thread thread = new Thread(attack);

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

    @Override
    public synchronized void passwordCracked() {
        // Called when a password has been cracked
        // Clean up all of the threads
        System.out.println("This called from the event listener!");

        // Stop all threads
        for (Thread thread : this.threads) {
            thread.interrupt();
        }
    }
}