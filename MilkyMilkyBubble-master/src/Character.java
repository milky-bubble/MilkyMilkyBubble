import java.awt.image.BufferedImage;

public class Character {
    protected int x, y;
    protected BufferedImage image;
    protected boolean dead;
    protected int speed;
    protected int direction;

    public Character(int x, int y, BufferedImage image, int direction) {
        this.x = x;
        this.y = y;
        this.dead = false;
        this.image = image;
        this.speed = Config.INIT_SPEED;
        this.direction = direction;
    }

    public void move() {}

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }
}
