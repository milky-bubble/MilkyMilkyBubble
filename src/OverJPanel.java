import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class OverJPanel extends JPanel {
    private ImageIcon background = ElementLoader.gameImageMap.get("overbackground");
    private int width = Config.WINDOW_WIDTH + Config.PLAYER_INFO;
    private int height = Config.WINDOW_HEIGHT + Config.BOARDER;
    public OverJPanel() {
        this.setLayout(null);
        JLabel jLabel = new JLabel(background);
        background.setImage(background.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        jLabel.setBounds(0, 0, width, height);

        this.add(jLabel);
        this.setVisible(true);
    }


}
