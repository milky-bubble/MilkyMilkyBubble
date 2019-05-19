import java.awt.image.BufferedImage;

public class MapBlock {
    private BufferedImage image;
    private int x, y;
    private boolean destructible;

    public MapBlock(BufferedImage image, int x, int y, boolean destructible) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.destructible = destructible;
    }
}
