/*
 * PL:
 * Program stworzony w ramach edukacji akademickiej na Politechnice Warszawskiej
 * Program ten może być rozpowszechniany zgodnie z licencją GPLv3 - tekst licencji dostępny pod adresem http://www.gnu.org/licenses/gpl-3.0.txt
 * 
 * EN:
 * This program was made for educational purposes during my education on Warsaw University of Technology
 * You can redistribute and add changes to the following program under the terms of GPLv3 license (http://www.gnu.org/licenses/gpl-3.0.txt)
 */
package me.fpoon.textgen.gui.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author mariusz
 */
public class Chart extends JPanel {

    /**
     *
     */
    public static class Bar {
        int value;
        String name;
        Color color;
        
        /**
         *
         * @param name
         * @param value
         * @param color
         */
        public Bar(String name, int value, Color color) {
            this.value = value;
            this.name = name;
            setColor(color);
        }
        
        /**
         *
         * @param color
         */
        public void setColor(Color color) {
            this.color = color;
        }
        
        /**
         *
         * @param g
         * @param chart
         */
        public void draw(Graphics2D g, Chart chart) {
            g.setColor(color);
            g.drawRect(value, value, value, value);
        }
    }
    
    List<Bar> bars;
    int chartWidth;
    int chartHeight;
    float upperLimit;
    
    int getBarIndex(Bar bar) {
        return bars.indexOf(bar);
    }
    
    /**
     *
     * @return
     */
    public int getBarWidth() {
        return chartWidth/bars.size();
    }
    
    /**
     *
     * @return
     */
    public int getMaxBarHeight() {
        return chartHeight;
    }
    
    /**
     *
     * @return
     */
    public float getUpperLimit() {
        return upperLimit;
    }
    
    
}
