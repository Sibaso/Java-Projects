package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class food {

	public int x,y;
	
	public food(game b) {
		Random r=new Random();
		x=Math.abs(r.nextInt())%(b.len-1)*b.size+b.minX;
		y=Math.abs(r.nextInt())%(b.len-1)*b.size+b.minY;
	}
	
	public void drawFood(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(x, y, 30, 30);
	}
	
	public void remove(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, 30, 30);
	}
	
}
