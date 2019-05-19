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
            case 1: y -= speed; break;
            case 2: y += speed; break;
            case 3: x -= speed; break;
            case 4: x += speed; break;
        }
        direction = 0;
    }
}
