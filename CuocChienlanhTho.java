/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuocchienlanhtho;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author pl
 */
public class CuocChienlanhTho {

    JFrame f=new JFrame();
    JPanel p=new JPanel();
    JPanel menu=new JPanel();
    int size=15;
    int turn=1;
    Random r=new Random();
    int[][]status = new int[size][size];
    button[][]bt=new button[size][size];
    JButton ok=new JButton();
    Point player1,player2;
    public CuocChienlanhTho(){
        f.setTitle("CCLT");
        f.setVisible(true);
        f.setSize(950, 660);f.setBackground(Color.black);
        f.setResizable(false);
        f.getContentPane().setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(p);f.add(menu);
        p.setVisible(true);p.setBounds(0, 0, 600, 600);
        p.setLayout(new GridLayout(size,size));
        menu.setVisible(true);
        menu.setBounds(620, 0, 300, 400);
        menu.setLayout(null);
        menu.add(ok);
        ok.setBounds(0, 100, 50, 50);ok.setBackground(Color.YELLOW);
        ok.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(queueA.isEmpty()&&queueE.isEmpty()){
                    clear(val);
                    queueA.add(new Point(player1.x,player1.y));
                    queueE.add(new Point(player2.x,player2.y));
                }
                luongGiaClone();
            }});
        int i,j;
        for(i=0;i<size;i++)
            for(j=0;j<size;j++){
                status[i][j]=0;
                bt[i][j]=new button(i,j);
                p.add(bt[i][j]);
            }
        p.updateUI();
        int x=r.nextInt(size),y=r.nextInt(size);
        player1=new Point(0,0);player2=new Point(14,14);
        bt[0][0].setBackground(Color.red);
        status[0][0]=1;
        bt[14][14].setBackground(Color.green);
        status[14][14]=2;
        for(i=0;i<5;i++){
                x=r.nextInt(size);
                y=r.nextInt(size);
                if((x!=player1.x||y!=player1.y)&&(x!=player2.x||y!=player2.y)){
                    bt[x][y].setBackground(Color.black);
                    status[x][y]=3;
                    bt[14-x][14-y].setBackground(Color.black);
                    status[14-x][14-y]=3;
                }
        }  
    }
    public class button extends JButton implements ActionListener {
        int i,j;
        public button(int hang,int cot){
            i=hang;j=cot;
            this.addActionListener(this);
            this.setBackground(Color.white);
        }
        public void move(){
            if(status[i][j]==1||status[i][j]==2||status[i][j]==3)return;
            if(turn==1){
                if((i!=player1.x-1||j!=player1.y)&&(i!=player1.x+1||j!=player1.y)&&(i!=player1.x||j!=player1.y-1)&&(i!=player1.x||j!=player1.y+1))return;
                this.setBackground(Color.red);
                status[i][j]=1;
                player1.setLocation(i,j);
                turn=2;
            }else if(turn==2){
                if((i!=player2.x-1||j!=player2.y)&&(i!=player2.x+1||j!=player2.y)&&(i!=player2.x||j!=player2.y-1)&&(i!=player2.x||j!=player2.y+1))return;
                this.setBackground(Color.green);
                status[i][j]=2;
                player2.setLocation(i,j);
                turn=1;
            }
            if(endGame(turn))System.out.printf("player%d lose\n",turn);
            //System.out.printf("%d-%d\n",luongGia(player1.x,player1.y),luongGia(player2.x,player2.y));
        }
        public void actionPerformed(ActionEvent e){
            if(turn==1)findTheWay(player1.x,player1.y,player2.x,player2.y);
            if(turn==2)findTheWay(player2.x,player2.y,player1.x,player1.y);
            //move();
        }
    }
    public boolean endGame(int turn){
        if(turn==1){
            if(!isSpace(player1.x-1,player1.y)&&!isSpace(player1.x+1,player1.y)&&!isSpace(player1.x,player1.y-1)&&!isSpace(player1.x,player1.y+1))
                return true;
        }else if(turn==2){
            if(!isSpace(player2.x-1,player2.y)&&!isSpace(player2.x+1,player2.y)&&!isSpace(player2.x,player2.y-1)&&!isSpace(player2.x,player2.y+1))
                return true;
        }
        return false;
    }
    ArrayList queueA = new ArrayList();
    ArrayList queueE = new ArrayList();
    public int luongGiaClone(){
        int sumA=0,sumE=0,i,j;
        if(!queueE.isEmpty()||!queueA.isEmpty()){
            Point p;
            if(!queueA.isEmpty()){
                p = (Point)queueA.remove(0);
                i=p.x;
                j=p.y;
                bt[i][j].setBackground(Color.pink);
                sumA+=tinhDiem(i,j);
                if(isSpace(i+1,j)){
                    queueA.add(new Point(i+1,j));
                    status[i+1][j]=val;
                }
                if(isSpace(i-1,j)){
                    queueA.add(new Point(i-1,j));
                    status[i-1][j]=val;
                }
                if(isSpace(i,j+1)){
                    queueA.add(new Point(i,j+1));
                    status[i][j+1]=val;
                }
                if(isSpace(i,j-1)){
                    queueA.add(new Point(i,j-1));
                    status[i][j-1]=val;
                }
            }
            if(!queueE.isEmpty()){
                p = (Point)queueE.remove(0);
                i=p.x;
                j=p.y;
                bt[i][j].setBackground(Color.yellow);
                sumE+=tinhDiem(i,j);
                if(isSpace(i+1,j)){
                    queueE.add(new Point(i+1,j));
                    status[i+1][j]=val;
                }
                if(isSpace(i-1,j)){
                    queueE.add(new Point(i-1,j));
                    status[i-1][j]=val;
                }
                if(isSpace(i,j+1)){
                    queueE.add(new Point(i,j+1));
                    status[i][j+1]=val;
                }
                if(isSpace(i,j-1)){
                    queueE.add(new Point(i,j-1));
                    status[i][j-1]=val;
                }
            }
        }
        //clear(val);
        return sumA-sumE;
    }
    public void clear(int x){
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)if(status[i][j]==x)status[i][j]=0;
    }
    public void clear(int x1,int x2){
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)if(status[i][j]==x1||status[i][j]==x2)status[i][j]=0;
    }
    public int is(int i,int j){
        if(i<0||i>=size||j<0||j>=size)return -1;
        return status[i][j];
    }
    public boolean isSpace(int i,int j){
        if(i<0||i>=size||j<0||j>=size)return false;
        if(status[i][j]==0)return true;
        return false;
    }
    public boolean isSpaceAndVal(int i,int j){
        if(i<0||i>=size||j<0||j>=size)return false;
        if(status[i][j]==0||status[i][j]==val)return true;
        return false;
    }
    int val=4;
    public void roll(){
        int temp=val;
        while(temp==val){
            temp=r.nextInt(5)+4;
        }
        val=temp;
    }
    public void in(){
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                System.out.printf("%d ",status[i][j]);
            }
            System.out.printf("\n");
        }              
    }
    
    public int luongGia(int xa,int ya,int xe,int ye){
        int sumA=0,sumE=0,i,j;
        ArrayList queueA = new ArrayList();
        ArrayList queueE = new ArrayList();
        queueA.add(new Point(xa,ya));
        queueE.add(new Point(xe,ye));
        while(!queueE.isEmpty()||!queueA.isEmpty()){
            Point p;
            if(!queueA.isEmpty()){
                p = (Point)queueA.remove(0);
                i=p.x;
                j=p.y;
                sumA+=tinhDiem(i,j);
                if(isSpace(i+1,j)){
                    queueA.add(new Point(i+1,j));
                    status[i+1][j]=val;
                }
                if(isSpace(i-1,j)){
                    queueA.add(new Point(i-1,j));
                    status[i-1][j]=val;
                }
                if(isSpace(i,j+1)){
                    queueA.add(new Point(i,j+1));
                    status[i][j+1]=val;
                }
                if(isSpace(i,j-1)){
                    queueA.add(new Point(i,j-1));
                    status[i][j-1]=val;
                }
            }
            if(!queueE.isEmpty()){
                p = (Point)queueE.remove(0);
                i=p.x;
                j=p.y;
                sumE+=tinhDiem(i,j);
                if(isSpace(i+1,j)){
                    queueE.add(new Point(i+1,j));
                    status[i+1][j]=val;
                }
                if(isSpace(i-1,j)){
                    queueE.add(new Point(i-1,j));
                    status[i-1][j]=val;
                }
                if(isSpace(i,j+1)){
                    queueE.add(new Point(i,j+1));
                    status[i][j+1]=val;
                }
                if(isSpace(i,j-1)){
                    queueE.add(new Point(i,j-1));
                    status[i][j-1]=val;
                }
            }
        }
        //val++;
        clear(val);
        //System.out.printf("%d-%d\n", sumA,sumE);
        return sumA-sumE;
    }
    public int luongGia2(int xa,int ya,int xe,int ye){
        int sumA=0,sumE=0,i,j;
        ArrayList queueA = new ArrayList();
        ArrayList queueE = new ArrayList();
        queueA.add(new Point(xa,ya));
        queueE.add(new Point(xe,ye));
        while(!queueE.isEmpty()||!queueA.isEmpty()){
            Point p;
            if(!queueA.isEmpty()){
                p = (Point)queueA.remove(0);
                i=p.x;
                j=p.y;
                sumA+=tinhDiem(i,j);
                if(isSpace(i+1,j)){
                    queueA.add(new Point(i+1,j));
                    status[i+1][j]=val;
                }
                if(isSpace(i-1,j)){
                    queueA.add(new Point(i-1,j));
                    status[i-1][j]=val;
                }
                if(isSpace(i,j+1)){
                    queueA.add(new Point(i,j+1));
                    status[i][j+1]=val;
                }
                if(isSpace(i,j-1)){
                    queueA.add(new Point(i,j-1));
                    status[i][j-1]=val;
                }
            }
            if(!queueE.isEmpty()){
                p = (Point)queueE.remove(0);
                i=p.x;
                j=p.y;
                sumE+=tinhDiem(i,j);
                if(isSpace(i+1,j)){
                    queueE.add(new Point(i+1,j));
                    status[i+1][j]=val;
                }
                if(isSpace(i-1,j)){
                    queueE.add(new Point(i-1,j));
                    status[i-1][j]=val;
                }
                if(isSpace(i,j+1)){
                    queueE.add(new Point(i,j+1));
                    status[i][j+1]=val;
                }
                if(isSpace(i,j-1)){
                    queueE.add(new Point(i,j-1));
                    status[i][j-1]=val;
                }
            }
        }
        //val++;
        clear(val);
        //System.out.printf("%d-%d\n", sumA,sumE);
        return sumA-sumE;
    }
    public boolean enemyInside(int xa,int ya,int xe,int ye){
        int i,j;
        boolean kq=false;
        ArrayList queue = new ArrayList();
        queue.add(new Point(xa,ya));
        while(!queue.isEmpty()){
            Point p;
            p = (Point)queue.remove(0);
            i=p.x;
            j=p.y;
            if(isSpace(i+1,j)){
                queue.add(new Point(i+1,j));
                status[i+1][j]=val;
            }
            if(isSpace(i-1,j)){
                queue.add(new Point(i-1,j));
                status[i-1][j]=val;
            }
            if(isSpace(i,j+1)){
                queue.add(new Point(i,j+1));
                status[i][j+1]=val;
            }
            if(isSpace(i,j-1)){
                queue.add(new Point(i,j-1));
                status[i][j-1]=val;
            }
        }
        if(is(xe-1,ye)==val||is(xe+1,ye)==val||is(xe,ye-1)==val||is(xe,ye+1)==val)kq=true;
        //val++;
        clear(val);
        return kq;
    }
    public int tinhDiem(int x,int y){
        int sum=0;
        if(isSpaceAndVal(x-1,y))sum++;
        if(isSpaceAndVal(x+1,y))sum++;
        if(isSpaceAndVal(x,y-1))sum++;
        if(isSpaceAndVal(x,y+1))sum++;
        return sum;
    }
    public int dem(int xa,int ya){
        int i,j,sum=0;
        ArrayList queue = new ArrayList();
        queue.add(new Point(xa,ya));
        while(!queue.isEmpty()){
            Point p;
            p = (Point)queue.remove(0);
            i=p.x;
            j=p.y;
            sum+=tinhDiem(i,j);
            if(isSpace(i+1,j)){
                queue.add(new Point(i+1,j));
                status[i+1][j]=val;
            }
            if(isSpace(i-1,j)){
                queue.add(new Point(i-1,j));
                status[i-1][j]=val;
            }
            if(isSpace(i,j+1)){
                queue.add(new Point(i,j+1));
                status[i][j+1]=val;
            }
            if(isSpace(i,j-1)){
                queue.add(new Point(i,j-1));
                status[i][j-1]=val;
            }
        }
        //val++;
        clear(val);
        return sum;
    }
    public int fill(int state,int xa,int ya){
        if(state==12)return dem(xa,ya)+state*5;
        int max=state*5;
        if(isSpace(xa-1,ya)){
            status[xa-1][ya]=turn;
            int temp=fill(state+1,xa-1,ya);
            status[xa-1][ya]=0;
            if(temp>max)max=temp;
        }
        if(isSpace(xa+1,ya)){
            status[xa+1][ya]=turn;
            int temp=fill(state+1,xa+1,ya);
            status[xa+1][ya]=0;
            if(temp>max)max=temp;
        }
        if(isSpace(xa,ya-1)){
            status[xa][ya-1]=turn;
            int temp=fill(state+1,xa,ya-1);
            status[xa][ya-1]=0;
            if(temp>max)max=temp;
        }
        if(isSpace(xa,ya+1)){
            status[xa][ya+1]=turn;
            int temp=fill(state+1,xa,ya+1);
            status[xa][ya+1]=0;
            if(temp>max)max=temp;
        }
        return max;
    }
    public void fillWay(int xa,int ya){
        int max=0,t=0;
        if(isSpace(xa-1,ya)){
            status[xa-1][ya]=turn;
            int temp=fill(1,xa-1,ya);
            status[xa-1][ya]=0;
            if(temp>max){
                max=temp;
                t=1;
            }
        }
        if(isSpace(xa+1,ya)){
            status[xa+1][ya]=turn;
            int temp=fill(1,xa+1,ya);
            status[xa+1][ya]=0;
            if(temp>max){
                max=temp;
                t=2;
            }
        }
        if(isSpace(xa,ya-1)){
            status[xa][ya-1]=turn;
            int temp=fill(1,xa,ya-1);
            status[xa][ya-1]=0;
            if(temp>max){
                max=temp;
                t=3;
            }
        }
        if(isSpace(xa,ya+1)){
            status[xa][ya+1]=turn;
            int temp=fill(1,xa,ya+1);
            status[xa][ya+1]=0;
            if(temp>max){
                max=temp;
                t=4;
            }
        }
        if(t==1)bt[xa-1][ya].move();
        else if(t==2)bt[xa+1][ya].move();
        else if(t==3)bt[xa][ya-1].move();
        else if(t==4)bt[xa][ya+1].move();
    }
    public void findTheWay(int xa,int ya,int xe,int ye){
        if(enemyInside(xa,ya,xe,ye))miniMax(xa,ya,xe,ye);
        else fillWay(xa,ya);
    }
    int count=0;
    int la=0;
    public void miniMax(int xa,int ya,int xe,int ye){
        int BestVal=-999,i=xa,j=ya,t=0;
        if(isSpace(xa-1,ya)){
            status[xa-1][ya]=turn;
            int temp=min(xa-1,ya,xe,ye,0,-999,999);
            status[xa-1][ya]=0;
            if(BestVal<temp){
                BestVal=temp;
                t=1;
            }
        }
        if(isSpace(xa+1,ya)){
            status[xa+1][ya]=turn;
            int temp=min(xa+1,ya,xe,ye,0,-999,999);
            status[xa+1][ya]=0;
            if(BestVal<temp){
                BestVal=temp;
                t=2;
            }
        }
        if(isSpace(xa,ya-1)){
            status[xa][ya-1]=turn;
            int temp=min(xa,ya-1,xe,ye,0,-999,999);
            status[xa][ya-1]=0;
            if(BestVal<temp){
                BestVal=temp;
                t=3;
            }
        }
        if(isSpace(xa,ya+1)){
            status[xa][ya+1]=turn;
            int temp=min(xa,ya+1,xe,ye,0,-999,999);
            status[xa][ya+1]=0;
            if(BestVal<temp){
                BestVal=temp;
                t=4;
            }
        }
        System.out.println(turn+":"+count);
        count=0;
        if(t==1)bt[xa-1][ya].move();
        else if(t==2)bt[xa+1][ya].move();
        else if(t==3)bt[xa][ya-1].move();
        else if(t==4)bt[xa][ya+1].move();
    }
    public int max(int xa,int ya,int xe,int ye,int state,int max,int min){//a Ally, e Enemy
        if(state==10){
            count++;
            return luongGia(xa,ya,xe,ye);
        }
        if(!enemyInside(xa,ya,xe,ye))return dem(xa,ya)-dem(xe,ye);
        int BestVal=-999;
        boolean key1=true,key2=true,key3=true,key4=true;
        int[]seq=khoangCach(xa,ya,xe,ye);
        if(isSpace(xa+order[seq[0]].x,ya+order[seq[0]].y)){
            status[xa+order[seq[0]].x][ya+order[seq[0]].y]=turn;
            int temp=min(xa+order[seq[0]].x,ya+order[seq[0]].y,xe,ye,state+1,max,min);
            status[xa+order[seq[0]].x][ya+order[seq[0]].y]=0;
            if(temp>BestVal)BestVal=temp;
            if(BestVal>=min)return BestVal;
            if(max<BestVal)max=BestVal;
            key1=false;
        }
        if(isSpace(xa+order[seq[1]].x,ya+order[seq[1]].y)){
            status[xa+order[seq[1]].x][ya+order[seq[1]].y]=turn;
            int temp=min(xa+order[seq[1]].x,ya+order[seq[1]].y,xe,ye,state+1,max,min);
            status[xa+order[seq[1]].x][ya+order[seq[1]].y]=0;
            if(temp>BestVal)BestVal=temp;
            if(BestVal>=min)return BestVal;
            if(max<BestVal)max=BestVal;
            key2=false;
        }
        if(isSpace(xa+order[seq[2]].x,ya+order[seq[2]].y)){
            status[xa+order[seq[2]].x][ya+order[seq[2]].y]=turn;
            int temp=min(xa+order[seq[2]].x,ya+order[seq[2]].y,xe,ye,state+1,max,min);
            status[xa+order[seq[2]].x][ya+order[seq[2]].y]=0;
            if(temp>BestVal)BestVal=temp;
            if(BestVal>=min)return BestVal;
            if(max<BestVal)max=BestVal;
            key3=false;
        }
        if(isSpace(xa+order[seq[3]].x,ya+order[seq[3]].y)){
            status[xa+order[seq[3]].x][ya+order[seq[3]].y]=turn;
            int temp=min(xa+order[seq[3]].x,ya+order[seq[3]].y,xe,ye,state+1,max,min);
            status[xa+order[seq[3]].x][ya+order[seq[3]].y]=0;
            if(temp>BestVal)BestVal=temp;
            if(BestVal>=min)return BestVal;
            if(max<BestVal)max=BestVal;
            key4=false;
        }
        if(key1&&key2&&key3&&key4){
            count++;
            return luongGia(xa,ya,xe,ye);
        }
        return BestVal;
    }
    public int min(int xa,int ya,int xe,int ye,int state,int max,int min){//a Ally, e Enemy
        if(state==10){
            count++;
            return -luongGia(xe,ye,xa,ya);
        }
        if(!enemyInside(xa,ya,xe,ye))return dem(xa,ya)-dem(xe,ye);
        int BestVal=999;
        boolean key1=true,key2=true,key3=true,key4=true;
        int[]seq=khoangCach(xe,ye,xa,ya);
        if(isSpace(xe+order[seq[0]].x,ye+order[seq[0]].y)){
            status[xe+order[seq[0]].x][ye+order[seq[0]].y]=3-turn;
            int temp=max(xa,ya,xe+order[seq[0]].x,ye+order[seq[0]].y,state+1,max,min);
            status[xe+order[seq[0]].x][ye+order[seq[0]].y]=0;
            if(temp<BestVal)BestVal=temp;
            if(BestVal<=max)return BestVal;
            if(min>BestVal)min=BestVal;
            key1=false;
        }
        if(isSpace(xe+order[seq[1]].x,ye+order[seq[1]].y)){
            status[xe+order[seq[1]].x][ye+order[seq[1]].y]=3-turn;
            int temp=max(xa,ya,xe+order[seq[1]].x,ye+order[seq[1]].y,state+1,max,min);
            status[xe+order[seq[1]].x][ye+order[seq[1]].y]=0;
            if(temp<BestVal)BestVal=temp;
            if(BestVal<=max)return BestVal;
            if(min>BestVal)min=BestVal;
            key2=false;
        }
        if(isSpace(xe+order[seq[2]].x,ye+order[seq[2]].y)){
            status[xe+order[seq[2]].x][ye+order[seq[2]].y]=3-turn;
            int temp=max(xa,ya,xe+order[seq[2]].x,ye+order[seq[2]].y,state+1,max,min);
            status[xe+order[seq[2]].x][ye+order[seq[2]].y]=0;
            if(temp<BestVal)BestVal=temp;
            if(BestVal<=max)return BestVal;
            if(min>BestVal)min=BestVal;
            key3=false;
        }
        if(isSpace(xe+order[seq[3]].x,ye+order[seq[3]].y)){
            status[xe+order[seq[3]].x][ye+order[seq[3]].y]=3-turn;
            int temp=max(xa,ya,xe+order[seq[3]].x,ye+order[seq[3]].y,state+1,max,min);
            status[xe+order[seq[3]].x][ye+order[seq[3]].y]=0;
            if(temp<BestVal)BestVal=temp;
            if(BestVal<=max)return BestVal;
            if(min>BestVal)min=BestVal;
            key4=false;
        }
        if(key1&&key2&&key3&&key4){
            count++;
            return -luongGia(xe,ye,xa,ya);
        }
        return BestVal;
    }
    Point[]order={new Point(-1,0),new Point(1,0),new Point(0,-1),new Point(0,+1)};
    public int[] khoangCach(int xa,int ya,int xe,int ye){
        int t=0,i,j;
        int[] seq=new int[4];
        int[]a=new int[4];
        a[0]=(xa-1-xe)*(xa-1-xe)+(ya-ye)*(ya-ye);
        a[1]=(xa+1-xe)*(xa+1-xe)+(ya-ye)*(ya-ye);
        a[2]=(xa-xe)*(xa-xe)+(ya-1-ye)*(ya-1-ye);
        a[3]=(xa-xe)*(xa-xe)+(ya+1-ye)*(ya+1-ye);
        seq[0]=0;
        seq[1]=1;
        seq[2]=2;
        seq[3]=3;
        int  temp =  0 ;  
    for  (i =  0 ; i <3 ; i ++) {  
        for  (j =  0 ; j <3  - i; j ++) {  
            if  (a [j]> a [j +  1 ]) {  
                temp = a [j];  
                a [j] = a [j +  1 ];  
                a [j +  1 ] = temp; 
                t=seq[j];
                seq[j]=seq[j+1];
                seq[j+1]=t;
            }  
        }  
    }
        return seq;
    }
    public static void main(String[] args) {
        new CuocChienlanhTho();
    }
    
}
