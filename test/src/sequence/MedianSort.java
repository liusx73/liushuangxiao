package sequence;

import java.util.Random;

public class MedianSort {

	private static int partition(int[] ar, int left, int right,int pivotIndex){
		int idx;
		int store;
		int pivot = ar[pivotIndex];
		Gadget.swap(ar, right, pivotIndex);
		store = left;
		for (idx = left; idx < right; idx++) {
			if(Gadget.cmp(ar[idx], pivot) <= 0){
				Gadget.swap(ar, idx, store);
				store ++ ;
			}
		}
		Gadget.swap(ar, right, store);
		return store;
	}
	
	public static void main(String[] args) {
		int[] array = { 1, 0, 2, 9, 5, 3, 6, 7, 4, 8 };
		
		printArray(array);
		partition(array, 0, 9, 4);
		System.out.println("--------------");
		printArray(array);
		
	}

	static void printArray(int[] array) {
		for (int i : array) {
			System.out.print(i);
		}
		System.out.println();
	}
	
	static void printArray(double[] array) {
		for (double i : array) {
			System.out.print(i);
		}
		System.out.println();
	}
}