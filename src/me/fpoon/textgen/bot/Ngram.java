/*
 * PL:
 * Program stworzony w ramach edukacji akademickiej na Politechnice Warszawskiej
 * Program ten może być rozpowszechniany zgodnie z licencją GPLv3 - tekst licencji dostępny pod adresem http://www.gnu.org/licenses/gpl-3.0.txt
 * 
 * EN:
 * This program was made for educational purposes during my education on Warsaw University of Technology
 * You can redistribute and modify the following program under the terms of GPLv3 license (http://www.gnu.org/licenses/gpl-3.0.txt)
 */
package me.fpoon.textgen.bot;

import java.util.*;

/**
 * Klasa ngramu
 * @author mariusz
 */
public class Ngram {
    /**
     * Klasa słowa
     * @author mariusz
     */
    public static class Word {
        public String word;
        public int instances;
        
        public Word(String word, int instances) {
            this.word = word;
            this.instances = instances;
        }
        
        public Word(String word) {
            this(word,1);
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Word))
                return false;   
            return word.equals(((Word)o).word);
        }
        
        @Override
        public int hashCode() {
            return word.hashCode();
        }
        
        @Override
        public String toString() {
            return word + " x" + instances;
        }
    }
    
    List<String> prefixes;
    List<Word> suffixes;
    int length;
    int size;
    int instances;
    
    public Ngram(int length) {
        this.length = length;
        this.size = 0;
        this.instances = 1;
        this.prefixes = new ArrayList<>();
        this.suffixes = new ArrayList<>();
    }
    
    /**
     * Inkrementuje ilość wystąpień danego ngramu
     */
    public void incrementInstances() {
        instances++;
    }
    
    /**
     * Dodaj słowo do ngramu
     * @param str Słowo do dodania
     */
    public void add(String str) {
        if (size < length-1)
            prefixes.add(str);
        else {
            Word word = new Word(str);
            if (suffixes.contains(word)) {
                suffixes.get(suffixes.indexOf(word)).instances++;
            }
            else {
                suffixes.add(word);
            }
        }
        size++;
    }
    
    /**
     * Funkcja zwracająca sufiks
     * @param f Znormalizowany współczynnik
     * @return  Sufiks
     */
    public String getSuffix(float f) {
        String ret = suffixes.get(0).word;
        int a = (int)Math.round(f*(float)instances);
        for (int i = 0, j = 0; i < a; j++)
        {
            ret = suffixes.get(j).word;
            i += suffixes.get(j).instances;
        }
        return ret;
    }
    
    /**
     * Skleja prefiksy do postaci pojedynczego stringa
     * @return Sklejone prefiksy
     */
    String prefToString()
    {
        String ret = "";
        for (String str : prefixes)
            ret += str + " ";
        return ret;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ngram))
            return false;
        String str = ((Ngram)o).prefToString();
        return prefToString().equals(str);
    }

    @Override
    public int hashCode() {
        return prefToString().hashCode();
    }
    
    @Override
    public String toString() {
        String ret = "";
        ret += length;
        ret += "-ngram x" + instances + "| prefs: ";
        for (String str : prefixes)
            ret += " "+ str;
        ret += " | " + suffixes.size() + " suffixes:";
        for (Word word : suffixes)
            ret += " " + word.toString();
        
        return ret;
    }
    
}
