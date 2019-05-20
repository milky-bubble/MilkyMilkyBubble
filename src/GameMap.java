import javax.imageio.ImageIO;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameMap {
    private static BufferedImage[][] floor = new BufferedImage[Config.GAME_HEIGHT][Config.GAME_WIDTH];
    private static MapBlock[][] block = new MapBlock[Config.GAME_HEIGHT][Config.GAME_WIDTH];

    private static BufferedImage player1Img;
    static {
        try {
            player1Img = ImageIO.read(new File("image/player1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Player player1 = new Player(0, 0, player1Img, 0);
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

        int dx1 = player1.getX(), dy1 = Config.BOARDER+player1.getY();
        int dx2 = dx1 + Config.BLOCK_SIZE, dy2 = dy1 + Config.BLOCK_SIZE;
        int sx1 = player1.turn*player1Img.getWidth()/4, sy1 = (player1.direction_cur-1)*player1Img.getHeight()/4;
        int sx2 = sx1 + player1Img.getWidth()/4, sy2 = sy1 + player1Img.getHeight()/4;
        g.drawImage(player1Img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

    }

    public void update() {
        player1.move();
    }

    public Player getPlayer1() {
        return player1;
    }

    public static BufferedImage[][] getFloor() {
        return floor;
    }

    public static void setFloor(BufferedImage[][] floor) {
        GameMap.floor = floor;
    }

    public static MapBlock[][] getBlock() {
        return block;
    }

    public static void setBlock(MapBlock[][] block) {
        GameMap.block = block;
    }

    public static BufferedImage getPlayer1Img() {
        return player1Img;
    }

    public static void setPlayer1Img(BufferedImage player1Img) {
        GameMap.player1Img = player1Img;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public ElementLoader getElementLoader() {
        return elementLoader;
    }

    public void setElementLoader(ElementLoader elementLoader) {
        this.elementLoader = elementLoader;
    }

    public List<List<String>> getGameMapBlock() {
        return GameMapBlock;
    }

    public void setGameMapBlock(List<List<String>> gameMapBlock) {
        GameMapBlock = gameMapBlock;
    }
}
