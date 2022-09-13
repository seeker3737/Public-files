import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import java.io.*;

import javafx.scene.canvas.GraphicsContext;


public class LineGraph extends Application {
    static int[] pa;
    @Override
    public void start(Stage st) throws Exception {
	Group root = new Group();
	Canvas canvas = new Canvas(900, 800);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	drawShapes(gc);
	
	root.getChildren().add(canvas);
	
	Scene scene = new Scene(root, 900, 800, Color.WHITE);
	st.setTitle("Line Graph");
	st.setScene(scene);
	st.show();
    }
    
    public static void main(String[] a) {
	try {
	    File file = new File("./data.txt");
	    FileReader fr = new FileReader(file);
	    // ファイルから一度に読み込んだり，マークを付けた点から再読み込みをしたりするための
	    // BufferedReaderの作成
	    BufferedReader br = new BufferedReader(fr);
	    
	    String str = br.readLine(); // 一行ずつ読み込む
	    
	    String[] data = str.split(" ");// 区切り文字" "で文字列を区切ru
	    pa = new int[data.length];	
	    for(int i = 0; i < data.length; i++){
		pa[i]=Integer.parseInt(data[i]);
	    }//mojiretu kara hairetu
	    br.close(); // BufferedReaderをcloseすると自動的にFileReaderもcloseする
	} catch (FileNotFoundException e) {
	    System.err.println("ファイルが開けませんでした．");
	} catch (IOException e) { // readLine()からの例外を処理
	    System.err.println("ファイルの読み込みに失敗しました．");
	}
	
	launch(a);
    }
    
    private void drawShapes(GraphicsContext gc) {
	
	if(pa.length<31){//要素数が31個以上では行わない
	    
	    int intMax = pa[0];//前後の比較で配列の最大値を求める
	    for (int i=1;i < pa.length;i++){
		if(intMax<pa[i]){
		    intMax=pa[i];
		}
	    }
	    
	    int k = (intMax/50)+1;
	
	if(k<14){//14は縦の描画領域の都合
	    
	    gc.setFill(Color.PINK);
	    gc.fillRect(50,50,25*pa.length,50*(k+1)-50);
	    
	    
	    gc.setStroke(Color.BLACK);
	    gc.setFill(Color.BLACK);
	    
	    
	    for(int i = 0; i<k; i++){
		gc.strokeLine(50,50+(i*50),50+25*pa.length,50+(50*i));
	    }
	    
	    gc.strokeLine(50, 50*(k+1), 50+25*pa.length, 50*(k+1)); // 横軸
	    gc.strokeLine(50, 50*(k+1), 50, 50); // 縦軸
	    
	    for (int i = 0; i < pa.length+1; i++) {
		gc.strokeLine(50 + i * 25, 50*(k+1), 50 + i * 25, 50*(k+1)+2); // 横軸の目盛
	    }
	    
	    for (int i = 0; i < k+1; i++) {
		gc.strokeLine(48, 50 + i * 50, 50, 50 + i * 50); // 縦軸の目盛
		gc.fillText(String.valueOf(i * 50), 20, 50*(k+1) - i * 50); // 縦軸の値
	    }
	    
	    for (int i = 0; i < pa.length; i++) {
		gc.setFill(Color.BLACK);
		gc.fillText(String.valueOf(i + 1), 57 + i * 25, 50*(k+1)+13); // 横軸の値
		
		
	    }
	    
	    for (int i=0; i< pa.length-1; i++){
		gc.setStroke(Color.BLUE);
		gc.strokeLine(57+i*25, 50*(k+1)-pa[i], 57+(i+1)*25, 50*(k+1)-pa[i+1]); //座標を結び折れ線グラフを描画
	    }
	    
	    
	    
	}//if
	else {
	    gc.setFill(Color.BLACK); // 色の指定
	    gc.fillText("扱うデータが大きすぎます!", 330, 350);//描画しきれない大きいデータが含まれていた場合
	}
	}//if2
	else{
	    gc.setFill(Color.BLACK); // 色の指定
	    gc.fillText("データ数が多すぎます!", 330, 350);//要素数が多く描画しきれない場合
	    
	}
	
	
    }
    
}
