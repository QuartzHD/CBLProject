package com.mycompany.type_project;

import javax.swing.*;
import javax.swing.Timer.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 *
 * @author Bobbert
 */
public class Type_Project {

    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel container = new JPanel(cardLayout);

    private static final String START = "start";
    private static final String DIFFICULTY = "difficulty";
    private static final String FIGHT = "fight";
    private static String charPool = "abcdefghijklmnopqrstuvwxyz ";

    private static JLabel textToType;
    private static JLabel marker;
    private static JLabel timerLabel;
    private static int countdown;
    private static Timer countdownTimer;
    private static int diffNum = 0;
    private static String stringToType;
    private static int stringIndex = 0;
    private static int botStringIndex = 0;
    public static volatile boolean playerWon = false;
    public static volatile boolean botWon = false;
    private static Bot bot;

    private static AnimatedSprite playerSpriteIdle;
    private static AnimatedSprite enemySpriteIdle;
    private static AnimatedSprite playerSpriteAttack;
    private static AnimatedSprite enemySpriteAttack;

    private static JPanel fightPanel;

    public static void main(String[] args) {

        JFrame mainFrame = new JFrame();

        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setMinimumSize(new Dimension(200, 200));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        container.add(createStartPanel(), START);
        container.add(createDifficultyPanel(), DIFFICULTY);
        container.add(createFightPanel(), FIGHT);
        mainFrame.add(container);

        mainFrame.setVisible(true);

    }

    private static JPanel createStartPanel() {

        JButton buttonStart = new JButton(START);
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridBagLayout());
        startPanel.add(buttonStart);
        buttonStart.setPreferredSize(new Dimension(300, 200));
        buttonStart.setMinimumSize(new Dimension(100, 100));

        buttonStart.addActionListener(e -> {
            System.out.println("buttonStart has been Clicked.");
            cardLayout.show(container, DIFFICULTY);
        });

        return startPanel;
    }

    private static JPanel createDifficultyPanel() {

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

        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new GridBagLayout());
        difficultyPanel.add(difficultySelect);

        buttonEasy.addActionListener(e -> {
            System.out.println("buttonEasy has been Clicked.");
            diffNum = 0;
            charPool = "abcdefghijklmnopqrstuvwxyz ";
            cardLayout.show(container, FIGHT);
            startTimer();
        });

        buttonIntermediate.addActionListener(e -> {
            System.out.println("buttonIntermediate has been Clicked.");
            diffNum = 1;
            charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";
            cardLayout.show(container, FIGHT);
            startTimer();
        });

        buttonHard.addActionListener(e -> {
            System.out.println("buttonHard has been Clicked.");
            diffNum = 2;
            charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ";
            cardLayout.show(container, FIGHT);
            startTimer();
        });

        buttonImpossible.addActionListener(e -> {
            System.out.println("buttonImpossible has been Clicked.");
            diffNum = 3;
            charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^*()-=_+[]{};:,./? ";
            cardLayout.show(container, FIGHT);
            startTimer();
        });

        return difficultyPanel;
    }

    private static void startTimer() {
        stringToType = generateString(10 + (diffNum * 5));
        updateTextDisplay();
        countdown = 3;
        timerLabel.setText(String.valueOf(countdown));
        countdownTimer.start();
        stringIndex = 0;

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            startFight();
        }).start();

    }

    private static void updateTextDisplay() {
        StringBuilder res = new StringBuilder();
        int amountOnSides = 2;

        for (int i = -amountOnSides; i <= amountOnSides; i++) {
            if (stringIndex + i < 0 || stringIndex + i > stringToType.length() - 1) {
                res.append(" ");
            } else {
                res.append(stringToType.charAt(stringIndex + i));
            }
        }

        textToType.setText(res.toString());
    }

    // Made this method so the inputlistener class and this class
    // can communicate. The input key is evaluated here too.
    public static void keyParse(char key) {
        System.out.println(key);
        if (stringToType.charAt(stringIndex) == key) {
            stringIndex += 1;
            updateTextDisplay();
            if (stringIndex == stringToType.length()) {
                System.out.println("Done!");
                playerWon = true;
                bot.interrupt();
                endFight();
            }
        } else {
            System.out.println("Wrong! Start over");
            stringIndex = 0;
            updateTextDisplay();
        }
    }

    private static void cleanUp() {
        textToType.removeKeyListener(textToType.getKeyListeners()[0]);
        botWon = false;
        botStringIndex = 0;
        playerWon = false;
        stringIndex = 0;
    }

    private static void startFight() {
        System.out.println("Started fight!");

        bot = new Bot(stringToType, diffNum);
        InputListener l = new InputListener();
        textToType.requestFocusInWindow();
        bot.start();
        textToType.addKeyListener(l);
    }

    public static void endFight() {
        if (botWon) {
            textToType.setText("Bot won!");
            enemySpriteIdle.stopAnimation();
            enemySpriteIdle.setVisible(false);

            enemySpriteAttack.setVisible(true);
            enemySpriteAttack.oneCycleAnimation(() -> {
                enemySpriteAttack.setVisible(false);
                enemySpriteIdle.setVisible(true);
                enemySpriteIdle.startAnimation();
            });
        } else if (playerWon) {
            textToType.setText("Player won!");
            playerSpriteIdle.stopAnimation();
            playerSpriteIdle.setVisible(false);

            playerSpriteAttack.setVisible(true);
            playerSpriteAttack.oneCycleAnimation(() -> {
                playerSpriteAttack.setVisible(false);
                playerSpriteIdle.setVisible(true);
                playerSpriteIdle.startAnimation();
            });
        }

        cleanUp();

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {

            }
            startTimer();
        }).start();
    }

    private static JPanel createFightPanel() {

        enemySpriteIdle = new AnimatedSprite("/enemy/idle/", 400, 600, 400, false);

        playerSpriteIdle = new AnimatedSprite("/player/idle/", 402, 600, 400, false);

        playerSpriteAttack = new AnimatedSprite("/player/attack/", 95, 600, 400, false);

        enemySpriteAttack = new AnimatedSprite("/enemy/attack/", 95, 600, 400, false);

        fightPanel = new BackgroundPanel("/background/fight/1.png", new BorderLayout());

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
        fightPanel.add(timerPanel, BorderLayout.NORTH);

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
        fightPanel.add(playerPanel, BorderLayout.WEST);

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
        fightPanel.add(enemyPanel, BorderLayout.EAST);

        // Text Random String Layout
        textToType = new JLabel();
        textToType.setOpaque(true);
        textToType.setPreferredSize(new Dimension(1000, 150));
        textToType.setBackground(Color.GRAY);
        textToType.setHorizontalAlignment(SwingConstants.CENTER);
        textToType.setForeground(Color.WHITE);
        textToType.setFont(new Font("Dialog", Font.PLAIN, 70));

        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(textToType);
        fightPanel.add(textPanel, BorderLayout.SOUTH);

        // Timer Logic
        countdownTimer = new Timer(1000, e -> {
            countdown -= 1;
            if (countdown <= 0) {
                timerLabel.setText("GO!");
                countdownTimer.stop();
                playerSpriteIdle.startAnimation();
                enemySpriteIdle.startAnimation();
            } else {
                timerLabel.setText(String.valueOf(countdown));
            }
        });

//        marker = new JLabel();
//        marker.setOpaque(true);
//        marker.setBackground(Color.MAGENTA);
//        marker.setLayout(null);
//        marker.setBounds(-100, 0, 100, 100);
//        fightPanel.add(marker);
        return fightPanel;
    }

    // range for characters that are typable on keyboard
    private static String generateString(int length) {
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
