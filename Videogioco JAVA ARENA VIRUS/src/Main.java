import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

class GamePanel extends JPanel {
    private int ballX = 100, ballY = 100;
    private int ballSize = 70;
    private Image ballImage;
    private double angle = 0;
    private Timer timer;
    private boolean[] keys = new boolean[256];
    private ArrayList<Projectile> projectiles;
    private ArrayList<CloneBall> cloneBalls;
    private ArrayList<EnemyBall> enemyBalls;
    private Random random;
    private int score = 0;
    private boolean gameOver = false;
    private static final int BORDER_SIZE = 30;

    // Aggiungi pulsanti
    private JButton retryButton;
    private JButton exitButton;

    public GamePanel() {
        setFocusable(true);
        initializeGame();
        setupKeyListener();
        setupMouseListener();
        setupTimer();
        setupSpawnTimers();
        setupButtons(); // Inizializza i pulsanti
    }

    private void initializeGame() {
        projectiles = new ArrayList<>();
        cloneBalls = new ArrayList<>();
        enemyBalls = new ArrayList<>();
        random = new Random();
        ballImage = new ImageIcon("img/virus.png").getImage();
    }

    private void setupButtons() {
        // Crea pulsante "Riprova"
        retryButton = new JButton("Riprova");
        styleButton(retryButton);
        retryButton.addActionListener(e -> restartGame());

        // Crea pulsante "Esci"
        exitButton = new JButton("Esci");
        styleButton(exitButton);
        exitButton.addActionListener(e -> System.exit(0));

        // Imposta layout manuale per pulsanti
        setLayout(null);
        add(retryButton);
        add(exitButton);
    }

    private void styleButton(JButton button) {
        // Cambia colore di sfondo e testo
        button.setBackground(Color.YELLOW);
        button.setForeground(Color.BLACK);

        // Imposta font e rimuovi il bordo per un look moderno
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        // Arrotonda gli angoli del pulsante
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
    }

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
            }
        });
    }

    private void setupMouseListener() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                angle = Math.atan2(e.getY() - (ballY + ballSize / 2), e.getX() - (ballX + ballSize / 2));
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!gameOver) {
                    shootProjectile();
                }
            }
        });
    }

    private void setupTimer() {
        timer = new Timer(10, e -> {
            if (!gameOver) {
                updateGameObjects();
                checkCollisions();
            }
            repaint();
        });
        timer.start();
    }

    private void setupSpawnTimers() {
        new Timer(1000 + random.nextInt(2000), e -> spawnCloneBall()).start();
        new Timer(2000 + random.nextInt(2000), e -> spawnEnemyBall()).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawGameObjects(g);
        drawScore(g);
        if (gameOver) {
            positionButtons();  // Posiziona dinamicamente i pulsanti quando il gioco è finito
            retryButton.setVisible(true);
            exitButton.setVisible(true);
        } else {
            retryButton.setVisible(false); // Nascondi i pulsanti durante il gioco
            exitButton.setVisible(false);
        }
        drawGameOverScreen(g);
    }

    private void positionButtons() {
        // Calcola posizione centrale
        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = getWidth() / 2 - buttonWidth / 2;
        int centerY = getHeight() / 2;

        // Posiziona i pulsanti uno sotto l'altro
        retryButton.setBounds(centerX, centerY - 40, buttonWidth, buttonHeight);
        exitButton.setBounds(centerX, centerY + 30, buttonWidth, buttonHeight);
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.YELLOW);
        g.fillRect(BORDER_SIZE, BORDER_SIZE, getWidth() - 2 * BORDER_SIZE, getHeight() - 2 * BORDER_SIZE);
    }

    private void drawGameObjects(Graphics g) {
        g.drawImage(ballImage, ballX, ballY, ballSize, ballSize, null);
        projectiles.forEach(projectile -> projectile.draw(g));
        cloneBalls.forEach(cloneBall -> cloneBall.draw(g));
        enemyBalls.forEach(enemyBall -> enemyBall.draw(g));
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Punteggio: " + score, 30, 70);
    }

    private void drawGameOverScreen(Graphics g) {
        if (gameOver) {
            // Sfondo nero per la schermata di game over
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Testo "GAME OVER"
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            String gameOverText = "GAME OVER";
            int gameOverTextWidth = g.getFontMetrics().stringWidth(gameOverText);
            g.drawString(gameOverText, getWidth() / 2 - gameOverTextWidth / 2, getHeight() / 2 - 100);

            // Testo del punteggio
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 40));
            String scoreText = "Il tuo punteggio: " + score;
            int scoreTextWidth = g.getFontMetrics().stringWidth(scoreText);
            g.drawString(scoreText, getWidth() / 2 - scoreTextWidth / 2, getHeight() / 2 +200);
        }
    }


    private void updateGameObjects() {
        moveBall();
        moveProjectiles();
        moveCloneBalls();
        moveEnemyBalls();
    }

    private void moveBall() {
        if (keys[KeyEvent.VK_UP]) ballY -= 8;
        if (keys[KeyEvent.VK_DOWN]) ballY += 8;
        if (keys[KeyEvent.VK_LEFT]) ballX -= 8;
        if (keys[KeyEvent.VK_RIGHT]) ballX += 8;

        ballX = Math.max(BORDER_SIZE, Math.min(getWidth() - ballSize - BORDER_SIZE, ballX));
        ballY = Math.max(BORDER_SIZE, Math.min(getHeight() - ballSize - BORDER_SIZE, ballY));
    }

    private void shootProjectile() {
        int projectileStartX = ballX + ballSize / 2;
        int projectileStartY = ballY + ballSize / 2;
        projectiles.add(new Projectile(projectileStartX, projectileStartY, angle));
    }

    private void spawnCloneBall() {
        int x = random.nextInt(getWidth() - 50 - 2 * BORDER_SIZE) + BORDER_SIZE;
        int y = random.nextInt(getHeight() - 40 - 2 * BORDER_SIZE) + BORDER_SIZE;
        String randomImagePath = "img/virus" + (random.nextInt(5) + 2) + ".png"; // Cambia l'immagine
        Image cloneImage = new ImageIcon(randomImagePath).getImage();
        cloneBalls.add(new CloneBall(x, y, cloneImage));
    }

    private void spawnEnemyBall() {
        int x = random.nextInt(getWidth() - 80 - 2 * BORDER_SIZE) + BORDER_SIZE;
        int y = random.nextInt(getHeight() - 80 - 2 * BORDER_SIZE) + BORDER_SIZE;
        Image enemyImage = new ImageIcon("img/nemici.png").getImage();
        enemyBalls.add(new EnemyBall(x, y, enemyImage));
    }

    private void moveProjectiles() {
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile projectile = projectiles.get(i);
            projectile.move();
            if (projectile.isOutOfBounds(getWidth(), getHeight())) {
                projectiles.remove(i);
                i--;
            }
        }
    }

    private void moveCloneBalls() {
        cloneBalls.forEach(CloneBall::move);
    }

    private void moveEnemyBalls() {
        enemyBalls.forEach(EnemyBall::move);
    }

    private void checkCollisions() {
        checkCloneBallCollisions();
        checkEnemyBallCollisions();
        checkProjectileEnemyCollisions();
    }

    private void checkCloneBallCollisions() {
        for (int i = 0; i < cloneBalls.size(); i++) {
            if (cloneBalls.get(i).getBounds().intersects(getBounds())) {
                score++;
                cloneBalls.remove(i);
                break;
            }
        }
    }

    private void checkEnemyBallCollisions() {
        for (EnemyBall enemyBall : enemyBalls) {
            if (enemyBall.getBounds().intersects(getBounds())) {
                gameOver = true;
                break;
            }
        }
    }

    private void checkProjectileEnemyCollisions() {
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile projectile = projectiles.get(i);
            for (int j = 0; j < enemyBalls.size(); j++) {
                EnemyBall enemyBall = enemyBalls.get(j);
                if (projectile.getBounds().intersects(enemyBall.getBounds())) {
                    projectiles.remove(i);
                    enemyBalls.remove(j);
                    score += 2;
                    break;
                }
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(ballX, ballY, ballSize, ballSize);
    }

    private void restartGame() {
        gameOver = false;
        score = 0;
        ballX = 100;
        ballY = 100;
        projectiles.clear();
        cloneBalls.clear();
        enemyBalls.clear();
        repaint();
    }
}



// Classe per i proiettili
class Projectile {
    private int x, y;
    private double angle;
    private static final int SIZE = 14;
    private static final int SPEED = 30;

    public Projectile(int startX, int startY, double angle) {
        this.x = startX;
        this.y = startY;
        this.angle = angle;
    }

    public void move() {
        x += (int)(SPEED * Math.cos(angle));
        y += (int)(SPEED * Math.sin(angle));
    }

    public boolean isOutOfBounds(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }
}

// Classe per le palline clonate
class CloneBall {
    private int x, y;
    private Image image;
    private double angle;
    private static final int SPEED = 5;

    public CloneBall(int x, int y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.angle = Math.random() * 2 * Math.PI; // Angolo casuale
    }

    public void move() {
        x += (int)(SPEED * Math.cos(angle));
        y += (int)(SPEED * Math.sin(angle));

        // Rimbalza ai bordi
        if (x < 30 || x > 2000 - 70 - 30) {
            angle = Math.PI - angle; // Rimbalza
        }
        if (y < 30 || y > 900 - 70 - 30) {
            angle = -angle; // Rimbalza
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, 50, 50, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 70, 70);
    }
}

// Classe per le palline nemiche
class EnemyBall {
    private int x, y;
    private Image image;
    private double angle;
    private static final int SPEED = 10;

    public EnemyBall(int x, int y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.angle = Math.random() * 2 * Math.PI; // Angolo casuale
    }

    public void move() {
        x += (int)(SPEED * Math.cos(angle));
        y += (int)(SPEED * Math.sin(angle));

        // Rimbalza ai bordi
        if (x < 30 || x > 2000 - 70 - 30) {
            angle = Math.PI - angle; // Rimbalza
        }
        if (y < 30 || y > 900 - 70 - 30) {
            angle = -angle; // Rimbalza
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, 100, 80, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50);
    }
}
