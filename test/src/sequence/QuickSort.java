package sequence;

import java.util.Random;

public class QuickSort {
	
	private static int MIN_POINT = 5;
	private static Random random = new Random(1);
	
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
	
	public static void quickSort(int[] ar , int start, int end){
		int numOfParam = end - start + 1;
//		System.out.println(start + "-" + end);
		if(numOfParam > MIN_POINT){
			int randomNum = Math.abs(random.nextInt());
			int splitPoint = start + randomNum%numOfParam;
			splitPoint = partition(ar, start, end, splitPoint);
			quickSort(ar, start, splitPoint);
			quickSort(ar, splitPoint + 1, end);
		}else{
			System.out.println(start + "-" + end + "-" + numOfParam);
			insertionSort(ar, start, end);
		}
		
	}
	
	public static void main(String[] args) {
		int[] ar = { 1, 0, 2, 9, 5, 3, 6, 7, 4, 8 };
//		int[] ar = { 0, 1, 2, 3, 5, 4, 6, 7, 8, 9 };
		
		printArray(ar);
		quickSort(ar, 0, 9);
//		insertionSort(ar, 4, 6);
//		partition(ar, 4, 9, 6);
		System.out.println("--------------");
		printArray(ar);
		
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
	
	private static void insertionSort(int[] ar, int start, int end) {
		for (int j = start; j <= end; j++) {
			int i = j - 1;
			Integer value = ar[j];
			while (i >= 0 && ar[i] > value) {
				ar[i + 1] = ar[i];
				i--;
			}

			ar[i + 1] = value;
		}
	}
}