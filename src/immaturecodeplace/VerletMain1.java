package immaturecodeplace;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.geom.*;



public class VerletMain1 {

    /**
     * @param args           
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println("something happened");
        
        String [] quots = {"All hail the masterfulness of Verlet integration!!!", "The verld of Verlets is very vacillating", "Now in 3D!"};
        
        // setup stuff
        JFrame frame = new JFrame("Now in 3D!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 950);
        frame.setResizable(false);
        Container main_panel = frame.getContentPane();
        main_panel.setLayout(new BorderLayout());
        
        // center game panel
        VerletPane verlet_pane = new VerletPane(main_panel);
        main_panel.add(verlet_pane, BorderLayout.CENTER);
        
        // bottom info panel
        JPanel bot = new JPanel();
        bot.setLayout(new FlowLayout());
        bot.add(new JLabel("hi"));
        main_panel.add(bot, BorderLayout.SOUTH);
        
        // right control panel
        JPanel right = new JPanel();
        right.setLayout(new GridLayout(3, 1));
        right.add(new JButton("a button"));
        right.add(new JButton("another button"));
        right.add(new JTextField("enter data"));
        main_panel.add(right, BorderLayout.EAST);
        
        // final packing stuff
        frame.setVisible(true);
        verlet_pane.timer.start();
    }
}
