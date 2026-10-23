package com.mycompany.type_project;

import javax.swing.*;
import java.awt.*;

public class ResultPanel extends JPanel {

    private static JLabel resultLabel;

    public ResultPanel() {
        setLayout(new GridBagLayout());
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 10));

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Dialog", Font.BOLD, 50));

        JButton retryButton = new JButton("Retry");
        JButton menuButton = new JButton("Main Menu");

        panel.add(resultLabel);
        panel.add(retryButton);
        panel.add(menuButton);
        add(panel);

        retryButton.addActionListener(e -> Type_Project.retryGame());
        menuButton.addActionListener(e -> Type_Project.returnToMainMenu());
    }

    public static void setResultText(String text) {
        if (resultLabel != null) {
            resultLabel.setText(text);
        }
    }
}
