// ここで各自必要なクラスをimportする
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.Node;
import javafx.scene.shape.Line;

public class TurtleGraphics extends Application {
    private int initialSceneWidth = 400; //window size
    private int initialSceneHeight = 400;
    
    @Override
    public void start(Stage st) throws Exception {
	Group root = new Group();
	
	drawTurtles(root);
	
	
	
	Scene scene = new Scene(root, initialSceneWidth, initialSceneHeight, Color.rgb(255,255,255));//ウィンドウの幅高さ色
	st.setTitle("Turtle Graphics1");
	st.setScene(scene);
	st.show();
    }
    
    public static void main(String[] a) {
	launch(a);
    }
    
    public void drawTurtles(Group root) {
	Turtle kamekichi0 = new Turtle(300,70,root);
	Turtle kamekichi1 = new Turtle(200,200,root);
	Turtle kamekichi2 = new Turtle(100,300,root);
	Turtle kamekichi3 = new Turtle(150,100,root);
	Turtle kamekichi4 = new Turtle(50,150,root);
	Turtle kamekichi5 = new Turtle(300,350,root);
	square(kamekichi0);
	square2(kamekichi1);
	triangles(kamekichi2,40,2,10);
	triangles(kamekichi3,40,2,20);
	tripen(kamekichi4);
	hexagon(kamekichi5);
    }
    
    public void square(Turtle t) {
	for (int i = 0; i < 4; i++) {
	    t.move(50);
	    t.turn(90);
	}
    }
    
    public void triangle(Turtle t, int size) {
	t.move(size);
	t.turn(120);
	t.move(size);
	t.turn(120);
	t.move(size);
	t.turn(120);
    }
    
    public void triangles(Turtle t, int n, int c, int d) {
	for (int i = 0; i++ < n; ) {
	    triangle(t, i * c);
	    t.turn(d);
	}
    }
    
    public void tripen(Turtle t){
	triangle(t, 50);
	square(t);
	pentagon(t);
    }
    
    public void pentagon(Turtle t){
	for(int i=0;i<5;i++){
	    t.move(50);
	    t.turn(72);
	}
    }
    
    public void hexagon(Turtle t){
	t.move(50);
	t.turn(60);
	t.penSize(2);
	t.move(50);
	t.turn(60);
	t.penColor(Color.RED);
	t.penSize(1);
	t.move(50);
	t.turn(60);
	t.penSize(2);
	t.move(50);
	t.turn(60);
	t.penColor(Color.BLUE);
	t.penSize(1);
	t.move(50);
	t.turn(60);
	t.penSize(2);
	t.move(50);
	t.turn(60);
    }
    
    
    public void square2(Turtle t) {
	t.move(50);
	t.turn(90);
	t.penColor(Color.rgb(255,0,0));
	t.penSize(2);
	t.move(50);
	t.turn(90);
	t.penColor(Color.rgb(0,255,0));
	t.penSize(3);
	t.move(50);
	t.turn(90);
	t.penColor(Color.rgb(0,0,255));
	t.penSize(4);
	t.move(50);
	t.turn(90);
    }
}




// この後にTurtleクラスを実装する．
class Turtle{
    //以下フィールド宣言
    private double currentX, currentY;//亀の現在位置
    private double cx, cy;   
    private double currentAngle = 0;
    private double currentpensize;
    private Color currentpencolor;
    private Group parts;  
    private Polygon pol;
    private double size = 1;
    
    //コンストラクタ宣言
    Turtle(double x, double y,Group root){//現在地currentXとcurrentYを初期化
	this.currentX=x;
	this.currentY=y;
	cx = x;
	cy = y;
	currentpensize=1;
	currentpencolor=Color.BLACK;
	parts = new Group();
	root.getChildren().add(parts);
	pol = new Polygon();
	parts.getChildren().add(pol);
	pol.setOnMouseDragged(this::mouseDragged);
	pol.setOnMousePressed(this::mousePressed);
	pol.getPoints().addAll(new Double[]{currentX, currentY});
	pol.setOpacity(0);
    }
    
    //以下メソッド    
    public void move(double length){
	double a = currentX;
	double b = currentY;
	currentX=currentX+length*Math.cos(currentAngle*(Math.PI/180.0));
	currentY=currentY+length*Math.sin(currentAngle*(Math.PI/180.0)); 
	Line line = new Line(a,b,currentX,currentY);
	line.setStroke(currentpencolor);
	line.setStrokeWidth(currentpensize);
	parts.getChildren().add(line);
	pol.getPoints().addAll(new Double[]{currentX, currentY});
    }
    
    public void turn(double deg){
	currentAngle=currentAngle - deg;
    }
    
    
    public void penSize(double size){
	currentpensize = size;
    }
    
    
    public void penColor(Color c){
	currentpencolor = c;
    }
    
    /*
      private void paint(GraphicsContext gc){
      }
    */
    
    public void mouseDragged(MouseEvent e) {
	// 円の現在地をマウスカーソルの位置に更新
	parts.setTranslateX(e.getSceneX()-cx);//oa
	parts.setTranslateY(e.getSceneY()-cy); 
    }
    
    public void mousePressed(MouseEvent e){
      	cx = e.getX();//=ab
	cy = e.getY();
    }
}
