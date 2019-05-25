import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Thread.sleep;

public class Player extends Character {
    public Player(int x, int y, int id, BufferedImage image, int direction) {
        super(x, y, id, image, direction);
    }

//    public void paint(Graphics g) {
//        drawSelf(g, id);
//    }
//
//    @Override
//    public void run() {
//        while(true) {
//            drawSelf();
//            try {
//                sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void move() {
        if(dead) return;
        switch(direction) {
            case 4:
                if(!crashUp()) {
                    y -= 1;
                }
                break;
            case 1:
                if(!crashDown()) {
                    y += 1;
                }
                break;
            case 2:
                if(!crashLeft()) {
                    x -= 1;
                }
                break;
            case 3:
                if(!crashRight()) {
                    x += 1;
                }
                break;
        }
        if(direction != 0) direction_cur = direction;
        if(direction != 0) turn = (turn + 1) % 4;
        direction = 0;
        pickItem();
    }
}
