package sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BucketSort {

	static void extract(List<Bucket<Double>> buckets, double[] array, int n){
		int low;
		int index = 0;
		int size;
		Bucket<Double> bucket = null;
		for (int i = 0; i < num; i++) {
			Entry<Double> ptr;
			Entry<Double> temp;
			bucket = buckets.get(i);
			if(bucket == null){
				continue;
			}
			size = bucket.getSize();
			if(size == 0){
				continue;
			}
			
			ptr = buckets.get(i).getHead();
			
			if(size == 1){
				array[index++] = ptr.getElement().intValue();
				ptr = null;
				buckets.get(i).setSize(0);
				continue;
			}
			
			low = index;
			array[index ++] = ptr.getElement();
			temp = ptr;
			ptr = ptr.getNext();
			temp = null;
			while(ptr != null){
				int k = index -1;
				while(k >= low && array[k] > ptr.getElement()){
					array[k+1] = array[k];
					k --;
				}
				array[i+1] = ptr.getElement();
				temp = ptr;
				ptr = ptr.getNext();
				temp = null;
				index ++;
			}
			buckets.get(i).setSize(0);
		}
	}
	
	/*
	 * 桶，以及桶的数量
	 */
	static List<Bucket<Double>> buckets;
	static int num = 0;
	
	private static int numBuckets(int numElements){
		return numElements;
	}
	
	private static int hash(double d){
		int bucket = (int) (num*d);
		return bucket;
	}
	
	public static void main(String[] args) {
		double[] array = new double[100];
		
		Random r = new Random(1);
		for (int i = 0; i < 100; i++) {
			array[i] = r.nextDouble()%1;
		}
		
		printArray(array);
		
		sortPointers(array, 100);
		System.out.println("--------------");
		printArray(array);
		
	}

	private static void initBuckets(){
		Bucket<Double> d = null;
		for (int i = 0; i < num; i++) {
			buckets.add(d);
		}
	}
	
	public static void sortPointers(double[] array, int n) {
		num = numBuckets(n);
		buckets = new ArrayList<Bucket<Double>>(num);
		initBuckets();
		for (int i = 0; i < n; i++) {
			int k = hash(array[i]);
			Entry<Double> e = new Entry<Double>();
			e.setElement(array[i]);
			
			Bucket<Double> bucket = buckets.get(k);
			if(bucket == null){
				bucket = new Bucket<Double>();
				bucket.setHead(e);
				buckets.set(k, bucket);
			}else if(bucket.getHead() == null){
				bucket.setHead(e);
			}else{
				e.setNext(bucket.getHead());
				bucket.setHead(e);
			}
			bucket.setSizePlusOne();
		}
		
		extract(buckets, array, n);
	}
	
	static void printArray(int[] array) {
		for (int i : array) {
			System.out.println(i);
		}
	}
	
	static void printArray(double[] array) {
		for (double i : array) {
			System.out.println(i);
		}
	}
}

class Entry<T>{
	private T element;
	private Entry<T> next;
	public T getElement() {
		return element;
	}
	public void setElement(T element) {
		this.element = element;
	}
	public Entry<T> getNext() {
		return next;
	}
	public void setNext(Entry<T> next) {
		this.next = next;
	}
}

class Bucket<T>{
	private int size;
	private Entry<T> head;
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Entry<T> getHead() {
		return head;
	}
	public void setHead(Entry<T> head) {
		this.head = head;
	}
	
	public void setSizePlusOne(){
		this.size ++;
	}
}