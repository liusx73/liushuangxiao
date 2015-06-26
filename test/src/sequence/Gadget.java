package sequence;


public class Gadget {
	public static int cmp(int a,int b){
		return Integer.compare(a, b);
	}
	
	public static int cmp(double a,double b){
		return Double.compare(a,b);
	}
	
	public static int cmp(String s1,String s2){
		return s1.compareTo(s2);
	}
	
	public static void swap(double[] ar,int index1, int index2){
		double temp = ar[index1];
		ar[index1] = ar[index2];
		ar[index2] = temp;
	}
	
	public static void swap(String[] ar,int index1, int index2){
		String temp = ar[index1];
		ar[index1] = ar[index2];
		ar[index2] = temp;
	}
	
	public static void swap(int[] ar,int index1, int index2){
		int temp = ar[index1];
		ar[index1] = ar[index2];
		ar[index2] = temp;
	}
}