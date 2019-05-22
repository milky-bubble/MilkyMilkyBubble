import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Bubble {
    private int x, y;
    private int power;
    private int turn;
    private int playerId;
    private boolean alive;

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
                alive = false;
                Character cur = GameMap.getPlayer(playerId);
                cur.setBubbleNum(cur.getBubbleNum()-1);
                bubbleExplode();
                System.gc();
            }
        };
        timer.schedule(task, 2500);
    }

    public void bubbleExplode() {
        MapBlock mb[][] = GameMap.getBlock();

        // Center Kill Players
        for(int i=1; i<=4; i++) {
            Character player = GameMap.getPlayer(i);
            if(player==null) continue;
            if(player.getX() == x && player.getY() == y) {
                player.setLife(player.getLife()-1);
                if(player.getLife()==0) GameMap.removePlayer(i);
            }
        }

        // Down
        for(int i=1; i<=power; i++) {
            // Block Into Floor
            if(y+i>=Config.GAME_HEIGHT) break;
            if(!mb[y+i][x].isDestructible() && !mb[y+i][x].isWalkable()) break;
            mb[y+i][x] = new MapBlock(ElementLoader.blockImageMap.get("00"), "00", x, y + i, false, true);

            // Random Items
            Random rand = new Random();
            if(rand.nextInt(100)>50) {
                Item item = new Item(x, y+i, 0, rand.nextInt(3));
                GameMap.getItems().add(item);
            }

            // Kill Players
            for(int j=1; j<=4; j++) {
                Character player = GameMap.getPlayer(j);
                if(player==null) continue;
                if(player.getX()==x && player.getY()==y+i) {
                    player.setLife(player.getLife()-1);
                    if(player.getLife()==0) {
                        player.setDead(true);
                        GameMap.removePlayer(j);
                    }
                }
            }
        }

        // Up
        for(int i=1; i<=power; i++) {
            // Bloc Into Floor
            if(y-i<0) break;
            if(!mb[y-i][x].isDestructible() && !mb[y-i][x].isWalkable()) break;
            mb[y-i][x] = new MapBlock(ElementLoader.blockImageMap.get("00"), "00", x, y - i, false, true);

            // Random Items
            Random rand = new Random();
            if(rand.nextInt(100)>50) {
                Item item = new Item(x, y-i, 0, rand.nextInt(3));
                GameMap.getItems().add(item);
            }

            // Kill Players
            for(int j=1; j<=4; j++) {
                Character player = GameMap.getPlayer(j);
                if(player==null) continue;
                if(player.getX()==x && player.getY()==y-i) {
                    player.setLife(player.getLife()-1);
                    if(player.getLife()==0) {
                        player.setDead(true);
                        GameMap.removePlayer(j);
                    }
                }
            }

        }

        // Right
        for(int i=1; i<=power; i++) {
            // Block Into Floor
            if(x+i>=Config.GAME_WIDTH) break;
            if(!mb[y][x+i].isDestructible() && !mb[y][x+i].isWalkable()) break;
            mb[y][x+i] = new MapBlock(ElementLoader.blockImageMap.get("00"), "00", x + i, y, false, true);

            // Random Item
            Random rand = new Random();
            if(rand.nextInt(100)>50) {
                Item item = new Item(x+i, y, 0, rand.nextInt(3));
                GameMap.getItems().add(item);
            }

            // Kill Players
            for(int j=1; j<=4; j++) {
                Character player = GameMap.getPlayer(j);
                if(player==null) continue;
                if(player.getX()==x+i && player.getY()==y) {
                    player.setLife(player.getLife()-1);
                    if(player.getLife()==0) {
                        player.setDead(true);
                        GameMap.removePlayer(j);
                    }
                }
            }
        }

        // Left
        for(int i=1; i<=power; i++) {
            // Block Into Floor
            if(x-i<0) break;
            if(!mb[y][x-i].isDestructible() && !mb[y][x-i].isWalkable()) break;
            mb[y][x-i] = new MapBlock(ElementLoader.blockImageMap.get("00"), "00", x-i, y, false, true);

            // Random Item
            Random rand = new Random();
            if(rand.nextInt(100)>50) {
                Item item = new Item(x-i, y, 0, rand.nextInt(3));
                GameMap.getItems().add(item);
            }

            // Kill Players
            for(int j=1; j<=4; j++) {
                Character player = GameMap.getPlayer(j);
                if(player==null) continue;
                if(player.getX()==x-i && player.getY()==y) {
                    player.setLife(player.getLife()-1);
                    if(player.getLife()==0) {
                        player.setDead(true);
                        GameMap.removePlayer(j);
                    }
                }
            }
        }
    }

    public void drawSelf(Graphics g, BufferedImage bubbleImg, int width, int height) {
        if(!alive) return;
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

    // Getters
    public boolean isAlive() { return alive; }

}
