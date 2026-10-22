package com.mycompany.type_project;

import javax.swing.*;
import javax.swing.Timer.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/*
    @author Ruben
    @author Robert
*/

public class Type_Project {

    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel container = new JPanel(cardLayout);
    
    private static StartPanel startPanel;
    private static DifficultyPanel difficultyPanel;
    private static FightPanel fightPanel;
    
    private static int diffNum;
    public static String charPool;
    public static String stringToType;    
    public static int playerStringIndex = 0;
    
    private static final int ATTACK_DAMAGE = 20;
    private static final int CRITICAL_DAMAGE = 40;
    private static final int CRITICAL_INCREASE = 15;
    private static final int PERFECT_CRITICAL_BONUS = 25;

    public static volatile boolean playerWon = false;
    public static volatile boolean botWon = false;
    
    private static Bot bot;
    private static int botHP = 100;
    private static int botXP;
    public static boolean botMadeMistake;
    private static int playerHP = 100;
    private static int playerXP;
    public static boolean playerMadeMistake = false;

    public static void main(String[] args) {

        JFrame mainFrame = new JFrame();

        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setMinimumSize(new Dimension(200, 200));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        startPanel = new StartPanel();
        difficultyPanel = new DifficultyPanel();
        fightPanel = new FightPanel("/background/fight/1.png", new BorderLayout());
        
        container.add(startPanel, "start");
        container.add(difficultyPanel, "difficulty");
        container.add(fightPanel, "fight");
        mainFrame.add(container);

        mainFrame.setVisible(true);

    }
    
    public static void showScreen(String screen) {
        cardLayout.show(container, screen);
    }
    
    public static void difficultySelected(String charPool, int diffNum) {
        Type_Project.diffNum = diffNum;
        Type_Project.charPool = charPool;
        showScreen("fight");
        stringToType = generateString();
        fightPanel.UpdateTextDisplay(0, 0, stringToType);
        fightPanel.startCountdown(() -> startRound());
    }

    public static void startRound() {
        System.out.print("Round started");
        bot = new Bot(stringToType, diffNum);
        InputListener l = new InputListener();
        FightPanel.textToType.requestFocusInWindow();
        FightPanel.textToType.addKeyListener(l);
        bot.start();
    }
    // Made this method so the inputlistener class and this class
    // can communicate. The input key is evaluated here too.
    public static void keyParse(char key) {
        System.out.println(key);
        if (stringToType.charAt(playerStringIndex) == key) {
            playerStringIndex += 1;
            fightPanel.UpdateTextDisplay(playerStringIndex, bot.botStringIndex, stringToType);
            if (playerStringIndex == stringToType.length()) {
                System.out.println("Done!");
                playerWon = true;
                bot.interrupt();
                endRound();
            }
        } else {
            System.out.println("Wrong! Start over");
            playerStringIndex = 0;
            playerMadeMistake = true;
            fightPanel.UpdateTextDisplay(playerStringIndex, bot.botStringIndex, stringToType);
        }
    }

    public static void endRound() {
        if (playerWon) {
            fightPanel.textToType.setText("Player won!");
            dealDamageToBot();
            fightPanel.playerAttackAnimation();
        } else if (botWon) {
            fightPanel.textToType.setText("Bot won!");
            dealDamageToPlayer();
            fightPanel.enemyAttackAnimation();
        }
        
        fightPanel.UpdateFightPanel(playerHP, playerXP, botHP, botXP);
        cleanUp();
        
        if (playerHP <= 0) {
            endFight(1);
            return;
        } else if (botHP <= 0) {
            endFight(0);
            return;
        }
        
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                
            }        
            stringToType = generateString();
            fightPanel.UpdateTextDisplay(0, 0, stringToType);
            fightPanel.startCountdown(() -> startRound());
        }).start();
    }
    
    public static void endFight(int winner) {   // 0 for player, 1 for bot
        if (winner == 0) {
            System.out.println("Player won!");
        } else {
            System.out.println("Bot won!");
        }
        
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                showScreen("start");
                playerHP = 100;
                playerXP = 0;
                botHP = 100;
                botXP = 0;
                fightPanel.UpdateFightPanel(playerHP, playerXP, botHP, botXP);
            } catch (InterruptedException e) {
                
            }
        }).start();
    }
    
    public static void dealDamageToBot() {
        if (playerXP >= 100) {
            playerXP = 0;
            botHP -= CRITICAL_DAMAGE;
        } else {
            botHP -= ATTACK_DAMAGE;
            playerXP += CRITICAL_INCREASE;
            if (!playerMadeMistake) {
                playerXP += PERFECT_CRITICAL_BONUS;
            }            
        }
    }
    
    public static void dealDamageToPlayer() {
        if (botXP >= 100) {
            botXP = 0;
            playerHP -= CRITICAL_DAMAGE;
        } else {
            playerHP -= ATTACK_DAMAGE;
            botXP += CRITICAL_INCREASE;
            if (!botMadeMistake) {
                botXP += PERFECT_CRITICAL_BONUS;
                botMadeMistake = false;
            }
        }
    }
    
    private static void cleanUp() {
        fightPanel.textToType.removeKeyListener(fightPanel.textToType.getKeyListeners()[0]);
        botWon = false;
        botMadeMistake = false;
        bot.botStringIndex = 0;
        playerWon = false;
        playerMadeMistake = false;
        playerStringIndex = 0;
    }
    
    // range for characters that are typable on keyboard
    private static String generateString() {
        int length = 10 + (diffNum * 5);
        
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        //Generates the string and makes sure the first and last character aren't spaces.
        for (int i = 0; i < length; i++) {
            boolean isEdge = i == 0 || i == length - 1;
            char c;
            do {
                int index = random.nextInt(charPool.length());
                c = charPool.charAt(index);
            } while (isEdge && c == ' ');

            sb.append(c);

        }

        return sb.toString();
    }
}
