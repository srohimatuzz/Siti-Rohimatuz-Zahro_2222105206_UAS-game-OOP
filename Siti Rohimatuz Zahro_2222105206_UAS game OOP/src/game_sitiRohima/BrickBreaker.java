package game_sitiRohima;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BrickBreaker extends JPanel implements KeyListener, ActionListener {

    // Game constants
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 20;
    private static final int BALL_SIZE = 20;
    private static final int BRICK_WIDTH = 80;
    private static final int BRICK_HEIGHT = 30;

    // Game variables
    private int paddleX = WIDTH / 2;
    private int paddleY = HEIGHT - PADDLE_HEIGHT - 20;
    private int ballX = WIDTH / 2;
    private int ballY = HEIGHT / 2;
    private int ballDX = 2;
    private int ballDY = -2;
    private boolean gameOver = false;
    private boolean restart = false;
    private int score = 0;

    // Brick array
    private boolean[][] bricks = new boolean[10][5];

    // Colors
    private Color paddleColor = Color.BLUE;
    private Color ballColor = Color.RED;
    private Color brickColor = Color.GREEN;

    public BrickBreaker() {
        addKeyListener(this);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        // Initialize bricks
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                bricks[i][j] = true;
            }
        }

        // Create timer for game loop
        Timer timer = new Timer(10, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // membuat paddle
        g.setColor(paddleColor);
        g.fillRect(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

        // membuat ball
        g.setColor(ballColor);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // membuat bricks
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (bricks[i][j]) {
                    g.setColor(brickColor);
                    g.fillRect(i * BRICK_WIDTH, j * BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT);
                }
            }
        }

        // membuat score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);

        // membuat game over message
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over! Press R to restart.", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            paddleX -= 10;
        } else if (key == KeyEvent.VK_RIGHT) {
            paddleX += 10;
        } else if (key == KeyEvent.VK_R) {
            restart = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update ball position
        ballX += ballDX;
        ballY += ballDY;

        // Collision with walls
        if (ballX < 0 || ballX > WIDTH - BALL_SIZE) {
            ballDX = -ballDX;
        }
        if (ballY < 0) {
            ballDY = -ballDY;
        }

        // Collision with paddle
        if (ballY > paddleY - BALL_SIZE && ballX > paddleX && ballX < paddleX + PADDLE_WIDTH) {
            ballDY = -ballDY;
        }

        // Collision with bricks
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (bricks[i][j] && ballX > i * BRICK_WIDTH && ballX < i * BRICK_WIDTH + BRICK_WIDTH
                        && ballY > j * BRICK_HEIGHT && ballY < j * BRICK_HEIGHT + BRICK_HEIGHT) {
                    bricks[i][j] = false;
                    score++;
                    ballDY = -ballDY;
                }
            }
        }

        // Game over
        if (ballY > HEIGHT) {
            gameOver = true;
        }

        // Restart game
        if (restart) {
            gameOver = false;
            restart = false;
            score = 0;
            ballX = WIDTH / 2;
            ballY = HEIGHT / 2;
            ballDX = 2;
            ballDY = -2;
            paddleX = WIDTH / 2;
            paddleY = HEIGHT - PADDLE_HEIGHT - 20;

            // Reset bricks
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 5; j++) {
                    bricks[i][j] = true;
                }
            }
        }

        // Repaint the panel
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Brick Breaker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BrickBreaker());
        frame.pack();
        frame.setVisible(true);
    }
}
