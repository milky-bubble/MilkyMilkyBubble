import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Bubble {
    private int x, y;
    private int power;
    private int turn;
    private int playerId;
    private boolean alive;

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Bubble(int x, int y, int power, int playerId) {
        this.x = x;
        this.y = y;
        this.power = power;
        this.turn = 0;
        this.alive = true;
        this.playerId = playerId;
    }

    public void lastForCertainTime() {
        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                setAlive(false);
                Player cur = GameMap.getPlayer(playerId);
                cur.setBubbleNum(cur.getBubbleNum()-1);
                bubbleExplode();
                System.gc();
            }
        };
        timer.schedule(task, 2500);
    }

    public void bubbleExplode() {
        MapBlock mb[][] = GameMap.getBlock();
        for(int i=1; i<=power; i++) {
            if(y+i>Config.GAME_HEIGHT) break;
            if(!mb[y+i][x].isDestructible()) break;
            mb[y+i][x] = new MapBlock(GameMap.getFloorImg(), "00", x, y + i, false, true);

            Random rand = new Random();
            if(rand.nextInt(100)>5) continue;
            Item item = new Item(x, y+i, 0, playerId, rand.nextInt(2));
            GameMap.getItems().add(item);
        }
        for(int i=1; i<=power; i++) {
            if(y-i<0) break;
            if(!mb[y-i][x].isDestructible()) break;
            mb[y-i][x] = new MapBlock(GameMap.getFloorImg(), "00", x, y - 1, false, true);

            Random rand = new Random();
            if(rand.nextInt(100)>5) continue;
            Item item = new Item(x, y-i, 0, playerId, rand.nextInt(2));
            GameMap.getItems().add(item);
        }
        for(int i=1; i<=power; i++) {
            if(x+i>Config.GAME_WIDTH) break;
            if(!mb[y][x+i].isDestructible()) break;
            mb[y][x+i] = new MapBlock(GameMap.getFloorImg(), "00", x + i, y, false, true);

            Random rand = new Random();
            if(rand.nextInt(100)>5) continue;
            Item item = new Item(x+i, y, 0, playerId, rand.nextInt(2));
            GameMap.getItems().add(item);
        }
        for(int i=1; i<=power; i++) {
            if(x-i<0) break;
            if(!mb[y][x-i].isDestructible()) break;
            mb[y][x-i] = new MapBlock(GameMap.getFloorImg(), "00", x-i, y, false, true);

            Random rand = new Random();
            if(rand.nextInt(100)>5) continue;
            Item item = new Item(x-i, y, 0, playerId, rand.nextInt(2));
            GameMap.getItems().add(item);
        }
    }

    public void drawSelf(Graphics g, BufferedImage bubbleImg, int width, int height) {
        if(alive == false) return;
        int dx1 = x*Config.BLOCK_SIZE;
        int dy1 = Config.BOARDER + y*Config.BLOCK_SIZE;
        int dx2 = dx1 + Config.BLOCK_SIZE;
        int dy2 = dy1 + Config.BLOCK_SIZE;
        int sx1 = (turn/9)*width/3;
        int sy1 = 0;
        int sx2 = sx1 + width/3;
        int sy2 = height;
        turn = ((turn + 1) % 27);
        g.drawImage(bubbleImg, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
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

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
