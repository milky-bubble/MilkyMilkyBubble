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
    public static int timeCount = 0;
    private int minute0, minute1;
    private int second0, second1;
    private JLabel time, player01, player02, player03, player04;
    private JLabel img1, img2, img3, img4;
    private JLabel title1, title2, title3, title4;
    private static JLabel[] status;
//    private JLabel status1, status2, status3, status4;
    private static Character[] player;
    public GameJPanel() throws IOException {
        gameMap = new GameMap();
        offScreenImage = null;
        this.setVisible(true);
        this.minute0 = 0;
        this.minute1 = 0;
        this.second0 = 0;
        this.second1 = 0;
        player = new Character[4];
        status = new JLabel[4];
        for(int i=0; i<4; i++) player[i] = GameMap.getPlayer(i+1);

        this.setLayout(null);

        time = new JLabel("Time:  " + minute1 + minute0 + ":" + second1 + second0 + "  ", JLabel.CENTER);
        time.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        time.setBounds(Config.WINDOW_WIDTH, 0, Config.PLAYER_INFO, 70);
        this.add(time);

        player01 = new JLabel();
        player01.setBounds(Config.WINDOW_WIDTH, 70, Config.PLAYER_INFO, 170);
        player01.setOpaque(true);
        player01.setBackground(new Color(184, 214, 231));
        this.add(player01);

        img1 = new JLabel(ElementLoader.gameImageMap.get("player01card"));
        img1.setBounds( 10, 10, 40, 52);
        img1.setOpaque(false);
        player01.add(img1);

        title1 = new JLabel("PLAYER 01", JLabel.CENTER);
        title1.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        title1.setBounds(60, 20, 100, 30);
        player01.add(title1);

        player02 = new JLabel();
        player02.setBounds(Config.WINDOW_WIDTH, 240, Config.PLAYER_INFO, 170);
        player02.setOpaque(true);
        player02.setBackground(new Color(253, 238, 132));
        this.add(player02);

        img2 = new JLabel(ElementLoader.gameImageMap.get("player02card"));
        img2.setBounds( 10, 10, 40, 52);
        img2.setOpaque(false);
        player02.add(img2);

        title2 = new JLabel("PLAYER 02", JLabel.CENTER);
        title2.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        title2.setBounds(60, 20, 100, 30);
        player02.add(title2);

        player03 = new JLabel();
        player03.setBounds(Config.WINDOW_WIDTH, 410, Config.PLAYER_INFO, 170);
        player03.setOpaque(true);
        player03.setBackground(new Color(249, 213, 228));
        this.add(player03);

        img3 = new JLabel(ElementLoader.gameImageMap.get("player03card"));
        img3.setBounds( 10, 10, 40, 52);
        img3.setOpaque(false);
        player03.add(img3);

        title3 = new JLabel("PLAYER 03", JLabel.CENTER);
        title3.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        title3.setBounds(60, 20, 100, 30);
        player03.add(title3);

        player04 = new JLabel();
        player04.setBounds(Config.WINDOW_WIDTH, 580, Config.PLAYER_INFO, 170);
        player04.setOpaque(true);
        player04.setBackground(new Color(249, 226, 189));
        this.add(player04);

        img4 = new JLabel(ElementLoader.gameImageMap.get("player04card"));
        img4.setBounds( 10, 10, 40, 52);
        img4.setOpaque(false);
        player04.add(img4);

        title4 = new JLabel("PLAYER 04", JLabel.CENTER);
        title4.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        title4.setBounds(60, 20, 100, 30);
        player04.add(title4);

        for(int i=0; i<4; i++) {
            status[i] = new JLabel("<html>Life: "+player[i].getLife()+
                    "<br>Bubble Number: "+player[i].getBubbleNumMax()+
                    "<br>Bubble Power: "+player[i].getBubblePower()+
                    "<br>Score: "+player[i].getScore()+"</html>", JLabel.CENTER);
            status[i].setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
            status[i].setBounds(10, 60, 150, 100);
            switch(i) {
                case 0: player01.add(status[i]); break;
                case 1: player02.add(status[i]); break;
                case 2: player03.add(status[i]); break;
                case 3: player04.add(status[i]);
            }
        }

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
                    case KeyEvent.VK_SPACE: gameMap.getPlayer(1).addBubble(); break;
                    default: gameMap.getPlayer(1).setDirection(0);
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    private int highestScore() {
        int Max = -1;
        for(int i=1; i<=4; i++) {
            if(GameMap.getPlayer(i)!=null && GameMap.getPlayer(i).getScore()>Max) Max = i;
        }
        return Max;
    }
    @Override
    public void run() {
        while(true) {
            timeCount++;
            showTime();
            if(GameMap.getPlayer(1)==null || (timeCount>40*600 && highestScore()!=GameMap.getPlayer(1).getScore())) {
                try {
                    sleep(1000);
                    GameFrame.gameOver();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            else if(timeCount>40*600 ||
                    ((GameMap.getPlayer(4)==null&&GameMap.getPlayer(2)==null&&GameMap.getPlayer(3)==null))) {
                try {
                    sleep(1000);
                    GameFrame.gameWin();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }

            offScreenImage = this.createImage(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT+ Config.BOARDER);
            Graphics gOff = offScreenImage.getGraphics();
            gameMap.update();
            gameMap.drawMap(gOff);
            if(GameMap.getPlayer(1)!=null) GameMap.getPlayer(1).updateSelf(gOff);
//            if(GameMap.getPlayer(2)!=null) GameMap.getPlayer(2).updateSelf(gOff);
            if(GameMap.getPlayer(3)!=null) GameMap.getPlayer(3).updateSelf(gOff);
            repaint();
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void showTime() {
        if(timeCount % 40 == 0) {
            second0++;
            if(second0 == 10) {
                second0 = 0;
                second1++;
                if(second1 == 6) {
                    second1 = 0;
                    minute0++;
                    if(minute0 == 10) {
                        minute0 = 0;
                        minute1++;
                    }
                }
            }
            time.setText("Time:  " + minute1 + minute0 + ":" + second1 + second0 + "  ");
        }
    }

    public static void setStatusText(int id) {
        id--;
        status[id].setText("<html>Life: "+player[id].getLife()+
                "<br>Bubble Number: "+player[id].getBubbleNumMax()+
                "<br>Bubble Power: "+player[id].getBubblePower()+
                "<br>Score: "+player[id].getScore()+"</html>");
    }

    public static void setDead(int id) {
        id--;
        status[id].setText("DEAD");
    }

}
