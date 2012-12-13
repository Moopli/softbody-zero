/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace.meshtransforms;

import immaturecodeplace.Point3D;
import immaturecodeplace.Verlet;
import immaturecodeplace.VerletTransformer;

/**
 *
 * @author Filip
 */
public class SimpleExplosion  extends Point3D implements VerletTransformer{
    
    public double center_force;
    public double radius;
    
    public SimpleExplosion(double x, double y, double z, double force, double radius){
        this.x = x; this.y = y; this.z = z; this.center_force = force; this.radius = radius;
    }
    
    /** 
     * Uses inverse-square decay relationship to apply forces to Verlets within radius.
     * 
     * X_force = center_force / (x_distance ** 2) .. same for all others
     * 
     */
    @Override
    public void transformVerlet(Verlet v) {
        if (dist3D(this, v) <= radius ) {
            double X_force = center_force / Math.pow(v.x - x, 2);
            double Y_force = center_force / Math.pow(v.y - y, 2);
            double Z_force = center_force / Math.pow(v.z - z, 2);
            
            X_force = Math.abs(X_force) > center_force ? center_force * Math.signum(X_force) : X_force;
            Y_force = Math.abs(Y_force) > center_force ? center_force * Math.signum(Y_force) : Y_force;
            Z_force = Math.abs(Z_force) > center_force ? center_force * Math.signum(Z_force) : Z_force;
            
            v.recieveForce(X_force, Y_force, Z_force);
        }
    }
}
