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
public class VerletBoundingBox implements VerletTransformer {
    
    public double minX = Double.NEGATIVE_INFINITY, maxX = Double.POSITIVE_INFINITY, 
            minY = Double.NEGATIVE_INFINITY, maxY = Double.POSITIVE_INFINITY, 
            minZ = Double.NEGATIVE_INFINITY, maxZ = Double.POSITIVE_INFINITY;
    
    public VerletBoundingBox(){}
    
    
    /**
     * Bounds the given Verlet within the set 3D box.
     * @param v 
     */
    @Override
    public void transformVerlet(Verlet v) {
        v.x =  v.x < minX ? minX : (v.x > maxX ? maxX: v.x);
        v.y =  v.y < minX ? minX : (v.y > maxX ? maxX: v.y);
        v.z =  v.z < minX ? minX : (v.z > maxX ? maxX: v.z);
    }
    
}
