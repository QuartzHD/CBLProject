package com.mycompany.type_project;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/*
@Ruben
@robert
*/

public class Type_Project {

    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel container = new JPanel(cardLayout);

    private static StartPanel startPanel;
    private static DifficultyPanel difficultyPanel;
    private static FightPanel fightPanel;
    private static PausePanel pausePanel;
    private static ResultPanel resultPanel;

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
    public static volatile boolean gamePaused = false;
    public static volatile boolean gameOver = false;
    public static volatile boolean roundActive = false;

    private static Bot bot;
    private static int botHP = 100;
    private static int botXP;
    public static boolean botMadeMistake;
    private static int playerHP = 100;
    private static int playerXP;
    public static boolean playerMadeMistake = false;

    private static Timer nextRoundTimer;

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();

        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setMinimumSize(new Dimension(200, 200));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startPanel = new StartPanel();
        difficultyPanel = new DifficultyPanel();
        fightPanel = new FightPanel("/background/fight/1.png", new BorderLayout());
        pausePanel = new PausePanel();
        resultPanel = new ResultPanel();

        container.add(startPanel, "start");
        container.add(difficultyPanel, "difficulty");
        container.add(fightPanel, "fight");
        container.add(pausePanel, "pause");
        container.add(resultPanel, "result");

        mainFrame.add(container);
        mainFrame.setVisible(true);
    }

    public static void showScreen(String screen) {
        cardLayout.show(container, screen);
    }

    public static void difficultySelected(String charPool, int diffNum) {
        Type_Project.diffNum = diffNum;
        Type_Project.charPool = charPool;
        gamePaused = false;
        gameOver = false;
        showScreen("fight");
        resetRoundState();
        stringToType = generateString();
        fightPanel.UpdateTextDisplay(0, 0, stringToType);
        fightPanel.startCountdown(() -> startRound());
    }

    public static void startRound() {
        if (gamePaused || gameOver) {
            return;
        }
        roundActive = true;
        System.out.print("Round started");
        bot = new Bot(stringToType, diffNum);
        Bot.botStringIndex = 0;
        InputListener l = new InputListener();
        fightPanel.giveTextFocus();
        FightPanel.textToType.addKeyListener(l);
        bot.start();
    }

    public static void keyParse(char key) {
        if (gamePaused || gameOver || !roundActive) {
            return;
        }

        System.out.println(key);
        if (stringToType.charAt(playerStringIndex) == key) {
            playerStringIndex += 1;
            fightPanel.UpdateTextDisplay(playerStringIndex, bot.botStringIndex, stringToType);
            if (playerStringIndex == stringToType.length()) {
                System.out.println("Done!");
                playerWon = true;
                roundActive = false;
                if (bot != null) {
                    bot.stopBot();
                }
                endRound();
            }
        } else {
            System.out.println("Wrong! Start over");
            playerStringIndex = 0;
            playerMadeMistake = true;
            fightPanel.UpdateTextDisplay(playerStringIndex, bot.botStringIndex, stringToType);
        }
    }

    public static void pauseGame() {
        gamePaused = true;
        if (roundActive) {
            if (bot != null) {
                bot.pauseBot();
            }
            fightPanel.pauseSprites();
        } else {
            fightPanel.pauseCountdown();
        }
        showScreen("pause");
    }

    public static void resumeGame() {
        gamePaused = false;
        showScreen("fight");
        fightPanel.giveTextFocus();
        if (roundActive) {
            if (bot != null) {
                bot.resumeBot();
            }
            fightPanel.resumeSprites();
        } else {
            fightPanel.resumeCountdown(() -> startRound());
        }
    }

    public static void retryGame() {
        gamePaused = false;
        gameOver = false;
        roundActive = false;
        if (nextRoundTimer != null && nextRoundTimer.isRunning()) {
            nextRoundTimer.stop();
        }
        if (bot != null) {
            bot.stopBot();
        }
        fightPanel.stopCountdown();

        playerHP = 100;
        playerXP = 0;
        botHP = 100;
        botXP = 0;
        fightPanel.UpdateFightPanel(playerHP, playerXP, botHP, botXP);

        difficultySelected(charPool, diffNum);
    }

    public static void returnToMainMenu() {
        gamePaused = false;
        gameOver = false;
        roundActive = false;
        if (nextRoundTimer != null && nextRoundTimer.isRunning()) {
            nextRoundTimer.stop();
        }
        if (bot != null) {
            bot.stopBot();
        }
        fightPanel.stopCountdown();

        playerHP = 100;
        playerXP = 0;
        botHP = 100;
        botXP = 0;
        fightPanel.UpdateFightPanel(playerHP, playerXP, botHP, botXP);

        showScreen("start");
    }

    public static void endRound() {
        roundActive = false;
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

        if (FightPanel.textToType.getKeyListeners().length > 0) {
            FightPanel.textToType.removeKeyListener(FightPanel.textToType.getKeyListeners()[0]);
        }

        if (playerHP <= 0) {
            showResult(false);
            return;
        } else if (botHP <= 0) {
            showResult(true);
            return;
        }

        nextRoundTimer = new Timer(3000, e -> {
            if (!gamePaused) {
                resetRoundState();

                stringToType = generateString();
                fightPanel.UpdateTextDisplay(0, 0, stringToType);
                fightPanel.startCountdown(() -> startRound());
            }
        });
        nextRoundTimer.setRepeats(false);
        nextRoundTimer.start();
    }

    public static void showResult(boolean playerWonGame) {
        gameOver = true;
        if (playerWonGame) {
            ResultPanel.setResultText("You Won!");
        } else {
            ResultPanel.setResultText("You Lost!");
        }

        Timer endTimer = new Timer(3000, e -> {
            showScreen("result");
        });
        endTimer.setRepeats(false);
        endTimer.start();
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

    private static void resetRoundState() {
        botWon = false;
        botMadeMistake = false;
        Bot.botStringIndex = 0;
        playerWon = false;
        playerMadeMistake = false;
        playerStringIndex = 0;
    }

    private static String generateString() {
        int length = 10 + (diffNum * 5);
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

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
