import java.awt.image.BufferedImage;

public class Player extends Character {
    public Player(int x, int y, int id, BufferedImage image, int direction) {
        super(x, y, id, image, direction);
    }

    @Override
    public void move() {
        if(dead) return;
        switch(direction) {
            case 4:
                if(!outOfUpBounds() && !crashUp()) y -= speed;
                break;
            case 1:
                if(!outOfDownBounds() && !crashDown()) y += speed;
                break;
            case 2:
                if(!outOfLeftBounds() && !crashLeft()) x -= speed;
                break;
            case 3:
                if(!outOfRightBounds() && !crashRight()) x += speed;
                break;
        }
        if(direction != 0 && direction != direction_cur) direction_cur = direction;
        if(direction != 0) turn = (turn + 1) % 4;
        direction = 0;
        pickItem();
    }
}
