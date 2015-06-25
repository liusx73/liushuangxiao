package sequence;

public class Heapsort {
	public static void heapify(int[] array, int index, int max) {
		int left = 2 * index + 1;
		int right = 2 * index + 2;
		int largest = -1;

		if (left < max && array[left] > array[index]) {
			largest = left;
		} else {
			largest = index;
		}

		if (right < max && array[right] > array[largest]) {
			largest = right;
		}

		if (largest != index) {
			int temp = array[index];
			array[index] = array[largest];
			array[largest] = temp;

			heapify(array, largest, max);
		}
	}

	public static void buildHeap(int[] array, int n) {
		for (int i = n / 2 - 1; i >= 0; i--) {
			heapify(array, i, n);
		}
	}

	public static void sortPointers(int[] array, int n) {
		buildHeap(array, n);
		for (int i = n - 1; i >= 1; i--) {
			int temp = array[0];
			array[0] = array[i];
			array[i] = temp;

			heapify(array, 0, i);
		}
	}

	public static void main(String[] args) {
		int[] array = { 1, 0, 2, 9, 5, 3, 6, 7, 4, 8 };
		printArray(array);
		sortPointers(array, array.length);
		printArray(array);

	}

	static void printArray(int[] array) {
		for (int i : array) {
			System.out.print(i + "  ");
		}
		System.out.println();
	}
}
