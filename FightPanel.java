package com.mycompany.type_project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;

public class FightPanel extends BackgroundPanel {

    private static JLabel timerLabel;
    private static Timer countdownTimer;
    public static JLabel textToType;

    private static JProgressBar playerHealthBar;
    private static JProgressBar playerCritBar;
    private static JProgressBar enemyHealthBar;
    private static JProgressBar enemyCritBar;

    private static AnimatedSprite playerSpriteIdle;
    private static AnimatedSprite playerSpriteAttack;
    private static AnimatedSprite enemySpriteIdle;
    private static AnimatedSprite enemySpriteAttack;

    private int countdownNum = 3;
    private boolean countdownRunning = false;

    public FightPanel(String path, BorderLayout bl) {

        // this way we sort of 'override' the constructor of the super class
        super(path, bl);

        enemySpriteIdle = new AnimatedSprite("/enemy/idle/", 400, 600, 400, false);
        playerSpriteIdle = new AnimatedSprite("/player/idle/", 402, 600, 400, false);
        playerSpriteAttack = new AnimatedSprite("/player/attack/", 95, 600, 400, false);
        enemySpriteAttack = new AnimatedSprite("/enemy/attack/", 95, 600, 400, false);

        playerSpriteIdle.startAnimation();
        enemySpriteIdle.startAnimation();

        // Player Bar Layout
        playerHealthBar = new JProgressBar(0, 100);
        playerHealthBar.setValue(100);
        playerHealthBar.setForeground(Color.RED);
        playerHealthBar.setStringPainted(true);
        playerHealthBar.setPreferredSize(new Dimension(200, 25));
        playerHealthBar.setMaximumSize(new Dimension(200, 25));
        playerHealthBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        playerCritBar = new JProgressBar(0, 100);
        playerCritBar.setValue(0);
        playerCritBar.setForeground(Color.BLUE);
        playerCritBar.setStringPainted(true);
        playerCritBar.setPreferredSize(new Dimension(130, 20));
        playerCritBar.setMaximumSize(new Dimension(130, 20));
        playerCritBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel playerBarPanel = new JPanel();
        playerBarPanel.setLayout(new BoxLayout(playerBarPanel, BoxLayout.Y_AXIS));
        playerBarPanel.setOpaque(false);
        playerBarPanel.add(playerHealthBar);
        playerBarPanel.add(Box.createVerticalStrut(5));
        playerBarPanel.add(playerCritBar);

        // Enemy Bar Layout
        enemyHealthBar = new JProgressBar(0, 100);
        enemyHealthBar.setValue(100);
        enemyHealthBar.setForeground(Color.RED);
        enemyHealthBar.setStringPainted(true);
        enemyHealthBar.setPreferredSize(new Dimension(200, 25));
        enemyHealthBar.setMaximumSize(new Dimension(200, 25));
        enemyHealthBar.setAlignmentX(Component.RIGHT_ALIGNMENT);

        enemyCritBar = new JProgressBar(0, 100);
        enemyCritBar.setValue(0);
        enemyCritBar.setForeground(Color.BLUE);
        enemyCritBar.setStringPainted(true);
        enemyCritBar.setPreferredSize(new Dimension(130, 20));
        enemyCritBar.setMaximumSize(new Dimension(130, 20));
        enemyCritBar.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel enemyBarPanel = new JPanel();
        enemyBarPanel.setLayout(new BoxLayout(enemyBarPanel, BoxLayout.Y_AXIS));
        enemyBarPanel.setOpaque(false);
        enemyBarPanel.add(enemyHealthBar);
        enemyBarPanel.add(Box.createVerticalStrut(5));
        enemyBarPanel.add(enemyCritBar);

        // Timer Layout
        timerLabel = new JLabel();
        timerLabel.setOpaque(true);
        Dimension timerSize = new Dimension(300, 120);
        timerLabel.setPreferredSize(timerSize);
        timerLabel.setMinimumSize(timerSize);
        timerLabel.setMaximumSize(timerSize);
        timerLabel.setBackground(Color.BLACK);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Dialog", Font.PLAIN, 75));

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        timerPanel.setOpaque(false);
        timerPanel.add(timerLabel);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topContainer.add(playerBarPanel, BorderLayout.WEST);
        topContainer.add(timerPanel, BorderLayout.CENTER);
        topContainer.add(enemyBarPanel, BorderLayout.EAST);

        add(topContainer, BorderLayout.NORTH);

        // Player Layout
        JPanel playerPanel = new JPanel();
        playerPanel.setOpaque(false);
        playerPanel.setLayout(new OverlayLayout(playerPanel));
        playerSpriteAttack.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerSpriteAttack.setAlignmentY(Component.CENTER_ALIGNMENT);
        playerSpriteIdle.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerSpriteIdle.setAlignmentY(Component.CENTER_ALIGNMENT);
        playerPanel.setBorder(
                BorderFactory.createEmptyBorder(170, 0, 0, 0)
        );

        playerPanel.add(playerSpriteAttack);
        playerSpriteAttack.setVisible(false);
        playerPanel.add(playerSpriteIdle);
        add(playerPanel, BorderLayout.WEST);

        // Enemy Layout
        JPanel enemyPanel = new JPanel();
        enemyPanel.setOpaque(false);
        enemyPanel.setLayout(new OverlayLayout(enemyPanel));
        enemySpriteAttack.setAlignmentX(Component.CENTER_ALIGNMENT);
        enemySpriteAttack.setAlignmentY(Component.CENTER_ALIGNMENT);
        enemySpriteIdle.setAlignmentX(Component.CENTER_ALIGNMENT);
        enemySpriteIdle.setAlignmentY(Component.CENTER_ALIGNMENT);
        enemyPanel.setBorder(
                BorderFactory.createEmptyBorder(170, 0, 0, 0)
        );

        enemyPanel.add(enemySpriteAttack);
        enemySpriteAttack.setVisible(false);
        enemyPanel.add(enemySpriteIdle);
        add(enemyPanel, BorderLayout.EAST);

        // Text Random String Layout
        textToType = new JLabel();
        textToType.setOpaque(true);
        textToType.setPreferredSize(new Dimension(1000, 150));
        textToType.setBackground(Color.GRAY);
        textToType.setHorizontalAlignment(SwingConstants.CENTER);
        textToType.setForeground(Color.WHITE);
        textToType.setFont(new Font("Monospaced", Font.PLAIN, 70));

        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(textToType);
        add(textPanel, BorderLayout.SOUTH);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "pauseGame");
        getActionMap().put("pauseGame", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (Type_Project.gamePaused) {
                    Type_Project.resumeGame();
                } else {
                    Type_Project.pauseGame();
                }
            }
        });

    }

    public static void UpdateFightPanel(int playerHP, int playerXP, int botHP, int botXP) {
        if (playerHP <= 0) {
            playerHealthBar.setValue(0);
        } else {
            playerHealthBar.setValue(playerHP);
        }

        if (playerXP >= 100) {
            playerCritBar.setValue(100);
        } else {
            playerCritBar.setValue(playerXP);
        }

        if (botHP <= 0) {
            enemyHealthBar.setValue(0);
        } else {
            enemyHealthBar.setValue(botHP);
        }

        if (botXP >= 100) {
            enemyCritBar.setValue(100);
        } else {
            enemyCritBar.setValue(botXP);
        }
    }

    public static void UpdateTextDisplay(int playerIndex, int botIndex, String stringToType) {
        StringBuilder res = new StringBuilder("<html>");
        int amountOnSides = 2;

        for (int i = -amountOnSides; i <= amountOnSides; i++) {
            char c;

            if (playerIndex + i < 0 || playerIndex + i > stringToType.length() - 1) {
                c = ' ';
            } else {
                c = stringToType.charAt(playerIndex + i);
            }

            String displayChar = (c == ' ') ? "&nbsp;" : String.valueOf(c);

            // player and bot different color
//            if (i == 0) {
//                res.append("<span style='color:rgb(0, 30, 173);'>").append(displayChar).append("<span>");
//            } else if (bot != null && stringIndex + i == bot.botStringIndex) {
//                res.append("<span style='color:rgb(255, 122, 122);'>").append(displayChar).append("<span>");
//            } else {
//                res.append("<span style='color:white;'>").append(displayChar).append("<span>");                
//            }
            if (i == 0) {
                res.append("<span style='color:white;'>").append(displayChar).append("<span>");
            } else if (playerIndex + i < botIndex) {
                res.append("<span style='color:rgb(173, 0, 0);'>").append(displayChar).append("<span>");
            } else {
                res.append("<span style='color:rgb(7, 157, 255);'>").append(displayChar).append("<span>");
            }
        }
        res.append("</html>");
        textToType.setText(res.toString());
    }

    public void startCountdown(Runnable doThis) {
        int[] num = {3};                // lambda expression in the timer requires num to be final. For some reason using an array works.
        timerLabel.setText(String.valueOf(num[0]));
        countdownTimer = new Timer(1000, e -> {
            num[0] -= 1;
            if (num[0] <= 0) {
                timerLabel.setText("GO!");
                countdownTimer.stop();
                doThis.run();
            } else {
                timerLabel.setText(String.valueOf(num[0]));
            }
        });
        countdownTimer.start();
    }

    public void pauseCountdown() {
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
            countdownRunning = true;
        }
    }

    public void resumeCountdown(Runnable doThis) {
        if (countdownRunning) {
            countdownTimer = new Timer(1000, e -> {
                countdownNum -= 1;
                if (countdownNum <= 0) {
                    timerLabel.setText("GO!");
                    countdownTimer.stop();
                    countdownRunning = false;
                    doThis.run();
                } else {
                    timerLabel.setText(String.valueOf(countdownNum));
                }
            });
            countdownTimer.start();
        }
    }

    public void stopCountdown() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        countdownRunning = false;
    }

    public void playerAttackAnimation() {
        playerSpriteIdle.stopAnimation();
        playerSpriteAttack.oneCycleAnimation(() -> {
            playerSpriteIdle.startAnimation();
        });
    }

    public void enemyAttackAnimation() {
        enemySpriteIdle.stopAnimation();
        enemySpriteAttack.oneCycleAnimation(() -> {
            enemySpriteIdle.startAnimation();
        });
    }

    public void giveTextFocus() {
        textToType.requestFocusInWindow();
    }

    public void pauseSprites() {
        playerSpriteIdle.stopAnimation();
        enemySpriteIdle.stopAnimation();
    }

    public void resumeSprites() {
        playerSpriteIdle.startAnimation();
        enemySpriteIdle.startAnimation();
    }

}
