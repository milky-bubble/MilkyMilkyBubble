import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;


public class GameFrame extends JFrame{
    private static GameFrame gameFrame;
    private static JPanel contentPane;
    private static CardLayout layout;
    private static BeginJPanel beginJPanel;
    private static GameJPanel gameJPanel;
    private static OverJPanel overJPanel;
    private GameFrame() throws IOException {
        this.setSize(Config.WINDOW_WIDTH + Config.PLAYER_INFO, Config.WINDOW_HEIGHT + Config.BOARDER);
        this.setTitle("Milky Milky Bubble");

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.contentPane = new JPanel();
        this.setContentPane(contentPane);

        this.layout = new CardLayout();
        this.contentPane.setLayout(layout);

        this.beginJPanel = new BeginJPanel();
        this.contentPane.add("begin", beginJPanel);

        this.layout.show(contentPane, "begin");
        this.setVisible(true);

    }

    public static GameFrame getGameFrame() throws IOException {
        if (gameFrame == null) {
            gameFrame = new GameFrame();
        }
        return gameFrame;
    }


    public static void startGame() throws IOException {
        GameFrame.getGameFrame().setVisible(false);
        GameFrame.getGameFrame().setVisible(true);

        gameJPanel = new GameJPanel();
        contentPane.add("game", gameJPanel);
        layout.show(contentPane, "game");

        Thread gameThread = new Thread(gameJPanel);
        gameThread.start();

    }

    public static void gameOver() throws IOException {
        GameFrame.getGameFrame().setVisible(false);
        GameFrame.getGameFrame().setVisible(true);

        overJPanel = new OverJPanel();
        contentPane.add("over", overJPanel);
        layout.show(contentPane, "over");
    }

    public static void gameRestart() throws IOException {
        GameFrame.getGameFrame().setVisible(false);
        GameFrame.getGameFrame().setVisible(true);

        beginJPanel = new BeginJPanel();
        contentPane.add("begin", beginJPanel);
        layout.show(contentPane, "begin");
    }

    public static void main(String[] args) throws IOException {
        ElementLoader elementLoader = ElementLoader.getElementLoader();
        GameFrame gameFrame = getGameFrame();
    }
}


