import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class GameView extends JFrame implements Runnable {
    private GameMap gameMap = new GameMap();
    private Image offScreenImage = null;

    public GameView() throws IOException {
        this.setSize(Config.WINDOW_WIDTH + Config.PLAYER_INFO, Config.WINDOW_HEIGHT+ Config.BOARDER);
        this.setTitle("Milky Milky Bubble");
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(GameMap.getPlayer(1)==null) return;
                super.keyPressed(e);
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP: gameMap.getPlayer(1).setDirection(4);break;
                    case KeyEvent.VK_DOWN: gameMap.getPlayer(1).setDirection(1); break;
                    case KeyEvent.VK_LEFT: gameMap.getPlayer(1).setDirection(2); break;
                    case KeyEvent.VK_RIGHT: gameMap.getPlayer(1).setDirection(3); break;
                    case KeyEvent.VK_ENTER: gameMap.getPlayer(1).addBubble(); break;
                    default: gameMap.getPlayer(1).setDirection(0);
                }
            }
        });
    }

    public void paint(Graphics g) {
        g.drawImage(offScreenImage, 0, 0, null);
    }

    @Override
    public void run() {
        while(true) {
            gameMap.update();
            offScreenImage = this.createImage(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT+ Config.BOARDER);
            Graphics gOff = offScreenImage.getGraphics();
            gameMap.drawMap(gOff);
            repaint();

            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        GameView gameView = new GameView();
        gameView.run();
    }

}
