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

    private int count = 0;
    @Override
    public void move() {
        if(dead) return;
        if(count++ % 50 != 0) return;
        direction = nextStep();
        switch(direction) {
            case 4:
                if(!crashUp()) y -= 1;
                break;
            case 1:
                if(!crashDown()) y += 1;
                break;
            case 2:
                if(!crashLeft()) x -= 1;
                break;
            case 3:
                if(!crashRight()) x += 1;
                break;
        }
        if(direction != 0 && direction != direction_cur) direction_cur = direction;
        if(direction != 0) turn = (turn + 1) % 4;
        direction = 0;
        pickItem();
    }
}
