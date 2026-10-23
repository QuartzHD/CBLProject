package com.mycompany.type_project;

import javax.swing.*;
import java.awt.*;

public class PausePanel extends JPanel {

    public PausePanel() {
        setLayout(new GridBagLayout());
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 10));

        JButton resumeButton = new JButton("Resume");
        JButton retryButton = new JButton("Retry");
        JButton menuButton = new JButton("Main Menu");

        panel.add(resumeButton);
        panel.add(retryButton);
        panel.add(menuButton);
        add(panel);

        resumeButton.addActionListener(e -> Type_Project.resumeGame());
        retryButton.addActionListener(e -> Type_Project.retryGame());
        menuButton.addActionListener(e -> Type_Project.returnToMainMenu());
    }
}
