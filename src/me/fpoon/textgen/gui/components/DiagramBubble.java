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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author mariusz
 */
public class DiagramBubble extends JPanel {

    static final Color[] BKG_ACTIVE = {Color.white, new Color(122, 196, 225)};
    static final Color[] BKG_INACTIVE = {Color.white, new Color(255, 211, 125)};
    static final Color[] BKG_EXTRA = {Color.white, new Color(127, 127, 255)};
    static final Color[] BKG_INEXTRA = {Color.white, new Color(255, 194, 48)};
    static final float[] colDist = {0.0f, 1.0f};
    Paint bkgExtra;

    String text;
    String extra;
    DiagramBubble parent;
    List<DiagramBubble> children;
    Paint fill;
    boolean active;
    int childWidth;

    public DiagramBubble(String s, DiagramBubble parent) {
        super();
        text = s;
        childWidth = 0;
        children = new ArrayList<>();
        this.parent = parent;
        activate(false);
        setOpaque(false);
        setExtra(null);
    }

    public void setExtra(String s) {
        extra = s;
    }

    public String getText() {
        return text;
    }

    public void add(DiagramBubble b) {
        children.add(b);
    }

    public void resizeChildren() {
        childWidth = 0;
        for (DiagramBubble child : children) {
            if (child.getWidth() > childWidth) {
                childWidth = child.getWidth();
            }
        }

        for (DiagramBubble child : children) {
            child.setBounds(child.getX(), child.getY(), childWidth, child.getHeight());
        }
    }

    public int getChildWidth() {
        return childWidth;
    }

    public int indexOf(DiagramBubble b) {
        return children.indexOf(b);
    }

    public int indexOf(String s) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getText().equals(s)) {
                return i;
            }
        }
        return -1;
    }

    public DiagramBubble get(String str) {
        int index = indexOf(str);
        if (index < 0) {
            return null;
        }
        return children.get(index);
    }

    public boolean contains(String str) {
        for (DiagramBubble bubble : children) {
            if (bubble.getText().equals(bubble)) {
                return true;
            }
        }
        return false;
    }

    public void activate(boolean a) {
        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float(0, 10);
        active = a;
        if (a) {
            fill = new LinearGradientPaint(start, end, colDist, BKG_ACTIVE);
            bkgExtra = new LinearGradientPaint(start, end, colDist, BKG_EXTRA);
        } else {
            fill = new LinearGradientPaint(start, end, colDist, BKG_INACTIVE);
            bkgExtra = new LinearGradientPaint(start, end, colDist, BKG_INEXTRA);
        }
    }

    public boolean isActive() {
        return active;
    }

    void drawTextShadow(int x, int y, Graphics2D g) {
        //g.drawString(text, x + 1, y - 1);
        g.drawString(text, x + 1, y + 1);
        //g.drawString(text, x - 1, y - 1);
        //g.drawString(text, x - 1, y + 1);

        //g.drawString(text, x + 1, y);
        //g.drawString(text, x - 1, y);
        //g.drawString(text, x, y + 1);
        //g.drawString(text, x, y - 1);
    }

    public void drawBubble(int width, int height, Graphics2D g) {
        int radius = getHeight();
        int strHeight = g.getFontMetrics().getHeight();
        int strWidth = g.getFontMetrics().stringWidth(text);
        g.setPaint(fill);
        g.fillRoundRect(0, 0, width - 1, height - 1, radius, radius);
        if (isActive()) {
            g.setColor(Color.darkGray);
        } else {
            g.setColor(Color.gray);
        }
        g.drawRoundRect(0, 0, width - 1, height - 1, radius, radius);
        g.setColor(Color.lightGray);
        drawTextShadow((width - strWidth) / 2, (height + strHeight / 2) / 2, g);
        g.setColor(Color.black);
        g.drawString(text, (width - strWidth) / 2, (height + strHeight / 2) / 2);
    }

    void drawExtra(Graphics2D g) {
        int radius = getHeight();
        int strHeight = g.getFontMetrics().getHeight();
        int strWidth = g.getFontMetrics().stringWidth(extra);
        g.setPaint(bkgExtra);
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        if (isActive()) {
            g.setColor(Color.darkGray);
        } else {
            g.setColor(Color.gray);
        }

        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g.setColor(Color.white);
        g.drawString(extra, getWidth() - strWidth - radius / 3, (getHeight() + strHeight / 2) / 2);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (extra == null) {
            drawBubble(getWidth(), getHeight(), g2d);
        } else {
            drawExtra(g2d);
            drawBubble(getWidth() - g.getFontMetrics().stringWidth(extra) - getHeight() / 2, getHeight(), g2d);
        }

        g2d.dispose();
    }
}
