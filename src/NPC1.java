import javafx.util.Pair;

import java.sql.*;
import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class NPC1 extends Character{
    boolean reach_player;
    boolean attack;
    int box_x, box_y;
    int npc_direction;
    boolean[][] record = new boolean[Config.GAME_HEIGHT][Config.GAME_WIDTH];
    LinkedList<Pair<Integer, Integer>> selfPath = new LinkedList<Pair<Integer, Integer>>();

    public NPC1(int x, int y, int id, BufferedImage image, int direction) {
        super(x, y, id, image, direction);
        reach_player = false;
        attack = false;
        for (int i = 0; i < Config.GAME_HEIGHT; i++)
            for (int j = 0; j < Config.GAME_WIDTH; j++)
                record[i][j] = true;
    }

    private boolean judgeEvade() {
        ArrayList<Bubble> bubbles = GameMap.getBubbles();
        MapBlock[][] mapBlock = GameMap.getBlock();

        for (Bubble temp : bubbles) {
            int explode_up = temp.getY();
            int explode_down = temp.getY();
            int explode_left = temp.getX();
            int explode_right = temp.getX();

            if (!temp.isAlive())
                continue;

            //left
            for (int i = 1; i <= temp.getPower(); i++) {
                if (temp.getX() - i < 0 || !(mapBlock[temp.getY()][temp.getX() - i].isDestructible() || mapBlock[temp.getY()][temp.getX() - i].isWalkable()))
                    break;
                else
                    explode_left = temp.getX() - i;
            }

            //right
            for (int i = 1; i <= temp.getPower(); i++) {
                if (temp.getX() + i >= Config.GAME_WIDTH || !(mapBlock[temp.getY()][temp.getX() + i].isDestructible() || mapBlock[temp.getY()][temp.getX() + i].isWalkable()))
                    break;
                else
                    explode_right = temp.getX() + i;
            }

            //up
            for (int i = 1; i <= temp.getPower(); i++) {
                if (temp.getY() - i < 0 || !(mapBlock[temp.getY() - i][temp.getX()].isDestructible() || mapBlock[temp.getY() - i][temp.getX()].isWalkable()))
                    break;
                else
                    explode_up = temp.getY() - i;
            }

            //down
            for (int i = 1; i <= temp.getPower(); i++) {
                if (temp.getY() + i >= Config.GAME_HEIGHT || !(mapBlock[temp.getY() + i][temp.getX()].isDestructible() || mapBlock[temp.getY() + i][temp.getX()].isWalkable()))
                    break;
                else
                    explode_down = temp.getY() + i;
            }

            if (((y >= explode_up) && (y <= explode_down) && x == temp.getX()) || (x >= explode_left) && (x <= explode_right) && y == temp.getY())
                return true;
            else
                continue;
        }
        return false;
    }

    private void computeSafeRegion() {
        MapBlock[][] mapBlock = GameMap.getBlock();
        ArrayList<Bubble> bubbles = GameMap.getBubbles();
        for (int i = 0; i < Config.GAME_HEIGHT; i++)
            for (int j = 0; j < Config.GAME_WIDTH; j++)
                record[i][j] = true;
        for (int i = 0; i < Config.GAME_HEIGHT; i++)
            for (int j = 0; j < Config.GAME_WIDTH; j++) {
                if (mapBlock[i][j].isWalkable()) {
                    if (bubbles.isEmpty())
                        record[i][j] = true;

                    for (Bubble temp : bubbles) {
                        if (!temp.isAlive())
                            continue;

                        int explode_up = temp.getY();
                        int explode_down = temp.getY();
                        int explode_left = temp.getX();
                        int explode_right = temp.getX();

                        //left
                        for (int k = 1; k <= temp.getPower(); k++) {
                            if (temp.getX() - k < 0 || !(mapBlock[temp.getY()][temp.getX() - k].isDestructible() || mapBlock[temp.getY()][temp.getX() - k].isWalkable())) {
                                break;
                            } else
                                explode_left = temp.getX() - k;
                        }

                        //right
                        for (int k = 1; k <= temp.getPower(); k++) {
                            if (temp.getX() + k >= Config.GAME_WIDTH || !(mapBlock[temp.getY()][temp.getX() + k].isDestructible() || mapBlock[temp.getY()][temp.getX() + k].isWalkable()))
                                break;
                            else
                                explode_right = temp.getX() + k;
                        }

                        //up
                        for (int k = 1; k <= temp.getPower(); k++) {
                            if (temp.getY() - k < 0 || !(mapBlock[temp.getY() - k][temp.getX()].isDestructible() || mapBlock[temp.getY() - k][temp.getX()].isWalkable()))
                                break;
                            else
                                explode_up = temp.getY() - k;
                        }

                        //down
                        for (int k = 1; k <= temp.getPower(); k++) {
                            if (temp.getY() + k >= Config.GAME_HEIGHT || !(mapBlock[temp.getY() + k][temp.getX()].isDestructible() || mapBlock[temp.getY() + k][temp.getX()].isWalkable()))
                                break;
                            else
                                explode_down = temp.getY() + k;
                        }
                        if (((i >= explode_up) && (i <= explode_down) && j == temp.getX()) || ((j >= explode_left) && (j <= explode_right) && i == temp.getY()))
                            record[i][j] = false;
                    }
                } else
                    record[i][j] = false;
            }
    }

    private boolean tryBubble(int x, int y) {
        MapBlock[][] mapBlock = GameMap.getBlock();

        int explode_up = y;
        int explode_down = y;
        int explode_left = x;
        int explode_right = x;

        //left
        for (int i = 1; i <= bubblePower; i++) {
            if (x - i < 0 || !(mapBlock[y][x - i].isDestructible() || mapBlock[y][x - i].isWalkable()))
                break;
            else
                explode_left = x - i;
        }

        //right
        for (int i = 1; i <= bubblePower; i++) {
            if (x + i >= Config.GAME_WIDTH || !(mapBlock[y][x + i].isDestructible() || mapBlock[y][x + i].isWalkable()))
                break;
            else
                explode_right = x + i;
        }

        //up
        for (int i = 1; i <= bubblePower; i++) {
            if (y - i < 0 || !(mapBlock[y - i][x].isDestructible() || mapBlock[y - i][x].isWalkable()))
                break;
            else
                explode_up = y - i;
        }

        //down
        for (int i = 1; i <= bubblePower; i++) {
            if (y + i >= Config.GAME_HEIGHT || !(mapBlock[y + i][x].isDestructible() || mapBlock[y + i][x].isWalkable()))
                break;
            else
                explode_down = y + i;
        }
        //System.out.println(explode_up+" "+explode_down+" "+explode_left+" "+explode_right);

        int escape_up = y;
        int escape_down = y;
        int escape_left = x;
        int escape_right = x;
        //left
        for (int i = 1; i <= bubblePower; i++) {
            if (x - i < 0 || !record[y][x - i])
                break;
            else
                escape_left = x - i;
        }

        //right
        for (int i = 1; i <= bubblePower; i++) {
            if (x + i >= Config.GAME_WIDTH || !record[y][x + i])
                break;
            else
                escape_right = x + i;
        }

        //up
        for (int i = 1; i <= bubblePower; i++) {
            if (y - i < 0 || !record[y - i][x])
                break;
            else
                escape_up = y - i;
        }

        //down
        for (int i = 1; i <= bubblePower; i++) {
            if (y + i >= Config.GAME_HEIGHT || !record[y + i][x])
                break;
            else
                escape_down = y + i;
        }
        //System.out.println(escape_up+" "+escape_down+" "+escape_left+" "+escape_right);

        //whether can escape

        //within explode scope
        //left ->up or down
        for (int i = x - 1; i >= escape_left; i--) {
            if (y > 0 && record[y - 1][i] || record[y + 1][i] && y < Config.GAME_HEIGHT - 1)
                return true;
            else
                continue;
        }

        //right ->up or down
        for (int i = x + 1; i <= escape_right; i++) {
            if (y > 0 && record[y - 1][i] || record[y + 1][i] && y < Config.GAME_HEIGHT - 1)
                return true;
            else
                continue;
        }

        //up ->left or right
        for (int i = y - 1; i >= escape_up; i--) {
            if (x > 0 && record[i][x - 1] || record[i][x + 1] && x < Config.GAME_WIDTH - 1)
                return true;
            else
                continue;
        }

        //down ->left or right
        for (int i = y + 1; i <= escape_down; i++) {
            if (x > 0 && record[i][x - 1] || record[i][x + 1] && x < Config.GAME_WIDTH - 1)
                return true;
            else
                continue;
        }

        //outside explode scope
        if (bubblePower <= 2) {
            //left
            if (explode_left == escape_left && escape_left > 0 && record[y][escape_left - 1] && x - escape_left < 3)
                return true;

            //right
            if (explode_right == escape_right && escape_right < Config.GAME_WIDTH - 1 && record[y][escape_right + 1] && escape_right - x < 3)
                return true;

            //up
            if (explode_up == escape_up && escape_up > 0 && record[escape_up - 1][x] && y - escape_up < 3)
                return true;

            //down
            if (explode_down == escape_down && escape_down < Config.GAME_HEIGHT - 1 && record[escape_down + 1][y] && escape_down - y < 3)
                return true;
        }

        return false;
    }

    private void findPath(Character player, int option) {
        MapBlock[][] mapBlock = GameMap.getBlock();

        LinkedList<Pair<Integer, Integer>>[][] temp_grid = new LinkedList[Config.GAME_HEIGHT][Config.GAME_WIDTH];

        for (int i = 0; i < Config.GAME_HEIGHT; i++)
            for (int j = 0; j < Config.GAME_WIDTH; j++) {
                temp_grid[i][j] = new LinkedList<>();
            }

        Queue<Pair<Integer, Integer>> queue = new LinkedList<Pair<Integer, Integer>>();
        ((LinkedList<Pair<Integer, Integer>>) queue).add(new Pair<>(x, y));

        boolean[][] visited = new boolean[Config.GAME_HEIGHT][Config.GAME_WIDTH];
        visited[y][x] = true;

        final int MAX_SEARCH_COUNT = 40;

        int search_count = 0;

        System.out.println(option);

        while (!queue.isEmpty()) {
            search_count++;
            if (search_count > MAX_SEARCH_COUNT)
                return;
            Pair<Integer, Integer> cur = ((LinkedList<Pair<Integer, Integer>>) queue).pop();
            visited[cur.getValue()][cur.getKey()] = true;

            Pair<Integer, Integer> offset = new Pair<>(player.getX() - x, player.getY() - y);
            ArrayList<Pair<Integer, Integer>> next = new ArrayList<Pair<Integer, Integer>>();

            //right and down
            if (offset.getKey() >= 0 && offset.getValue() >= 0) {
                next.add(new Pair<>(cur.getKey() + 1, cur.getValue()));
                next.add(new Pair<>(cur.getKey(), cur.getValue() + 1));
                next.add(new Pair<>(cur.getKey() - 1, cur.getValue()));
                next.add(new Pair<>(cur.getKey(), cur.getValue() - 1));
            }

            //right and up
            else if (offset.getKey() >= 0 && offset.getValue() < 0) {
                next.add(new Pair<>(cur.getKey() + 1, cur.getValue()));
                next.add(new Pair<>(cur.getKey(), cur.getValue() - 1));
                next.add(new Pair<>(cur.getKey() - 1, cur.getValue()));
                next.add(new Pair<>(cur.getKey(), cur.getValue() + 1));
            }

            //left and down
            else if (offset.getKey() < 0 && offset.getValue() >= 0) {
                next.add(new Pair<>(cur.getKey() - 1, cur.getValue()));
                next.add(new Pair<>(cur.getKey(), cur.getValue() + 1));
                next.add(new Pair<>(cur.getKey() + 1, cur.getValue()));
                next.add(new Pair<>(cur.getKey(), cur.getValue() - 1));
            }

            //left and up
            else if (offset.getKey() < 0 && offset.getValue() < 0) {
                next.add(new Pair<>(cur.getKey() - 1, cur.getValue()));
                next.add(new Pair<>(cur.getKey(), cur.getValue() - 1));
                next.add(new Pair<>(cur.getKey() + 1, cur.getValue()));
                next.add(new Pair<>(cur.getKey(), cur.getValue() + 1));
            }

            //whether reachable
            if (option == 0)
                if (cur.getKey() == player.getX() && cur.getValue() == player.getY()) {
                    reach_player = true;
                    return;
                }

            //find player
            if (option == 1)
                if (cur.getKey() == player.getX() && cur.getValue() == player.getY()) {
                    selfPath = temp_grid[cur.getValue()][cur.getKey()];
                    return;
                }

            //avoid bubble
            if (option == 2)
                if (record[cur.getValue()][cur.getKey()]) {
                    selfPath = temp_grid[cur.getValue()][cur.getKey()];
                    return;
                }

            //find box
            if (option == 3)
                for (Pair<Integer, Integer> temp : next) {
                    if (temp.getKey() < 0 || temp.getKey() >= Config.GAME_WIDTH || temp.getValue() < 0 || temp.getValue() >= Config.GAME_HEIGHT) {
                        continue;
                    }
                    if (mapBlock[temp.getValue()][temp.getKey()].isDestructible()) {
                        if (!tryBubble(cur.getKey(), cur.getValue()))
                            continue;
                        selfPath = temp_grid[cur.getValue()][cur.getKey()];
                        box_x = cur.getKey();
                        box_y = cur.getValue();
                        return;
                    }
                }

            //from left-down to right-up
            for (Pair<Integer, Integer> temp : next) {

                if (temp.getKey() < 0 || temp.getKey() >= Config.GAME_WIDTH || temp.getValue() < 0 || temp.getValue() >= Config.GAME_HEIGHT)
                    continue;

                if (visited[temp.getValue()][temp.getKey()])
                    continue;

                if (option == 1 || option == 3) {
                    if (record[temp.getValue()][temp.getKey()]) {
                        ((LinkedList<Pair<Integer, Integer>>) queue).addLast(temp);
                        if (temp_grid[temp.getValue()][temp.getKey()].isEmpty()) {
                            for (Pair<Integer, Integer> pos : temp_grid[cur.getValue()][cur.getKey()])
                                temp_grid[temp.getValue()][temp.getKey()].add(pos);
                            temp_grid[temp.getValue()][temp.getKey()].addLast(temp);
                        }

                    } else
                        continue;
                } else if (option == 0 || option == 2) {
                    if (mapBlock[temp.getValue()][temp.getKey()].isWalkable()) {
                        ((LinkedList<Pair<Integer, Integer>>) queue).addLast(temp);
                        if (temp_grid[temp.getValue()][temp.getKey()].isEmpty()) {
                            for (Pair<Integer, Integer> pos : temp_grid[cur.getValue()][cur.getKey()])
                                temp_grid[temp.getValue()][temp.getKey()].add(pos);
                            temp_grid[temp.getValue()][temp.getKey()].addLast(temp);
                        }
                    } else
                        continue;
                }
                for(int i=0;i<Config.GAME_HEIGHT;i++) {
                    for (int j = 0; j < Config.GAME_WIDTH; j++)
                        System.out.print(temp_grid[i][j]);
                    System.out.println();
                }
                System.out.println();

            }
        }
    }

    private void nextStep() {
        /*
         * Down: return 1;
         * Left: return 2;
         * Right: return 3;
         * Up: return 4;
         */
        //int path_count = 0;
        if (count++ % 5 != 0)
            return;
        //   path_count++;
        //    if (path_count > 4)
        // break;
        Pair<Integer,Integer>step=null;
        if(!selfPath.isEmpty()) {
            step = selfPath.pop();
            int move_x = step.getKey() - x;
            int move_y = step.getValue() - y;
//            System.out.println(move_x+" "+move_y);
//            System.out.println(x+" "+y);
            if (move_x == 1)
                npc_direction = 3;
            else if (move_x == -1)
                npc_direction = 2;
            else if (move_y == 1)
                npc_direction = 1;
            else if (move_y == -1)
                npc_direction = 4;

            direction = npc_direction;
            switch (direction) {
                case 4:
                    if (!crashUp()) y -= 1;
                    break;
                case 1:
                    if (!crashDown()) y += 1;
                    break;
                case 2:
                    if (!crashLeft()) x -= 1;
                    break;
                case 3:
                    if (!crashRight()) x += 1;
                    break;
            }
            if (direction != 0 && direction != direction_cur) direction_cur = direction;
            if (direction != 0) turn = (turn + 1) % 4;
            direction = 0;
            pickItem();
        }


        if (attack) {
            if (tryBubble(x, y))
                addBubble();
        } else {
            if (x == box_x && y == box_y)
                if (tryBubble(x, y)) {
                    addBubble();
                }
        }
    }


    private int count = 0;
    private int wait = 0;
    @Override
    public void move() {
        if(selfPath.isEmpty()) {
            if (dead) return;
            computeSafeRegion();
            findPath(GameMap.getPlayer1(), 0);
            if (judgeEvade()) {
                attack = false;
                findPath(GameMap.getPlayer1(), 2);
                //nextStep();
            } else {
                if (!reach_player) {
                    attack = false;
                    findPath(GameMap.getPlayer1(), 3);
                    //nextStep();
                } else {
                    attack = true;
                    findPath(GameMap.getPlayer1(), 1);
                    //nextStep();
                }
            }
        }
        System.out.println(selfPath);
        System.out.println(x+" "+y);
        nextStep();
        System.out.println(x+" "+y);
    }
}
