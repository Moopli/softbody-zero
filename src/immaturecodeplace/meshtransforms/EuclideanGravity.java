/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace.meshtransforms;

import immaturecodeplace.Verlet;
import immaturecodeplace.VerletTransformer;

/**
 *
 * @author filip
 */
public class EuclideanGravity implements VerletTransformer{
    
    public double gravX = 0, gravY = 0, gravZ = 0;
    
    
    @Override
    public void transformVerlet(Verlet v) {
        v.x += gravX; v.y += gravY; v.z += gravZ;
    }
    
}
