package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class snake {

	public ArrayList body;
	public int len;
	private Point tail;
	enum direct{left,right,up,down};
	public direct d;
	
	public snake() {
		len=1;
		body=new ArrayList();
		body.add(new Point(330,330));
		tail=new Point(330,330);
		d=direct.left;
	}
	
	public Point getHead() {
		return (Point)body.get(0);
	}
	
	public void moveLeft(game b) {
		d=direct.left;
		Point p=getHead();
		body.add(0,new Point(p.x-b.size,p.y));
		tail=(Point)body.remove(body.size()-1);
	}
	public void moveRight(game b) {
		d=direct.right;
		Point p=getHead();
		body.add(0,new Point(p.x+b.size,p.y));
		tail=(Point)body.remove(body.size()-1);
	}
	public void moveUp(game b) {
		d=direct.up;
		Point p=getHead();
		body.add(0,new Point(p.x,p.y-b.size));
		tail=(Point)body.remove(body.size()-1);
	}
	public void moveDown(game b) {
		d=direct.down;
		Point p=getHead();
		body.add(0,new Point(p.x,p.y+b.size));
		tail=(Point)body.remove(body.size()-1);
	}
	public void move(game b) {
		if(d==direct.left) {
			moveLeft(b);
		}else if(d==direct.right) {
			moveRight(b);
		}else if(d==direct.up) {
			moveUp(b);
		}else if(d==direct.down) {
			moveDown(b);
		}
	}
	
	public void changeDirect(KeyEvent e) {
		if(d!=direct.down&&(e.getKeyCode()==KeyEvent.VK_UP||e.getKeyCode()==KeyEvent.VK_W)) {
    		d=direct.up;
    	}else if(d!=direct.up&&(e.getKeyCode()==KeyEvent.VK_DOWN||e.getKeyCode()==KeyEvent.VK_S)) {
    		d=direct.down;
    	}else if(d!=direct.right&&(e.getKeyCode()==KeyEvent.VK_LEFT||e.getKeyCode()==KeyEvent.VK_A)) {
    		d=direct.left;
    	}else if(d!=direct.left&&(e.getKeyCode()==KeyEvent.VK_RIGHT||e.getKeyCode()==KeyEvent.VK_D)) {
    		d=direct.right;
    	}
	}
	
	public void eat(game b) {
		body.add(new Point(0,0));
		len++;
	}
	
	public int lookAt(game b,int x,int y) {
		Point p=getHead();
		int i=0;
		while(true) {
			if(p.x+i*x<b.minX||p.x+i*x>b.maxX||p.y+i*y<b.minY||p.y+i*y>b.maxY) {
				//System.out.println(i);
				return i;
			}
	    	for(int j=1;j<body.size();j++) {
	    		Point t=(Point)body.get(j);
	    		if(p.x+i*x==t.x&&p.y+i*y==t.y) {
	    			//System.out.println(i);
	    			return i;
	    		}
	    	}
	    	if(p.x+i*x==b.f.x&&p.y+i*y==b.f.y) {
	    		//System.out.println(-i);
	    		return -i;
	    	}
			i+=b.size;
		}
	}
	
	public int[] see(game b) {
		int[]a=new int[8];
		a[0]=lookAt(b,-1,0);
		a[1]=lookAt(b,-1,1);
		a[2]=lookAt(b,0,1);
		a[3]=lookAt(b,1,1);
		a[4]=lookAt(b,1,0);
		a[5]=lookAt(b,1,-1);
		a[6]=lookAt(b,0,-1);
		a[7]=lookAt(b,-1,-1);
		return a;
	}
	
	public void drawSnake(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(tail.x, tail.y, 30, 30);
		Point p=getHead();
		g.setColor(Color.GREEN);
		g.fillRect(p.x, p.y, 30, 30);
	}
	
	public void remove(Graphics g) {
		for(int i=0;i<len;i++) {
			Point p=(Point)body.get(i);
			g.setColor(Color.WHITE);
			g.fillRect(p.x, p.y, 30, 30);
		}
		g.fillRect(tail.x, tail.y, 30, 30);
	}
	
}
