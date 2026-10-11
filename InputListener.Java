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
        /// ArrayList<Integer> blacklist = new ArrayList();
       
        if (e.getKeyChar() >= 32 && e.getKeyChar() <= 126) {
            Type_Project.keyParse(e.getKeyChar());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
