import javax.imageio.ImageIO;
import javax.swing.*;
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

    private ElementLoader() throws IOException {
        properties = new Properties();
        blockImageMap = new HashMap<String, BufferedImage>();
        itemImageMap = new HashMap<Integer, BufferedImage>();
        initBlockImageMap();
        initItemImageMap();
    }

    public static ElementLoader getElementLoader() throws IOException {
        if (elementLoader == null) {
            elementLoader = new ElementLoader();
        }
        return elementLoader;
    }

    private static void initBlockImageMap() throws IOException {
        BufferedImage blueHouse = ImageIO.read(new File("image/bluehouse.png"));
        BufferedImage redHouse = ImageIO.read(new File("image/redhouse.png"));
        BufferedImage yellowHouse = ImageIO.read(new File("image/yellowhouse.png"));
        BufferedImage redBlock = ImageIO.read(new File("image/redblock.png"));
        BufferedImage yellowBlock = ImageIO.read(new File("image/yellowblock.png"));
        BufferedImage box = ImageIO.read(new File("image/box.png"));
        BufferedImage townBush = ImageIO.read(new File("image/townbush.png"));
        BufferedImage townTree = ImageIO.read(new File("image/townTree.png"));
        BufferedImage floor = ImageIO.read(new File("image/floor.png"));
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
        BufferedImage powerGiftImg = ImageIO.read(new File("image/powergift.png"));
        BufferedImage bubbleNumGiftImg = ImageIO.read(new File("image/bubblenumgift.png"));
        itemImageMap.put(0, powerGiftImg);
        itemImageMap.put(1, bubbleNumGiftImg);
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
