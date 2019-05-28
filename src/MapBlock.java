import java.awt.image.BufferedImage;

public class MapBlock {
    private BufferedImage image;
    private int x, y;
    private String id;
    private boolean destructible;
    private boolean walkable;
    private boolean hasBubble;

    // Constructor
    public MapBlock(BufferedImage image, String id, int x, int y, boolean destructible, boolean walkable) {
        this.image = image;
        this.id = id;
        this.x = x;
        this.y = y;
        this.destructible = destructible;
        this.walkable = walkable;
        this.hasBubble = false;
    }

    // Getters & Setters
    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public boolean isDestructible() {
        return destructible;
    }
    public boolean isWalkable() { return walkable; }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isHasBubble() {
        return hasBubble;
    }

    public void setHasBubble(boolean hasBubble) {
        this.hasBubble = hasBubble;
    }
}
