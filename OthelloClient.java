import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;


class OthelloClient extends JFrame{
    final static int BLACK = 1;
    final static int WHITE = -1;

    public static JTextField tf;
    private JTextArea ta;
    private JLabel label;
    private OthelloCanvas canvas;
    public static int[][] board=
    {
	{0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0},
	{0,0,0,1,-1,0,0,0},
	{0,0,0,-1,1,0,0,0},
	{0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0}
    };
    
    String mess;
    public static int myturn;
    public static int turn;
    Socket socket;
    public static PrintWriter pw;
    BufferedReader br;


    public OthelloClient() {	
	try{
	    socket = new Socket("localhost",53421);
	    pw = new PrintWriter(socket.getOutputStream());
	    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    
	    
	    this.setSize(640,320);
	    this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e)  {
			sendMessage("CLOSE");
			/* ウインドウが閉じられた時の処理 */
			System.exit(0);
		    }
		});	 
	    tf = new JTextField(40);
	    tf.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
			/* テキストフィールドに文字が入力された時の処理 */
			if(tf.getText().equals("quit")){
			    System.exit(0);
			}
			
			//ここに送信部分追加
			if(tf.getText().split(" ")[0].equals("NICK")){
			    String str =tf.getText().split(" ")[1];
			    sendMessage("NICK"+" "+str);
			}
			
			
			if(tf.getText().split(" ")[0].equals("PUT")){
			   String str1 =tf.getText().split(" ")[1];
			   String str2 =tf.getText().split(" ")[2];
			    sendMessage("PUT"+" "+str1+" "+str2);
			}
			
			if(tf.getText().split(" ")[0].equals("SAY")){
			    String str = tf.getText().split(" ")[1];
			    sendMessage("SAY"+" "+str);
			}	
			
			System.out.println(tf.getText());
			tf.setText(""); //テキストフィールドの文字を初期化
		    }
		}
		);
	    ta = new JTextArea(18,40);
	    ta.setLineWrap(true);
	    ta.setEditable(false);
	    label = new JLabel();
	    
	    JPanel mainp = (JPanel)getContentPane();
	    JPanel ep = new JPanel();
	    JPanel wp = new JPanel();
	    canvas = new OthelloCanvas();
	    GridLayout gl = new GridLayout(1,2);
	    gl.setHgap(5);
	    mainp.setLayout(gl);
	    ep.setLayout(new BorderLayout());
	    ep.add(new JScrollPane(ta),BorderLayout.CENTER);
	    ep.add(tf,BorderLayout.SOUTH);
	    wp.setLayout(new BorderLayout());
	    wp.add(label,BorderLayout.SOUTH);
	    wp.add(canvas,BorderLayout.CENTER);
	    mainp.add(wp);
	    mainp.add(ep);
	    this.setVisible(true);
	    
	    //受信部分追加
	    while(true){
		mess=br.readLine();
		System.out.println(mess);
		if(mess.split(" ")[0].equals("START")){
		    myturn=Integer.parseInt(mess.split(" ")[1]);
		    if(myturn == 1){
			ta.append("You are black."+"\n");
		    }else{
			ta.append("You are white."+"\n");
		    }
		}
		
		if(mess.split(" ")[0].equals("BOARD")){
		    int k =1;
		    for(int j=0;j<8;j++){
			for(int i=0;i<8;i++){
			    board[j][i]=Integer.parseInt(mess.split(" ")[k]);
			    canvas.repaint();
			    k++;
			}
		    }	
		    
		}

		if(mess.split(" ")[0].equals("TURN")){
		    turn=Integer.parseInt(mess.split(" ")[1]);
		    if(turn==myturn){
			ta.append("Your turn." + "\n");	
			
			int i;
			int j;									
			for(j=0;j<8;j++){
			    for(i=0;i<8;i++){
				if(canPutDown(i,j)==true){
				    sendMessage("PUT"+" "+ String.valueOf(i) +" "+String.valueOf(j));
				}
			    }
			}
			
			
					
		    }else{
			ta.append("Opponent's Turn." + "\n");
		    }		    
		}

		
		if(mess.split(" ")[0].equals("SAY")){
		    ta.append(mess.split(" ")[2] + "\n");   
		}

		if(mess.split(" ")[0].equals("CLOSE")){
		    ta.append("connection failed." + "\n");   
		}

		if(mess.split(" ")[0].equals("ERROR")){
		    if(mess.split(" ")[1].equals("1")){
			ta.append("書式が間違っているエラー" + "\n");   
		    }
		    if(mess.split(" ")[1].equals("2")){
			ta.append("PUT命令で指定した盤目に自身の色の石が置けないエラー" + "\n");   
		    }
		    if(mess.split(" ")[1].equals("3")){
			ta.append("相手のターンの時にPUT命令が送られた場合のエラー" + "\n");   
		    }
		    if(mess.split(" ")[1].equals("4")){
			ta.append("処理できない命令が送られた場合のエラー" + "\n");   
		    }   
		}
		
		if(mess.split(" ")[0].equals("END")){
		    ta.append( mess + "\n");   
		}		
	    }

	    
	    
	    
	}catch(IOException e){
	    e.printStackTrace();	
	}
	
    }
    public static void sendMessage(String str){
	pw.print(str+"\r\n");
	pw.flush();
    }    
    

      
    public boolean canPutDown(int x, int y) {
	if(x>=8 || y >=8)
	    return false;
	
        if (OthelloClient.board[x][y] != 0)
            return false;
        //オーバーロード
        if (canPutDown(x, y, 1, 0))
            return true; // 下
        if (canPutDown(x, y, 0, 1))
            return true; // 右
        if (canPutDown(x, y, -1, 0))
            return true; // 上
        if (canPutDown(x, y, 0, -1))
            return true; // 左
        if (canPutDown(x, y, 1, 1))
            return true; // 右下
        if (canPutDown(x, y, -1, -1))
            return true; // 左上
        if (canPutDown(x, y, 1, -1))
            return true; // 左下
        if (canPutDown(x, y, -1, 1))
            return true; // 右上
	
        return false;
    }
    
    
    public boolean canPutDown(int x, int y, int vecX, int vecY) {
        int putStone;
	
        if (OthelloClient.myturn == -1) {
            putStone = -1;
        } else {
            putStone = 1;
        }
	
        
        x += vecX;
        y += vecY;
	
        if (x < 0 || x >= 8 || y < 0 || y >= 8)
            return false;
        
        if (OthelloClient.board[x][y] == putStone)
            return false;
        
        if (OthelloClient.board[x][y] == 0)
            return false;
	        
        x += vecX;
        y += vecY;
       
        while (x >= 0 && x < 8 && y >= 0 && y < 8) {
            
            if (OthelloClient.board[x][y] == 0)
                return false;
           
            if (OthelloClient.board[x][y] == putStone)
                return true;
            x += vecX;
            y += vecY;
        }
       
        return false;
    }
    
    
    
    
    
    
    public static void main(String args[]) {
	new OthelloClient();
    }
}

class OthelloCanvas extends JPanel {
    private final static int startx = 20;
    private final static int starty = 10;
    private final static int gap = 30;
    /*    private byte[][] board ={
	{0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0},
	{0,0,0,1,-1,0,0,0},
	{0,0,0,-1,1,0,0,0},
	{0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0}
    };  //サンプルデータ
    */


    public OthelloCanvas(){
	this.addMouseListener(new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
		    /* 盤目上でマウスがクリックされた時の処理 */
		    Point p = e.getPoint();
		    System.out.println(""+p); //デバッグ用表示
		    
		    //ここに送信部分追加
		    
		    


		}
	    });
    }

    public void paintComponent(Graphics g){
	g.setColor(new Color(0,180,0));
	g.fillRect(startx,starty,gap*8,gap*8);

	g.setColor(Color.BLACK);
	for(int i=0;i<9;i++){
	    g.drawLine(startx,starty+i*gap,startx+8*gap,starty+i*gap);
	    g.drawLine(startx+i*gap,starty,startx+i*gap,starty+8*gap);
	}
	for(int i=0;i<8;i++){
	    for(int j=0;j<8;j++){
		if(OthelloClient.board[i][j]==OthelloClient.BLACK){
		    g.setColor(Color.BLACK);
		    g.fillOval(startx+gap*i,starty+gap*j,gap,gap);
		}else if(OthelloClient.board[i][j]==OthelloClient.WHITE){
		    g.setColor(Color.WHITE);
		    g.fillOval(startx+gap*i,starty+gap*j,gap,gap);
		}
	    }
	}
    }
}
