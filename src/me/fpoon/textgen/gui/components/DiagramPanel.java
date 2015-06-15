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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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

    /**
     *
     */
    public static final int ARROW_WIDTH = 20;

    /**
     *
     */
    public static final int GAP = 10;
    
    /**
     *
     */
    public static class Arrow {
        DiagramBubble parent;
        DiagramBubble child;
        
        /**
         *
         * @param parent
         * @param child
         */
        public Arrow(DiagramBubble parent, DiagramBubble child) {
            setParent(parent);
            setChild(child);
        }
        
        /**
         *
         * @param b
         */
        public void setParent(DiagramBubble b) {
            parent = b;
        }
        
        /**
         *
         * @param b
         */
        public void setChild(DiagramBubble b) {
            child = b;
        }
        
        /**
         *
         * @return
         */
        public boolean isActive() {
            return parent.isActive() && child.isActive();
        }
        
        /**
         *
         * @param g
         */
        public void draw(Graphics2D g) {
            int x, y, x1, y1, x2, y2;
            x = parent.getBounds().x + parent.getBounds().width;
            y = parent.getBounds().y + parent.getBounds().height/2;
            x2 = child.getBounds().x;
            y2 = child.getBounds().y + child.getBounds().height/2;
            x1 = (x2-x)/2 + x;
            y1 = y;
            if (parent.isActive() && child.isActive()) {
                g.setStroke(new BasicStroke(3));
                g.setColor(Color.darkGray);
            } else
                g.setColor(Color.gray);
            g.drawLine(x, y, x1, y1);
            g.drawLine(x1, y1, x1, y2);
            g.drawLine(x1, y2, x2, y2);
            g.setStroke(new BasicStroke(1));
        }
    }
    
    List<DiagramBubble> bubbles;
    List<Arrow> arrows;
    
    /**
     *
     */
    public DiagramPanel() {
        super();
        setBackground(Color.white);
        setLayout(null);
        arrows = new ArrayList<>();
    }
    
    /**
     *
     * @param s
     * @param parent
     * @return
     */
    public DiagramBubble addBubble(String s, DiagramBubble parent) {
        return addBubble(s, parent, null);
    }
    
    /**
     *
     * @param s
     * @param parent
     * @param extra
     * @return
     */
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
        height = this.getPreferredSize().height > height ? this.getPreferredSize().height : height;
        this.setPreferredSize(new Dimension(width, height));
        
        return bubble;
    }
    
    /**
     *
     * @param output
     * @param bot
     */
    public void displayOutput(String output, Bot bot) {
        removeAll();
        arrows.clear();
        //setPreferredSize(new Dimension(0,0));
        if (output == null) return;

        String [] words = output.split(" ");
        
        DiagramBubble parent = null;
        Ngram seed = bot.getNgram(words);
        if (seed == null) return;
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
        List<Arrow> selected = new LinkedList<>();
        for (Arrow arrow : arrows) {
            if (arrow.isActive())
                selected.add(arrow);
            else
                arrow.draw(g2d);
        }
        
        for (Arrow arrow : selected) {
            arrow.draw(g2d);
        }
        g2d.dispose();
    }
    
}
