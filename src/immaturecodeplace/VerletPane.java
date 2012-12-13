/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace;

import immaturecodeplace.meshtransforms.*;

import java.util.ArrayList;
import java.util.LinkedList;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

/**
 *
 * @author filip
 */

/**
 * The content pane.
 *
 * @author Filip
 *
 */
@SuppressWarnings("serial")
class VerletPane extends JComponent implements ActionListener, KeyListener {

    Timer timer = new Timer(10, this);
    Container parent_panel;
    ArrayList<VerletEdge> edges = new ArrayList<VerletEdge>();
    ArrayList<Verlet> verlets = new ArrayList<Verlet>();
    BufferedImage buffer;
    Graphics2D draw_1;
    
    // 3D STUFF
    Camera3D camera;
    
    // Transformation stuff
    
    VerletBoundingBox vbb;
    VerletMesh sheet;
    EuclideanGravity grav;
    
    ExplosiveMouseMesh explodable;
    VerletMouseMesh draggable;
    
    LinkedList<VerletMesh> meshes;
    
    
    public VerletPane(Container parent_panel) {
        this.parent_panel = parent_panel;
        
        //buffer = new BufferedImage(parent_panel.getWidth(), parent_panel.getHeight(), BufferedImage.TRANSLUCENT);
        //draw_1 = buffer.createGraphics();
        
        //timer.start();
        vbb = new VerletBoundingBox();
        
        vbb.maxX = 800; vbb.minX = 10;
        vbb.maxY = 800; vbb.minY = 10;
        
        grav = new EuclideanGravity();
        //grav.gravY = 0.5;
        
        //VerletMesh loner = VerletMeshFactory.generateLoneVerlet(10,10,10);
        sheet = VerletMeshFactory.generateSheet(15, 15, 3, 30, 2);
        //VerletMesh lattice = VerletMeshFactory.generateLattice(20, 20, 20, 10, 3, 1);
        //VerletMesh toroid3d = VerletMeshFactory.generate3DToroid(3, 5, 20, 10, 3, 5000);
        //VerletMesh toroid = VerletMeshFactory.generateToroid(20,20,10,10, 5000);
        //VerletMesh ball = VerletMeshFactory.generateBall(20, 10, 100, 5000);
        
        for (int i = 0; i < 4; i++) {
            VerletMesh sheet2 = VerletMeshFactory.generateSheet(15, 15, 3, 27 + i, 2);
            verlets.addAll(sheet2.verlets); edges.addAll(sheet2.edges);
        }
        
        // Arbitrary convex polyhedra
        //VerletMesh polyhedron = VerletMeshFactory.generateConvexPolyhedron(VerletGroupBuilder.testPolyhedron(), 10, 5000);
        //verlets.addAll(polyhedron.verlets); edges.addAll(polyhedron.edges);
        
        verlets.addAll(sheet.verlets); edges.addAll(sheet.edges);
        //verlets.addAll(lattice.verlets); edges.addAll(lattice.edges);
        //verlets.addAll(toroid3d.verlets); edges.addAll(toroid3d.edges);
        //verlets.addAll(toroid.verlets); edges.addAll(toroid.edges);
        //verlets.addAll(ball.verlets); edges.addAll(ball.edges);
        
        
        parent_panel.addKeyListener(this);
        
        camera = new Camera3D();
        initCam();
        parent_panel.addKeyListener(camera);
        
        /* 
         * -- Easy:
         * create a mouseclick-verlet-transformer thingy, and make a big 
         * verletmesh of all the verlets which can be click-dragged;
         * 
         * then register the mouseclick-verlet-transformer to the frame.
         * 
         * -- Harder:
         * Create a 'physics' mesh, which contains all verlets and edges which 
         * should be move-ing and retitute-ing
         * 
         */
        
        draggable = new VerletMouseMesh(verlets, edges);
        explodable = new ExplosiveMouseMesh(verlets, edges);
        
        this.addMouseListener(explodable);
        this.addMouseListener(draggable);
        this.addMouseMotionListener(draggable);
    }
    
    
    public void initCam(){
        camera.window_width = parent_panel.getWidth();
        camera.window_height = parent_panel.getHeight();
        camera.pitch = 0;
        camera.roll = 0;
        camera.yaw = 0;
        camera.x_pos = parent_panel.getWidth() / 2;
        camera.y_pos = parent_panel.getHeight() / 2;
        camera.z_pos = -300;
        camera.viewscreen_height = parent_panel.getHeight();
        camera.viewscreen_width = parent_panel.getWidth();
        camera.viewscreen_depth = 1000;
        
        camera.x_pos = 100;
        camera.y_pos = 100;
        
        // Useful coords 
        // p: 0.13
        // y: -2.4849999999999692
        // r: 1.4799999999999907
    }
    @Override
    public void paintComponent(Graphics g) {
        //System.out.println("doodle");

        repaint2D((Graphics2D) g);
        //repaint3D((Graphics2D) g);
        
    }

    public void repaint2D(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, parent_panel.getWidth(), parent_panel.getHeight());
        for (VerletEdge e : edges) {
            e.render(g2);
        }
        for (Verlet v: verlets){
            v.render2D(g2);
        }
    }
    
    public void repaint3D(Graphics2D g2){
        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, parent_panel.getWidth(), parent_panel.getHeight());
        for (VerletEdge e : edges) {
            e.render3D(g2, camera);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        //System.out.println("tick");
        //System.out.println("p: " + camera.pitch + "\t\ty: " + camera.yaw + "\t\tr: " + camera.roll);
        
       
        for (Verlet v : verlets) {
            v.move();
            v.keepBounds(parent_panel.getWidth(), parent_panel.getHeight());
        }
        
        //sheet.transformVerlets(vbb);
        //sheet.transformVerlets(grav);
        
        ArrayList<VerletEdge> removethese = new ArrayList<VerletEdge>();
        for (VerletEdge e : edges) {
            if (e.restitute()){
                removethese.add(e);
            }
        }
        //edges.removeAll(removethese);
        //sheet.edges.removeAll(removethese);
        //draggable.edges.removeAll(removethese);
        //explodable.edges.removeAll(removethese);
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            for (VerletEdge d : edges) {
                d.distance = 10;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            for (VerletEdge d : edges) {
                d.distance = 100;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_0) {
            for (VerletEdge d : edges) {
                d.distance += 5;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_1) {
            for (VerletEdge d : edges) {
                d.distance = Math.max(10, d.distance - 5);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_3) {
            for (VerletEdge d : edges) {
                d.distance = Math.hypot(d.a.x - d.b.x, d.a.y - d.b.y);
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }
}