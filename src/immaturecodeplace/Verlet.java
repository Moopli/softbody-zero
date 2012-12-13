/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;



/**
 * A class which implements a Verlet-integrated particle. Verlet integration is
 * a method which involves replacing velocity equations by simplified
 * integrations thereof. to read more, see 
 * <a href="http://www.gamasutra.com/view/feature/2904/advanced_character_physics.php">this</a>
 *
 *
 *
 * @author Filip
 *
 */
public class Verlet extends Point3D{
    
    public double x_, y_, z_, xa, ya, za, mass, t;
    public boolean held = false, motile = true;

    public Verlet(int xpos, int ypos) {
        this(xpos, ypos, 0);
    }

    public Verlet(int xpos, int ypos, int zpos) {
        x = xpos; 
        y = ypos;
        x_ = xpos;
        y_ = ypos;
        z = zpos;
        z_ = zpos;
        za = 0;
        xa = 0;
        ya = 0.00;
        mass = 1;
        t = System.currentTimeMillis();
    }
    
    /* 
     * All of the following is optimized for a 2-D system; and must be modified 
     * in keeping with the whole 3D perspective business.
     * 
     * Mouse calculations will have to use the camera as an intermediary; 
     * meaning that verlets will no longer listen for mouse events, instead 
     * having cursor 'projection axis' data available.
     * 
     * -- now of course, the verlets no longer listen to the mouse events; 
     *      leaving one more step...a big one though
     */
    
    public boolean mousePressed(MouseEvent e) {
        if (Math.hypot(x - e.getX(), y - e.getY()) < 20) {
            held = true;
        }
        return held;
    }

    public void mouseReleased(MouseEvent e) {
        held = false;
    }

    public boolean mouseDragged(MouseEvent e) {
        if (held) {
            x = e.getX();
            y = e.getY();
            x_ = x;
            y_ = y;
        }
        return held;
    }

    public void keepBounds(int maxx, int maxy) {
        x = Math.max(Math.min(maxx, x), 0);
        y = Math.max(Math.min(maxy, y), 0);
    }

    public void move() {
        if (!motile) {
            return;
        }
        double timenow = System.currentTimeMillis(); // time-corrected integration
        if (!held) {
            x = 2 * x - x_ + xa * (timenow - t) * (timenow - t) / mass;
            y = 2 * y - y_ + ya * (timenow - t) * (timenow - t) / mass;
        }
        x_ = x;
        y_ = y;
        t = timenow;
    }

    public void render2D(Graphics2D g2){
        g2.setColor(Color.orange);
        g2.drawOval((int)x, (int)y, 2, 2);
    }
    
    /**
     * Called to apply a force to this Verlet.
     * @param xf The x-direction force applied
     * @param yf -||- for y
     * @param zf -||- for z
     */
    public void recieveForce(double xf, double yf, double zf) {
        this.x += xf / mass; this.y += yf / mass; this.z += zf / mass;
    }
    
}
