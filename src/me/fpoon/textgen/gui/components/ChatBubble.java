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
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import javax.swing.JPanel;
import me.fpoon.textgen.bot.Bot;

/**
 *
 * @author mariusz
 */
public class ChatBubble extends JPanel {

    /**
     *
     */
    public static final int GAP = 10;

    /**
     *
     */
    public static final int RADIUS = 10;

    /**
     *
     */
    public static final int TAIL   = 10;
    String message = "Masz wiadomość!";
    Paint fill;
    float[] colDist   = {0.0f, 1.0f};
    Color[] botColors = {Color.white, new Color(122, 196, 225)};
    Color[] humanColors = {Color.white, new Color(139, 224, 48)};
    boolean isBot = false;
    
    /**
     *
     */
    public ChatBubble() {
        this("Masz wiadomość!", null);
    }
    
    /**
     *
     * @param msg
     * @param sender
     */
    public ChatBubble(String msg, Object sender) {
        super();
        if (sender instanceof Bot)
            isBot = true;
        setString(msg);
        setBkg(sender);
    }
    
    /**
     *
     * @param sender
     */
    public void setBkg(Object sender) {
        Point2D start = new Point2D.Float(0,0);
        Point2D end   = new Point2D.Float(0,10);
        if (isBot)
            fill = new LinearGradientPaint(start, end, colDist, botColors);
        else
            fill = new LinearGradientPaint(start, end, colDist, humanColors);
    }
    
    /**
     *
     * @param str
     */
    public void setString(String str) {
        message = str;
        updateSize();
    }
    
    void updateSize() {
        int strwidth = getFontMetrics(getFont()).stringWidth(message);
        this.setPreferredSize(new Dimension(strwidth+40, 40));
        this.setBounds(this.getBounds().x, this.getBounds().y, strwidth+TAIL*2+2*RADIUS, 40+GAP);
    }
    
    GeneralPath createShape() {
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        
        path.moveTo(RADIUS,0); // Górna linia
        path.lineTo(getWidth()-RADIUS-1-TAIL, 0); // j.w.
        if (!isBot) {
            path.quadTo(getWidth()-1-TAIL, 0, getWidth()-1-TAIL, RADIUS); // Prawy górny róg
            path.lineTo(getWidth()-1-TAIL, getHeight()-GAP-1-RADIUS); // Prawa linia
            path.quadTo(getWidth()-1-TAIL, getHeight()-GAP-1, getWidth()-1, getHeight()-GAP-1); // Góra dzyndzla
            path.quadTo(getWidth()-1-TAIL-(RADIUS/2), getHeight()-GAP-1, getWidth()-1-TAIL-(RADIUS/2), getHeight()-GAP-1-(RADIUS/2)); // Dół dzyndzla
            path.quadTo(getWidth()-1-TAIL-(RADIUS/2), getHeight()-GAP-1, getWidth()-RADIUS-1-TAIL, getHeight()-GAP-1);
        }
        else {
            path.quadTo(getWidth()-2-TAIL, 0, getWidth()-2-TAIL, RADIUS);
            path.lineTo(getWidth()-2-TAIL, getHeight()-GAP-1-RADIUS); // Prawa linia
            path.quadTo(getWidth()-2-TAIL, getHeight()-2-GAP, getWidth()-RADIUS-2-TAIL, getHeight()-GAP-1); // Prawy dolny róg
        }
        path.lineTo(RADIUS, getHeight()-GAP-1); // Dolna linia
        if (isBot) {
            path.quadTo(RADIUS/2, getHeight()-GAP-1, RADIUS/2, getHeight()-GAP-1-RADIUS/2); // Lewy dolny róg
            path.quadTo(RADIUS/2, getHeight()-GAP-1, -TAIL, getHeight()-GAP-1); // Dół dzyndzla
            path.quadTo(0, getHeight()-GAP-1, 0, getHeight()-GAP-1-RADIUS); // Góra dzyndzla
        }
        else {
            path.quadTo(0, getHeight()-1-GAP, 0, getHeight()-GAP-RADIUS-1); // Lewy dolny róg
        }
        
        path.lineTo(0, RADIUS); // Lewa linia
        path.quadTo(0, 0, RADIUS, 0); // Lewy górny róg
        path.closePath();
        if (isBot)
            path.transform(AffineTransform.getTranslateInstance(TAIL,0));
        return path;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        updateSize();
        Graphics2D g2d = (Graphics2D)g.create();
        GeneralPath bubble = createShape();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.darkGray);
        g2d.fill(bubble.createTransformedShape(AffineTransform.getTranslateInstance(2, 2)));
        g2d.setPaint(fill);
        g2d.fill(bubble);
       // g2d.fillRoundRect(0,0,this.getWidth()-1,this.getHeight()-1-GAP,20,20);
        g2d.setColor(Color.gray);
        //g2d.drawRoundRect(0,0,this.getWidth()-1,this.getHeight()-1-GAP,20,20);
        g2d.draw(bubble);
        g2d.setColor(Color.lightGray);
        g2d.drawString(message, (int) ((isBot == false) ? RADIUS+1+TAIL/2 : RADIUS+1+TAIL*1.5), RADIUS+1+g2d.getFontMetrics().getHeight());
        g2d.setColor(Color.black);
        g2d.drawString(message, (int) ((isBot == false) ? RADIUS+TAIL/2 : RADIUS+TAIL*1.5), RADIUS+g2d.getFontMetrics().getHeight());
        
        g2d.dispose();
    }
}
