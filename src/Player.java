import java.awt.image.BufferedImage;
import java.net.ServerSocket;

public class Player extends Character {
    public Player(int x, int y, BufferedImage image, int direction) {
        super(x, y, image, direction);
    }

    @Override
    public void move() {
        super.move();
        switch(direction) {
            case 4:
                if(!outOfUpBounds()) y -= speed;
                break;
            case 1:
                if(!outOfDownBounds()) y += speed;
                break;
            case 2:
                if(!outOfLeftBounds()) x -= speed;
                break;
            case 3:
                if(!outOfRightBounds()) x += speed;
                break;
        }
        if(direction != 0 && direction != direction_cur) direction_cur = direction;
        if(direction != 0) turn = (turn + 1) % 4;
        direction = 0;
    }
}
