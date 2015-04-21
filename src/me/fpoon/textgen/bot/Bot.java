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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Klasa bota - generatora łańcuchów markowa.
 * @author mariusz
 */
public class Bot {
    Set<String> words;
    List<Ngram> ngrams;
    Random rand;
    int length;
    
    public Bot(int length) {
        this.length = length;
        words = new LinkedHashSet<>();
        ngrams = new ArrayList<>();
        rand = new Random();
    }
    
    /**
     * Analizuje tekst trenigowy z ustawionym domyślnym kodowanie znaków
     * @param path         Ścieżka dostępu do tekstu treningowego
     * @throws IOException Jeśli nie można otworzyć pliku
     */    
    public void analyze(String path) throws IOException {
        analyze(path, Charset.defaultCharset());
    }
    
    /**
     * Analizuje tekst trenigowy
     * @param path         Ścieżka dostępu do tekstu treningowego
     * @param encoding     Kodowanie pliku
     * @throws IOException Jeśli nie można otworzyć pliku
     */
    public void analyze(String path, Charset encoding) throws IOException {
        Ngram[] ngs = new Ngram[length];
        int[]   foo = new int[length];
        String text = new String(Files.readAllBytes(Paths.get(path)), encoding);
        String[] parts = text.split("\\s");
        int i = 0, a = 0;
        for (String str : parts) {
            if (str == null)
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
            if ((i%10000) == 0)
                System.out.println("Przeanalizowano "+i+" słów");
        }
        
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
        if (ngrams.contains(ngram)) {
            Ngram ng = ngrams.get(ngrams.indexOf(ngram));
            ng.incrementInstances();
            ng.add(ngram.suffixes.get(0).word);
        }
        else
            ngrams.add(ngram);
    }
    
    /**
     * Znajduje kolejny n-gram wykorzystując n-2 ostatnich prefiksów ngramu i podany sufiks
     * @param ngram Ngram, z którego n-2 ostatnich prefiksów tworzy wyszukiwanie
     * @param suffix Sufiks, który będzie ostatnim prefiksem szukanego Ngramu
     * @return 
     */
    Ngram nextNgram(Ngram ngram, String suffix) {
        Ngram foo = new Ngram(this.length);
        int index;
        
        for (int i = 2; i < ngram.prefixes.size(); i++) {
            foo.add(ngram.prefixes.get(i));
        }
        foo.add(suffix);
        
        index = ngrams.indexOf(foo);
        if (index < 0) return null;
        return ngrams.get(index);
    }
}
