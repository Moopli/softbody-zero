/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace;

import immaturecodeplace.meshtransforms.SimpleExplosion;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

/**
 *
 * @author 301916706
 */
public class ExplosiveMouseMesh extends VerletMesh implements MouseListener{
    
    
    
    SimpleExplosion expl = new SimpleExplosion(0,0,0,600,70);
    
    public ExplosiveMouseMesh(Collection<Verlet> verlets, Collection<VerletEdge> edges ){
        super(verlets, edges);
    }
    
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isControlDown()){
            
            expl.x = e.getX();
            expl.y = e.getY();

            for (Verlet v : verlets) {
                expl.transformVerlet(v);
            }
        }
        
    }
    
    
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    
    
    
    
}
