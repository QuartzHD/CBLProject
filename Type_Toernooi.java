
import javax.swing.*;
import javax.swing.Timer.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 *
 * @author Bobbert
 */
public class Type_Toernooi {

    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel container = new JPanel(cardLayout);

    private static final String START = "start";
    private static final String DIFFICULTY = "difficulty";
    private static final String FIGHT = "fight";

    private static JLabel textToType;
    private static JLabel timerLabel;
    private static int countdown;
    private static Timer countdownTimer;
    private static int diffNum = 5;

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
            diffNum = 5;
            startFight();
            cardLayout.show(container, FIGHT);

        });

        buttonIntermediate.addActionListener(e -> {
            System.out.println("buttonIntermediate has been Clicked.");
            diffNum = 10;
            startFight();
            cardLayout.show(container, FIGHT);

        });

        buttonHard.addActionListener(e -> {
            System.out.println("buttonHard has been Clicked.");
            diffNum = 15;
            startFight();
            cardLayout.show(container, FIGHT);

        });

        buttonImpossible.addActionListener(e -> {
            System.out.println("buttonImpossible has been Clicked.");
            diffNum = 20;
            startFight();
            cardLayout.show(container, FIGHT);

        });

        return difficultyPanel;
    }

    private static void startFight() {
        textToType.setText(generateString(diffNum));
        countdown = 3;
        timerLabel.setText(String.valueOf(countdown));
        countdownTimer.start();

    }

    private static JPanel createFightPanel() {
        // verander dit later afhankelijk van difficulty

        JLabel playerSprite = new JLabel();
        playerSprite.setOpaque(true);
        playerSprite.setBackground(Color.BLUE);
        playerSprite.setPreferredSize(new Dimension(200, 400));
        playerSprite.setText("Player");

        JLabel enemySprite = new JLabel();
        enemySprite.setOpaque(true);
        enemySprite.setBackground(Color.RED);
        enemySprite.setPreferredSize(new Dimension(200, 400));
        enemySprite.setText("Enemy");

        JPanel fightPanel = new JPanel();
        fightPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0, 50, 0, 50);
        fightPanel.add(playerSprite, constraints);

        constraints.gridx = 1;
        textToType = new JLabel();
        textToType.setOpaque(true);
        textToType.setPreferredSize(new Dimension(800, 150));
        textToType.setBackground(Color.GRAY);
        textToType.setHorizontalAlignment(SwingConstants.CENTER);
        textToType.setForeground(Color.WHITE);
        textToType.setFont(new Font("Dialog", Font.PLAIN, 70));

        fightPanel.add(textToType, constraints);

        timerLabel = new JLabel();
        timerLabel.setOpaque(true);
        timerLabel.setPreferredSize(new Dimension(300, 150));
        timerLabel.setBackground(Color.BLACK);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Dialog", Font.PLAIN, 75));
        constraints.anchor = GridBagConstraints.NORTH;
        fightPanel.add(timerLabel, constraints);

        countdownTimer = new Timer(1000, e -> {

            countdown -= 1;
            if (countdown <= 0) {
                timerLabel.setText("GO!");
                countdownTimer.stop();

            } else {
                timerLabel.setText(String.valueOf(countdown));                
            }            


        });

        constraints.gridx = 2;
        constraints.anchor = GridBagConstraints.EAST;
        fightPanel.add(enemySprite, constraints);

        return fightPanel;
    }

    // Here we generate a Random number generator in an ASCII value
    // range for characters that are typable on keyboard
    private static String generateString(int length) {
        int leftLimit = 32;
        int rightLimit = 126;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            Random random = new Random();
            int num = random.ints(leftLimit, rightLimit).findFirst().getAsInt();
            sb.append((char) num);

        }

        return sb.toString();
    }
}
