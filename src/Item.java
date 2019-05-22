import java.awt.*;
import java.awt.image.BufferedImage;

public class Item {
    private int x, y;
    private int turn;
    private boolean alive;
    private int category;

    // Constructor
    public Item(int x, int y, int turn, int category) {
        this.x = x;
        this.y = y;
        this.turn = turn;
        this.alive = true;
        this.category = category;
    }

    // Being Picked Up
    public void pickedUp(int playerId) {
        Character player = GameMap.getPlayer(playerId);
        switch(category) {
            case 0:
                if(player.getBubblePower()<Config.POWERMAX)
                    player.setBubblePower(player.getBubblePower()+1);
                break;
            case 1:
                if(player.getBubbleNumMax()<Config.BUBBLEMAX)
                    player.setBubbleNumMax(player.getBubbleNumMax()+1);
                break;
            case 2:
                if(player.getLife()<Config.LIFEMAX)
                    player.setLife(player.getLife()+1);
        }
    }

    // Draw Self Function
    public void drawSelf(Graphics g, BufferedImage itemImg, int width, int height) {
        if(!alive) return;
        int dx1 = x*Config.BLOCK_SIZE;
        int dy1 = Config.BOARDER + y*Config.BLOCK_SIZE;
        int dx2 = dx1 + Config.BLOCK_SIZE;
        int dy2 = dy1 + Config.BLOCK_SIZE;
        int sx1 = (turn/16)*width/4;
        int sy1 = 0;
        int sx2 = sx1 + width/4;
        int sy2 = height;
        turn = ((turn + 1) % 64);
        g.drawImage(itemImg, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

    }

    // Getters & Setters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public int getCategory() {
        return category;
    }
}
