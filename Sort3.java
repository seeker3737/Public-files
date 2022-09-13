public class Sort3{
    private int array[];
    public Sort3(int n){
	 int[] array1 = new int[n/2];
	 int[] array2 = new int[n-(n/2)];
	 for(int i=0;i<(n/2);i++){
	     array1[i] = (int)(Math.random()*Integer.MAX_VALUE);
	 }
	 for(int i=0;i<n-(n/2);i++){
	     array2[i] = (int)(Math.random()*Integer.MAX_VALUE);
	 }
	 //ここからソート呼び出し
	 long start = System.currentTimeMillis();
	 BubbleSort bs1 = new BubbleSort(array1);
	 BubbleSort bs2 = new BubbleSort(array2);
	 array1 = bs1.getArray();
	 array2 = bs2.getArray();
	 array = merge(array1,array2);
	 //printArray(array); //配列表示
	 long end = System.currentTimeMillis();
	 System.out.println("sort?: "+sortCheck(array)+
			   ", Processing time: "+(end-start)+"ms");
    }

    /** ソートチェック */
    public static boolean sortCheck(int array[]){
	 for(int i=0;i<array.length-1;i++){
	     if(array[i]>array[i+1])return false;
	 }
	 return true;
    }
    
    /** 確認用の配列表示メソッド */
    public static void printArray(int array[]){
	 for(int i=0;i<array.length;i++){
	     System.out.print(array[i]+" ");
	 }
	 System.out.println();
    }
    
    public static int[] merge(int[]a, int[]b){
	int i=0;
	int j=0;
	int[] c = new int[a.length + b.length];
	while(i<a.length || j<b.length){
	    if(j>=b.length || (i<a.length && a[i]<b[j])){
		c[i+j]=a[i];
		i++;
	    }
	    else{
		c[i+j]=b[j];
		j++;
	    }
	}
	return c;
    }

    public static void main(String args[]){
	new Sort3(100000);
    }
}


/**
  バブルソート(逐次)
*/
class BubbleSort{
    private int array[];
    BubbleSort(int[] n){
	 array = n;
	 sort();
    }

    /** ソート コンストラクタから自動で実行される */
    private void sort(){
	 for(int i=array.length-1;i>0;i--){
	     for(int j=0;j<i;j++){
		 if(array[j]>array[j+1]){
		     int tmp = array[j];
		     array[j] = array[j+1];
		     array[j+1] = tmp;
		 }
	     }
	 }
    }

    /** ソート結果を得るメソッド */
    public int[] getArray(){
	 return array;
    }
}
