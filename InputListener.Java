package com.mycompany.type_project;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 *
 * @author ruben
 */
public class InputListener implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    @Override
    public void keyPressed(KeyEvent e) {
        
    }
    @Override
    public void keyReleased(KeyEvent e) {
        /// ArrayList<Integer> blacklist = new ArrayList();
       
        if (e.getKeyCode() >= 32 && e.getKeyCode() <= 126) {
            Type_Toernooi.keyParse(e.getKeyChar());
        } else {
        }
    }
}
