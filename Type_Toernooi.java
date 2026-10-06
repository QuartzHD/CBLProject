
import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 *
 * @author Bobbert
 */
public class Type_Toernooi {

    public static void main(String[] args) {

        JFrame mainFrame = new JFrame();

        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setMinimumSize(new Dimension(200, 200));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton buttonStart = new JButton("Start");
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridBagLayout());
        startPanel.add(buttonStart);
        buttonStart.setPreferredSize(new Dimension(300, 200));
        buttonStart.setMinimumSize(new Dimension(100, 100));

        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

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

        container.add(startPanel, "start");
        container.add(difficultyPanel, "difficulty");
        mainFrame.add(container);


        buttonStart.addActionListener(e -> {
            System.out.println("buttonStart has been Clicked.");
            cardLayout.show(container, "difficulty");

        });
        
        mainFrame.setVisible(true);
    }
}
