package gamecaro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class GameCaro extends JFrame {
    
    IOFile f = new IOFile();
    ArrayList arr=new ArrayList();
    int n=20,m=20;
    boolean AIplay=false;
    byte AIturn=2;
    ArrayList<Point>  moveXO = new ArrayList<Point>();//0,2,4,.. la X ; 1,3,5,.. la O
    byte turn=0;//0 nothing , 1 X , 2 O
    JPanel BanCo=new JPanel();Panel menu=new Panel();
    XOButton[][] bt=new XOButton[n][m];
    Button undo = new Button("Undo");
    Button redo = new Button("Redo");
    Button pvp = new Button("PvP");
    Button com = new Button("Play with AI");
    Button learn = new Button("Learn");
    byte[][] status=new byte[n][m];
    int[][] TrangThai=new int[6][2];
    long[] attack={0,3,24,192,1536,12288,98304};
    long[] defend={0,1,9,81,729,6561,59849};
    long[][] point={{0,0},{2,1},{24,9},{192,81},{1536,729},{12288,6561}};
    String[] Winner={"draw","player 1","player 2","computer"};
    JLabel L = new JLabel("Chose game mode");
    ImageIcon[] Recent={null,new ImageIcon("C:\\Users\\pl\\Pictures\\Saved Pictures\\image\\X.png"),
                         new ImageIcon("C:\\Users\\pl\\Pictures\\Saved Pictures\\image\\O.png")};
    ImageIcon[] QuanCo={null,new ImageIcon("C:\\Users\\pl\\Pictures\\Saved Pictures\\image\\recentX.png"),
                         new ImageIcon("C:\\Users\\pl\\Pictures\\Saved Pictures\\image\\recentO.png")};
    File file = new File("output.txt");
    public GameCaro(){
        
        //arr=(ArrayList)f.doc("data.txt");
        this.setTitle("Game Caro");
        this.setVisible(true);
        this.setSize(950, 660);this.setBackground(Color.black);
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(menu);
        this.add(BanCo);
        menu.add(L);menu.add(undo);menu.add(pvp);menu.add(com);menu.add(learn);
        BanCo.setVisible(true);BanCo.setBounds(0, 0, 600, 600);
        BanCo.setLayout(new GridLayout(n,m));
        int i,j;
        for(i=0;i<n;i++)
            for(j=0;j<m;j++){
                status[i][j]=0;
                bt[i][j]=new XOButton(i,j);
                BanCo.add(bt[i][j]);
            }
        BanCo.updateUI();
        menu.setVisible(true);
        menu.setBounds(620, 0, 300, 400);
        menu.setLayout(null);
        L.setBounds(0, 0, 300, 50);
        L.setFont(new Font("label", Font.ITALIC, 30));
        undo.setBounds(0, 100, 50, 50);undo.setBackground(Color.YELLOW);
        undo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(AIplay){
                    Undo();Undo();
                }else Undo();
            }});
        pvp.setBounds(0,150,150,50);pvp.setBackground(Color.orange);
        pvp.setFont(new Font("pvp",Font.CENTER_BASELINE,25));
        pvp.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                //PvP();
                dataOut();
            }});
        com.setBounds(0,200,150,50);com.setBackground(Color.orange);
        com.setFont(new Font("pvp",Font.CENTER_BASELINE,25));
        com.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                AICaro();
            }});
        learn.setBounds(0,250,150,50);com.setBackground(Color.orange);
        learn.setFont(new Font("pvp",Font.CENTER_BASELINE,25));
        learn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
            }});
    }
    
    public class XOButton extends JButton implements ActionListener {
        
        int i,j;
        public XOButton(int I,int J){
            this.addActionListener(this);
            this.setBackground(Color.yellow);
            i=I;
            j=J;
        }
        public void DanhCo(){
            if(turn==0)return;
            if(status[i][j]==1||status[i][j]==2)return;
            Point oCo;
            this.setIcon(Recent[turn]);
            if(!moveXO.isEmpty()){
                oCo=moveXO.get(moveXO.size()-1);
                bt[oCo.x][oCo.y].setIcon(QuanCo[3-turn]);
            }
            status[i][j]=turn;
            oCo = new Point(i,j);
            moveXO.add(oCo);
            //Point[]playZone=playZone();
            //System.out.printf("%d:%d-%d\n",moveXO.size(),TinhDiem2((byte)1,playZone),TinhDiem2((byte)2,playZone));
            if(XuLiKetQua(turn))return;
            turn = (byte) (3-turn);
            L.setIcon(QuanCo[turn]);L.setText(Winner[turn]+"Turn");
        }
        public void actionPerformed(ActionEvent e){
            if(AIplay){
                if(turn==1){
                    //Point p=MiniMax();
                    //bt[p.x][p.y].DanhCo();
                    DanhCo();
                }
                if(turn==2)/*AIMove(turn);*/{
                    Point p=MiniMax(1);
                    bt[p.x][p.y].DanhCo();
                }
            }
            else DanhCo();
        }
    }
    
    public void Undo(){
        int i,j,k;
        if(moveXO.isEmpty())return;
        k=moveXO.size()-1;
        i=moveXO.get(k).x;
        j=moveXO.get(k).y;
        moveXO.remove(k);
        status[i][j]=0;
        bt[i][j].setIcon(null);
        turn=(byte) (3-turn);
        L.setIcon(QuanCo[turn]);
    }
    
    public void Clear(){
        int i,j;
        for(i=0;i<n;i++)
            for(j=0;j<m;j++){
                status[i][j]=0;
                bt[i][j].setIcon(null);
                moveXO.clear();
                turn=1;
            }
        turn=0;
        AIplay=false;
        L.setText("Chon che do choi");
        L.setIcon(null);
    }
    
    public void PvP(){
        Clear();
        turn=1;
        L.setText("PvP");
    }
    
    byte[] view=new byte[4];
    public void AIMove(byte xo){
        if(moveXO.isEmpty()){
            bt[n/2][m/2].DanhCo();
            return;
        }
        Point move = new Point();
        int i,j;
        long max=0,temp;
        Point[] playZone=playZone();
        for(i=playZone[0].x;i<=playZone[1].x;i++)
            for(j=playZone[0].y;j<=playZone[1].y;j++){
                if(status[i][j]==0){
                    temp=DanhGia(i,j,xo,playZone);
                    if(temp>max){
                        max=temp;
                        move.setLocation(i, j);
                    }
                }
            }
        bt[move.x][move.y].DanhCo();
    }
    
    public long DanhGia(int i,int j,byte xo,Point[] playZone){
        long sum=0;
        sum+=TinhDiem(i,j,-1,-1,xo,playZone);//1
        sum+=TinhDiem(i,j,0,-1,xo,playZone);//2
        sum+=TinhDiem(i,j,1,-1,xo,playZone);//3
        sum+=TinhDiem(i,j,1,0,xo,playZone);//4
        return sum;
    }
    
    public long TinhDiem(int i,int j,int ti,int tj,byte xo,Point[] playZone){
        int[] dem1=Dem(i,j,ti,tj,playZone);
        int[] dem2=Dem(i,j,-ti,-tj,playZone);
        int sum=0;
        if(dem1[0]==3-xo&&dem2[0]==xo){
            if(dem1[2]==xo&&dem1[1]<4){
                if(dem2[1]>3)return attack[dem2[1]];
                return 0;
            }
            if(dem2[2]==3-xo&&dem2[1]<4){
                if(dem1[1]>3)return defend[dem1[1]];
                return 0;
            }
            sum+= defend[dem1[1]]+attack[dem2[1]];
        }
        if(dem1[0]==xo&&dem2[0]==3-xo){
            if(dem1[2]==3-xo&&dem1[1]<4){
                if(dem2[1]>3)return attack[dem2[1]];
                return 0;
            }
            if(dem2[2]==xo&&dem2[1]<4){
                if(dem1[1]>3)return defend[dem1[1]];
                return 0;
            }
            sum+= attack[dem1[1]]+defend[dem2[1]];
        }
        if(dem1[0]==3-xo&&dem2[0]==3-xo){
            if(dem1[2]==xo&&dem2[2]==xo&&dem1[1]+dem2[1]<4)return 0;
            sum+= defend[dem1[1]+dem2[1]];
            if(dem2[1]+dem1[1]==3&&dem1[2]==0&&dem2[2]==0)sum+=300;
        }
        if(dem1[0]==xo&&dem2[0]==xo){
            if(dem1[2]==3-xo&&dem2[2]==3-xo&&dem1[1]+dem2[1]<4)return 0;
            sum+= attack[dem1[1]+dem2[1]];
            if(dem2[1]+dem1[1]==3&&dem1[2]==0&&dem2[2]==0)sum+=500;
        }
        if(dem1[0]==3-xo&&dem2[0]==0){
            sum+= defend[dem1[1]];
            if(dem1[1]==3&&dem1[2]==0)sum+=300;
        }
        if(dem1[0]==0&&dem2[0]==3-xo){
            sum+= defend[dem2[1]];
            if(dem2[1]==3&&dem2[2]==0)sum+=300;
        }
        if(dem1[0]==xo&&dem2[0]==0){
            sum+= attack[dem1[1]];
            if(dem1[1]==3&&dem1[2]==0)sum+=500;
        }
        if(dem1[0]==0&&dem2[0]==xo){
            sum+= attack[dem2[1]];
            if(dem2[1]==3&&dem2[2]==0)sum+=500;
        }
        if(dem1[2]==0){
            if(dem1[0]==xo)sum+=attack[dem1[1]-1];
            else if(dem1[0]==0){
                if(dem2[0]==xo)sum+=attack[dem2[1]-1];
                else if(dem2[0]==3-xo)sum+=defend[dem2[1]-1];
            }
            else if(dem1[0]==0)sum+=defend[dem1[1]-1];
        }
        if(dem2[2]==0){
            if(dem2[0]==xo)sum+=attack[dem2[1]-1];
            else if(dem2[0]==0){
                if(dem1[0]==xo)sum+=attack[dem1[1]-1];
                else if(dem1[0]==3-xo)sum+=defend[dem1[1]-1];
            }
            else if(dem2[0]==0)sum+=defend[dem2[1]-1];
        }
        return sum;
    }
    
    public int[] Dem(int i,int j,int ti,int tj,Point[] playZone){// dem tu diem (i,j) theo huong (ti,tj) co bao nhieu quan co cung loai lien tiep
        int t,count=0;
        int[]kq=new int[3];
        kq[0]=0;//dem loai nao : 1 la X , 2 la O
        kq[1]=0;// dem duoc bao nhieu
        kq[2]=0;//gap loai nao thi dung dem : 0 la rong , 1 la X , 2 la O
        if(!In(i+1*ti,j+1*tj,playZone))return kq;
        if(status[i+1*ti][j+1*tj]==0)return kq;
        kq[0]=status[i+1*ti][j+1*tj];count++;
        for(t=2;t<5;t++){
            if(!In(i+t*ti,j+t*tj,playZone))break;
            if(status[i+t*ti][j+t*tj]==kq[0])count++;
            else{ 
                kq[2]=status[i+t*ti][j+t*tj];
                break;
            }
        }
        kq[1]=count;
        return kq;
    }
    
    public boolean In(int i,int j,Point[] playZone){
        if(i<playZone[0].x||j<playZone[0].y||i>playZone[1].x||j>playZone[1].y)return false;
        else return true;
    }
    
    public Point[] playZone(){
        Point[] MinMax={new Point(0,0),new Point(n-1,m-1)};
        if(moveXO.isEmpty())return MinMax;
        Point temp=moveXO.get(0);
        int i,minHang=temp.x,minCot=temp.y,maxHang=temp.x,maxCot=temp.y;
        for(i=1;i<moveXO.size();i++){
            temp=moveXO.get(i);
            if(temp.x<minHang)minHang=temp.x;
            if(temp.x>maxHang)maxHang=temp.x;
            if(temp.y<minCot)minCot=temp.y;
            if(temp.y>maxCot)maxCot=temp.y;
        }
        if(minHang-1>=0)minHang-=1;
        if(minCot-1>=0)minCot-=1;
        if(maxHang+1<n)maxHang+=1;
        if(maxCot+1<m)maxCot+=1;
        MinMax[0].setLocation(minHang, minCot);
        MinMax[1].setLocation(maxHang, maxCot);
        //System.out.printf("playZone %d,%d %d,%d\n",minHang,maxHang,minCot,maxCot);
        return MinMax;
    }
    
    public void AICaro(){
        Clear();
        turn=1;
        AIplay=true;
        L.setText("Play with AI");
    }
    
    public long max(int state,long max,long min){
        Point[]playZone=playZone();
        if(state==0)return TinhDiem2(turn,playZone)-TinhDiem2((byte) (3-turn),playZone);
        int i,j,bestI,bestJ;
        //bestI=playZone[0].x;bestJ=playZone[1].y;
        long bestVal,val;
        bestVal=-99999;
        for(i=playZone[0].x;i<=playZone[1].x;i++)
            for(j=playZone[0].y;j<=playZone[1].y;j++){
                if(status[i][j]==0){
                    status[i][j]=turn;moveXO.add(new Point(i,j));
                    val=min(state-1,max,min);
                    status[i][j]=0;moveXO.remove(moveXO.size()-1);
                    if(bestVal<val){
                        bestVal=val;
                        //bestI=i;bestJ=j;
                        //exception.add(state+1,(Point)temp.get(state+1));
                    }
                    if(bestVal>=min){
                        //temp.add(state,new Point(bestI,bestJ));
                        return bestVal;
                    }
                    if(max<bestVal)max=bestVal;
            }
        }//System.out.printf("state%d:%d\n",state,bestVal);
        //temp.add(state,new Point(bestI,bestJ));
        return bestVal;
    }
    
    public long min(int state,long max,long min){
        Point[]playZone=playZone();
        if(state==0)return TinhDiem2(turn,playZone)-TinhDiem2((byte) (3-turn),playZone);
        int i,j,bestI,bestJ;
        //bestI=playZone[0].x;bestJ=playZone[1].y;
        long bestVal,val;
        bestVal=99999;
        for(i=playZone[0].x;i<=playZone[1].x;i++)
            for(j=playZone[0].y;j<=playZone[1].y;j++){
                if(status[i][j]==0){
                    status[i][j]=(byte) (3-turn);moveXO.add(new Point(i,j));
                    val=max(state-1,max,min);
                    status[i][j]=0;moveXO.remove(moveXO.size()-1);
                    if(bestVal>val){
                        bestVal=val;
                        //bestI=i;bestJ=j;
                        //exception.add(state+1,(Point)temp.get(state+1));
                    }
                    if(bestVal<=max){
                        //temp.add(state,new Point(bestI,bestJ));
                        return bestVal;
                    }
                    if(min>bestVal)min=bestVal;
                }
            }//System.out.printf("state%d:%d\n",state,bestVal);
        //temp.add(state,new Point(bestI,bestJ));
        return bestVal;
    }
    
    public Point MiniMax(int state){//du doan state nuoc di cua ke dich
        if(moveXO.isEmpty()){
            bt[n/2][m/2].DanhCo();
        }
        Point[]playZone=playZone();
        int i,j;
        long bestVal,val;
        int I=playZone[0].x,J=playZone[1].y;
        bestVal=-99999;
        for(i=playZone[0].x;i<=playZone[1].x;i++)
            for(j=playZone[0].y;j<=playZone[1].y;j++){
                if(status[i][j]==0){
                    status[i][j]=turn;moveXO.add(new Point(i,j));
                    val=min(state,-99999,99999);
                    status[i][j]=0;moveXO.remove(moveXO.size()-1);
                    if(bestVal<val){
                        bestVal=val;
                        I=i;J=j;
                    }
                }
            }
        return new Point(I,J);
    }
    
    public long miniMax2(int state){
        Point[]playZone=playZone();
        int i,j;
        long bestVal,val;
        int I=playZone[0].x,J=playZone[1].y;
        bestVal=-99999;
        for(i=playZone[0].x;i<=playZone[1].x;i++)
            for(j=playZone[0].y;j<=playZone[1].y;j++){
                if(status[i][j]==0){
                    status[i][j]=turn;moveXO.add(new Point(i,j));
                    val=min(state,-99999,99999);
                    status[i][j]=0;moveXO.remove(moveXO.size()-1);
                    //System.out.printf("%d,%d:%d \n",i,j,val);
                    if(bestVal<val){
                        bestVal=val;
                        I=i;J=j;
                    }
                }
            }
        //System.out.printf("best:%d,%d:%d \n",I,J,bestVal);
        return bestVal;
    }
    
    public void dataColect(){
        int t;
        int size=moveXO.size();
        Point predict,actual,temp;
        System.out.println(size);
        if(size%2==AIturn-1){// neu thua diem cuoi cung se la cua ke dich
            temp=moveXO.remove(size-1);
            status[temp.x][temp.y]=0;
            temp=moveXO.remove(size-2);
            status[temp.x][temp.y]=0;
            turn=(byte) (3-AIturn);
            for(t=size-3;t>=0;t-=2){
                actual=moveXO.remove(t);
                status[actual.x][actual.y]=0;
                findProblem(actual,turn);
                if(t>0){
                    temp=moveXO.remove(t-1);
                    status[temp.x][temp.y]=0;
                }
            }
            f.ghi(arr, "data.txt");
        }
    }
    public void findProblem(Point actualA,byte xo){
        long predict,actual,temp1,temp2;
        int[][]aA=new int [6][2];
        int i,j;
        Point[]playZone;
        //actual
        status[actualA.x][actualA.y]=xo;moveXO.add(new Point(actualA.x,actualA.y));
        turn=AIturn;
        Point p=MiniMax(0);;
        turn=(byte) (3-AIturn);
        //status[actualE.x][actualE.y]=(byte) (3-xo);moveXO.add(new Point(actualE.x,actualE.y));
        status[p.x][p.y]=(byte) (3-xo);moveXO.add(new Point(p.x,p.y));
        playZone=playZone();
        temp1=TinhDiem2(xo,playZone);
        for(i=1;i<6;i++)
            for(j=0;j<2;j++)aA[i][j]=TrangThai[i][j];
        temp2=TinhDiem2((byte) (3-xo),playZone);
        for(i=1;i<6;i++)
            for(j=0;j<2;j++)aA[i][j]-=TrangThai[i][j];
        actual=temp1-temp2;
        status[actualA.x][actualA.y]=0;moveXO.remove(moveXO.size()-1);
        status[p.x][p.y]=0;moveXO.remove(moveXO.size()-1);
        predict=miniMax2(1);
        if(predict==actual)return;
        System.out.printf("actual:%d - predict:%d\n",actual,predict);
        arr.add(aA);
        arr.add(predict);
         for(i=1;i<6;i++)
            for(j=0;j<2;j++){
                if(aA[i][j]!=0){
                    long c=0,d=point[i][j];
                    if(j==0){
                        c=point[i][j+1];
                        if(i<5)d=point[i+1][j+1];
                        else d=point[i][j];
                    }
                    else{
                        c=point[i-1][j-1];
                        d=point[i][j-1];
                    }
                    if(i==1&&j==0)d=point[2][1]/4;
                    if(i==2&&j==1)c=point[1][0]*4;
                    point[i][j]=learning((long)aA[i][j],actual-predict-aA[i][j]*point[i][j],point[i][j],c,d);
                }
            }
        for(j=0;j<2;j++){
            for(i=1;i<6;i++){
                System.out.printf("%d ",point[i][j]);
            }System.out.printf("\n");
        }System.out.printf("\n");
    }
    Random r=new Random();
    
    public long learning(long a,long b,long x,long c,long d){
        if(a==0)return x;
        if(a<0){
            if(-b/a<d&&-b/a>c)return (long)(Math.random()*(-b/a-c-1))+c+1;
        }
        if(a>0){
            if(-b/a<d&&-b/a>c)return (long)(Math.random()*(d-1+b/a))-b/a;
        }
        return x;
    }
    
    public void inPlayZone(Point[]playZone){
        System.out.printf("playZone:\n");
        for(int i=playZone[0].x;i<=playZone[1].x;i++){
            for(int j=playZone[0].y;j<=playZone[1].y;j++){
                System.out.printf("%d ",status[i][j]);
            }
            System.out.printf("\n");
        }    
    }
    public long TinhDiem2(byte xo,Point[]playZone){
        long sum=0;
        refresh();
        demHang(xo,playZone);
        demCot(xo,playZone);
        demCheoTrai(xo,playZone);
        demCheoPhai(xo,playZone);
        for(int i=1;i<6;i++)
            for(int j=0;j<2;j++)sum+=TrangThai[i][j]*point[i][j];
        return sum;
    }
    
    public void refresh(){
        for(int i=0;i<6;i++)
            for(int j=0;j<2;j++)TrangThai[i][j]=0;
    }
    
    public void demHang(byte xo,Point[]playZone){
        int count=0,i,j;
        byte truoc=(byte) (3-xo);
        for(i=playZone[0].x;i<=playZone[1].x;i++){
            for(j=playZone[0].y;j<=playZone[1].y;j++){
                if(status[i][j]==xo){
                    count++;
                }else{
                    if(count>4){
                        TrangThai[5][0]++;return;
                    }
                    if(truoc==0&&status[i][j]==0)TrangThai[count][0]++;
                    if(truoc==0&&status[i][j]!=0||truoc!=0&&status[i][j]==0)TrangThai[count][1]++;
                    count=0;truoc=status[i][j];
                }
            }
            if(count>4){
                TrangThai[5][0]++;return;
            }
            if(truoc==0)TrangThai[count][1]++;
            count=0;truoc=(byte) (3-xo);
        }
    }
    
    public void demCot(byte xo,Point[]playZone){
        int count=0,i,j;
        byte truoc=(byte) (3-xo);
        for(j=playZone[0].y;j<=playZone[1].y;j++){
            for(i=playZone[0].x;i<=playZone[1].x;i++){
                if(status[i][j]==xo){
                    count++;
                }else{
                    if(count>4){
                        TrangThai[5][0]++;return;
                    }
                    if(truoc==0&&status[i][j]==0)TrangThai[count][0]++;
                    if(truoc==0&&status[i][j]!=0||truoc!=0&&status[i][j]==0)TrangThai[count][1]++;
                    count=0;truoc=status[i][j];
                }
            }
            if(count>4){
                TrangThai[5][0]++;return;
            }
            if(truoc==0)TrangThai[count][1]++;
            count=0;truoc=(byte) (3-xo);
        }
    }
    
    public void demCheoPhai(byte xo,Point[]playZone){
        int count=0,i,j;
        byte truoc=(byte) (3-xo);
        int a=playZone[1].x-playZone[0].x,b=playZone[1].y-playZone[0].y;
        //System.out.printf("playZone %d,%d %d,%d\n",playZone[0].x,playZone[1].x,playZone[0].y,playZone[1].y);
        if(b>a){
            for(j=playZone[0].y;j<=playZone[1].y;j++){
                for(i=playZone[0].x;i<=playZone[1].x&&i+j-playZone[0].x<=playZone[1].y;i++){//System.out.printf("%d,%d ",i,i+j-playZone[0].x);
                    if(status[i][i+j-playZone[0].x]==xo){
                        count++;
                    }else{
                        if(count>4){
                        TrangThai[5][0]++;return;
                        }
                        if(truoc==0&&status[i][i+j-playZone[0].x]==0)TrangThai[count][0]++;
                        if(truoc==0&&status[i][i+j-playZone[0].x]!=0||truoc!=0&&status[i][i+j-playZone[0].x]==0)TrangThai[count][1]++;
                        count=0;truoc=status[i][i+j-playZone[0].x];
                    }
                }//System.out.printf("\n");
                if(count>4){
                    TrangThai[5][0]++;return;
                }
                if(truoc==0)TrangThai[count][1]++;
                count=0;truoc=(byte) (3-xo);
                if(j<playZone[0].y+a){
                for(i=playZone[1].x;j+i-playZone[1].x>=playZone[0].y;i--){
                    if(status[i][i+j-playZone[1].x]==xo){
                        count++;
                    }else{
                        if(count>4){
                            TrangThai[5][0]++;return;
                        }
                        if(truoc==0&&status[i][i+j-playZone[1].x]==0)TrangThai[count][0]++;
                        if(truoc==0&&status[i][i+j-playZone[1].x]!=0||truoc!=0&&status[i][i+j-playZone[1].x]==0)TrangThai[count][1]++;
                        count=0;truoc=status[i][i+j-playZone[1].x];
                    }
                }
                if(count>4){
                    TrangThai[5][0]++;return;
                }
                if(truoc==0)TrangThai[count][1]++;
                count=0;truoc=(byte) (3-xo);}
            }
        }else{
            for(i=playZone[0].x;i<=playZone[1].x;i++){
                for(j=playZone[0].y;j<=playZone[1].y&&i+j-playZone[0].y<=playZone[1].x;j++){//System.out.printf("%d,%d ",i+j-playZone[0].y,j);
                    if(status[i+j-playZone[0].y][j]==xo){
                        count++;
                    }else{
                        if(count>4){
                            TrangThai[5][0]++;return;
                        }
                        if(truoc==0&&status[i+j-playZone[0].y][j]==0)TrangThai[count][0]++;
                        if(truoc==0&&status[i+j-playZone[0].y][j]!=0||truoc!=0&&status[i+j-playZone[0].y][j]==0)TrangThai[count][1]++;
                        count=0;truoc=status[i+j-playZone[0].y][j];
                    }
                }//System.out.printf("\n");
                if(count>4){
                    TrangThai[5][0]++;return;
                }
                if(truoc==0)TrangThai[count][1]++;
                count=0;truoc=(byte) (3-xo);
                if(i<playZone[0].x+b){
                for(j=playZone[1].y;i+j-playZone[1].y>=playZone[0].x;j--){
                    if(status[i+j-playZone[1].y][j]==xo){
                        count++;
                    }else{
                        if(count>4){
                            TrangThai[5][0]++;return;
                        }
                        if(truoc==0&&status[i+j-playZone[1].y][j]==0)TrangThai[count][0]++;
                        if(truoc==0&&status[i+j-playZone[1].y][j]!=0||truoc!=0&&status[i+j-playZone[1].y][j]==0)TrangThai[count][1]++;
                        count=0;truoc=status[i+j-playZone[1].y][j];
                    }
                }
                if(count>4){
                    TrangThai[5][0]++;return;
                }
                if(truoc==0)TrangThai[count][1]++;
                count=0;truoc=(byte) (3-xo);}
            }
        }
    }
    
    public void demCheoTrai(byte xo,Point[]playZone){
        int count=0,i,j;
        byte truoc=(byte) (3-xo);
        int a=playZone[1].x-playZone[0].x,b=playZone[1].y-playZone[0].y;
        if(b>a){
            for(j=playZone[0].y;j<=playZone[1].y;j++){
                for(i=playZone[0].x;i<=playZone[1].x&&j-i+playZone[0].x>=playZone[0].y;i++){
                    if(status[i][j-i+playZone[0].x]==xo){
                        count++;
                    }else{
                        if(count>4){
                            TrangThai[5][0]++;return;
                        }
                        if(truoc==0&&status[i][j-i+playZone[0].x]==0)TrangThai[count][0]++;
                        if(truoc==0&&status[i][j-i+playZone[0].x]!=0||truoc!=0&&status[i][j-i+playZone[0].x]==0)TrangThai[count][1]++;
                        count=0;truoc=status[i][j-i+playZone[0].x];
                    }
                }
                if(count>4){
                    TrangThai[5][0]++;return;
                }
                if(truoc==0)TrangThai[count][1]++;
                count=0;truoc=(byte) (3-xo);
                if(j>playZone[1].y-a){
                for(i=playZone[1].x;j-i+playZone[1].x<=playZone[1].y;i--){
                    if(status[i][j-i+playZone[1].x]==xo){
                        count++;
                    }else{
                        if(count>4){
                            TrangThai[5][0]++;return;
                        }
                        if(truoc==0&&status[i][j-i+playZone[1].x]==0)TrangThai[count][0]++;
                        if(truoc==0&&status[i][j-i+playZone[1].x]!=0||truoc!=0&&status[i][j-i+playZone[1].x]==0)TrangThai[count][1]++;
                        count=0;truoc=status[i][j-i+playZone[1].x];
                    }
                }
                if(count>4){
                    TrangThai[5][0]++;return;
                }
                if(truoc==0)TrangThai[count][1]++;
                count=0;truoc=(byte) (3-xo);}
            }
        }else{
            for(i=playZone[0].x;i<=playZone[1].x;i++){
                for(j=playZone[0].y;j<=playZone[1].y&&i-j+playZone[0].y>=playZone[0].x;j++){
                    if(status[i-j+playZone[0].y][j]==xo){
                        count++;
                    }else{
                        if(count>4){
                            TrangThai[5][0]++;return;
                        }
                        if(truoc==0&&status[i-j+playZone[0].y][j]==0)TrangThai[count][0]++;
                        if(truoc==0&&status[i-j+playZone[0].y][j]!=0||truoc!=0&&status[i-j+playZone[0].y][j]==0)TrangThai[count][1]++;
                        count=0;truoc=status[i-j+playZone[0].y][j];
                    }
                }
                if(count>4){
                    TrangThai[5][0]++;return;
                }
                if(truoc==0)TrangThai[count][1]++;
                count=0;truoc=(byte) (3-xo);
                if(i>playZone[1].x-b){
                for(j=playZone[1].y;i-j+playZone[1].y<=playZone[1].x;j--){
                    if(status[i-j+playZone[1].y][j]==xo){
                        count++;
                    }else{
                        if(count>4){
                            TrangThai[5][0]++;return;
                        }
                        if(truoc==0&&status[i-j+playZone[1].y][j]==0)TrangThai[count][0]++;
                        if(truoc==0&&status[i-j+playZone[1].y][j]!=0||truoc!=0&&status[i-j+playZone[1].y][j]==0)TrangThai[count][1]++;
                        count=0;truoc=status[i-j+playZone[1].y][j];
                    }
                }
                if(count>4){
                    TrangThai[5][0]++;return;
                }
                if(truoc==0)TrangThai[count][1]++;
                count=0;truoc=(byte) (3-xo);}
            }
        }
    }
    
    public boolean XuLiKetQua(byte xo){
        if((checkHang(xo))||(checkCot(xo))||(checkCheoPhai(xo))||(checkCheoTrai(xo))){
            dataColect();
            int op = JOptionPane.showConfirmDialog(this,
                    Winner[xo]+" da thang ! Ban co muon choi lai khong ?", "Ket qua",
                    JOptionPane.YES_NO_OPTION);
            if(op==JOptionPane.YES_OPTION){
                if(AIplay)AICaro();
                else PvP();
            }
            //else Clear();
            return true;
        }
        return false;
    }
    
    public boolean checkHang(byte xo){//xo=1 check X , xo=2 check O
        int count=0,i,j;
        for(i=0;i<n;i++){
            for(j=0;j<m;j++){
                if(status[i][j]==xo){
                    count++;
                    if(count>4)return true;
                }
                else count = 0;
            }count=0;
        }   
        return false;	
    }
    
    public boolean checkCot(byte xo){
        int count=0,i,j;
        for(j=0;j<m;j++){
            for(i=0;i<n;i++){
                if(status[i][j]==xo){
                    count++;
                    if(count>4)return true;
                }
                else count = 0;
            }count=0;
        }
	return false;
    }
    
    public boolean checkCheoPhai(byte xo){
        int count=0,i,j;
        for(i=0;i<n;i++){
            for(j=0;i+j<n;j++){
                if(status[i+j][j]==xo){
                    count++;
                    if(count>4)return true;
                }
                else count = 0;
            }count=0;
            for(j=n-1;i+j-n+1>=0;j--){
                if(status[i+j-n+1][j]==xo){
                    count++;
                    if(count>4)return true;
                }
                else count = 0;
            }count=0;
        }
        return false;
    }
    
    public boolean checkCheoTrai(byte xo){
        int count=0,i,j;
        for(i=n-1;i>=0;i--){
            for(j=0;i-j>=0&&j<n;j++){
                if(status[i-j][j]==xo){
                    count++;
                    if(count>4)return true;
                }
                else count = 0;
            }count=0;
            for(j=n-1;i-j+n-1<n&&j>=0;j--){
                if(status[i-j+n-1][j]==xo){
                    count++;
                    if(count>4)return true;
                }
                else count = 0;
            }count=0;
        }
	return false;
    }
    
    public void dataOut(){
        int i,j,t;
        for(t=0;t<arr.size();t+=2){
            int[][] list=new int[6][2];
            list=(int[][])arr.get(t);
            for(j=0;j<2;j++){
                for(i=1;i<6;i++){
                    System.out.printf("%d ",list[i][j]);
                }System.out.printf("\n");
            }
            long l=(long)arr.get(t+1);
            System.out.println(l);
            System.out.printf("\n");
        }
    }
    
    public static void main(String[] args) {
        new GameCaro();
    } 
}
