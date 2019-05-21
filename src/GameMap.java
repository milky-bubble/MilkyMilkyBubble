import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameMap {
    private static BufferedImage player1Img;
    private static BufferedImage player2Img;
    private static BufferedImage player3Img;
    private static BufferedImage player4Img;
    private static BufferedImage bubbleImg;
    private static BufferedImage floorImg;


    static {
        try {
            player1Img = ImageIO.read(new File("image/player1.png"));
            player2Img = ImageIO.read(new File("image/player2.png"));
            player3Img = ImageIO.read(new File("image/player3.png"));
            player4Img = ImageIO.read(new File("image/player4.png"));
            bubbleImg = ImageIO.read(new File("image/bubble.png"));
            floorImg = ImageIO.read((new File("image/floor.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage[][] floor = new BufferedImage[Config.GAME_HEIGHT][Config.GAME_WIDTH];
    private static MapBlock[][] block = new MapBlock[Config.GAME_HEIGHT][Config.GAME_WIDTH];
    private static ArrayList<Bubble> bubbles = new ArrayList<Bubble>();
    private static ArrayList<Item> items = new ArrayList<Item>();
    private static Player player1 = new Player(0, 0, 1, player1Img, 0);
    private static Player player2 = new Player((Config.GAME_WIDTH-1)*Config.BLOCK_SIZE, 0, 2, player2Img, 0);
    private static Player player3 = new Player(0, (Config.GAME_HEIGHT-1)*Config.BLOCK_SIZE, 3, player3Img, 0);
    private static Player player4 = new Player((Config.GAME_WIDTH-1)*Config.BLOCK_SIZE, (Config.GAME_HEIGHT-1)*Config.BLOCK_SIZE, 4, player4Img, 0);
    private ElementLoader elementLoader = ElementLoader.getElementLoader();
    private List<List<String>> GameMapBlock = elementLoader.readBlockInfo();


    public GameMap() throws IOException {
        for(int i=0; i<Config.GAME_HEIGHT; i++) {
            for(int j=0; j<Config.GAME_WIDTH; j++) {
                File file = new File("image/floor.png");
                floor[i][j] = ImageIO.read(file);
            }
        }

        for(int i=0; i<Config.GAME_HEIGHT; i++) {
            for(int j=0; j<Config.GAME_WIDTH; j++) {
                boolean destructible = (GameMapBlock.get(i).get(j).equals("41")
                        || GameMapBlock.get(i).get(j).equals("42")
                        || GameMapBlock.get(i).get(j).equals("43"));
                boolean walkable = (GameMapBlock.get(i).get(j).equals("00"));
                block[i][j] = new MapBlock(elementLoader.blockImageMap.get(GameMapBlock.get(i).get(j)),
                        GameMapBlock.get(i).get(j), j, i,
                        destructible, walkable);
            }
        }
    }

    public void drawMap(Graphics g) {
        for(int i=0; i<Config.GAME_HEIGHT; i++) {
            for(int j=0; j<Config.GAME_WIDTH; j++) {
                g.drawImage(floor[i][j], j*Config.BLOCK_SIZE, Config.BOARDER+i*Config.BLOCK_SIZE,
                        (j+1)*Config.BLOCK_SIZE, Config.BOARDER+(i+1)*Config.BLOCK_SIZE,
                        0, 0, floor[i][j].getWidth(), floor[i][j].getHeight(), null);
                g.drawImage(block[i][j].getImage(), j*Config.BLOCK_SIZE, Config.BOARDER+i*Config.BLOCK_SIZE,
                        (j+1)*Config.BLOCK_SIZE, Config.BOARDER+(i+1)*Config.BLOCK_SIZE,
                        0, 0, block[i][j].getImage().getWidth(), block[i][j].getImage().getHeight(), null);
            }
        }


        for(int i=0; i<items.size(); i++) {
            BufferedImage image = elementLoader.itemImageMap.get(items.get(i).getCategory());
            items.get(i).drawSelf(g, image, image.getWidth(), image.getHeight());
        }
        for(int i=0; i<bubbles.size(); i++) {
            bubbles.get(i).drawSelf(g, bubbleImg, bubbleImg.getWidth(), bubbleImg. getHeight());
        }



        int dx1, dx2, dy1, dy2, sx1, sx2, sy1, sy2;

        dx1 = player1.getX();
        dy1 = Config.BOARDER+player1.getY();
        dx2 = dx1 + Config.BLOCK_SIZE;
        dy2 = dy1 + Config.BLOCK_SIZE;
        sx1 = player1.turn*player1Img.getWidth()/4;
        sy1 = (player1.direction_cur-1)*player1Img.getHeight()/4;
        sx2 = sx1 + player1Img.getWidth()/4;
        sy2 = sy1 + player1Img.getHeight()/4;
        if(!player1.isDead()) g.drawImage(player1Img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

        dx1 = player2.getX();
        dy1 = Config.BOARDER+player2.getY();
        dx2 = dx1 + Config.BLOCK_SIZE;
        dy2 = dy1 + Config.BLOCK_SIZE;
        sx1 = player2.turn*player2Img.getWidth()/4;
        sy1 = (player2.direction_cur-1)*player2Img.getHeight()/4;
        sx2 = sx1 + player2Img.getWidth()/4;
        sy2 = sy1 + player2Img.getHeight()/4;
        if(!player2.isDead()) g.drawImage(player2Img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

        dx1 = player3.getX();
        dy1 = Config.BOARDER+player3.getY();
        dx2 = dx1 + Config.BLOCK_SIZE;
        dy2 = dy1 + Config.BLOCK_SIZE;
        sx1 = player3.turn*player3Img.getWidth()/4;
        sy1 = (player3.direction_cur-1)*player3Img.getHeight()/4;
        sx2 = sx1 + player3Img.getWidth()/4;
        sy2 = sy1 + player3Img.getHeight()/4;
        if(!player3.isDead()) g.drawImage(player3Img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

        dx1 = player4.getX();
        dy1 = Config.BOARDER+player4.getY();
        dx2 = dx1 + Config.BLOCK_SIZE;
        dy2 = dy1 + Config.BLOCK_SIZE;
        sx1 = player4.turn*player4Img.getWidth()/4;
        sy1 = (player4.direction_cur-1)*player4Img.getHeight()/4;
        sx2 = sx1 + player4Img.getWidth()/4;
        sy2 = sy1 + player4Img.getHeight()/4;
        if(!player4.isDead()) g.drawImage(player4Img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }

    public void update() {
        player1.move();
    }



    public static MapBlock[][] getBlock() {
        return block;
    }

    public static ArrayList<Bubble> getBubbles() {
        return bubbles;
    }

    public static void setBubbles(ArrayList<Bubble> bubbles) {
        GameMap.bubbles = bubbles;
    }

    public static BufferedImage getFloorImg() {
        return floorImg;
    }

    public static void setFloorImg(BufferedImage floorImg) {
        GameMap.floorImg = floorImg;
    }

    public static ArrayList<Item> getItems() {
        return items;
    }

    public static void setItems(ArrayList<Item> items) {
        GameMap.items = items;
    }

    public static Player getPlayer(int id) {
        switch(id) {
            case 1: return player1;
            case 2: return player2;
            case 3: return player3;
            case 4: return player4;
            default: return null;
        }
    }
}
