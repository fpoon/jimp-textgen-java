/*
 * PL:
 * Program stworzony w ramach edukacji akademickiej na Politechnice Warszawskiej
 * Program ten może być rozpowszechniany zgodnie z licencją GPLv3 - tekst licencji dostępny pod adresem http://www.gnu.org/licenses/gpl-3.0.txt
 * 
 * EN:
 * This program was made for educational purposes during my education on Warsaw University of Technology
 * You can redistribute and modify the following program under the terms of GPLv3 license (http://www.gnu.org/licenses/gpl-3.0.txt)
 */
package me.fpoon.textgen;

import java.io.IOException;
import me.fpoon.textgen.bot.Bot;

/**
 *
 * @author mariusz
 */
public class Textgen {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Bot bot = new Bot(2);
        try {
            bot.analyze("test/pantadeusz.txt");
        } catch (IOException e) {
            System.err.println(e);
        }
        
        System.out.println(bot.generate(100));
    }
    
}
