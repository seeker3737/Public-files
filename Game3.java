import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Game3 extends JPanel {
    static final int EMPTY = 0, KURO = 1, SIRO = 2;
    static final int YMAX = 8, XMAX = 8;
    static ArrayList<Figure> figs = new ArrayList<Figure>();
    boolean turn = true;
    int winner = EMPTY;
    static int[][] board = new int[YMAX][XMAX];
    Text t1 = new Text(20, 20, "五目リバーシ、次の手番：⿊", new Font("Serif", Font.BOLD, 22));
    Circle c1 = new Circle(Color.BLUE, 100, 100, 30);
    Circle c2 = new Circle(Color.RED, 100, 100, 30);
    public Game3() {
	figs.add(t1);
	figs.add(c1);
	figs.add(c2);
	long tm0 = System.currentTimeMillis();
	new javax.swing.Timer(50, new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    double tm = 0.001 * (System.currentTimeMillis() - tm0);
		    c1.moveTo(20 + (int) (tm * 100) % 250, 100);
		    c2.moveTo(20 + (int) (tm * 100) % 250, 200);
		    repaint();
		}
	    }).start();
	for (int i = 0; i < YMAX * XMAX; ++i) {
	    int r = i / YMAX, c = i % YMAX;
	    figs.add(new Rect(Color.GREEN, 80 + r * 20, 40 + c * 20, 18, 18));		
	}
	setOpaque(false);
	addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent evt) {
		    Rect r = pick(evt.getX(), evt.getY());
		    if (r == null || winner != EMPTY) {
			return;
		    }
		    int x = (r.getX() - 80) / 20, y = (r.getY() - 40) / 20;
			if (board[y][x] != EMPTY) {
			    t1.setText("空いていません");
			    repaint();
			    return;
			}
			if (turn) {
			    figs.add(new Kuro(r.getX(), r.getY(), 8));
			    board[y][x] = KURO;
			    turnStone2(x,y);
			} else {
			    figs.add(new Siro(r.getX(), r.getY(), 8));
			    board[y][x] = SIRO;
			    turnStone(x,y);
			}
			int a = ck(x, y, 1, 1), b = ck(x, y, 1, -1), c = ck(x, y, 1, 0), d = ck(x, y, 0, 1);
			if (a > 4 || b > 4 || c > 4 || d > 4) {
			    t1.setText((turn ? "⿊" : "⽩") + "の勝利！");
			    winner = turn ? KURO : SIRO;
			} else {
			    turn = !turn;
			    t1.setText("次の手番：" + (turn ? "⿊" : "⽩"));
			}
			repaint();
		}
	    });
    }
    
    public Rect pick(int x, int y) {
	Rect r = null;
	for (Figure f : figs) {
	    if (f instanceof Rect && ((Rect) f).hit(x, y)) {
		r = (Rect) f;
	    }
	}
	return r;
    }
    
    public void paintComponent(Graphics g) {
	for (Figure f : figs) {
	    f.draw(g);
	}
    }
    
    private int ck(int x, int y, int dx, int dy) {
	int s = board[y][x], count = 1;
	for (int i = 1; ck1(x + dx * i, y + dy * i, s); ++i) {
	    ++count;
	}
	for (int i = 1; ck1(x - dx * i, y - dy * i, s); ++i) {
	    ++count;
	}
	return count;
    }
    
    private boolean ck1(int x, int y, int s) {
	return 0 <= x && x < XMAX && 0 <= y && y < YMAX && board[y][x] == s;
    }
    
    public static void main(String[] args) {
	JFrame app = new JFrame();
	app.add(new Game3());
	app.setSize(500, 300);
	app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	app.setVisible(true);
    }
    
    interface Figure {
	public void draw(Graphics g);
    }
    
    static class Text implements Figure {
	int xpos, ypos;
	String txt;
	Font fn;
	
	public Text(int x, int y, String t, Font f) {
	    xpos = x;
	    ypos = y;
	    txt = t;
	    fn = f;
	}
	
	public void setText(String t) {
	    txt = t;
	}
	
	public void draw(Graphics g) {
	    g.setColor(Color.BLACK);
	    g.setFont(fn);
	    g.drawString(txt, xpos, ypos);
	}
    }
    
    static class Circle implements Figure {
	Color col;
	int xpos, ypos, rad;
	
	public Circle(Color c, int x, int y, int r) {
	    col = c;
	    xpos = x;
	    ypos = y;
	    rad = r;
	}
	
	public boolean hit(int x, int y) {
	    return (xpos - x) * (xpos - x) + (ypos - y) * (ypos - y) <= rad * rad;
	}
	
	public void setColor(Color c) {
	    col = c;
	}
	
	public void moveTo(int x, int y) {
	    xpos = x;
	    ypos = y;
	}
	
	public void draw(Graphics g) {
	    g.setColor(col);
	    g.fillOval(xpos - rad, ypos - rad, rad * 2, rad * 2);
	}
    }
    
    
    
    
    
    
    
    static class Siro implements Figure {
	int xpos, ypos, size;
	
	public Siro(int x, int y, int s) {
	    xpos = x;
	    ypos = y;
	    size = s;
	}
	
	public void draw(Graphics g) {
	    g.setColor(Color.WHITE);
	    ((Graphics2D) g).setStroke(new BasicStroke(4));
	    g.fillOval(xpos - size, ypos - size, 2 * size, 2 * size);
	}
    }
    
    static class Kuro implements Figure {
	int xpos, ypos, size;
	
	public Kuro(int x, int y, int s) {
	    xpos = x;
	    ypos = y;
	    size = s;
	}
	
	public void draw(Graphics g) {
	    g.setColor(Color.BLACK);
	    ((Graphics2D) g).setStroke(new BasicStroke(4));
	    g.fillOval(xpos - size, ypos - size, 2 * size, 2 * size);
	}
    }
    
    static class Rect implements Figure {
	Color col;
	int xpos, ypos, width, height;
	
	public Rect(Color c, int x, int y, int w, int h) {
	    col = c;
	    xpos = x;
	    ypos = y;
	    width = w;
	    height = h;
	}
	
	public boolean hit(int x, int y) {
	    return xpos - width / 2 <= x && x <= xpos + width / 2 && ypos - height / 2 <= y && y <= ypos + height / 2;
	}
	
	public int getX() {
	    return xpos;
	}
	
	public int getY() {
	    return ypos;
	}
	
	public void draw(Graphics g) {
	    g.setColor(col);
	    g.fillRect(xpos - width / 2, ypos - height / 2, width, height);
	}
    }
    static public void turnStone(int x, int y) {
	turnLeftUp(x, y);
	turnUp(x, y);
	turnRightUp(x, y);
	turnLeft(x, y);
	turnRight(x, y);
	turnLeftDown(x, y);
	turnDown(x, y);
	turnRightDown(x, y);	
    }
    
    static public void turnStone2(int x, int y) {
	turnLeftUp2(x, y);
	turnUp2(x, y);
	turnRightUp2(x, y);
	turnLeft2(x, y);
	turnRight2(x, y);
	turnLeftDown2(x, y);
	turnDown2(x, y);
	turnRightDown2(x, y);	
    }
    
    
    
    static public void turnLeftUp(int x, int y) {
	if (y > 1 && x > 1) {
	    int next = board[y - 1][x - 1];
	    if (next==KURO) {
		for (int i = 2; true; i++) {
		    if (x - i < 0 || y - i < 0 || board[y - i][x - i]==EMPTY) {
			break;
		    } else if (board[y - i][x - i]==SIRO) {
			for (int t = 1; t < i; t++) {
			    board[y - t][x - t] = SIRO;
			    figs.add(new Siro(x*20+80-t*20, y*20+40-t*20, 8));
			}
			break;
		    }
		}
	    }
	    
	}
    }
    
    static public void turnUp(int x, int y) {
	if (y > 1) {
	    int next = board[y - 1][x];
	    if (next==KURO) {
		for (int i = 2; true; i++) {
		    if (y - i < 0 || board[y - i][x]==EMPTY) {
			break;
		    } else if (board[y - i][x]==SIRO) {
			for (int t = 1; t < i; t++) {
			    board[y - t][x] = SIRO;
			    figs.add(new Siro(x*20+80, y*20+40-t*20, 8));
			}
			break;
		    }
		}
	    }
	    
	}
    }
    
    static public void turnRightUp(int x, int y) {
	if (y > 1 && x < 6) {
	    int next = board[y - 1][x + 1];
	    if (next==KURO) {	
		for (int i = 2; true; i++) {
		    if (x + i > 7 || y - i < 0 || board[y - i][x + i]==EMPTY) {
			break;
		    } else if (board[y - i][x + i]==SIRO) {
			for (int t = 1; t < i; t++) {
			    board[y - t][x + t] = SIRO;
			    figs.add(new Siro(x*20+80+t*20, y*20+40-t*20, 8));
			}
			break;
		    }
		}
	    }
	    
	}
    }
    
    static public void turnDown(int x, int y) {
	if (y < 6) {
	    int next = board[y + 1][x];
	    if (next==KURO) {
		for (int i = 2; true; i++) {
		    if (y + i > 7 || board[y + i][x]==EMPTY) {
			break;
		    } else if (board[y + i][x]==SIRO) {
			for (int t = 1; t < i; t++) {
			    board[y + t][x] = SIRO;
			    figs.add(new Siro(x*20+80, y*20+40+t*20, 8));
			}
			break;
		    }
		}
	    }
	    
	}
    }
    
    static public void turnRight(int x, int y) {
	if (x < 6) {
	    int next = board[y][x + 1];    
	    if (next==KURO) {
		for (int i = 2; true; i++) {
		    if (x + i > 7 || board[y][x + i]==EMPTY) {
			break;
		    } else if (board[y][x + i]==SIRO) {
			for (int t = 1; t < i; t++) {
			    board[y][x + t] = SIRO;
			    figs.add(new Siro(x*20+80+t*20, y*20+40, 8));
			}
			break;
		    }
		}
	    }	    
	}
    }
    
    static public void turnLeftDown(int x, int y) {
	if (y < 6 && x > 1) {
	    int next = board[y + 1][x - 1];
	    if (next==KURO) {
		for (int i = 2; true; i++) {
		    if (x - i < 0 || y + i > 7 || board[y + i][x - i]==EMPTY) {
			break;
		    } else if (board[y + i][x - i]==SIRO) {
			for (int t = 1; t < i; t++) {
			    board[y + t][x - t] = SIRO;
			    figs.add(new Siro(x*20+80-t*20, y*20+40+t*20, 8));
			}
			break;
		    }
		}
	    }	    
	}
    }
    
    static public void turnLeft(int x, int y) {
	if (x > 1) {
	    int next = board[y][x - 1];
	    if (next==KURO) {
		for (int i = 2; true; i++) {
		    if (x - i < 0 || board[y][x - i]==EMPTY) {
			break;
		    } else if (board[y][x - i]==SIRO) {
			for (int t = 1; t < i; t++) {
			    board[y][x - t] = SIRO;
			    figs.add(new Siro(x*20+80-t*20, y*20+40, 8));
			}
			break;
		    }
		}
	    }	    
	}
    }
    static public void turnRightDown(int x, int y) {
	if (y < 6 && x < 6) {
	    int next = board[y + 1][x + 1];
	    if (next==KURO) {
		for (int i = 2; true; i++) {
		    if (x + i > 7 || y + i > 7 || board[y + i][x + i]==EMPTY) {
			break;
		    } else if (board[y + i][x + i]==SIRO) {
			for (int t = 1; t < i; t++) {
			    board[y + t][x + t] = SIRO;
			    figs.add(new Siro(x*20+80+t*20, y*20+40+t*20, 8));
			}
			break;
		    }
		}
	    }	    
	}
    }
    //2    
    
    static public void turnLeftUp2(int x, int y) {
	if (y > 1 && x > 1) {
	    int next = board[y - 1][x - 1];
	    if (next==SIRO) {
		for (int i = 2; true; i++) {
		    if (x - i < 0 || y - i < 0 || board[y - i][x - i]==EMPTY) {
			break;
		    } else if (board[y - i][x - i]==KURO) {
			for (int t = 1; t < i; t++) {
			    board[y - t][x - t] = KURO;
			    figs.add(new Kuro(x*20+80-t*20, y*20+40-t*20, 8));
			}
			break;
		    }
		}
	    }	    
	}
    }
    
    static public void turnUp2(int x, int y) {
	if (y > 1) {
	    int next = board[y - 1][x];
	    if (next==SIRO) {
		for (int i = 2; true; i++) {
		    if (y - i < 0 || board[y - i][x]==EMPTY) {
			break;
		    } else if (board[y - i][x]==KURO) {
			for (int t = 1; t < i; t++) {
			    board[y - t][x] = KURO;
			    figs.add(new Kuro(x*20+80, y*20+40-t*20, 8));
			}
			break;
		    }
		}
	    }	    
	}
    }
    
    static public void turnRightUp2(int x, int y) {
	if (y > 1 && x < 6) {
	    int next = board[y - 1][x + 1];
	    if (next==SIRO) {	       
		for (int i = 2; true; i++) {
		    if (x + i > 7 || y - i < 0 || board[y - i][x + i]==EMPTY) {
			break;
		    } else if (board[y - i][x + i]==KURO) {			
			for (int t = 1; t < i; t++) {  
			    board[y - t][x + t] = KURO;
			    figs.add(new Kuro(x*20+80+t*20, y*20+40-t*20, 8));
			}
			break;
		    }
		}
	    }	    
	}
    }
    
    static public void turnDown2(int x, int y) {
	if (y < 6) {	  
	    int next = board[y + 1][x];
	    if (next==SIRO) {		
		for (int i = 2; true; i++) {		    
		    if (y + i > 7 || board[y + i][x]==EMPTY) {
			break;
		    } else if (board[y + i][x]==KURO) {
			for (int t = 1; t < i; t++) {
			    board[y + t][x] = KURO;
			    figs.add(new Kuro(x*20+80, y*20+40+t*20, 8));
			}
			break;
		    }
		}
	    }	    
	}
    }
    
    static public void turnRight2(int x, int y) {
	if (x < 6) {
	    int next = board[y][x + 1];	    
	    if (next==SIRO) {
		for (int i = 2; true; i++) {
		    if (x + i > 7 || board[y][x + i]==EMPTY) {
			break;
		    } else if (board[y][x + i]==KURO) {
			for (int t = 1; t < i; t++) {
			    board[y][x + t] = KURO;
			    figs.add(new Kuro(x*20+80+t*20, y*20+40, 8));
			}
			break;
		    }
		}
	    }	    
	}
    }
    
    static public void turnLeftDown2(int x, int y) {
	if (y < 6 && x > 1) {
	    int next = board[y + 1][x - 1];
	    if (next==SIRO) {
		for (int i = 2; true; i++) {
		    if (x - i < 0 || y + i > 7 || board[y + i][x - i]==EMPTY) {
			break;
		    } else if (board[y + i][x - i]==KURO) {
			for (int t = 1; t < i; t++) {
			    board[y + t][x - t] = KURO;
			    figs.add(new Kuro(x*20+80-t*20, y*20+40+t*20, 8));
			}
			break;
		    }
		}
	    }	    
	}
    }
    
    static public void turnLeft2(int x, int y) {
	if (x > 1) {
	    int next = board[y][x - 1];
	    if (next==SIRO) {
		for (int i = 2; true; i++) {
		    if (x - i < 0 || board[y][x - i]==EMPTY) {
			break;
		    } else if (board[y][x - i]==KURO) {
			for (int t = 1; t < i; t++) {
			    board[y][x - t] = KURO;
			    figs.add(new Kuro(x*20+80-t*20, y*20+40, 8));
			}
			break;
		    }
		}
	    }    
	}
    }
    static public void turnRightDown2(int x, int y) {
	if (y < 6 && x < 6) {
	    int next = board[y + 1][x + 1];
	    if (next==SIRO) {
		for (int i = 2; true; i++) {
		    if (x + i > 7 || y + i > 7 || board[y + i][x + i]==EMPTY) {
			break;
		    } else if (board[y + i][x + i]==KURO) {
			for (int t = 1; t < i; t++) {
			    board[y + t][x + t] = KURO;
			    figs.add(new Kuro(x*20+80+t*20, y*20+40+t*20, 8));
			}
			break;
		    }
		}
	    }	    
	}
    }    
}
