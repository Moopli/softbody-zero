/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * A set of Verlet and VereltEdge handles. 
 * Connected groups of Verlets and VerletEdges are usually related in some way; 
 * the VerletMesh is a method for keeping them all nice and organized. if a 
 * group of Verlets all need the same transform applied to them (gravity 
 * perhaps); the VerletMesh can be used to apply the transform to all at once.
 * 
 * Of course, a VerletMesh need not only contain connected Verlets and 
 * VerletEdges, and most importantly (this deserves to be stressed several 
 * times) -- the Mesh carries handles, not the values themselves. The Mesh is 
 * explicitly designed for pass-by-reference -- it never replaces the Verlets it 
 * contains (unless it is revising its jurisdiction), it always transforms 
 * extant Verlets through their own handles. Hence, one can say that they work 
 * in parallel with the simulate-render cycle, used for 'extra' stuff, never 
 * usurping the capacity to create Verlets and whatnot.
 * 
 * 
 * 
 * @author filip
 */
public class VerletMesh {
    
    public HashSet<Verlet> verlets;
    public HashSet<VerletEdge> edges;
    
    public MeshTransformConcatenator registeredTransform;
    
    /**
     *
     */
    public VerletMesh(){
        this.verlets = new HashSet<Verlet>();
        this.edges = new HashSet<VerletEdge>();
    }
    
    /**
     *
     * @param verlets
     * @param edges
     */
    public VerletMesh(Collection<Verlet> verlets, Collection<VerletEdge> edges ){
        this.verlets = new HashSet<Verlet>();
        this.edges = new HashSet<VerletEdge>();
        this.verlets.addAll(verlets);
        this.edges.addAll(edges);
    }
    
    
    
    public void transformVerlets(VerletTransformer vt){
        Iterator i = verlets.iterator();
        while (i.hasNext()){
            vt.transformVerlet((Verlet)i.next());
        }
    }
    
    public void transformEdges(VerletEdgeTransformer vet){
        Iterator i = edges.iterator();
        while (i.hasNext()){
            vet.transformEdge((VerletEdge)i.next());
        }
    }
    
    /**
     * Transforms all Verlets and VerletEdges according to the mesh's 
     * registeredTransform. If the registeredTransform does not exist, this does 
     * nothing.
     */
    public void transformRegistered(){
        if (registeredTransform == null) return;
        this.transformVerlets(registeredTransform);
        this.transformEdges(registeredTransform);
    }
}