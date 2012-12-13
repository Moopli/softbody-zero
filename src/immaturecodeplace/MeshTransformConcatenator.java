/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace;

import java.util.LinkedList;
import java.util.List;

/**
 * A utility class used to manage large numbers of Verlet/VerletEdge transforms 
 * all to be applied together. Each MeshTransformConcatenator (abbrev. MTC) has 
 * two queues to manage -- one of VerletTransforms, the other of 
 * VerletEdgeTransforms. It can then apply the transforms either in series or 
 * parallel.
 * 
 * @author filip
 */
public class MeshTransformConcatenator implements VerletTransformer, VerletEdgeTransformer{
    
    // The following public due to popular demand
    public LinkedList<VerletTransformer> verlet_ts;
    public LinkedList<VerletEdgeTransformer> edge_ts;
    
    public MeshTransformConcatenator(){
        verlet_ts = new LinkedList<VerletTransformer>();
        edge_ts = new LinkedList<VerletEdgeTransformer>();
    }
    
    public MeshTransformConcatenator(List<VerletTransformer> vts, List<VerletEdgeTransformer> vets){
        this();
        verlet_ts.addAll(vts);
        edge_ts.addAll(vets);
    }
    
    
    public MeshTransformConcatenator(VerletTransformer vt, VerletEdgeTransformer vet){
        this();
        verlet_ts.add(vt);
        edge_ts.add(vet);
    }
    
    public MeshTransformConcatenator(VerletTransformer vt){
        this();
        verlet_ts.add(vt);
        
    }
    public MeshTransformConcatenator(VerletEdgeTransformer vet){
        this();
        edge_ts.add(vet);
    }
    
    /**
     * Transforms a Verlet using all transforms of the queue -- amenable to parallelization. 
     * @param v 
     */
    @Override
    public void transformVerlet(Verlet v) {
        for (VerletTransformer vt : verlet_ts){
            vt.transformVerlet(v);
        }
    }
    
    /**
     * Transforms a VerletEdge using all transforms of the queue -- amenable to parallelization.
     * @param ve 
     */
    @Override
    public void transformEdge(VerletEdge ve) {
        for (VerletEdgeTransformer vet : edge_ts){
            vet.transformEdge(ve);
        }
    }
    /**
     * Applies the VerletTransforms in the queue in 'BFS' order -- the first transform to all, the second to all, etcetera.
     * @param mesh The mesh whose Verlets will get transformed.
     */
    public void parallelVerletTransform(VerletMesh mesh ){
        for (VerletTransformer vt : verlet_ts){
            mesh.transformVerlets(vt);
        }
    }
    /**
     * Applies the VerletEdgeTransforms in the queue in 'BFS' order -- the first transform to all, the second to all, etcetera.
     * @param mesh The mesh whose VerletEdges will get transformed.
     */
    public void parallelEdgeTransform(VerletMesh mesh ){
        for (VerletEdgeTransformer vet : edge_ts){
            mesh.transformEdges(vet);
        }
    }
}
