import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

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
    protected int count;
    protected int score;

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
        this.score = 0;
        this.count = 10;
    }

    MapBlock[][] mb = GameMap.getBlock();

    public void updateSelf(Graphics g) {
        move();
        if(direction != 0 && count > 5) {
            count = 0;
            direction_cur = direction;
            direction = 0;
        }
        count++;
        int dx1 = x * Config.BLOCK_SIZE;
        int dy1 = y * Config.BLOCK_SIZE;
        int dx2 = dx1 + Config.BLOCK_SIZE;
        int dy2 = dy1 + Config.BLOCK_SIZE;
        int sx1 = turn * image.getWidth() / 4;
        int sy1 = (direction_cur - 1) * image.getHeight()/4;
        int sx2 = sx1 + image.getWidth() / 4;
        int sy2 = sy1 + image.getHeight()/4;
        if(count>5) {
            g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
            return;
        }
        switch(direction_cur) {
            case 4:
                if (!crashUp()) {
                    dy1 -= count * Config.STEP;
                    dy2 -= count * Config.STEP;
                    turn = (turn + 1) % 4;
                    g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
                    if (count == 5) {
                        y -= 1;
                        pickItem();
                    }
                }
                else g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

                break;
            case 1:
                if (!crashDown()) {
                    dy1 += count * Config.STEP;
                    dy2 += count * Config.STEP;
                    turn = (turn + 1) % 4;
                    g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
                    if (count == 5) {
                        y += 1;
                        pickItem();
                    }
                }
                else g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

                break;
            case 2:
                if (!crashLeft()) {
                    dx1 -= count * Config.STEP;
                    dx2 -= count * Config.STEP;
                    turn = (turn + 1) % 4;
                    g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
                    if (count == 5) {
                        x -= 1;
                        pickItem();
                    }
                }
                else g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

                break;
            case 3:
                if (!crashRight()) {
                    dx1 += count * Config.STEP;
                    dx2 += count * Config.STEP;
                    turn = (turn + 1) % 4;
                    g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
                    if (count == 5) {
                        x += 1;
                        pickItem();
                    }
                }
                else g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
                break;
        }

    }

    public void drawSelf(Graphics g, int id) {
        BufferedImage image = ElementLoader.playerImageMap.get(id);
        int dx1 = x * Config.BLOCK_SIZE;
        int dy1 = y * Config.BLOCK_SIZE;
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
        Bubble b = new Bubble(x, y, bubblePower, id, GameJPanel.timeCount);
        bubbleNum++;
        GameMap.getBubbles().add(b);
        mb[y][x].setHasBubble(true);
//        mb[y][x].setWalkable(false);
        // b.lastForCertainTime();
    }

    public void pickItem() {
        if(dead) return;
        for(int i=0; i<GameMap.getItems().size(); i++) {
            if(!GameMap.getItems().get(i).isAlive()) continue;
            if(x == GameMap.getItems().get(i).getX()
            && y == GameMap.getItems().get(i).getY()) {
                GameMap.getItems().get(i).setAlive(false);
                GameMap.getItems().get(i).pickedUp(id);
            }
        }
    }

    public boolean crashDown() {
        if(y+1>=Config.GAME_HEIGHT) return true;
        if(!mb[y+1][x].isWalkable() || mb[y+1][x].isHasBubble()) return true;
        return false;
    }

    public boolean crashUp() {
        if(y-1<0)return true;
        if(!mb[y-1][x].isWalkable() || mb[y-1][x].isHasBubble()) return true;
        return false;
    }

    public boolean crashLeft() {
        if(x-1<0)return true;
        if(!mb[y][x-1].isWalkable() || mb[y][x-1].isHasBubble()) return true;
        return false;
    }

    public boolean crashRight() {
        if(x+1>=Config.GAME_WIDTH) return true;
        if(!mb[y][x+1].isWalkable() || mb[y][x+1].isHasBubble()) return true;
        return false;
    }


    public void move() {}

    // Getters and Setters
    public void setDirection(int direction) {
        this.direction = direction;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public void setDead(boolean dead) {
        this.dead = dead;
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

    public int getScore() {
        return score;
    }
}
