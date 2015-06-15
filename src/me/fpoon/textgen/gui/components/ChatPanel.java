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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import me.fpoon.textgen.bot.Bot;

/**
 *
 * @author mariusz
 */
public class ChatPanel extends JPanel {
    int totalLength = 0;
    int width;

    /**
     *
     */
    public List<JPanel> panels;
    
    /**
     *
     */
    public ChatPanel() {
        super();
        //setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setLayout(null);
        resetBubbles();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ((ChatPanel)e.getComponent()).refresh();
            }
        });
        refresh();
    }
    
    /**
     *
     */
    public final void resetBubbles() {
        this.removeAll();
        panels = new ArrayList<>();
        this.setPreferredSize(new Dimension(0,0));
        this.revalidate();
    }
    
    /**
     *
     */
    public void grow() {
        setPreferredSize(new Dimension(0,totalLength));
        JScrollPane pane = (JScrollPane)SwingUtilities.getAncestorOfClass(JScrollPane.class, this);
        if (pane != null)  {
            pane.getVerticalScrollBar().setValue(totalLength);
        }
        
    }
    
    /**
     *
     * @param msg
     * @param sender
     */
    public void addMessage(String msg, Object sender) {
        if (sender instanceof Bot)
            System.out.print("<bot> ");
        System.out.println(msg);
        ChatBubble bubble = new ChatBubble(msg, sender);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        if (sender instanceof Bot)
            panel.add(bubble, BorderLayout.CENTER);
        else
            panel.add(bubble, BorderLayout.EAST);
        panel.setBounds(0, totalLength, getWidth() , bubble.getHeight());
        this.add(panel);
        panels.add(panel);
        totalLength += bubble.getHeight();
        grow();
        this.revalidate();
        //refresh();
    }
    
    /**
     *
     */
    public void refresh() {
        width = getWidth();
        int y = 0;
        for (JPanel panel : panels) {
            ChatBubble bubble = (ChatBubble)panel.getComponent(0);
            bubble.updateSize();
            panel.setBounds(0, y, getWidth(), bubble.getHeight());
            y += bubble.getHeight();  
        }
        totalLength = y;
        grow();
        this.revalidate();
    }
    
    public void clear() {
        resetBubbles();
        refresh();
        this.repaint();
    }
}
