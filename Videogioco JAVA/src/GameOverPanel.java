import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverPanel extends JPanel {
    private JFrame frame;

    public GameOverPanel(JFrame frame) {
        this.frame = frame;  // Assegna il frame passato

        // Imposta il layout e il colore di sfondo
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Etichetta del Game Over
        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverLabel.setForeground(Color.YELLOW);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 60));

        // Pulsante per uscire
        JButton exitButton = new JButton("Esci");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setBackground(Color.YELLOW);
        exitButton.setForeground(Color.BLACK);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(300, 100));
        exitButton.setFont(new Font("Arial", Font.BOLD, 30));
        exitButton.addActionListener(e -> System.exit(0));

        // Aggiungi tutti i componenti al pannello
        add(Box.createVerticalStrut(150));
        add(gameOverLabel);
        add(Box.createVerticalStrut(20));
        add(exitButton);

    }


}
