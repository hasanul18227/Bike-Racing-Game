import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BikeRacingGame extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 400;
    int boardHeight = 640;

    // Images
    Image roadImg;
    Image bikeImg;
    Image carImg;

    // Player (bike)
    int bikeWidth = 50;
    int bikeHeight = 100;
    int bikeX = boardWidth / 2 - bikeWidth / 2;
    int bikeY = boardHeight - bikeHeight - 30;

    class Bike {
        int x, y, width, height;
        Image img;

        Bike(Image img) {
            this.x = bikeX;
            this.y = bikeY;
            this.width = bikeWidth;
            this.height = bikeHeight;
            this.img = img;
        }
    }

    class Car {
        int x, y, width, height;
        Image img;
        int speed;

        Car(Image img, int x, int y, int speed) {
            this.x = x;
            this.y = y;
            this.width = 50;
            this.height = 100;
            this.img = img;
            this.speed = speed;
        }
    }

    Bike bike;
    ArrayList<Car> cars = new ArrayList<>();
    Timer gameLoop;
    Timer spawnCarTimer;
    Random random = new Random();
    boolean gameOver = false;
    int score = 0;

    BikeRacingGame() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Load images
        roadImg = new ImageIcon(getClass().getResource("./road.png")).getImage();
        bikeImg = new ImageIcon(getClass().getResource("./bike.png")).getImage();
        carImg = new ImageIcon(getClass().getResource("./car.png")).getImage();

        bike = new Bike(bikeImg);

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        spawnCarTimer = new Timer(1300, e -> spawnCar());
        spawnCarTimer.start();
    }

    void spawnCar() {
        int lane = random.nextInt(boardWidth - 50);
        int speed = 4 + random.nextInt(4);
        cars.add(new Car(carImg, lane, -100, speed));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    void draw(Graphics g) {
        g.drawImage(roadImg, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(bike.img, bike.x, bike.y, bike.width, bike.height, null);

        for (Car car : cars) {
            g.drawImage(car.img, car.x, car.y, car.width, car.height, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        if (gameOver) {
            g.drawString("Game Over! Score: " + score, 60, 300);
        } else {
            g.drawString("Score: " + score, 10, 30);
        }
    }

    void move() {
        if (gameOver) return;

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            car.y += car.speed;

            if (car.y > boardHeight) {
                cars.remove(i);
                i--;
                score++;
                continue;
            }

            if (collision(bike, car)) {
                gameOver = true;
                gameLoop.stop();
                spawnCarTimer.stop();
            }
        }
    }

    boolean collision(Bike a, Car b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
            restartGame();
        }

        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && bike.x > 0) {
                bike.x -= 20;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && bike.x + bike.width < boardWidth) {
                bike.x += 20;
            }
        }
    }

    void restartGame() {
        bike.x = bikeX;
        cars.clear();
        score = 0;
        gameOver = false;
        gameLoop.start();
        spawnCarTimer.start();
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bike Racing Game");
        BikeRacingGame game = new BikeRacingGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
