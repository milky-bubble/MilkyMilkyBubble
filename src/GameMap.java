import javax.imageio.ImageIO;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameMap {
    private static BufferedImage[][] floor = new BufferedImage[Config.GAME_HEIGHT][Config.GAME_WIDTH];
    private static BufferedImage[][] block = new BufferedImage[Config.GAME_HEIGHT][Config.GAME_WIDTH];

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
                block[i][j] = elementLoader.blockMap.get(GameMapBlock.get(i).get(j));
            }
        }
    }


    public void drawMap(Graphics g) {

        for(int i=0; i<Config.GAME_HEIGHT; i++) {
            for(int j=0; j<Config.GAME_WIDTH; j++) {
                g.drawImage(floor[i][j], j*Config.BLOCK_SIZE, Config.BOARDER+i*Config.BLOCK_SIZE,
                        (j+1)*Config.BLOCK_SIZE, Config.BOARDER+(i+1)*Config.BLOCK_SIZE,
                        0, 0, floor[i][j].getWidth(), floor[i][j].getHeight(), null);
                g.drawImage(block[i][j], j*Config.BLOCK_SIZE, Config.BOARDER+i*Config.BLOCK_SIZE,
                        (j+1)*Config.BLOCK_SIZE, Config.BOARDER+(i+1)*Config.BLOCK_SIZE,
                        0, 0, block[i][j].getWidth(), block[i][j].getHeight(), null);

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
}
