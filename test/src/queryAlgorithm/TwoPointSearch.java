package queryAlgorithm;

import sequence.Gadget;

public class TwoPointSearch {
	private static boolean search(int[] ar, int target){
		boolean isExist = false;
		int low = 0;
		int higth = ar.length -1;
		int middle;
		int cmp;
		while(!isExist && low <= higth){
			middle = (low + higth) / 2;
			cmp = Gadget.cmp(target, ar[middle]);
			if(cmp > 0){
				low = middle;
			}else if(cmp < 0){
				higth = middle;
			}else{
				System.out.println(middle);
				isExist = true;
			}
		}
		
		return isExist;
	}
	
	public static void main(String[] args) {
		int[] ar = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int target = 5;
		System.out.println(search(ar,target));
	}
	
}
