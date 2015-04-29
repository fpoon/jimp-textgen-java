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
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import me.fpoon.textgen.bot.Bot;
import me.fpoon.textgen.bot.Ngram;
import me.fpoon.textgen.bot.Ngram.Word;

/**
 *
 * @author mariusz
 */
public class DiagramPanel extends JPanel {
    public static final int ARROW_WIDTH = 20;
    public static final int GAP = 10;
    
    public static class Arrow {
        DiagramBubble parent;
        DiagramBubble child;
        
        public Arrow(DiagramBubble parent, DiagramBubble child) {
            setParent(parent);
            setChild(child);
        }
        
        public void setParent(DiagramBubble b) {
            parent = b;
        }
        
        public void setChild(DiagramBubble b) {
            child = b;
        }
        
        public void draw(Graphics2D g) {
            int x, y, x1, y1;
            x = parent.getBounds().x + parent.getBounds().width;
            y = parent.getBounds().y + parent.getBounds().height/2;
            x1 = child.getBounds().x;
            y1 = child.getBounds().y + child.getBounds().height/2;
            if (parent.isActive() && child.isActive())
                g.setColor(Color.darkGray);
            else
                g.setColor(Color.gray);
            g.drawLine(x, y, x1, y1);
        }
    }
    
    List<DiagramBubble> bubbles;
    List<Arrow> arrows;
    
    public DiagramPanel() {
        super();
        setBackground(Color.white);
        setLayout(null);
        arrows = new ArrayList<>();
    }
    
    public DiagramBubble addBubble(String s, DiagramBubble parent) {
        return addBubble(s, parent, null);
    }
    
    public DiagramBubble addBubble(String s, DiagramBubble parent, String extra) {
        DiagramBubble bubble = new DiagramBubble(s, parent);
        bubble.setExtra(extra);
        int x = 0, y = 0;
        int width = this.getFontMetrics(getFont()).stringWidth(s)+10;
        if (extra != null)
            width += this.getFontMetrics(getFont()).stringWidth(extra)+10;
        int height = this.getFontMetrics(getFont()).getHeight()+10;
        
        if (parent != null) {
            parent.add(bubble);
            x = parent.getBounds().x + parent.getBounds().width + ARROW_WIDTH;
            y = parent.getBounds().y + (parent.getBounds().height + GAP)*parent.indexOf(bubble);
            arrows.add(new Arrow(parent, bubble));
        }
        
        bubble.setBounds(x, y, width, height);
        
        this.add(bubble);
        
        if (parent != null)
            parent.resizeChildren();
        
        width = bubble.getX()+bubble.getWidth();
        width = getWidth() > width ? getWidth() : width;
        height = bubble.getY()+bubble.getHeight();
        height = getHeight() > width ? getHeight() : height;
        this.setPreferredSize(new Dimension(width, height));
        
        return bubble;
    }
    
    public void displayOutput(String output, Bot bot) {
        if (output == null) return;
        removeAll();
        arrows.clear();
        String [] words = output.split(" ");
        
        DiagramBubble parent = null;
        Ngram seed = bot.getNgram(words);
        for (String str : seed.getPrefixes()) {
                parent = addBubble(str, parent, "seed");
                parent.activate(true);
        }
        do {
            //System.out.println(Arrays.toString(words));

            for (Word suf : seed.getSuffixes()) {
                String extra = String.format("%.1f%%", ((float)suf.instances/(float)seed.getInstances())*100);
                addBubble(suf.word, parent, extra);
            }
            parent = parent.get(words[bot.getLength()-1]);
            parent.activate(true);
            
            words = Arrays.copyOfRange(words, 1, words.length);
            //System.out.println("Mod: "+ Arrays.toString(words));
            seed = bot.getNgram(words);
        } while (words.length != bot.getLength()-1);
        
        this.repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Arrow arrow : arrows) {
            arrow.draw(g2d);
        }
        g2d.dispose();
    }
    
}
