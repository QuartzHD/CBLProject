package com.mycompany.type_project;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author Bobbert
 */
public class AnimatedSprite extends JLabel {
    
    private ImageIcon[] frames;
    private int index;
    private Timer timer;

    public AnimatedSprite(String imageFolder, int delay) {
        List<ImageIcon> loadedFrames = new ArrayList<>();
        
        int number = 1;
        
        while(true) {
            String path = imageFolder + number + ".png";
            URL imageURL = getClass().getResource(path);
            
            if (imageURL == null) {
                break;
            }
            
            loadedFrames.add(new ImageIcon(imageURL));
            number++;
        }
        
        if(loadedFrames.isEmpty()) {
            throw new IllegalArgumentException("no images found in: " + imageFolder);
        }
       
    frames = loadedFrames.toArray(new ImageIcon[0]);
    
    setIcon(frames[0]);
    
    timer = new Timer(delay, e -> {
       index = (index + 1) % frames.length;
       setIcon(frames[index]);
    });
       
    timer.start();
    
    }
    
    public void stopAnimation() {
        timer.stop();
    }
    
    public void startAnimation() {
        timer.start();
    }
    
}
