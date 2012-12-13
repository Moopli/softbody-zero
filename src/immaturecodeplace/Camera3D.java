/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package immaturecodeplace;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author filip
 */
/**
 * This class wraps camera data
 * -- WIP NOTES --
 * Currently in 3D -- however, seems buggy somehow (must figure out the problem)
 * 
 * On to defining the perspective camera -- see Camera.py for descriptive 
 * implementation.
 * 
 * Axes: Facing +x with +y left +z is up
 * 
 * 
 * 
 * 
 * 
 */
class Camera3D implements KeyListener{
    
    public int window_width;
    public int window_height;
    
    public double x_pos;
    public double y_pos;
    public double z_pos;
    
    public double pitch;
    public double yaw;
    public double roll;
    
    public double viewscreen_width;
    public double viewscreen_height;
    public double viewscreen_depth;
    
    /**
     * Calculates the perspective view x-y position of the given 3D point; from 
     * the camera's current reference frame.
     * @param point The 3D point whose position will be computed.
     * @return the integer array {x,y} of the point's position on-screen.
     */
    public int[] ViewTransform(Verlet p){
        /*
         * This works by rotating the point's cartesian coordiantes to a system 
         * where the camera faces along +x, with +z up.
         */
        
        
        double x =  p.x - x_pos;
        double y =  p.y - y_pos;
        double z =  p.z - z_pos;
        
        double x_ = x, x__ = x, y_ = y, y__ = y, z_ = z, z__ = z;
        
        
        // yaw,
        x__ = Math.cos(yaw) * x_ + Math.sin(yaw) * y_;
        y__ = -Math.sin(yaw) * x_ + Math.cos(yaw) * y_;
        z__ = z_;
        
        //roll,
        x_ = x__;
        y_ = -Math.sin(roll) * z__ + Math.cos(roll) * y__;
        z_ = Math.cos(roll) * y__ + Math.sin(roll) * z__;

        //pitch,
        x__ = Math.cos(pitch) * x_ + Math.sin(pitch) * z_;
        y__ = y_;
        z__ = -Math.sin(pitch) * x_ + Math.cos(pitch) * z_;
        
        //depth clipping,
        int[] ret = new int[2];
        
        if (x_ < 0) {
            // The point is behind the camera and must be depth clipped
            ret[0] = Integer.MAX_VALUE; ret[1] = Integer.MAX_VALUE;
            
        } else {
            
            int pov_x = (int)(z__ * viewscreen_depth / x__) ;//+ this.window_width / 2;
            int pov_y = (int)(y__ * viewscreen_depth / x__) ;//+ this.window_height / 2;
        
        
            //side clipping done here
        
        
            ret[0] = pov_x; ret[1] = pov_y;
            
        }
        
        
        //System.out.println("point\t" + p.x + "\t\t" + p.y + "\t\t" + p.z + "\tret\t" + ret[0] + "\t\t" + ret[1]);
        
        return ret;
        
    }
    
    /**
     * Given the x and y positions of a click on the view-screen, this produces 
     * two 3D vectors (or at least some equivalent); which defines the ray cast 
     * by the camera towards whatever the user clicked on. This can be used for 
     * weapons, 3D Verlet interaction, whatever.
     * 
     * 
     * 
     * @param x
     * @param y 
     */
    public void projectClick(int x, int y){
        // TODO Write click-projection routine, which grabs the \
        // first verlet hit by the projection
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pitch += 0.0005;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pitch -= 0.0005;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            yaw += 0.0005;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            yaw -= 0.0005;
        } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
            roll += 0.0005;
        } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
            roll -= 0.0005;
        } 
        
        else if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
            x_pos += 0.5;
        } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
            x_pos -= 0.5;
        } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
            y_pos += 0.5;
        } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
            y_pos -= 0.5;
        } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
            z_pos += 0.5;
        } else if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
            z_pos -= 0.5;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            moveFwd(2);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            moveFwd(-2);
        }
    }
    
    
    /**
     * Moves the camera 'distance' IGUs in the direction the camera is facing.
     * @param distance 
     */
    public void moveFwd(double distance){
        // requires pitch and yaw
        
        double dy, dz, dx, a;
        
        dz = distance * Math.cos(pitch);
        a = distance * Math.sin(pitch);
        
        dx = a * Math.cos(yaw);
        dy = a * Math.sin(yaw);
        
        // some of these may have to be inverted for clean movement
        this.x_pos += dx;
        this.y_pos += dy;
        this.z_pos += dz;
        
        
        
    }
    
    public void moveLft(double distance){
        // requires pitch, yaw, and roll
        /*
         * First, similarly to above, we find the vector pointing forward. 
         * Then, we rotate it pi/2 rad left.
         * 
         * However, how do we know which way left is?
         * We use roll (aka R). R is an angle, measured CCW.
         * 
         * once we have our heading vector, 
         * 
         * 
         * argh this is difficult to define -- does a roll of zero mean +z is 
         * up? -z is up? +x? -x? +y? -y? HUR DUR
         * 
         * anyways -- once we know what a roll of zero entails, 
         * we can 
         * 
         */
        
        
        final double ROLL_ZERO = 0; // up is along z ... ?
        
        //the direction left is
        double left_dir = ROLL_ZERO + roll + Math.PI / 2;
        
        
        
        
        
    }
    
    public void moveRgt(double distance){
        moveLft(-distance);
    }
    
    
    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
    
}
