package com.pcap.data.generator;

import com.pcap.data.Dictionary;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author JamesDavies
 * @date 11/02/2017
 * PCAP
 */
public class DictionaryGenerator {

    private String charset;
    private String[] words;
    private HashMap<String, Integer> numberSubstitutes;

    public DictionaryGenerator(Dictionary dictionary) {
        this.charset = "0123456789qwertyuiopasdfghjklzxcvbnm!@£$";
        this.words = dictionary.getWords();
        this.numberSubstitutes = new HashMap<>();

        populateSubstitutes();

        ArrayList<String> generatedWords = new ArrayList<>();

        // Apply these transformations on the words
        for (String word : this.words) {

            word = word.replaceAll("\\s+", "");

            // Check if number is purely numeric
            if (isNumber(word) || isPlural(word)) {
                System.out.println(word);
                continue;
            }

            generatedWords.add(word.toLowerCase());

            for (String subWord : numberSubstitution(word)) {

                // Appended characters
                generatedWords.add(subWord);
            }

            System.out.println("Generint words " + word);
        }

        System.out.println("New words generated: " + generatedWords.size());

        appendWordsToDictionary(generatedWords.toArray(new String[0]));
    }

    private boolean isNumber(String string) {
        try {
            double result = Double.parseDouble(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isPlural(String string) {
        return (string.contains("'s"));
    }

    /**
     * Populate the hashmap with numbers and their key values
     */
    private void populateSubstitutes() {
        this.numberSubstitutes.put("o", 0);
        this.numberSubstitutes.put("a", 4);
        this.numberSubstitutes.put("e", 3);
        this.numberSubstitutes.put("i", 1);
        this.numberSubstitutes.put("s", 5);
        this.numberSubstitutes.put("b", 8);
        this.numberSubstitutes.put("g", 9);
        this.numberSubstitutes.put("t", 7);
        this.numberSubstitutes.put("z", 2);
    }


    /**
     * Replace certain letters with numbers.
     * e.g. P4SS -> P45S -> P455 ...
     */
    private String[] numberSubstitution(String word) {

        if (!word.matches("[a-zA-Z]+")) return new String[0];

        ArrayList<String> words = new ArrayList<>();

        int occurances = 0;
        // Count occurances
        for (int i = 0; i < word.length(); i++) {
            if (this.numberSubstitutes.containsKey(String.valueOf(word.charAt(i)).toLowerCase())) {
                occurances++;
            }
        }
        if (occurances > 3) {
            return new String[0];
        }

        word = word.toLowerCase();

        for (int i = 0; i < word.length(); i++) {

            // Check if the current letter (either upper or lowercase) is in the hashmap
            String currentChar = String.valueOf(word.charAt(i));
            String lowercaseChar = currentChar.toLowerCase();
            String uppercaseChar = currentChar.toUpperCase();

            if (this.numberSubstitutes.containsKey(lowercaseChar)) {
                // We can replace it
                String replacement = String.valueOf(this.numberSubstitutes.get(lowercaseChar));
                word = word.replaceAll("[" + uppercaseChar + lowercaseChar + "]", replacement);
                System.out.println(word);
                words.add(word);
            }
        }

        return words.toArray(new String[0]);
    }

    private void appendWordsToDictionary(String[] words) {
        try {
            FileWriter fw = new FileWriter("newdictionary.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            // Print every word to the new dictionary
            for (String word : words) {
                out.println(word);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
