package com.pcap;

import com.pcap.data.Dictionary;
import com.pcap.data.generator.DictionaryGenerator;

public class Main {


    /**
     * 1. Load in the PCAP
     */
    public static void main(String[] args) throws Exception {

        String pcapInput = args[0];
        String dictionaryPath = args[1];

        // Load in a dictionary.txt
        Dictionary dictionary = new Dictionary(dictionaryPath);

        new DictionaryGenerator(dictionary);

        // Initialise the pcap file
        //PcapFile file = new PcapFile(pcapInput);

        // Initialise the cracker
        //PasswordCracker cracker = new PasswordCracker(file, dictionary);

        // Run the cracker to decrypt the messages
        //cracker.crack();
    }
}
