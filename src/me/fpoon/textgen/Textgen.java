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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.swing.JFrame;
import me.fpoon.textgen.bot.Bot;
import me.fpoon.textgen.gui.MainForm;
import me.fpoon.textgen.gui.VisualFrame;

/**
 *
 * @author mariusz
 */
public class Textgen {
    
    public static Bot bot;
    public static VisualFrame visualWnd;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        bot = new Bot(2);
        JFrame mainWnd = (JFrame)new MainForm();
        mainWnd.setVisible(true);
        visualWnd = new VisualFrame();
        visualWnd.setVisible(true);
    }
    
}
