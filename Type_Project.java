    package com.mycompany.type_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

// twee JLabels voor healthbar en crit bar dus 4 in totaal
// letter gekleurd in het midden vinden
/**
 *
 * @author Bobbert
 */
public class Type_Project {

    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel container = new JPanel(cardLayout);

    private static final String START = "START";
    private static final String STOP = "EXIT GAME";
    private static final String DIFFICULTY = "difficulty";
    private static final String FIGHT = "fight";
    private static final String PAUSE = "pause";
    private static final String RESULT = "result";
    private static String charPool = "abcdefghijklmnopqrstuvwxyz ";

    private static int ATTACK_DAMAGE = 20;
    private static int CRITICAL_DAMAGE = 40;
    private static int CRITICAL_INCREASE = 25;
    private static int PERFECT_CRITICAL_BONUS = 40;

    private static JLabel textToType;
    private static JLabel marker;
    private static JLabel timerLabel;
    private static JLabel resultLabel;
    private static JProgressBar playerHealthBar;
    private static JProgressBar playerCritBar;
    private static JProgressBar enemyHealthBar;
    private static JProgressBar enemyCritBar;
    private static int countdown;
    private static Timer countdownTimer;
    private static Timer nextRoundTimer;
    private static int diffNum = 0;
    private static String stringToType;
    private static int stringIndex = 0;
    private static int botStringIndex = 0;
    public static volatile boolean playerWon = false;
    public static volatile boolean botWon = false;

    private static Bot bot;
    private static int botHP = 100;
    private static int botXP;
    public static volatile boolean botMadeMistake;
    private static int playerHP = 100;
    private static int playerXP;
    public static volatile boolean playerMadeMistake;

    private static AnimatedSprite playerSpriteIdle;
    private static AnimatedSprite enemySpriteIdle;
    private static AnimatedSprite playerSpriteAttack;
    private static AnimatedSprite enemySpriteAttack;

    private static JPanel fightPanel;
    private static boolean gamePaused = false;
    private static boolean gameOver = false;
    private static boolean roundActive = false;
    private static boolean pausedDuringRoundDelay = false;

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setMinimumSize(new Dimension(200, 200));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        container.add(createStartPanel(), START);
        container.add(createDifficultyPanel(), DIFFICULTY);
        container.add(createFightPanel(), FIGHT);
        container.add(createPausePanel(), PAUSE);
        container.add(createResultPanel(), RESULT);
        mainFrame.add(container);
        mainFrame.setVisible(true);
    }

    private static JPanel createStartPanel() {
        JPanel startPanelSelect = new JPanel();
        startPanelSelect.setLayout(new GridLayout(2, 1, 0, 10));
        JButton buttonStart = new JButton(START);
        JButton buttonStop = new JButton(STOP);
        startPanelSelect.add(buttonStart);
        startPanelSelect.add(buttonStop);

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridBagLayout());
        startPanel.add(startPanelSelect);

        buttonStart.addActionListener(e -> {
            System.out.println("buttonStart has been Clicked.");
            cardLayout.show(container, DIFFICULTY);
        });
        buttonStop.addActionListener(e -> {
            System.out.println("buttonStop has been Clicked.");
            System.exit(0);
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
            diffNum = 0;
            charPool = "abcdefghijklmnopqrstuvwxyz ";
            cardLayout.show(container, FIGHT);
            startTimer();
        });
        buttonIntermediate.addActionListener(e -> {
            diffNum = 1;
            charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";
            cardLayout.show(container, FIGHT);
            startTimer();
        });
        buttonHard.addActionListener(e -> {
            diffNum = 2;
            charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ";
            cardLayout.show(container, FIGHT);
            startTimer();
        });
        buttonImpossible.addActionListener(e -> {
            diffNum = 3;
            charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^*()-=_+[]{};:,./? ";
            cardLayout.show(container, FIGHT);
            startTimer();
        });
        return difficultyPanel;
    }

    private static JPanel createPausePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("PAUSED");
        title.setFont(new Font("Dialog", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton resumeButton = new JButton("Resume");
        JButton retryButton = new JButton("Retry");
        JButton menuButton = new JButton("Main Menu");
        Dimension buttonSize = new Dimension(180, 35);
        for (JButton button : new JButton[]{resumeButton, retryButton, menuButton}) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(buttonSize);
        }
        content.add(title);
        content.add(Box.createVerticalStrut(25));
        content.add(resumeButton);
        content.add(Box.createVerticalStrut(10));
        content.add(retryButton);
        content.add(Box.createVerticalStrut(10));
        content.add(menuButton);
        panel.add(content);

        resumeButton.addActionListener(e -> resumeGame());
        retryButton.addActionListener(e -> retryGame());
        menuButton.addActionListener(e -> returnToMainMenu());
        return panel;
    }

    private static JPanel createResultPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        resultLabel = new JLabel("You Won!");
        resultLabel.setFont(new Font("Dialog", Font.BOLD, 40));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton retryButton = new JButton("Retry");
        JButton menuButton = new JButton("Main Menu");
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryButton.setMaximumSize(new Dimension(180, 35));
        menuButton.setMaximumSize(new Dimension(180, 35));

        content.add(resultLabel);
        content.add(Box.createVerticalStrut(25));
        content.add(retryButton);
        content.add(Box.createVerticalStrut(10));
        content.add(menuButton);
        panel.add(content);

        retryButton.addActionListener(e -> retryGame());
        menuButton.addActionListener(e -> returnToMainMenu());
        return panel;
    }

    private static void setupEscapeKey() {
        fightPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pauseGame");
        fightPanel.getActionMap().put("pauseGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gamePaused) {
                    resumeGame();
                } else if (!gameOver) {
                    pauseGame();
                }
            }
        });
    }

    private static void pauseGame() {
        if (gamePaused || gameOver) return;
        gamePaused = true;
        pausedDuringRoundDelay = nextRoundTimer != null && nextRoundTimer.isRunning();
        if (countdownTimer != null) countdownTimer.stop();
        if (nextRoundTimer != null) nextRoundTimer.stop();
        if (bot != null) bot.pauseBot();
        playerSpriteIdle.stopAnimation();
        enemySpriteIdle.stopAnimation();
        cardLayout.show(container, PAUSE);
    }

    private static void resumeGame() {
        if (!gamePaused || gameOver) return;
        gamePaused = false;
        cardLayout.show(container, FIGHT);
        if (bot != null) bot.resumeBot();
        if (roundActive) {
            playerSpriteIdle.startAnimation();
            enemySpriteIdle.startAnimation();
        } else if (pausedDuringRoundDelay) {
            pausedDuringRoundDelay = false;
            startTimer();
            return;
        } else if (countdownTimer != null && countdown > 0) {
            countdownTimer.start();
        }
        textToType.requestFocusInWindow();
    }

    private static void retryGame() {
        stopActiveTimersAndBot();
        resetGameState();
        cardLayout.show(container, FIGHT);
        startTimer();
    }

    private static void returnToMainMenu() {
        stopActiveTimersAndBot();
        resetGameState();
        cardLayout.show(container, START);
    }

    private static void stopActiveTimersAndBot() {
        if (countdownTimer != null) countdownTimer.stop();
        if (nextRoundTimer != null) nextRoundTimer.stop();
        if (bot != null) {
            bot.stopBot();
            bot = null;
        }
        if (textToType != null) {
            for (KeyListener listener : textToType.getKeyListeners()) {
                if (listener instanceof InputListener) textToType.removeKeyListener(listener);
            }
        }
        if (playerSpriteIdle != null) playerSpriteIdle.stopAnimation();
        if (enemySpriteIdle != null) enemySpriteIdle.stopAnimation();
        if (playerSpriteAttack != null) playerSpriteAttack.stopAnimation();
        if (enemySpriteAttack != null) enemySpriteAttack.stopAnimation();
    }

    private static void resetGameState() {
        gamePaused = false;
        gameOver = false;
        roundActive = false;
        pausedDuringRoundDelay = false;
        playerHP = 100;
        playerXP = 0;
        botHP = 100;
        botXP = 0;
        playerWon = false;
        botWon = false;
        playerMadeMistake = false;
        botMadeMistake = false;
        stringIndex = 0;
        botStringIndex = 0;
        if (playerSpriteAttack != null) playerSpriteAttack.setVisible(false);
        if (enemySpriteAttack != null) enemySpriteAttack.setVisible(false);
        if (playerSpriteIdle != null) playerSpriteIdle.setVisible(true);
        if (enemySpriteIdle != null) enemySpriteIdle.setVisible(true);
        if (playerHealthBar != null) updateFightPanel();
    }

    private static void startTimer() {
        if (gamePaused || gameOver) return;
        if (bot != null) {
            bot.stopBot();
            bot = null;
        }
        roundActive = false;
        stringToType = generateString(10 + (diffNum * 5));
        stringIndex = 0;
        botStringIndex = 0;
        playerWon = false;
        botWon = false;
        playerMadeMistake = false;
        botMadeMistake = false;
        updateTextDisplay();
        updateFightPanel();
        countdown = 3;
        timerLabel.setText(String.valueOf(countdown));
        countdownTimer.start();
    }

    public static void updateTextDisplay() {
        if (textToType == null || stringToType == null || stringToType.isEmpty()) return;
        StringBuilder res = new StringBuilder("<html>");
        int amountOnSides = 2;
        for (int i = -amountOnSides; i <= amountOnSides; i++) {
            char c;
            if (stringIndex + i < 0 || stringIndex + i > stringToType.length() - 1) {
                c = ' ';
            } else {
                c = stringToType.charAt(stringIndex + i);
            }
            String displayChar = (c == ' ') ? "&nbsp;" : String.valueOf(c);
            if (i == 0) {
                res.append("<span style='color:white;'>").append(displayChar).append("</span>");
            } else if (bot != null && stringIndex + i < bot.botStringIndex) {
                res.append("<span style='color:rgb(173, 0, 0);'>").append(displayChar).append("</span>");
            } else {
                res.append("<span style='color:rgb(7, 157, 255);'>").append(displayChar).append("</span>");
            }
        }
        res.append("</html>");
        textToType.setText(res.toString());
    }

    public static void keyParse(char key) {
        if (gamePaused || gameOver || !roundActive || stringToType == null || stringIndex >= stringToType.length()) return;
        System.out.println(key);
        if (stringToType.charAt(stringIndex) == key) {
            stringIndex += 1;
            updateTextDisplay();
            if (stringIndex == stringToType.length()) {
                System.out.println("Done!");
                playerWon = true;
                if (bot != null) bot.stopBot();
                endRound();
            }
        } else {
            System.out.println("Wrong! Start over");
            stringIndex = 0;
            playerMadeMistake = true;
            updateTextDisplay();
        }
    }

    private static void cleanUp() {
        if (textToType != null) {
            for (KeyListener listener : textToType.getKeyListeners()) {
                if (listener instanceof InputListener) textToType.removeKeyListener(listener);
            }
        }
        botWon = false;
        botStringIndex = 0;
        playerWon = false;
        stringIndex = 0;
    }

    private static void startRound() {
        if (gamePaused || gameOver || roundActive) return;
        System.out.println("Started fight!");
        roundActive = true;
        bot = new Bot(stringToType, diffNum);
        InputListener l = new InputListener();
        textToType.requestFocusInWindow();
        textToType.addKeyListener(l);
        bot.start();
    }

    public static void endRound() {
        if (gamePaused || gameOver) return;
        roundActive = false;
        if (nextRoundTimer != null) nextRoundTimer.stop();

        if (botWon) {
            textToType.setText("Bot won!");
            enemySpriteIdle.stopAnimation();
            enemySpriteIdle.setVisible(false);
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
            enemySpriteAttack.setVisible(true);
            enemySpriteAttack.oneCycleAnimation(() -> {
                enemySpriteAttack.setVisible(false);
                enemySpriteIdle.setVisible(true);
                if (!gameOver && !gamePaused) enemySpriteIdle.startAnimation();
            });
        } else if (playerWon) {
            textToType.setText("Player won!");
            playerSpriteIdle.stopAnimation();
            playerSpriteIdle.setVisible(false);
            if (playerXP >= 100) {
                playerXP = 0;
                botHP -= CRITICAL_DAMAGE;
            } else {
                botHP -= ATTACK_DAMAGE;
                playerXP += CRITICAL_INCREASE;
                if (!playerMadeMistake) {
                    playerXP += PERFECT_CRITICAL_BONUS;
                    playerMadeMistake = false;
                }
            }
            playerSpriteAttack.setVisible(true);
            playerSpriteAttack.oneCycleAnimation(() -> {
                playerSpriteAttack.setVisible(false);
                playerSpriteIdle.setVisible(true);
                if (!gameOver && !gamePaused) playerSpriteIdle.startAnimation();
            });
        }

        updateFightPanel();
        cleanUp();
        if (playerHP <= 0) {
            showResult(false);
            return;
        } else if (botHP <= 0) {
            showResult(true);
            return;
        }

        nextRoundTimer = new Timer(3000, e -> {
            if (!gamePaused && !gameOver) startTimer();
        });
        nextRoundTimer.setRepeats(false);
        nextRoundTimer.start();
    }

    private static void showResult(boolean playerIsWinner) {
        gameOver = true;
        gamePaused = false;
        roundActive = false;
        if (countdownTimer != null) countdownTimer.stop();
        if (nextRoundTimer != null) nextRoundTimer.stop();
        if (bot != null) {
            bot.stopBot();
            bot = null;
        }
        playerSpriteIdle.stopAnimation();
        enemySpriteIdle.stopAnimation();
        playerSpriteAttack.stopAnimation();
        enemySpriteAttack.stopAnimation();
        playerSpriteIdle.setVisible(true);
        enemySpriteIdle.setVisible(true);
        playerSpriteAttack.setVisible(false);
        enemySpriteAttack.setVisible(false);
        resultLabel.setText(playerIsWinner ? "You Won!" : "You Lost!");
        cardLayout.show(container, RESULT);
    }

    private static JPanel createFightPanel() {
        enemySpriteIdle = new AnimatedSprite("/enemy/idle/", 400, 600, 400, false);
        playerSpriteIdle = new AnimatedSprite("/player/idle/", 402, 600, 400, false);
        playerSpriteAttack = new AnimatedSprite("/player/attack/", 95, 600, 400, false);
        enemySpriteAttack = new AnimatedSprite("/enemy/attack/", 95, 600, 400, false);
        fightPanel = new BackgroundPanel("/background/fight/1.png", new BorderLayout());

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
        fightPanel.add(topContainer, BorderLayout.NORTH);

        // Player Layout
        JPanel playerPanel = new JPanel();
        playerPanel.setOpaque(false);
        playerPanel.setLayout(new OverlayLayout(playerPanel));
        playerSpriteAttack.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerSpriteAttack.setAlignmentY(Component.CENTER_ALIGNMENT);
        playerSpriteIdle.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerSpriteIdle.setAlignmentY(Component.CENTER_ALIGNMENT);
        playerPanel.setBorder(BorderFactory.createEmptyBorder(170, 0, 0, 0));
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
        enemyPanel.setBorder(BorderFactory.createEmptyBorder(170, 0, 0, 0));
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
        textToType.setFont(new Font("Monospaced", Font.PLAIN, 70));

        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(textToType);
        fightPanel.add(textPanel, BorderLayout.SOUTH);

        countdownTimer = new Timer(1000, e -> {
            countdown -= 1;
            if (countdown <= 0) {
                timerLabel.setText("GO!");
                countdownTimer.stop();
                if (!gamePaused && !gameOver) {
                    playerSpriteIdle.startAnimation();
                    enemySpriteIdle.startAnimation();
                    startRound();
                }
            } else {
                timerLabel.setText(String.valueOf(countdown));
            }
        });

        setupEscapeKey();
        return fightPanel;
    }

    private static void updateFightPanel() {
        playerHealthBar.setValue(Math.max(0, playerHP));
        playerCritBar.setValue(Math.min(100, playerXP));
        enemyHealthBar.setValue(Math.max(0, botHP));
        enemyCritBar.setValue(Math.min(100, botXP));
    }

    // range for characters that are typable on keyboard
    private static String generateString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        // Generates the string and makes sure the first and last character aren't spaces.
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
