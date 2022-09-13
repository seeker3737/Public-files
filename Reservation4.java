public class Reservation4{
    int seat[][];
    int[] z;
    public Reservation4(int n, int m){
	seat = new int[n][m];
	for(int i=0;i<n;i++){
	    for(int j=0;j<m;j++){
		seat[i][j]=-1;
	    }
	}
	z=new int[n];
	for(int i=0;i<n;i++){
	    z[i]=0;
	}
    }
    boolean reserve(int id, int num){
	for(int i=0;i<6;i++){
	    if(15-z[i]>=num){
		if(seat[i][z[i]]==-1){
		    for(int j=z[i];j<(z[i]+num);j++){
			seat[i][j]=id;
		    }
		    z[i]=z[i]+num;
		}
		return true;//end  
	    }
	}
	return false;
    }
   
    void printSeat(){
	 for(int i=0;i<seat.length;i++){
	     for(int j=0;j<seat[i].length;j++){
		 System.out.print(seat[i][j]+" ");
	     }
	     System.out.println();
	 }
    }
   
    public static void main(String args[]){
	 int thread_num = 5;      //予約を取りに来る顧客(窓口)数
	 Reservation4 rs = new Reservation4(6,15); //6,15は座席数
	 Passengers ps[] = new Passengers[thread_num];
	 for(int i=0;i<thread_num;i++){
	     ps[i] = new Passengers(i,rs);
	 }
	 for(int i=0;i<thread_num;i++){
	     try{
		 ps[i].join();
	     }catch(InterruptedException e){
	     }
	 }
	 rs.printSeat();
    }
}

class Passengers extends Thread{
    int id;
    Reservation4 rs;
    public Passengers(int n, Reservation4 rs){
	 this.id = n;
	 this.rs = rs;
	 this.start();
    }

    synchronized public void run(){
	 for(int i=0;i<10;i++){ //10回行う
	     int num = (int)(Math.random()*6+1);
	     if(rs.reserve(id, num)){
		 System.out.println("ID:"+id+"  reserved "+num+" seats.");
	     }
	 }
    }
}
