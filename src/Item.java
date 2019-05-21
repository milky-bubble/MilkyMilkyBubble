import java.awt.*;
import java.awt.image.BufferedImage;

public class Item {
    private int x, y;
    private int turn;
    private int playerId;
    private boolean alive;
    private int category;

    public Item(int x, int y, int turn, int playerId, int category) {
        this.x = x;
        this.y = y;
        this.turn = turn;
        this.playerId = playerId;
        this.alive = true;
        this.category = category;
    }


    public void pickedUp(int playerId) {
        Player player = GameMap.getPlayer(playerId);
        switch(category) {
            case 0:
                if(player.getBubblePower()<Config.POWERMAX)
                    player.setBubblePower(player.getBubblePower()+1);
                break;
            case 1:
                if(player.getBubbleNumMax()<Config.BUBBLEMAX)
                    player.setBubbleNumMax(player.getBubbleNumMax()+1);
                break;
        }
    }

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

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
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

    public void setCategory(int category) {
        this.category = category;
    }
}
