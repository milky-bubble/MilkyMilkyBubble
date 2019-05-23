import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class GameJPanel extends JPanel implements Runnable{
    private Image offScreenImage;
    private GameMap gameMap;

    public GameJPanel() throws IOException {
        gameMap = new GameMap();
        offScreenImage = null;
        this.setVisible(true);
        this.setFocusable(true);
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

    @Override
    public void paint(Graphics g) {
        g.drawImage(offScreenImage, 0, 0, null);
    }

    @Override
    public void run() {
        while(true) {
            if(GameMap.getPlayer(1)==null) {
                try {
                    GameFrame.gameOver();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            gameMap.update();
            offScreenImage = this.createImage(Config.WINDOW_WIDTH+Config.PLAYER_INFO, Config.WINDOW_HEIGHT+ Config.BOARDER);
            Graphics gOff = offScreenImage.getGraphics();

            gameMap.drawMap(gOff);
            repaint();
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
