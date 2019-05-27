import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

class Point{
    public int x;
    public int y;
    public Vector<Integer>path;
    public Point(int x,int y){
        this.x=x;
        this.y=y;
        this.path=new Vector<>();
    }
}
public class NPC2 extends Character {

    final String DANGER_MARKER="-1";
    final int POWER=1;
    boolean[][]visited;
    private Vector<Integer>path=new Vector<>();

    public NPC2(int x, int y, int id, BufferedImage image, int direction) {
        super(x, y, id, image, direction);
    }

    private boolean outOfBound(int x,int y){
        if(x>=0 && x<=19 && y>=0 &&y<=14)
        {
            return false;
        }
        return true;
    }

    //calculate the safe region
    private String[][] getDangerZone(){
        String[][] dangerZone=new String[20][15];
        for(int i=0;i<20;i++)
        {
            for(int j=0;j<15;j++)
            {
                dangerZone[i][j]="1";
            }
        }
        ArrayList<Bubble> bubbleList=GameMap.getBubbles();
        MapBlock[][] block=GameMap.getBlock();
        for(Bubble tmp:bubbleList){
            if(tmp.isAlive()) {
                //System.out.println(tmp.getX()+" "+tmp.getY());
                Point bubbleLoc = new Point(tmp.getX(), tmp.getY());
                int power = tmp.getPower();
                System.out.println("power:"+power);
                dangerZone[bubbleLoc.x][bubbleLoc.y] = DANGER_MARKER;
                for (int i=bubbleLoc.x-1;i >= Math.max(0, bubbleLoc.x - power); i--) {
                    if (block[bubbleLoc.y][i].isDestructible()||block[bubbleLoc.y][i].isWalkable()) {
                        dangerZone[i][bubbleLoc.y] = DANGER_MARKER;
                    } else {
                        boolean result=block[bubbleLoc.y][i].isWalkable();
                        //System.out.println("leftbound:"+i);
                        break;
                    }
                }
                for (int i = bubbleLoc.x+1; i <= Math.min(19, bubbleLoc.x + power); i++) {
                    if(block[bubbleLoc.y][i].isDestructible()||block[bubbleLoc.y][i].isWalkable()) {
                        dangerZone[i][bubbleLoc.y] = DANGER_MARKER;
                    }
                    else{

                        break;
                    }
                }
                for(int j=bubbleLoc.y-1;j>=Math.max(0,bubbleLoc.y-power);j--){
                    if(block[j][bubbleLoc.x].isDestructible()||block[j][bubbleLoc.x].isWalkable()){
                        dangerZone[bubbleLoc.x][j]=DANGER_MARKER;
                    }
                    else{
                         break;
                    }
                }
                for(int j=bubbleLoc.y+1;j<=Math.min(14,bubbleLoc.y+power);j++)
                {
                    if(block[j][bubbleLoc.x].isDestructible()||block[j][bubbleLoc.x].isWalkable()){
                        dangerZone[bubbleLoc.x][j]=DANGER_MARKER;
                    }
                    else{
                        break;
                    }
                }
            }
        }
        return dangerZone;
    }

    //judge if there is bubble in the position
    private boolean judgeLocBubble(int x,int y){
        ArrayList<Bubble> bubbleList=GameMap.getBubbles();
        for(Bubble tmp:bubbleList){
            if(tmp.isAlive()&&tmp.getX()==x&&tmp.getY()==y){
                return true;
            }
        }
        return false;
    }

    //find the safe path to avoid bubble
    //use the mean of bfs
    private boolean findSafePath(){
        visited=new boolean[20][15];
        for(int i=0;i<20;i++)
        {
            for(int j=0;j<15;j++)
            {
                visited[i][j]=false;
            }
        }
        String[][] dangerZone=getDangerZone();
        Queue<Point> queue=new LinkedList<>();
        MapBlock[][] block=GameMap.getBlock();
        Point start=new Point(this.x,this.y);//the position of the player is the start
        int[][] next={{0,1},{-1,0},{1,0},{0,-1}};//the direction of the next step
        queue.add(start);
        //start to find
        while(!queue.isEmpty()) {
            Point fPoint = queue.poll();
            System.out.println(fPoint.x+" "+fPoint.y);
            //if the area is safe and walkable then the road is find
            visited[fPoint.x][fPoint.y] = true;
            if (block[fPoint.y][fPoint.x].isWalkable() && !dangerZone[fPoint.x][fPoint.y].equals(DANGER_MARKER)
                    && !judgeLocBubble(fPoint.x, fPoint.y)) {
                path.clear();
                path.addAll(fPoint.path);
                return true;
            }
            //choose the next walkable step
            //judge the next point
            int tx,ty;
            for(int i=0;i<4;i++){
                tx=fPoint.x+next[i][0];
                ty=fPoint.y+next[i][1];
                if(!outOfBound(tx,ty)) {
                    //System.out.println(tx+" "+ty);
                    if (!visited[tx][ty] && block[ty][tx].isWalkable()&& !judgeLocBubble(tx,ty)) {
                        Point tPoint = new Point(tx, ty);
                        tPoint.path.addAll(fPoint.path);
                        tPoint.path.addElement(i + 1);
                        queue.add(tPoint);
                    }
                }
            }
        }
        return false;
    }

    private boolean judgeNearby(int x,int y){
        final int MAX_POWER=3;
        for(int i=(-1)*MAX_POWER;i<=MAX_POWER;i++)
        {
            if((x==this.x && y==(this.y+i)) || (x==(this.x+i) && y==this.y))
            {
                return true;
            }
        }
        return false;
    }

    private boolean findSafePathAfter(){
        visited=new boolean[20][15];
        String[][] dangerZone=getDangerZone();
        Queue<Point> queue=new LinkedList<>();
        MapBlock[][] block=GameMap.getBlock();
        Point start=new Point(this.x,this.y);//the position of the player is the start
        int[][] next={{0,1},{-1,0},{1,0},{0,-1}};//the direction of the next step
        queue.add(start);
        //start to find
        while(!queue.isEmpty()) {
            Point fPoint = queue.poll();
            //if the area is safe and walkable then the road is find
            visited[fPoint.x][fPoint.y] = true;
            if (block[fPoint.y][fPoint.x].isWalkable() && !dangerZone[fPoint.x][fPoint.y].equals(DANGER_MARKER)
                    && !judgeLocBubble(fPoint.x, fPoint.y)
                    && !judgeNearby(fPoint.x,fPoint.y)
               )
            {
                path.clear();
                path.addAll(fPoint.path);
                return true;
            }
            //choose the next walkable step
            //judge the next point
            int tx,ty;
            for(int i=0;i<4;i++){
                tx=fPoint.x+next[i][0];
                ty=fPoint.y+next[i][1];
                if(!outOfBound(tx,ty)) {
                    if (!visited[tx][ty] && block[ty][tx].isWalkable() && !judgeLocBubble(tx,ty)) {
                        Point tPoint = new Point(tx, ty);
                        tPoint.path.addAll(fPoint.path);
                        tPoint.path.addElement(i + 1);
                        queue.add(tPoint);
                    }
                }
            }
        }
        return false;
    }

    private boolean findSafePathAfterBFS(int dx,int dy){
        visited=new boolean[20][15];
        String[][] dangerZone=getDangerZone();
        Queue<Point> queue=new LinkedList<>();
        MapBlock[][] block=GameMap.getBlock();
        Point start=new Point(dx,dy);//the position of the player is the start
        int[][] next={{0,1},{-1,0},{1,0},{0,-1}};//the direction of the next step
        queue.add(start);
        //start to find
        while(!queue.isEmpty()) {
            Point fPoint = queue.poll();
            //if the area is safe and walkable then the road is find
            visited[fPoint.x][fPoint.y] = true;
            if (block[fPoint.y][fPoint.x].isWalkable() && !dangerZone[fPoint.x][fPoint.y].equals(DANGER_MARKER)
                    && !judgeLocBubble(fPoint.x, fPoint.y)
                    && !judgeNearby(fPoint.x,fPoint.y)
            )
            {
                return true;
            }
            //choose the next walkable step
            //judge the next point
            int tx,ty;
            for(int i=0;i<4;i++){
                tx=fPoint.x+next[i][0];
                ty=fPoint.y+next[i][1];
                if(!outOfBound(tx,ty)) {
                    if (!visited[tx][ty] && block[ty][tx].isWalkable() && !judgeLocBubble(tx,ty)) {
                        Point tPoint = new Point(tx, ty);
                        tPoint.path.addAll(fPoint.path);
                        tPoint.path.addElement(i + 1);
                        queue.add(tPoint);
                    }
                }
            }
        }
        return false;
    }

    //check if other players are near me
    private boolean ifAttackPlayer(){
        Character[] player=new Character[4];
        int[][] next={{-1,-1},{0,-1},{1,-1},{-1,0},{0,0},{1,0},{-1,1},{0,1},{1,1}};
        for(int i=0;i<4;i++){
            player[i]=GameMap.getPlayer(i+1);
        }
        for(int i=0;i<4;i++){
            if(i!=2 && player[i]!=null){
                int leftBound=Math.max(player[i].getX()-POWER,0);
                int rightBound=Math.min(player[i].getX()+POWER,19);
                int upBound=Math.max(player[i].getY()-POWER,0);
                int downBound=Math.min(player[i].getY()+POWER,14);
                for(int j=0;j<9;j++) {
                    Point tmp=new Point(this.x+next[j][0],this.y+next[j][1]);
                    if(tmp.x>=leftBound && tmp.x<=rightBound && tmp.y>=upBound && tmp.y<=downBound
                            &&this.bubbleNumMax-this.bubbleNum>0 )
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //randomly choose the destination
    private void randomFindPath(){
        int di;
        int dj;
        String[][] dangerZone=getDangerZone();
        MapBlock[][] block=GameMap.getBlock();
        do{
            Random rand1=new Random();
            di=rand1.nextInt(19);
            Random rand2=new Random();
            dj=rand2.nextInt(14);
        }while(dangerZone[di][dj].equals(DANGER_MARKER)||(!block[dj][di].isWalkable() && !block[dj][di].isDestructible()));
        System.out.println("destination:"+di+" "+dj);
        System.out.println("find the destination");
        BFS(di,dj,dangerZone);
    }


    //try to reach the player's position
    private void findPath(){
        String[][] dangerZone=getDangerZone();
        MapBlock[][] block=GameMap.getBlock();
        Character[] player=new Character[4];
        for(int i=0;i<4;i++){
            player[i]=GameMap.getPlayer(i+1);
        }
        for(int i=0;i<4;i++)
        {
            if(i!=2 && player[i]!=null)
            {
                if(BFS(player[i].getX(),player[i].getY(),dangerZone)){
                    System.out.println("find the player");
                    break;
                }
            }
        }
    }

    //bfs the path
    private boolean BFS(int x,int y,String[][]dangerZone){
        MapBlock[][]block=GameMap.getBlock();
        visited=new boolean[20][15];
        int[][] next={{0,1},{-1,0},{1,0},{0,-1}};//the direction of the next step
        //get the start point
        Point start=new Point(this.x,this.y);
        Queue<Point>queue=new LinkedList<>();
        queue.add(start);
        //begin to find the path
        while(!queue.isEmpty()){
            Point fPoint=queue.poll();
            visited[fPoint.x][fPoint.y]=true;
            //if the path is found
            if(fPoint.x==x && fPoint.y==y){
                path.clear();
                path.addAll(fPoint.path);
                return true;
            }
            int tx;
            int ty;
            for(int i=0;i<4;i++){
                tx=fPoint.x+next[i][0];
                ty=fPoint.y+next[i][1];
                if(!outOfBound(tx,ty)) {
                    if(tx==x && ty==y && block[ty][tx].isDestructible()){
                        System.out.println("there is obstacle");
                        if(findSafePathAfterBFS(fPoint.x,fPoint.y)){
                            Point tPoint = new Point(tx, ty);
                            tPoint.path.addAll(fPoint.path);
                            tPoint.path.addElement(i + 1);
                            queue.add(tPoint);
                        }
                    }
                    if (!dangerZone[tx][ty].equals(DANGER_MARKER) && !visited[tx][ty]
                            && block[ty][tx].isWalkable() && !judgeLocBubble(tx,ty)) {
                                Point tPoint = new Point(tx, ty);
                                tPoint.path.addAll(fPoint.path);
                                System.out.println("direction "+i+1);
                                tPoint.path.addElement(i + 1);
                                queue.add(tPoint);
                    }
                }
            }
        }
        return false;
    }

    private boolean judgeStop(){
        String[][] dangerZone=getDangerZone();
        MapBlock[][] block=GameMap.getBlock();
        int[][] next={{0,1},{-1,0},{1,0},{0,-1}};//the direction of the next step
        int tx;
        int ty;
        for(int i=0;i<4;i++)
        {
            tx=this.x+next[i][0];
            ty=this.y+next[i][1];
            if(!outOfBound(tx,ty)){
                if(!dangerZone[tx][ty].equals(DANGER_MARKER) && block[ty][tx].isWalkable() && !judgeLocBubble(tx,ty)){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean judgeForward(int nextStop){
        boolean go=false;
        MapBlock[][] block=GameMap.getBlock();
        switch(nextStop){
            case 0:go=true;
                   break;
            case 1:if(block[this.y+1][this.x].isWalkable()){go=true;}
                   break;
            case 2:if(block[this.y][this.x-1].isWalkable()){go=true;}
                   break;
            case 3:if(block[this.y][this.x+1].isWalkable()){go=true;}
                   break;
            case 4:if(block[this.y-1][this.x].isWalkable()){go=true;}
                   break;
        }
        return go;
    }

    //count the players' bomb
    int CountBubble(){
        int count=0;
        ArrayList<Bubble>BubbleList=GameMap.getBubbles();
        for(Bubble temp:BubbleList){
            if(temp.getPlayerId()==2 && temp.isAlive())
            {
                count++;
            }
        }
        return count;
    }

    //choose the next step
    private int nextStep() {
        /**
         * TODO: WPL
         * Down: return 1;
         * Left: return 2;
         * Right: return 3;
         * Up: return 4;
         *
         * if need to add bubble, just use function addBubble();
         */

        // Here is an example, one step down and put one bubble
        // NPC1 can only put one bubble at first
        // So only after the bubble explode can he puts another one

        String[][] dangerZone = getDangerZone();
        int[][] next={{0,0},{0,1},{-1,0},{1,0},{0,-1}};//the direction of the next step
        if (dangerZone[this.x][this.y].equals(DANGER_MARKER)) {
            System.out.println("dangerous!");
            boolean findResult = findSafePath();
            //can not go
            if (!findResult) {
                return 0;
            }
        }
        /*for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.print(dangerZone[i][j]);
            }
            System.out.print("\n");
        }*/
        else if(ifAttackPlayer()){
            System.out.println("yes");
            if(CountBubble()>=0 && CountBubble()<=1){
                System.out.println(CountBubble());
                if(findSafePathAfter())
                {
                    addBubble();
                }
            }
        }
        else{
            findPath();
        }
        if(path.isEmpty()){
            int count=0;
            do{
                count++;
                System.out.println("empty!");
                randomFindPath();

            }while(path.isEmpty() && count<=500);
        }
        if(path.isEmpty()) {
            if (judgeStop()) {
                return 0;
            }
            else{
                Random rand = new Random();
                int direction = rand.nextInt(4);
                dangerZone=getDangerZone();
                if (!outOfBound(this.x+next[direction][0],this.y+next[direction][1])
                        && !dangerZone[this.x+next[direction][0]][this.y+next[direction][1]].equals(DANGER_MARKER)) {
                    path.addElement(direction);
                    System.out.println("add random");
                }
            }
        }
        int next_Move=0;
        if(path.size()>0){
            next_Move=path.firstElement();
            //System.out.println(nextMove);
            path.removeElementAt(0);
        }
        if(!judgeForward(next_Move)){
            if(CountBubble()>=0 && CountBubble()<=1){
                //System.out.println(CountBubble());
                if(findSafePathAfter())
                {
                    addBubble();
                }
            }
            else{
                return 0;
            }
        }
        System.out.println(next_Move);
        return next_Move;
        /*Random rand=new Random();
        int direction=rand.nextInt(4);
        return direction;*/
    }

    private int count = 0;
    @Override
    public void move() {
        if (dead) return;
        if (count++ % 5 != 0) return;
        direction = nextStep();
        System.out.println("nextStep:direction");
        //System.out.println(direction);
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
}
