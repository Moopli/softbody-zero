/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace;

import java.util.HashSet;

/**
 * The VerletGroupbuilder is responsible for constructing large numbers of 
 * Verlets and VerletEdges, using the data pulled in by a StAX parser.
 * @author 301916706
 */
public class VerletGroupBuilder {
    
    
    public static HashSet<Verlet> testPolyhedron(){
        HashSet<Verlet> verlets = new HashSet<Verlet>();
        // we're going to generate a simple polyhedron -- an octagonal prism
        verlets.add(new Verlet(0,0,0));
        verlets.add(new Verlet(0,100,0));
        verlets.add(new Verlet(-100,-100,0));
        verlets.add(new Verlet(-200,-100,0));
        verlets.add(new Verlet(-300,0,0));
        verlets.add(new Verlet(-300,100,0));
        verlets.add(new Verlet(-200,200,0));
        verlets.add(new Verlet(-100,200,0));
        
        verlets.add(new Verlet(0,0,500));
        verlets.add(new Verlet(0,100,500));
        verlets.add(new Verlet(-100,-100,500));
        verlets.add(new Verlet(-200,-100,500));
        verlets.add(new Verlet(-300,0,500));
        verlets.add(new Verlet(-300,100,500));
        verlets.add(new Verlet(-200,200,500));
        verlets.add(new Verlet(-100,200,500));
        
        return verlets;
        
    }
    
}
