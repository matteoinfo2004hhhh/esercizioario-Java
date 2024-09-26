import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class BallGame {
    private JFrame frame; // Riferimento al JFrame
    private GamePanel gamePanel; // Riferimento al pannello di gioco

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BallGame().createAndShowGUI());
    }



    // Metodo per creare e mostrare il GUI
    private void createAndShowGUI() {
        frame = new JFrame("Arena Virus");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null); // Centra la finestra
        showMainMenu(); // Mostra il menu principale
        frame.setVisible(true);
    }

    // Metodo per mostrare il menu principale
    private void showMainMenu() {
        JPanel menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                resizeComponents(frame, this); // Chiama il metodo per ridimensionare i componenti
            }
        };

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.BLACK);

        // Titolo principale: ARENA VIRUS
        JLabel titleLabel = new JLabel("ARENA VIRUS");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.YELLOW);

        // Sottotitolo: Nome dello sviluppatore
        JLabel developerLabel = new JLabel("Sviluppato da: MATTEO BARCELLONA");
        developerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        developerLabel.setForeground(Color.LIGHT_GRAY);

        // Pulsante "Gioca"
        JButton playButton = new JButton("Gioca");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setBackground(Color.YELLOW);
        playButton.setForeground(Color.BLACK);
        playButton.setFocusPainted(false);
        playButton.setPreferredSize(new Dimension(300, 100));
        playButton.setFont(new Font("Arial", Font.BOLD, 30));

        // Pulsante "Esci"
        JButton exitButton = new JButton("Esci");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.BLACK);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(300, 100));
        exitButton.setFont(new Font("Arial", Font.BOLD, 30));

        // Aggiunge componenti al pannello
        menuPanel.add(Box.createVerticalStrut(150));
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(developerLabel);
        menuPanel.add(Box.createVerticalStrut(100));
        menuPanel.add(playButton);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(exitButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(menuPanel);
        frame.revalidate();
        frame.repaint();

        // Listener del pulsante per avviare il gioco
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(); // Avvia il gioco
            }
        });

        // Listener del pulsante per uscire
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Esci dal gioco
            }
        });

        // Aggiunge un listener per gestire il ridimensionamento della finestra
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents(frame, menuPanel);
            }
        });
    }

    // Metodo per avviare il gioco
    private void startGame() {
        gamePanel = new GamePanel(); // Passa il riferimento alla classe principale
        frame.getContentPane().removeAll(); // Rimuove il menu principale
        frame.getContentPane().add(gamePanel); // Aggiunge il pannello di gioco
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow(); // Sposta il focus sul pannello di gioco
    }

    // Metodo per gestire il game over
    public void gameOver() {
        GameOverPanel gameOverPanel = new GameOverPanel(frame); // Crea il pannello GameOver
        frame.getContentPane().removeAll(); // Rimuove il pannello di gioco
        frame.getContentPane().add(gameOverPanel); // Aggiunge il pannello di Game Over
        frame.revalidate(); // Rende il pannello visibile
        frame.repaint(); // Ridisegna il frame
    }

    // Metodo per ridimensionare i componenti in base alla dimensione della finestra
    private void resizeComponents(JFrame frame, JPanel panel) {
        int width = frame.getWidth();
        int height = frame.getHeight();

        // Ridimensiona il titolo principale
        JLabel titleLabel = (JLabel) panel.getComponent(1); // Indice 1: Titolo "ARENA VIRUS"
        titleLabel.setFont(new Font("Arial", Font.BOLD, Math.max(50, width / 15)));

        // Ridimensiona il nome dello sviluppatore
        JLabel developerLabel = (JLabel) panel.getComponent(3); // Indice 3: Nome sviluppatore
        developerLabel.setFont(new Font("Arial", Font.ITALIC, Math.max(20, width / 40)));

        // Ridimensiona i pulsanti
        JButton playButton = (JButton) panel.getComponent(5); // Indice 5: Pulsante "Gioca"
        JButton exitButton = (JButton) panel.getComponent(7); // Indice 7: Pulsante "Esci"

        // Imposta la dimensione dei pulsanti in base alla larghezza e altezza
        int buttonWidth = Math.max(300, width / 3);
        int buttonHeight = Math.max(100, height / 10);
        playButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        playButton.setFont(new Font("Arial", Font.BOLD, Math.max(30, width / 25)));

        exitButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        exitButton.setFont(new Font("Arial", Font.BOLD, Math.max(30, width / 25)));

        // Ridisegna il pannello
        panel.revalidate();
        panel.repaint();
    }
}
