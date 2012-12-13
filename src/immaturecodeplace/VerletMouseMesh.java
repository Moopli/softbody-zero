/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;

/**
 *
 * @author 301916706
 */
public class VerletMouseMesh extends VerletMesh implements MouseListener, MouseMotionListener {
    
    
    public VerletMouseMesh(){
        super();
        
    }
    
    public VerletMouseMesh(Collection<Verlet> verlets, Collection<VerletEdge> edges ){
        super(verlets, edges);
    }
    

    @Override
    public void mousePressed(MouseEvent e) {
        
        for (Verlet v : verlets) {
            if (v.mousePressed(e) && !e.isShiftDown()) {
                e.consume();
                break;
            }
        }
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
        for (Verlet v : verlets) {
            v.mouseReleased(e);
        }
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
        for (Verlet v : verlets) {
            if (v.mouseDragged(e) && !e.isShiftDown()) {
                e.consume();
                break;
            }
        }
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
    
    
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }
}
