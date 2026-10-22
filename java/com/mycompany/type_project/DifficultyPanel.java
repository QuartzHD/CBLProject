package com.mycompany.type_project;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DifficultyPanel extends JPanel {
     
    public DifficultyPanel() {
        JPanel difficultySelect = new JPanel();
        difficultySelect.setLayout(new GridLayout(4, 1, 0, 10));
        JButton buttonEasy = new JButton("Easy");
        JButton buttonIntermediate = new JButton("Intermediate");
        JButton buttonHard = new JButton("Hard");
        JButton buttonImpossible = new JButton("Impossible");
        difficultySelect.add(buttonEasy);
        difficultySelect.add(buttonIntermediate);
        difficultySelect.add(buttonHard);
        difficultySelect.add(buttonImpossible);

        new JPanel();
        setLayout(new GridBagLayout());
        add(difficultySelect);

        buttonEasy.addActionListener(e -> {
            System.out.println("buttonEasy has been Clicked.");
            int diffNum = 0;
            String charPool = "abcdefghijklmnopqrstuvwxyz ";
            Type_Project.difficultySelected(charPool, diffNum);
        });

        buttonIntermediate.addActionListener(e -> {
            System.out.println("buttonIntermediate has been Clicked.");
            int diffNum = 1;
            String charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";
            Type_Project.difficultySelected(charPool, diffNum);
        });

        buttonHard.addActionListener(e -> {
            System.out.println("buttonHard has been Clicked.");
            int diffNum = 2;
            String charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ";
            Type_Project.difficultySelected(charPool, diffNum);
        });

        buttonImpossible.addActionListener(e -> {
            System.out.println("buttonImpossible has been Clicked.");
            int diffNum = 3;
            String charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^*()-=_+[]{};:,./? ";
            Type_Project.difficultySelected(charPool, diffNum);
        });
    }

}
