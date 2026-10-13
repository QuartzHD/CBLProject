package com.mycompany.type_project;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

import java.net.*;

/**
 *
 * @author Bobbert
 */
public class AnimatedSprite extends JLabel {

    private ImageIcon[] frames;
    private int index;
    private Timer timer;

    private int spriteWidth;
    private int spriteHeight;

    public AnimatedSprite(String imageFolder,
            int delay,
            int width,
            int height,
            boolean autoStart) {

        spriteWidth = width;
        spriteHeight = height;

        List<ImageIcon> loadedFrames = new ArrayList<>();

        int numberOfPNG = 1;

        while (true) {
            String path = imageFolder + numberOfPNG + ".png";
            URL imageURL = getClass().getResource(path);

            if (imageURL == null) {
                break;
            }

            ImageIcon original = new ImageIcon(imageURL);

            Image scaledImage = original.getImage().getScaledInstance(spriteWidth, spriteHeight, Image.SCALE_SMOOTH);

            loadedFrames.add(new ImageIcon(scaledImage));
            numberOfPNG++;
        }

        if (loadedFrames.isEmpty()) {
            throw new IllegalArgumentException("no images found in: " + imageFolder);
        }

        frames = loadedFrames.toArray(new ImageIcon[0]);

        setIcon(frames[0]);
        setPreferredSize(new Dimension(width, height));

        timer = new Timer(delay, e -> {
            index = (index + 1) % frames.length;
            setIcon(frames[index]);
        });

        if (autoStart) {
            timer.start();
        }

    }

    public void stopAnimation() {
        timer.stop();
    }

    public void startAnimation() {
        timer.start();
    }

}
