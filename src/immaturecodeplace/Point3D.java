/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace;

/**
 *
 * @author filip
 */
public class Point3D {

    public double x = 0, y = 0, z = 0;
    
    /**
     * Returns the 3D Euclidean distance between two Point3D objects
     * @param a
     * @param b
     * @return
     */
    public static double dist3D(Point3D a, Point3D b) {
        return Math.hypot(Math.hypot(a.x - b.x, a.y - b.y), a.z - b.z);
    }
}