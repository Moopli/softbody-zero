/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO LIST:
 * - Outsource the production of Verlet Meshes to an external scripting language
 *  - The VereltMeshFactory would be recruited to produce Verlet Mesh 
 *    'primitives', collections of Verlets and VerletEdges which can be produced 
 *    to certain parameters then "zipped" together to produce more complex 
 *    shapes. 
 * - Mesh Primitives -
 * Primitives would be meshes constructed according to few parameters -- 
 * probably limited to rectangular meshes, parallelepipeds, chains, and convex 
 * polyhedra. Rectangular meshes, parallelepiped, and chains (as a base case of 
 * a rectangular mesh) can already be constructed by the factory; leaving the 
 * convex polyhedra.
 * 
 * - Convex Polyhedra -
 * Convex polyhedra can be specified by the unordered set of their vertices. 
 * Other polyhedra require some more precautions, however every not-completely-
 * convex polyhedron can be decomposed into a set of convex polyhedra, then 
 * 'zipped' back together (more on zipping later). Constructing a convex 
 * polyhedron is easy -- given the vertices, one just attaches them all 
 * together, taking heed of their distances. Something similar is already done 
 * with the Ball-Spawner, however here the distances of points is not taken into 
 * account.
 * 
 * - Zipping -
 * Zipping has been alluded to already, and is a very important VerletMesh 
 * operation. Given two ordered groups of Verlets, a zipper simply attaches them 
 * together. Different zippers can exist for different requirements -- one to 
 * only attach each Verlet to it's partner, another to attach each Verlet from 
 * each group to all from the other, another for zigzag stitches, you get the 
 * idea.
 * 
 */


/**
 *
 * @author filip
 */
public class VerletMeshFactory {
    
    
    /**
     * Creates a ball - really just a special case of a convex polyhedron.
     * @param num The number of Verlets the ball will consist of.
     * @param elast The elasticity of the edges.
     * @param linesize The length of each edge.
     * @return 
     */
    public static VerletMesh generateBall(int num, double elast, double linesize, double linestrength) {
        ArrayList<Verlet> verlies = new ArrayList<Verlet>();
        ArrayList<VerletEdge> edgies = new ArrayList<VerletEdge>();

        for (int i = 0; i < num; i++) {
            verlies.add(new Verlet(i, (int)Math.sqrt(i), i*i));
        }
        for (int i = 0; i < num; i++) {
            for (int j = i + 1; j < num; j++) {
                edgies.add(new VerletEdge(verlies.get(i), verlies.get(j), elast, linesize, linestrength));
            }
        }
        VerletMesh ret = new VerletMesh (verlies, edgies);
        return ret;

    }
    
    /**
     * Generates a 3D lattice of given height, width, depth, etc.
     * @param x Pick a dimension, any dimension.
     * @param y Pick another dimension.
     * @param z The final dimension.
     * @param linesize The length of each VerletEdge.
     * @param softness The elasticity of each VerletEdge.
     * @return 
     */
    public static VerletMesh generateLattice(int x, int y, int z, int linesize, int softness, double linestrength ){
        ArrayList<ArrayList<ArrayList<Verlet>>> verls3d = new ArrayList<ArrayList<ArrayList<Verlet>>>();

        double linesizesqrt2 = linesize * Math.pow(2, 0.5);

        ArrayList<VerletEdge> edgies = new ArrayList<VerletEdge>();
        for (int lvlN = 0; lvlN < z; lvlN++) {
            verls3d.add(new ArrayList<ArrayList<Verlet>>());

            for (int rowN = 0; rowN < y; rowN++) {
                verls3d.get(lvlN).add(new ArrayList<Verlet>());
                for (int colN = 0; colN < x; colN++) {
                    verls3d.get(lvlN).get(rowN).add(new Verlet(colN * linesize, rowN * linesize, lvlN * linesize));
                    
                    if (rowN != 0) {
                        edgies.add(new VerletEdge(verls3d.get(lvlN).get(rowN).get(colN), verls3d.get(lvlN).get(rowN - 1).get(colN), softness, linesize, linestrength));

                        if (colN != x - 1) {
                            edgies.add(new VerletEdge(verls3d.get(lvlN).get(rowN).get(colN), verls3d.get(lvlN).get(rowN - 1).get(colN + 1), softness, linesizesqrt2, linestrength));
                        }
                        if (colN != 0) {
                            edgies.add(new VerletEdge(verls3d.get(lvlN).get(rowN).get(colN), verls3d.get(lvlN).get(rowN - 1).get(colN - 1), softness, linesizesqrt2, linestrength));
                        }
                    }
                    if (colN != 0) {
                        edgies.add(new VerletEdge(verls3d.get(lvlN).get(rowN).get(colN), verls3d.get(lvlN).get(rowN).get(colN - 1), softness, linesize, linestrength));
                    }
                    if (lvlN != 0){
                        edgies.add(new VerletEdge(verls3d.get(lvlN).get(rowN).get(colN), verls3d.get(lvlN - 1).get(rowN).get(colN), softness, linesize, linestrength));
                    }
                }

            }
        }
        ArrayList<Verlet> verlies = new ArrayList<Verlet>();
        for (ArrayList<ArrayList<Verlet>> lvl : verls3d) {
            for (ArrayList<Verlet> row : lvl) {
                verlies.addAll(row);
            }
        }

        VerletMesh ret = new VerletMesh (verlies, edgies);
        return ret;
    }
    
    /**
     * Generates a single Verlet, with a single inactive VerletEdge binding the Verlet to itself.
     * @deprecated This is no longer necessary and only exists for historical reasons.
     * @param x
     * @param y
     * @param z
     * @return 
     */
    @Deprecated
    public static VerletMesh generateLoneVerlet(int x, int y, int z){
        
        
       Verlet v = new Verlet(x,y,z);
       ArrayList<Verlet> vs = new ArrayList<Verlet>();
       vs.add(v);
       ArrayList<VerletEdge> edgies = new ArrayList<VerletEdge>();
       edgies.add(new VerletEdge(v,v,200));
       VerletMesh ret = new VerletMesh (vs, edgies);
       return ret;
    }
    /**
     * Produces a sheet of Verlet particles, amazingly clothlike.
     *
     * @param length
     * @param height
     * @param elast
     * @param linesize
     * @return
     */
    public static VerletMesh generateSheet(int length, int height, double elast, int linesize, double linestrength) {
        ArrayList<ArrayList<Verlet>> verls2d = new ArrayList<ArrayList<Verlet>>();

        double linesizesqrt2 = linesize * Math.pow(2, 0.5);

        ArrayList<VerletEdge> edgies = new ArrayList<VerletEdge>();

        for (int rowN = 0; rowN < height; rowN++) {
            verls2d.add(new ArrayList<Verlet>());
            for (int colN = 0; colN < length; colN++) {
                verls2d.get(rowN).add(new Verlet(colN * linesize, rowN * linesize));

                if (rowN != 0) {
                    edgies.add(new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN - 1).get(colN), elast, linesize, linestrength));

                    if (colN != length - 1) {
                        edgies.add(new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN - 1).get(colN + 1), elast, linesizesqrt2, linestrength));
                    }
                    if (colN != 0) {
                        edgies.add(new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN - 1).get(colN - 1), elast, linesizesqrt2, linestrength));
                    }
                }
                if (colN != 0) {
                    edgies.add(new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN).get(colN - 1), elast, linesize, linestrength));
                }
            }

        }

        ArrayList<Verlet> verlies = new ArrayList<Verlet>();
        for (ArrayList<Verlet> row : verls2d) {
            verlies.addAll(row);
        }

        VerletMesh ret = new VerletMesh (verlies, edgies);
        return ret;
    }
    /**
     * Generates a Verlet sheet welded back upon itself.
     * @param length
     * @param height
     * @param elast
     * @param linesize
     * @return 
     */
    public static VerletMesh generateTube(int length, int height, double elast, int linesize, double linestrength) {
        ArrayList<ArrayList<Verlet>> verls2d = new ArrayList<ArrayList<Verlet>>();

        double linesizesqrt2 = linesize * Math.pow(2, 0.5);

        ArrayList<VerletEdge> edgies = new ArrayList<VerletEdge>();

        for (int rowN = 0; rowN < height; rowN++) {
            verls2d.add(new ArrayList<Verlet>());
            for (int colN = 0; colN < length; colN++) {
                verls2d.get(rowN).add(new Verlet(colN * linesize, rowN * linesize));

                if (rowN != 0) {
                    edgies.add(new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN - 1).get(colN), elast, linesize, linestrength));
                    /*
                     if (colN != length - 1){
                     edgies.add ( new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN-1).get(colN+1), elast, linesizesqrt2) );
                     }
                     if (colN != 0){
                     edgies.add ( new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN-1).get(colN-1), elast, linesizesqrt2) );
                     }
                     */
                }
                if (colN != 0) {
                    edgies.add(new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN).get(colN - 1), elast, linesize, linestrength));
                }
            }
            edgies.add(new VerletEdge(verls2d.get(rowN).get(length - 1), verls2d.get(rowN).get(0), elast, linesize, linestrength));
     }

        ArrayList<Verlet> verlies = new ArrayList<Verlet>();
        for (ArrayList<Verlet> row : verls2d) {
            verlies.addAll(row);
        }

        VerletMesh ret = new VerletMesh (verlies, edgies);
        return ret;
    }
    /**
     * Generates a toroid, a rectangular Verlet sheet with each side welded to the opposite.
     * @param length
     * @param height
     * @param elast
     * @param linesize
     * @return 
     */
    public static VerletMesh generateToroid(int length, int height, double elast, int linesize, double linestrength) {
        ArrayList<ArrayList<Verlet>> verls2d = new ArrayList<ArrayList<Verlet>>();

        double linesizesqrt2 = linesize * Math.pow(2, 0.5);

        ArrayList<VerletEdge> edgies = new ArrayList<VerletEdge>();

        for (int rowN = 0; rowN < height; rowN++) {
            verls2d.add(new ArrayList<Verlet>());
            for (int colN = 0; colN < length; colN++) {
                verls2d.get(rowN).add(new Verlet(colN * linesize, rowN * linesize));

                if (rowN != 0) {
                    edgies.add(new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN - 1).get(colN), elast, linesize, linestrength));
                    /*
                     if (colN != length - 1){
                     edgies.add ( new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN-1).get(colN+1), elast, linesizesqrt2, linestrength) );
                     }
                     if (colN != 0){
                     edgies.add ( new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN-1).get(colN-1), elast, linesizesqrt2, linestrength) );
                     }
                     */
                }
                if (colN != 0) {
                    edgies.add(new VerletEdge(verls2d.get(rowN).get(colN), verls2d.get(rowN).get(colN - 1), elast, linesize, linestrength));
                }
                if (colN == length - 1) {
                }
            }

            edgies.add(new VerletEdge(verls2d.get(rowN).get(length - 1), verls2d.get(rowN).get(0), elast, linesize, linestrength));
            
        }
        for (int i = 0; i < length; i++) {
            edgies.add(new VerletEdge(verls2d.get(height - 1).get(i), verls2d.get(0).get(i), elast, linesize, linestrength));
        }
        ArrayList<Verlet> verlies = new ArrayList<Verlet>();
        for (ArrayList<Verlet> row : verls2d) {
            verlies.addAll(row);
        }

        VerletMesh ret = new VerletMesh (verlies, edgies);
        return ret;
    }
    
    /**
     * Generates a 3D Mobius toroid.
     * @param x
     * @param y
     * @param z
     * @param linesize
     * @param softness
     * @return 
     */
    public static VerletMesh generate3DToroid(int x, int y, int z, int linesize, int softness , double linestrength){
        ArrayList<ArrayList<ArrayList<Verlet>>> verls3d = new ArrayList<ArrayList<ArrayList<Verlet>>>();

        double linesizesqrt2 = linesize * Math.pow(2, 0.5);

        ArrayList<VerletEdge> edgies = new ArrayList<VerletEdge>();
        for (int lvlN = 0; lvlN < z; lvlN++) {
            verls3d.add(new ArrayList<ArrayList<Verlet>>());

            for (int rowN = 0; rowN < y; rowN++) {
                verls3d.get(lvlN).add(new ArrayList<Verlet>());
                for (int colN = 0; colN < x; colN++) {
                    verls3d.get(lvlN).get(rowN).add(new Verlet(colN * linesize, rowN * linesize, lvlN * linesize));
                    
                    if (rowN != 0) {
                        edgies.add(new VerletEdge(verls3d.get(lvlN).get(rowN).get(colN), verls3d.get(lvlN).get(rowN - 1).get(colN), softness, linesize, linestrength));

                        if (colN != x - 1) {
                            edgies.add(new VerletEdge(verls3d.get(lvlN).get(rowN).get(colN), verls3d.get(lvlN).get(rowN - 1).get(colN + 1), softness, linesizesqrt2, linestrength));
                        }
                        if (colN != 0) {
                            edgies.add(new VerletEdge(verls3d.get(lvlN).get(rowN).get(colN), verls3d.get(lvlN).get(rowN - 1).get(colN - 1), softness, linesizesqrt2, linestrength));
                        }
                    }
                    if (colN != 0) {
                        edgies.add(new VerletEdge(verls3d.get(lvlN).get(rowN).get(colN), verls3d.get(lvlN).get(rowN).get(colN - 1), softness, linesize, linestrength));
                    }
                    if (lvlN != 0){
                        edgies.add(new VerletEdge(verls3d.get(lvlN).get(rowN).get(colN), verls3d.get(lvlN - 1).get(rowN).get(colN), softness, linesize, linestrength));
                    }
                    if (lvlN == z-1){
                        edgies.add(new VerletEdge(verls3d.get(lvlN).get(rowN).get(colN), verls3d.get(0).get(y - rowN - 1).get(x - colN - 1), softness, linesize, linestrength));
                    }
                }

            }
        }
        ArrayList<Verlet> verlies = new ArrayList<Verlet>();
        for (ArrayList<ArrayList<Verlet>> lvl : verls3d) {
            for (ArrayList<Verlet> row : lvl) {
                verlies.addAll(row);
            }
        }

        VerletMesh ret = new VerletMesh (verlies, edgies);
        return ret;
    }
    
    /*
     * 'GENERATE' functions are fundamentally different from the "SPAWN' 
     * functions they replace -- GENERATE functions return the constructed 
     * meshes as VerletMeshes, instead of arrays consisting of edges and 
     * verlets. this is much more robust, and we will soon have implementations 
     * of the above with meshes instead of arrays.
     */
    
    /**
     * Generates a convex polyhedron given the group of Verlets.
     * @param verlets
     * @return A VerletMesh containing the polyhedron.
     */
    public static VerletMesh generateConvexPolyhedron(Collection<Verlet> verlets, double elasticity, double linestrength){
        ArrayList<VerletEdge> edges = new ArrayList<VerletEdge>();
        for (Verlet v: verlets){
            for (Verlet w: verlets){
                if (v != w){
                    edges.add(new VerletEdge(v,w,elasticity,dist(v,w), linestrength));
                }
            }
        }
        
        return new VerletMesh(verlets, edges);
    }
    
    /**
     * Zips the Verlets in the given Meshes to each other (all-to-all).
     * @param a
     * @param b
     * @param elasticity The elasticity 
     * @return Returns a VerletMesh containing the VerletEdges which zip together a and b.
     */
    public static VerletMesh zipMeshesAll2All(VerletMesh a, VerletMesh b, double elasticity, double linestrength){
        VerletMesh zips = new VerletMesh();
        for (Verlet v : a.verlets){
            for (Verlet w : b.verlets){
                zips.edges.add(new VerletEdge(v, w, elasticity, dist(v, w), linestrength));
            }
        }
        
        return zips;
    }
    
    
    
    /**
     * returns the Euclidean distance between two Verlets.
     * @param v1
     * @param v2
     * @return 
     */
    public static double dist(Point3D v1, Point3D v2){
        return Math.hypot(v1.x - v2.x, Math.hypot(v1.y - v2.y, v1.z - v2.z) );
    }
    
}
