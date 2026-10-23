package com.mycompany.type_project;

import javax.swing.*;
import java.awt.*;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.io.*;

public class StartPanel extends JPanel {

    public CardLayout cardLayout;
    private static final String START = "START";
    private static final String STOP = "Exit game";

    public StartPanel() {
        cardLayout = new CardLayout();
        JPanel startPanelSelect = new JPanel();
        startPanelSelect.setLayout(new GridLayout(2, 1, 0, 10));
        JButton buttonStart = new JButton(START);
        JButton buttonStop = new JButton(STOP);

        startPanelSelect.add(buttonStart);
        startPanelSelect.add(buttonStop);

        setLayout(new GridBagLayout());
        add(startPanelSelect);

        buttonStart.addActionListener(e -> {
            System.out.println("buttonStart has been Clicked.");
            Type_Project.showScreen("difficulty");
        });

        buttonStop.addActionListener(e -> {
            System.out.println("buttonStop has been Clicked.");
            System.exit(0);
        });
    }

}
