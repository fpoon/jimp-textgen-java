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

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Klasa bota - generatora łańcuchów markowa.
 * @author mariusz
 */
public class Bot implements Serializable{
    Set<String> words;
    List<Ngram> ngrams;
    Random rand;
    int length;
    int totalWords;
    
    /**
     *
     * @param length
     */
    public Bot(int length) {
        this.length = length;
        words = new LinkedHashSet<>();
        ngrams = new ArrayList<>();
        rand = new Random();
    }
    
    /**
     *
     * @return
     */
    public int getLength() {
        return length;
    }
    
    public int getTotalWords() {
        return totalWords;
    }
    
    public int getUniqueWords() {
        return words.size();
    }
    
    public int getNgramsNumber() {
        return ngrams.size();
    }
    
    /**
     * Analizuje tekst trenigowy z ustawionym domyślnie kodowaniem znaków
     * @param path         Ścieżka dostępu do tekstu treningowego
     * @throws IOException Jeśli nie można otworzyć pliku
     */    
    public void analyzeFile(String path) throws IOException {
        analyzeFile(path, Charset.defaultCharset());
    }
    
    /**
     * Analizuje tekst trenigowy z pliku
     * @param path         Ścieżka dostępu do tekstu treningowego
     * @param encoding     Kodowanie pliku
     * @throws IOException Jeśli nie można otworzyć pliku
     */
    public void analyzeFile(String path, Charset encoding) throws IOException {
        analyze(new String(Files.readAllBytes(Paths.get(path)), encoding));
    }
    
    /**
     *
     * @param str
     * @return
     */
    public String deformatString(String str) {
        String ret = "", foo = str.trim().toLowerCase();
        for (char c : foo.toCharArray()) {
            if (Character.getType(c) == Character.LOWERCASE_LETTER)
                ret += c;
            if (Character.getType(c) == Character.DECIMAL_DIGIT_NUMBER)
                ret += c;
        }
        return ret;
    }
    
    /**
     * Analizuje podany łańcuch znaków
     * @param text Łańcuch do analizy
     */
    public void analyze(String text) {
        Ngram[] ngs = new Ngram[length];
        int[]   foo = new int[length];
        String[] parts = text.split("\\s");
        int i = 0, a = 0;
        for (String str : parts) {
            if (str == null)
                continue;
            str = deformatString(str);
            if (str.length() == 0)
                continue;
            add(str);
            if (i < length)
                a = i+1;
            else 
                a = length;
            for (int j = 0; j < a; j++) {
                if (ngs[j] == null) ngs[j] = new Ngram(length);
                ngs[j].add(str);
                foo[j]++;
                if (foo[j] == length) {
                    add(ngs[j]);
                    ngs[j] = null;
                    foo[j] = 0;
                }
                
            }
            i++;
            totalWords++;
            if ((i%10000) == 0)
                System.out.println("Przeanalizowano "+i+" słów");
        }
        
        System.out.println("Zakończono analizę. Wczytano "+i+" słów.");
        /*for (Ngram n : ngrams)
            System.out.println(n);*/
    }
    
    /**
     * Generuje łańcuch Markowa o zadanej długości
     * @param length Długość wygenerowanego łańcucha
     * @return Wygenerowany łancuch
     */
    public String generate(int length) {
        if (ngrams.isEmpty())
            return "";
        String suffix, ret = "";
        Ngram ngram = ngrams.get(rand.nextInt(ngrams.size()));
        for (String str : ngram.prefixes)
            ret += str + " ";
        
        for (int i = 0; i < length && ngram != null; i++) {
            suffix = ngram.getSuffix(rand.nextFloat());
            ret += suffix + " ";
            ngram = nextNgram(ngram, suffix);
            //System.out.println(ngram);
        }
        return ret;
    }
    
    /**
     * Dodaje słowo do bazy bota
     * @param str Słowo do dodania
     */
    public void add(String str) {
        words.add(str);
    }
    
    /**
     * Dodaje ngram do bazy bota
     * @param ngram Ngram do dodania
     */
    public void add(Ngram ngram) {
        int index = ngrams.indexOf(ngram);
        if (index >= 0) {
            Ngram ng = ngrams.get(index);
            ng.incrementInstances();
            ng.add(ngram.suffixes.get(0).word);
        }
        else
            ngrams.add(ngram);
    }
    
    /**
     * Znajduje n-gram na podstawie tablicy słów
     * @param words Tablica słow w chodzących w skład n-gramu
     * @return      Znaleziony n-gram lub null jeśli ngramu brak
     */
    public Ngram getNgram(String words[]) {
        Ngram foo = new Ngram(this.length);
        for (String str : words) {
            foo.add(str);
        }
        int index = ngrams.indexOf(foo);
        if (index < 0) return null;
        System.out.println(ngrams.get(index));
        return ngrams.get(index);
    }
    
    /**
     * Znajduje kolejny n-gram wykorzystując prefiksy ngramu (oprócz pierwszego) i podany sufiks
     * @param ngram  Ngram, z którego lista prefiksów posłuży do poszukiwania kolejnego ngramu
     * @param suffix Sufiks, który będzie ostatnim prefiksem szukanego Ngramu
     * @return       Znaleziony n-gram lub null jeśli ngramu brak
     */
    Ngram nextNgram(Ngram ngram, String suffix) {
        Ngram foo = new Ngram(this.length);
        int index;
        
        for (int i = 1; i < ngram.prefixes.size(); i++) {
            foo.add(ngram.prefixes.get(i));
        }
        foo.add(suffix);
        
        index = ngrams.indexOf(foo);
        if (index < 0) return null;
        return ngrams.get(index);
    }
    
    @Override
    public String toString() {
        String ret = "Zbiór słów bota\n";
        ret += words.toString();
        ret += "\nZbiór ngramów:\n";
        ret += ngrams.toString();
        
        return ret;
    }
}
