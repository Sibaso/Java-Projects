package snake;

import java.applet.Applet;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class game extends JFrame implements ActionListener,KeyListener {
	
	private final int DELAY = 150;
    private Timer timer;
    public final int size=30;
    public final int len=21;
    public final int minX=30;
    public final int minY=60;
    public final int maxX=(len-1)*size+minX;
    public final int maxY=(len-1)*size+minY;
    public snake s;
    public food f;
    
	public game() {
		setTitle("snake");
        setSize(720, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(Color.white);
        this.addKeyListener(this);
        //this.setIconImage(new ImageIcon("C:\\Users\\pl\\Pictures\\Saved Pictures\\gamefish.png").getImage());
        timer = new Timer(DELAY, this);
        timer.start();
        s=new snake();
        f=new food(this);
	}

    public void actionPerformed(ActionEvent e) {
    	s.move(this);
        repaint();
    }
    
    public void keyPressed( KeyEvent e )  {
    	if(e.getKeyCode()==KeyEvent.VK_SPACE) {
    		if(timer.isRunning())timer.stop();
    		else timer.start();
    	}
    	s.changeDirect(e);
    	repaint();
    } 
    
    public void keyReleased( KeyEvent e )  {}
    
    public void keyTyped( KeyEvent e )  {}
    
    public boolean checkRule() {
    	Point p=s.getHead();
    	if(p.x<minX||p.x>maxX||p.y<minY||p.y>maxY) {
    		return true;
    	}
    	ArrayList a=(ArrayList)s.body;
    	for(int i=1;i<a.size();i++) {
    		Point t=(Point)a.get(i);
    		if(p.x==t.x&&p.y==t.y) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public void endGame(Graphics g) {
    	timer.stop();
		int op = JOptionPane.showConfirmDialog(this,
                "Game Over! Your score is : "+s.len+"\nDo you want to play again ?", "End Game",
               JOptionPane.YES_NO_OPTION);
        if(op==JOptionPane.YES_OPTION){
        	s.remove(g);
        	f.remove(g);
        	s=new snake();
        	f=new food(this);
        	timer.start();
        }else {
        	this.dispose();
        }
    }
    
    public void paint(Graphics g) {
    	if(checkRule()) {
    		endGame(g);
    	}
    	g.setColor(Color.BLACK);
    	g.drawRect(minX, minY, size*len, size*len);
    	f.drawFood(g);
    	s.drawSnake(g);
    	Point p=s.getHead();
    	if(p.x==f.x&&p.y==f.y) {
    		s.eat(this);
    		f=new food(this);
    	}
    	s.see(this);
    }
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                game x = new game();
                x.setVisible(true);
            }
        });

	}
	
}
