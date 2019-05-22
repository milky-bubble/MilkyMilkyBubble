import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class Character {
    protected int x, y;
    protected BufferedImage image;
    protected int id;
    protected boolean dead;
    protected int direction;
    protected int turn;
    protected int direction_cur;
    protected int bubbleNum;
    protected int bubblePower;
    protected int bubbleNumMax;
    protected int life;

    public Character(int x, int y, int id, BufferedImage image, int direction) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.dead = false;
        this.image = image;
        this.direction = direction;
        this.turn = 0;
        this.direction_cur = 1;
        this.bubbleNum = 0;
        this.bubbleNumMax = 1;
        this.bubblePower = 1;
        this.life = 1;
    }

    MapBlock[][] mb = GameMap.getBlock();

    public void drawSelf(Graphics g, int id) {
        BufferedImage image = ElementLoader.playerImageMap.get(id);
        int dx1 = x;
        int dy1 = Config.BOARDER + y;
        int dx2 = dx1 + Config.BLOCK_SIZE;
        int dy2 = dy1 + Config.BLOCK_SIZE;
        int sx1 = turn * image.getWidth() / 4;
        int sy1 = (direction_cur - 1) * image.getHeight()/4;
        int sx2 = sx1 + image.getWidth() / 4;
        int sy2 = sy1 + image.getHeight()/4;
        g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }

    public void addBubble() {
        if(dead) return;
        if(bubbleNum == bubbleNumMax) return;
        Bubble b = new Bubble(x/Config.BLOCK_SIZE, y/Config.BLOCK_SIZE, bubblePower, id);
        bubbleNum++;
        GameMap.getBubbles().add(b);
        b.lastForCertainTime();
    }

    public void pickItem() {
        if(dead) return;
        for(int i=0; i<GameMap.getItems().size(); i++) {
            if(!GameMap.getItems().get(i).isAlive()) continue;
            if(x/Config.BLOCK_SIZE == GameMap.getItems().get(i).getX()
            && y/Config.BLOCK_SIZE == GameMap.getItems().get(i).getY()) {
                GameMap.getItems().get(i).setAlive(false);
                GameMap.getItems().get(i).pickedUp(id);
            }
        }
    }

    public boolean crashDown() {
        if(outOfDownBounds()) return true;
        int ax = (int)Math.ceil((double)(y+speed)/Config.BLOCK_SIZE);
        int ay1 = (int)Math.floor((double)x/Config.BLOCK_SIZE);
        int ay2 = (int)Math.ceil((double)x/Config.BLOCK_SIZE);
        if(mb[ax][ay1].isWalkable()==false || mb[ax][ay2].isWalkable()==false) return true;
        return false;
    }

    public boolean crashUp() {
        if(outOfUpBounds()) return true;
        int ax = (int)Math.floor((double)(y-speed)/Config.BLOCK_SIZE);
        int ay1 = (int)Math.floor((double)x/Config.BLOCK_SIZE);
        int ay2 = (int)Math.ceil((double)x/Config.BLOCK_SIZE);
        if(mb[ax][ay1].isWalkable()==false || mb[ax][ay2].isWalkable()==false) return true;
        return false;
    }

    public boolean crashLeft() {
        if(outOfLeftBounds()) return true;
        int ax1 = (int)Math.floor((double)y/Config.BLOCK_SIZE);
        int ax2 = (int)Math.ceil((double)y/Config.BLOCK_SIZE);
        int ay = (int)Math.floor((double)(x-speed)/Config.BLOCK_SIZE);
        if(mb[ax1][ay].isWalkable()==false || mb[ax2][ay].isWalkable()==false) return true;
        return false;
    }

    public boolean crashRight() {
        if(outOfRightBounds()) return true;
        int ax1 = (int)Math.ceil((double)y/Config.BLOCK_SIZE);
        int ax2 = (int)Math.floor((double)y/Config.BLOCK_SIZE);
        int ay = (int)Math.ceil((double)(x+speed)/Config.BLOCK_SIZE);
        if(mb[ax1][ay].isWalkable()==false || mb[ax2][ay].isWalkable()==false) return true;
        return false;
    }

    public boolean outOfLeftBounds() {
        return x-speed < 0;
    }

    public boolean outOfRightBounds() {
        return x+speed > Config.WINDOW_WIDTH-Config.BLOCK_SIZE;
    }

    public boolean outOfUpBounds() {
        return y-speed < 0;
    }

    public boolean outOfDownBounds() {
        return y+speed > Config.WINDOW_HEIGHT-Config.BLOCK_SIZE;
    }

    public void move() {}

    public int getDirection_cur() {
        return direction_cur;
    }

    public void setDirection_cur(int direction_cur) {
        this.direction_cur = direction_cur;
    }

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

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBubbleNum() {
        return bubbleNum;
    }

    public void setBubbleNum(int bubbleNum) {
        this.bubbleNum = bubbleNum;
    }

    public int getBubblePower() {
        return bubblePower;
    }

    public void setBubblePower(int bubblePower) {
        this.bubblePower = bubblePower;
    }

    public int getBubbleNumMax() {
        return bubbleNumMax;
    }

    public void setBubbleNumMax(int bubbleNumMax) {
        this.bubbleNumMax = bubbleNumMax;
    }

    public MapBlock[][] getMb() {
        return mb;
    }

    public void setMb(MapBlock[][] mb) {
        this.mb = mb;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

}
