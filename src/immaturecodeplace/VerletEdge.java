/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author filip
 */

/**
 * A class, each of which maintains a link between two Verlet instances.
 *
 * @author Filip
 * 
 */
public class VerletEdge {
    
    boolean can_break = true;
    
    
    double distance, elasticity;
    Verlet a, b;
    
    public static final double DEFAULT_STRESS = 5000;
    double stress = DEFAULT_STRESS;
    
    public VerletEdge(Verlet A, Verlet B, double softness, double length, double stress) {
        a = A;
        b = B;
        elasticity = softness;
        distance = length;
        this.stress = stress;
    }
    
    public void setStress(boolean destructible, int stress_level){
        can_break = destructible;
        stress = stress_level;
    }
    
    public VerletEdge(Verlet A, Verlet B, double softness, double length) {
        this(A,B,softness, length, DEFAULT_STRESS);
    }

    public VerletEdge(Verlet A, Verlet B, double softness) {
        this(A, B, softness, 100, DEFAULT_STRESS);
    }

    public VerletEdge(Verlet A, Verlet B) {
        this(A, B, 1, 100);
    }

    public boolean restitute() {
        double xdist = a.x - b.x; // how rightward A is of B
        double ydist = a.y - b.y; // how bottomward A is of B
        double zdist = a.z - b.z; // how closeward (or maybe farward..) A is of B
        
        double h = (double) Math.hypot( (double) Math.hypot(xdist, ydist), zdist);
        double diff;
        if (h == 0) {
            diff = Double.MAX_VALUE;
        } else {
            diff = (h - distance) / h;
        }
        if (!a.held) {
            a.x -= xdist * 0.5 * (diff / elasticity);
            a.y -= ydist * 0.5 * (diff / elasticity);
            a.z -= zdist * 0.5 * (diff / elasticity);
        }
        if (!b.held) {
            b.y += ydist * 0.5 * (diff / elasticity);
            b.x += xdist * 0.5 * (diff / elasticity);
            b.z += zdist * 0.5 * (diff / elasticity);
        }
        
        // true means the edge breaks
        if (can_break && diff > stress){
            return true;
        } else {
            return false;
        }
    }

    public void render(Graphics2D g2) {
        double shader = Math.min(Math.hypot(a.x - b.x, a.y - b.y), distance) / (double) Math.max(Math.hypot(a.x - b.x, a.y - b.y), distance);


        g2.setColor(new Color(230, (int) (255 * shader), (int) (255 * shader)));
        g2.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
        g2.setColor(new Color(0, 0, 0));


    }
    
    public void render3D(Graphics2D g2, Camera3D camera){
        double shader = Math.min(Math.hypot(a.x - b.x, a.y - b.y), distance) / (double) Math.max(Math.hypot(a.x - b.x, a.y - b.y), distance);

        g2.setColor(new Color(230, (int) (255 * shader), (int) (255 * shader)));
        
        int AX, AY, BX, BY;
        int[] Apos = camera.ViewTransform(a);
        int[] Bpos = camera.ViewTransform(b);
        AX = Apos[0];
        AY = Apos[1];
        BX = Bpos[0];
        BY = Bpos[1];
        
        if (AX == Integer.MAX_VALUE || AY == Integer.MAX_VALUE || BX == Integer.MAX_VALUE || BY == Integer.MAX_VALUE ) {
            ;/* this would be the 'if out of bounds' check*/;
        } else {
            g2.drawLine(AX, AY, BX, BY);
        }
        
        g2.setColor(new Color(0, 0, 0));
    }
}
