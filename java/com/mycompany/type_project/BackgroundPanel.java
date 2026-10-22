package com.mycompany.type_project;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BackgroundPanel extends JPanel {
    
    private Image backgroundImage;
    
    public BackgroundPanel(String path, LayoutManager layout) {
        super(layout);
        URL url = getClass().getResource(path);
        
        if (url == null) {
            throw new IllegalArgumentException(
            "Background not found:" + path
            );
        }
        
        backgroundImage = new ImageIcon(url).getImage();
    }
    
    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    g.drawImage(
    backgroundImage, 0, 0, getWidth(), getHeight(), this
    );
}


}
