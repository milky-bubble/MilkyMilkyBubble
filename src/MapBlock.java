import java.awt.image.BufferedImage;

public class MapBlock {
    private BufferedImage image;
    private int x, y;
    private String id;
    private boolean destructible;
    private boolean walkable;

    public MapBlock(BufferedImage image, String id, int x, int y, boolean destructible, boolean walkable) {
        this.image = image;
        this.id = id;
        this.x = x;
        this.y = y;
        this.destructible = destructible;
        this.walkable = walkable;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
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

    public boolean isDestructible() {
        return destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }
}
