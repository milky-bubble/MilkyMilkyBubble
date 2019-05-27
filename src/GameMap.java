import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameMap {

    // Init ElementLoader

    // Game Elements
    private static BufferedImage[][] floor = new BufferedImage[Config.GAME_HEIGHT][Config.GAME_WIDTH];
    private static MapBlock[][] block = new MapBlock[Config.GAME_HEIGHT][Config.GAME_WIDTH];
    private static ArrayList<Bubble> bubbles = new ArrayList<Bubble>();
    private static ArrayList<Item> items = new ArrayList<Item>();
    private static Character player1 = new Player(0, 0, 1, ElementLoader.playerImageMap.get(1), 0);
    private static Character player2 = new NPC1(Config.GAME_WIDTH-1, 0, 2, ElementLoader.playerImageMap.get(2), 0);
    private static Character player3 = new NPC2(0, Config.GAME_HEIGHT-1, 3, ElementLoader.playerImageMap.get(3), 0);
    private static Character player4 = new NPC3(Config.GAME_WIDTH-1, Config.GAME_HEIGHT-1, 4, ElementLoader.playerImageMap.get(4), 0);


    // Init Game Elements
    public GameMap() throws IOException {
        for(int i=0; i<Config.GAME_HEIGHT; i++) {
            for(int j=0; j<Config.GAME_WIDTH; j++) {
                floor[i][j] = ElementLoader.blockImageMap.get("00");
            }
        }

        for(int i=0; i<Config.GAME_HEIGHT; i++) {
            for(int j=0; j<Config.GAME_WIDTH; j++) {
                List<List<String>> GameMapBlock = ElementLoader.readBlockInfo();
                boolean destructible = (GameMapBlock.get(i).get(j).equals("41")
                        || GameMapBlock.get(i).get(j).equals("42")
                        || GameMapBlock.get(i).get(j).equals("43"));
                boolean walkable = (GameMapBlock.get(i).get(j).equals("00"));
                block[i][j] = new MapBlock(ElementLoader.blockImageMap.get(GameMapBlock.get(i).get(j)),
                        GameMapBlock.get(i).get(j), j, i,
                        destructible, walkable);
            }
        }
    }

    public void drawMap(Graphics g) {
        // Draw Floor & Blocks
        for (int i = 0; i < Config.GAME_HEIGHT; i++) {
            for (int j = 0; j < Config.GAME_WIDTH; j++) {
                g.drawImage(floor[i][j], j * Config.BLOCK_SIZE, i * Config.BLOCK_SIZE,
                        (j + 1) * Config.BLOCK_SIZE, (i + 1) * Config.BLOCK_SIZE,
                        0, 0, floor[i][j].getWidth(), floor[i][j].getHeight(), null);

                g.drawImage(block[i][j].getImage(), j * Config.BLOCK_SIZE, i * Config.BLOCK_SIZE,
                        (j + 1) * Config.BLOCK_SIZE,  (i + 1) * Config.BLOCK_SIZE,
                        0, 0, block[i][j].getImage().getWidth(), block[i][j].getImage().getHeight(), null);
            }
        }

        // Draw Items
        for(Item item : items) {
            BufferedImage image = ElementLoader.itemImageMap.get(item.getCategory());
            item.drawSelf(g, image, image.getWidth(), image.getHeight());
        }

        ArrayList<Bubble> bubbles0 = (ArrayList<Bubble>) bubbles.clone();
        // Draw Bubbles
        for(Bubble bubble : bubbles0) {
            BufferedImage bubbleImg = ElementLoader.bubbleImageMap.get("bubble");
            bubble.drawSelf(g, bubbleImg, bubbleImg.getWidth(), bubbleImg.getHeight());
        }

        // Draw Players
        if (player1 != null) player1.drawSelf(g, 1);
        if (player2 != null) player2.drawSelf(g, 2);
        if (player3 != null) player3.drawSelf(g, 3);
        if (player4 != null) player4.drawSelf(g, 4);
    }

    // Player Move
    public void update() {
        if(player1 != null) player1.move();
        if(player2 != null) player2.move();
        if(player3 != null) player3.move();
        if(player4 != null) player4.move();
    }

    // Getters & Setters

    public static MapBlock[][] getBlock() {
        return block;
    }
    public static ArrayList<Bubble> getBubbles() {
        return bubbles;
    }
    public static ArrayList<Item> getItems() {
        return items;
    }
    public static Character getPlayer(int id) {
        switch(id) {
            case 1: return player1;
            case 2: return player2;
            case 3: return player3;
            case 4: return player4;
            default: return null;
        }
    }
    public static void removePlayer(int id) {
        switch(id) {
            case 1: player1 = null; break;
            case 2: player2 = null; break;
            case 3: player3 = null; break;
            case 4: player4 = null; break;
        }
    }

    public static Character getPlayer1() {
        return player1;
    }
}
