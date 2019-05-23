import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class BeginJPanel extends JPanel {
    private ImageIcon background = ElementLoader.gameImageMap.get("beginbackground");
    private ImageIcon beginButton = ElementLoader.gameImageMap.get("beginbutton");
    private int width = Config.WINDOW_WIDTH + Config.PLAYER_INFO;
    private int height = Config.WINDOW_HEIGHT + Config.BOARDER;
    public BeginJPanel() {
        this.setLayout(null);
        JLabel jLabel = new JLabel(background);
        background.setImage(background.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        jLabel.setBounds(0, 0, width, height);

        JButton jButton = new JButton(beginButton);
        beginButton.setImage(beginButton.getImage().getScaledInstance(100, 50, Image.SCALE_DEFAULT));
        jButton.setBounds(width/3, height/3, 100, 50);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    GameFrame.startGame();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.add(jLabel);
        this.add(jButton);
        this.setVisible(true);
    }

}
