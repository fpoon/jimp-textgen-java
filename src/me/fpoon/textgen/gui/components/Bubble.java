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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author mariusz
 */
public class Bubble extends JPanel {
    String message = "Masz wiadomość!";
    
    
    public void setString(String str) {
        message = str;
        updateSize();
    }
    
    void updateSize() {
        int strwidth = getFontMetrics(getFont()).stringWidth(message);
        this.setPreferredSize(new Dimension(strwidth+40, 40));
        this.setBounds(this.getBounds().x, this.getBounds().y, strwidth+40, 40);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        updateSize();
        Graphics2D g2d = (Graphics2D)g.create();
        
        g2d.setColor(Color.cyan);
        g2d.fillRoundRect(0,0,this.getWidth()-1,this.getHeight()-1,20,20);
        g2d.setColor(Color.black);
        g2d.drawRoundRect(0,0,this.getWidth()-1,this.getHeight()-1,20,20);
        g2d.drawString(message, 20, 20);
        
        g2d.dispose();
    }
}
