import java.awt.image.BufferedImage;

public class NPC3 extends Character {
    public NPC3(int x, int y, int id, BufferedImage image, int direction) {
        super(x, y, id, image, direction);
    }

    private int nextStep() {
        /**
         * TODO: ZC
         * Down: return 1;
         * Left: return 2;
         * Right: return 3;
         * Up: return 4;
         *
         * if need to add bubble, just use function addBubble();
         */

        // Here is an example, one step up and put one bubble
        // NPC1 can only put one bubble at first
        // So only after the bubble explode can he puts another one
        addBubble();
        return 4;
    }

    private int timeCount = 0;
    @Override
    public void move() {
        if(dead) return;
        if(timeCount++ % 10 != 0) return;
        direction = nextStep();
    }
}
