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
    private int startTime;
    private boolean alive;
    private int count;
    private int left, right, up, down;

    public Bubble(int x, int y, int power, int playerId, int startTime) {
        this.x = x;
        this.y = y;
        this.power = power;
        this.turn = 0;
        this.alive = true;
        this.playerId = playerId;
        this.startTime = startTime;
        this.count = 0;
        this.left = 0;
        this.right = 0;
        this.up = 0;
        this.down = 0;
    }

    public void lastForCertainTime() {
        if(GameJPanel.timeCount-startTime == 50) {
            alive = false;
            Character cur = GameMap.getPlayer(playerId);
            if(cur!=null) cur.setBubbleNum(cur.getBubbleNum()-1);
            bubbleExplode();
            GameMap.getBlock()[y][x].setHasBubble(false);
        }
    }

    private void calculateRange() {
        MapBlock mb[][] = GameMap.getBlock();
        for(int i=1; i<=power; i++) {
            if(y+i>=Config.GAME_HEIGHT) break;
            if(mb[y+i][x].isDestructible()) {
                down++;
                break;
            }
            else if(mb[y+i][x].isWalkable()) down++;
            else break;
        }
        for(int i=1; i<=power; i++) {
            if(y-i<0) break;
            if(mb[y-i][x].isDestructible()) {
                up++;
                break;
            }
            else if(mb[y-i][x].isWalkable()) up++;
            else break;
        }
        for(int i=1; i<=power; i++) {
            if(x-i<0) break;
            if(mb[y][x-i].isDestructible()) {
                left++;
                break;
            }
            else if(mb[y][x-i].isWalkable()) left++;
            else break;
        }
        for(int i=1; i<=power; i++) {
            if(x+i>=Config.GAME_WIDTH) break;
            if(mb[y][x+i].isDestructible()) {
                right++;
                break;
            }
            else if(mb[y][x+i].isWalkable()) right++;
            else break;
        }
    }

    public void bubbleExplode() {
        MapBlock mb[][] = GameMap.getBlock();
        calculateRange();
        // Center Kill Players
        for(int i=1; i<=4; i++) {
            Character player = GameMap.getPlayer(i);
            if(player==null) continue;
            if(player.getX() == x && player.getY() == y) {
                player.setLife(player.getLife() - 1);
                GameJPanel.setStatusText(i);
                if (player.getLife() == 0) {
                    GameMap.removePlayer(i);
                    GameJPanel.setDead(i);
                    if (GameMap.getPlayer(playerId) != null) GameMap.getPlayer(playerId).score += 500;
                }
            }
        }

        // Down
        for(int i=1; i<=down; i++) {
            boolean flag = mb[y+i][x].isWalkable();
            // Block Into Floor
            mb[y+i][x] = new MapBlock(ElementLoader.blockImageMap.get("00"), "00", x, y + i, false, true);
            if(GameMap.getPlayer(playerId) != null && !flag) GameMap.getPlayer(playerId).score += 20;

            // Random Items
            if(!flag) {
                Random rand = new Random();
                if (rand.nextInt(100) > 85) {
                    Item item = new Item(x, y + i, 0, rand.nextInt(3));
                    GameMap.getItems().add(item);
                }
                else if (rand.nextInt(200) > 199) {
                    Item item = new Item(x, y + i, 0, 2);
                    GameMap.getItems().add(item);
                }
            }
            // Kill Players
            for(int j=1; j<=4; j++) {
                Character player = GameMap.getPlayer(j);
                if(player==null) continue;
                if(player.getX()==x && player.getY()==y+i) {
                    player.setLife(player.getLife()-1);
                    GameJPanel.setStatusText(j);
                    if(player.getLife()==0) {
                        if(GameMap.getPlayer(playerId) != null) GameMap.getPlayer(playerId).score += 500;
                        player.setDead(true);
                        GameJPanel.setDead(j);
                        GameMap.removePlayer(j);
                    }
                }
            }
            if(GameMap.getPlayer(playerId)!=null) GameJPanel.setStatusText(playerId);
        }

        // Up
        for(int i=1; i<=up; i++) {
            boolean flag = mb[y-i][x].isWalkable();
            // Block Into Floor
            mb[y-i][x] = new MapBlock(ElementLoader.blockImageMap.get("00"), "00", x, y - i, false, true);
            if(GameMap.getPlayer(playerId) != null && !flag) GameMap.getPlayer(playerId).score += 20;

            // Random Items
            if(!flag) {
                Random rand = new Random();
                if (rand.nextInt(100) > 85) {
                    Item item = new Item(x, y - i, 0, rand.nextInt(3));
                    GameMap.getItems().add(item);
                } else if (rand.nextInt(200) > 199) {
                    Item item = new Item(x, y - i, 0, 2);
                    GameMap.getItems().add(item);
                }
            }

            // Kill Players
            for(int j=1; j<=4; j++) {
                Character player = GameMap.getPlayer(j);
                if(player==null) continue;
                if(player.getX()==x && player.getY()==y-i) {
                    player.setLife(player.getLife()-1);
                    GameJPanel.setStatusText(j);
                    if(player.getLife()==0) {
                        if(GameMap.getPlayer(playerId) != null) GameMap.getPlayer(playerId).score += 500;
                        player.setDead(true);
                        GameJPanel.setDead(j);
                        GameMap.removePlayer(j);
                    }
                }
            }
            if(GameMap.getPlayer(playerId)!=null) GameJPanel.setStatusText(playerId);
        }

        // Right
        for(int i=1; i<=right; i++) {
            boolean flag = mb[y][x+i].isWalkable();
            // Block Into Floor
            if(GameMap.getPlayer(playerId) != null && !flag) GameMap.getPlayer(playerId).score += 20;
            mb[y][x+i] = new MapBlock(ElementLoader.blockImageMap.get("00"), "00", x + i, y, false, true);

            // Random Item
            if(!flag) {
                Random rand = new Random();
                if (rand.nextInt(100) > 85) {
                    Item item = new Item(x + i, y, 0, rand.nextInt(3));
                    GameMap.getItems().add(item);
                } else if (rand.nextInt(200) > 199) {
                    Item item = new Item(x + i, y, 0, 2);
                    GameMap.getItems().add(item);
                }
            }

            // Kill Players
            for(int j=1; j<=4; j++) {
                Character player = GameMap.getPlayer(j);
                if(player==null) continue;
                if(player.getX()==x+i && player.getY()==y) {
                    player.setLife(player.getLife()-1);
                    GameJPanel.setStatusText(j);
                    if(player.getLife()==0) {
                        if(GameMap.getPlayer(playerId) != null) GameMap.getPlayer(playerId).score += 500;
                        player.setDead(true);
                        GameJPanel.setDead(j);
                        GameMap.removePlayer(j);
                    }
                }
            }
            if(GameMap.getPlayer(playerId)!=null) GameJPanel.setStatusText(playerId);
        }

        // Left
        for(int i=1; i<=left; i++) {
            boolean flag = mb[y][x-i].isWalkable();
            // Block Into Floor
            mb[y][x-i] = new MapBlock(ElementLoader.blockImageMap.get("00"), "00", x-i, y, false, true);
            if(GameMap.getPlayer(playerId) != null && !flag) GameMap.getPlayer(playerId).score += 20;
            // Random Item
            if(!flag) {
                Random rand = new Random();
                if (rand.nextInt(100) > 85) {
                    Item item = new Item(x - i, y, 0, rand.nextInt(2));
                    GameMap.getItems().add(item);
                } else if (rand.nextInt(200) > 199) {
                    Item item = new Item(x - i, y, 0, 2);
                    GameMap.getItems().add(item);
                }
            }

            // Kill Players
            for(int j=1; j<=4; j++) {
                Character player = GameMap.getPlayer(j);
                if(player==null) continue;
                if(player.getX()==x-i && player.getY()==y) {
                    player.setLife(player.getLife()-1);
                    GameJPanel.setStatusText(j);
                    if(player.getLife()==0) {
                        if(GameMap.getPlayer(playerId) != null) GameMap.getPlayer(playerId).score += 500;
                        player.setDead(true);
                        GameMap.removePlayer(j);
                        GameJPanel.setDead(j);
                    }
                }
            }
            if(GameMap.getPlayer(playerId)!=null) GameJPanel.setStatusText(playerId);
        }
    }

    public void drawSelf(Graphics g, BufferedImage bubbleImg, int width, int height) {
        lastForCertainTime();
        if(alive) {
            int dx1 = x * Config.BLOCK_SIZE;
            int dy1 = y * Config.BLOCK_SIZE;
            int dx2 = dx1 + Config.BLOCK_SIZE;
            int dy2 = dy1 + Config.BLOCK_SIZE;
            int sx1 = (turn / 4) * width / 3;
            int sy1 = 0;
            int sx2 = sx1 + width / 3;
            int sy2 = height;
            turn = ((turn + 1) % 12);
            g.drawImage(bubbleImg, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
        }
        else if(++count<=5){
            BufferedImage centerImg = ElementLoader.bubbleImageMap.get("center");
            BufferedImage leftImg = ElementLoader.bubbleImageMap.get("left");
            BufferedImage rightImg = ElementLoader.bubbleImageMap.get("right");
            BufferedImage upImg = ElementLoader.bubbleImageMap.get("up");
            BufferedImage downImg = ElementLoader.bubbleImageMap.get("down");
            BufferedImage horizontalImg = ElementLoader.bubbleImageMap.get("horizontal");
            BufferedImage verticalImg = ElementLoader.bubbleImageMap.get("vertical");
            g.drawImage(centerImg, x * Config.BLOCK_SIZE, y * Config.BLOCK_SIZE,
                    (x+1) * Config.BLOCK_SIZE, (y+1) * Config.BLOCK_SIZE,
                    0, 0, centerImg.getWidth(), centerImg.getHeight(), null);
            for(int i=1; i<left; i++) {
                g.drawImage(horizontalImg, (x-i) * Config.BLOCK_SIZE, y * Config.BLOCK_SIZE,
                        (x-i+1) * Config.BLOCK_SIZE, (y+1) * Config.BLOCK_SIZE,
                        0, 0, horizontalImg.getWidth(), horizontalImg.getHeight(), null);
            }
            for(int i=1; i<right; i++) {
                g.drawImage(horizontalImg, (x+i) * Config.BLOCK_SIZE, y * Config.BLOCK_SIZE,
                        (x+i+1) * Config.BLOCK_SIZE, (y+1) * Config.BLOCK_SIZE,
                        0, 0, horizontalImg.getWidth(), horizontalImg.getHeight(), null);
            }
            for(int i=1; i<up; i++) {
                g.drawImage(verticalImg, x * Config.BLOCK_SIZE, (y-i) * Config.BLOCK_SIZE,
                        (x+1) * Config.BLOCK_SIZE, (y-i+1) * Config.BLOCK_SIZE,
                        0, 0, verticalImg.getWidth(), verticalImg.getHeight(), null);
            }
            for(int i=1; i<down; i++) {
                g.drawImage(verticalImg, x * Config.BLOCK_SIZE, (y+i) * Config.BLOCK_SIZE,
                        (x+1) * Config.BLOCK_SIZE, (y+i+1) * Config.BLOCK_SIZE,
                        0, 0, verticalImg.getWidth(), verticalImg.getHeight(), null);
            }
            if(left!=0)
                g.drawImage(leftImg, (x-left) * Config.BLOCK_SIZE, y * Config.BLOCK_SIZE,
                    (x-left+1) * Config.BLOCK_SIZE, (y+1) * Config.BLOCK_SIZE,
                    0, 0, leftImg.getWidth(), leftImg.getHeight(), null);
            if(right!=0)
                g.drawImage(rightImg, (x+right) * Config.BLOCK_SIZE, y * Config.BLOCK_SIZE,
                        (x+right+1) * Config.BLOCK_SIZE, (y+1) * Config.BLOCK_SIZE,
                        0, 0, rightImg.getWidth(), rightImg.getHeight(), null);
            if(up!=0)
                g.drawImage(upImg, x * Config.BLOCK_SIZE, (y-up) * Config.BLOCK_SIZE,
                        (x+1) * Config.BLOCK_SIZE, (y-up+1) * Config.BLOCK_SIZE,
                        0, 0,upImg.getWidth(), upImg.getHeight(), null);
            if(down!=0)
                g.drawImage(downImg, x * Config.BLOCK_SIZE, (y+down) * Config.BLOCK_SIZE,
                        (x+1) * Config.BLOCK_SIZE, (y+down+1) * Config.BLOCK_SIZE,
                        0, 0, downImg.getWidth(), downImg.getHeight(), null);
        }
    }

    // Getters
    public boolean isAlive() { return alive; }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getPower() { return power; }

    public int getPlayerId(){return playerId;}
}
