import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.font.ImageGraphicAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map;

public class ElementLoader {
    private static ElementLoader elementLoader;
    private static Properties properties;
    public static Map<String, BufferedImage> blockImageMap;
    public static Map<Integer, BufferedImage> itemImageMap;
    public static Map<Integer, BufferedImage> playerImageMap;
    public static Map<String, BufferedImage> bubbleImageMap;
    public static Map<String, ImageIcon> gameImageMap;

    private ElementLoader() throws IOException {
        properties = new Properties();
        blockImageMap = new HashMap<String, BufferedImage>();
        itemImageMap = new HashMap<Integer, BufferedImage>();
        playerImageMap = new HashMap<Integer, BufferedImage>();
        bubbleImageMap = new HashMap<String, BufferedImage>();
        gameImageMap = new HashMap<String, ImageIcon>();
        initBlockImageMap();
        initItemImageMap();
        initPlayerImageMap();
        initBubbleImageMap();
        initGameImageMap();
    }

    public static ElementLoader getElementLoader() throws IOException {
        if (elementLoader == null) {
            elementLoader = new ElementLoader();
        }
        return elementLoader;
    }

    private static void initBlockImageMap() throws IOException {
        BufferedImage blueHouse = ImageIO.read(new File("image/map/bluehouse.png"));
        BufferedImage redHouse = ImageIO.read(new File("image/map/redhouse.png"));
        BufferedImage yellowHouse = ImageIO.read(new File("image/map/yellowhouse.png"));
        BufferedImage redBlock = ImageIO.read(new File("image/map/redblock.png"));
        BufferedImage yellowBlock = ImageIO.read(new File("image/map/yellowblock.png"));
        BufferedImage box = ImageIO.read(new File("image/map/box.png"));
        BufferedImage townBush = ImageIO.read(new File("image/map/townbush.png"));
        BufferedImage townTree = ImageIO.read(new File("image/map/townTree.png"));
        BufferedImage floor = ImageIO.read(new File("image/map/floor.png"));
        blockImageMap.put("00", floor);
        blockImageMap.put("21", blueHouse);
        blockImageMap.put("22", redHouse);
        blockImageMap.put("23", yellowHouse);
        blockImageMap.put("31", townBush);
        blockImageMap.put("32", townTree);
        blockImageMap.put("41", redBlock);
        blockImageMap.put("42", yellowBlock);
        blockImageMap.put("43", box);
    }

    private static void initItemImageMap() throws IOException {
        BufferedImage powerGiftImg = ImageIO.read(new File("image/item/powergift.png"));
        BufferedImage bubbleNumGiftImg = ImageIO.read(new File("image/item/bubblenumgift.png"));
        BufferedImage lifeCardGiftImg = ImageIO.read(new File("image/item/lifecardgift.png"));
        itemImageMap.put(0, powerGiftImg);
        itemImageMap.put(1, bubbleNumGiftImg);
        itemImageMap.put(2, lifeCardGiftImg);
    }

    private static void initPlayerImageMap() throws IOException {
        BufferedImage player1Img = ImageIO.read(new File("image/player/player1.png"));
        BufferedImage player2Img = ImageIO.read(new File("image/player/player2.png"));
        BufferedImage player3Img = ImageIO.read(new File("image/player/player3.png"));
        BufferedImage player4Img = ImageIO.read(new File("image/player/player4.png"));
        playerImageMap.put(1, player1Img);
        playerImageMap.put(2, player2Img);
        playerImageMap.put(3, player3Img);
        playerImageMap.put(4, player4Img);
    }

    private static void initBubbleImageMap() throws IOException {
        BufferedImage bubbleImg = ImageIO.read(new File("image/bubble/bubble.png"));
        bubbleImageMap.put("bubble", bubbleImg);
    }

    private static void initGameImageMap() throws IOException {
        ImageIcon iconImg = new ImageIcon("image/game/icon.jpg");
        ImageIcon beginBackgroundImg = new ImageIcon("image/game/beginbackground.png");
        ImageIcon beginButtonImg = new ImageIcon("image/game/beginbutton.png");
        ImageIcon overBackgroundImg = new ImageIcon("image/game/overbackground.png");
        ImageIcon player01Img = new ImageIcon("image/game/player1.png");
        ImageIcon player02Img = new ImageIcon("image/game/player2.png");
        ImageIcon player03Img = new ImageIcon("image/game/player3.png");
        ImageIcon player04Img = new ImageIcon("image/game/player4.png");
        gameImageMap.put("icon", iconImg);
        gameImageMap.put("beginbackground", beginBackgroundImg);
        gameImageMap.put("beginbutton", beginButtonImg);
        gameImageMap.put("overbackground", overBackgroundImg);
        gameImageMap.put("player01card", player01Img);
        gameImageMap.put("player02card", player02Img);
        gameImageMap.put("player03card", player03Img);
        gameImageMap.put("player04card", player04Img);
    }

    public static List<List<String>> readBlockInfo() throws IOException {
        List<List<String>> mapList = new ArrayList<>();
        InputStream inputStream = ElementLoader.class.getClassLoader().getResourceAsStream("blockInfo.properties");
        properties.clear();
        properties.load(inputStream);
        Set<Object> sortSet = new TreeSet<>(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
                    int a = Integer.parseInt(o1.toString());
                    int b = Integer.parseInt(o2.toString());
                    if(a>b) {
                        return 1;
                    } else if (a<b) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    return -1;
                }
            }
        });
        sortSet.addAll(properties.keySet());
        for(Object o:sortSet) {
            String info = properties.getProperty(o.toString());
            mapList.add(Arrays.asList(info.split(",")));
        }
        return mapList;
    }

}
