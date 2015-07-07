package sequence;


public class InsertionSort {
	public static void sortPointers(Integer[] ar, int n) {
		for (int j = 0; j < n; j++) {
			int i = j - 1;
			Integer value = ar[j];
			while (i >= 0 && ar[i] > value) {
				ar[i + 1] = ar[i];
				i--;
			}

			ar[i + 1] = value;
		}
	}

	public static void main(String[] args) {
		Integer[] a = { 100, 93, 97, 92, 96, 99, 92, 89, 93, 97, 90, 94, 92, 95 };
		int[] b = { 100, 93, 97, 92, 96, 99, 92, 89, 93, 97, 90, 94, 92, 95 };
		int length = a.length;
		// 89 90 92 92 92 93 93 94 95 96 97 97 99 100
		sortPointers(a, length);
		sortValues(b, length);
		printArray(a);
		printArray(b);

	}

	static void printArray(Integer[] array) {
		for (int i : array) {
			System.out.print(i + "  ");
		}
		System.out.println();
	}
	
	static void printArray(int[] array) {
		for (int i : array) {
			System.out.print(i + "  ");
		}
		System.out.println();
	}

	static void sortValues(int[] ar, int n) {
		int saved ;
		for (int j = 1; j < n; j++) {
			int i = j - 1;
			int value = ar[j];
			while (i >= 0 && ar[i] > value) {
				i--;
			}
			if(++i == j){
				continue;
			}
			saved = value;
			System.arraycopy(ar, i, ar, i+1, j-i);
			ar[i] = saved;
		}
	}
}
