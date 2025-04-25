import javax.swing.*;

public class App {
    public static void main(String[] args) {
        int boardWidth = 400;
        int boardHeight = 640;

        JFrame frame = new JFrame("Bike Racing Game");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); 
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BikeRacingGame game = new BikeRacingGame();
        frame.add(game);
        frame.pack();
        game.requestFocus();
        frame.setVisible(true);
    }
}
