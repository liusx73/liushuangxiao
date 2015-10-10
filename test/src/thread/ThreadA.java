package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadA implements Runnable {
	public static int num = 0;
	public final static LinkedBlockingQueue<Integer> nums = new LinkedBlockingQueue<Integer>();
	private static int last = 0;
	private static int max = 0;
	
	@Override
	public void run() {
		do {
			m();
		} while (! nums.isEmpty());
	}
	
	private void m(){
		for (int i = 0; i < 10; i++) {
			addToQueue();
		}
		takeInQueue();
	}
	
	private boolean addToQueue(){
		boolean success = false;
		synchronized (nums) {
			if(num < 100000){
				num ++;
				success = nums.add(num);
			}
		}
		
		return success;
	}
	
	private Integer takeInQueue(){
		Integer c = null;
		if(nums.size() > 0){
			synchronized (nums) {
				if(nums.size() > 0){
					c = nums.poll();
//					if((c - last) != 1){
//						System.out.println("ha");
//					}
					
					if(max < c){
						max = c;
					}
				}
			}
		}
		
		if(c == null){
			
		}else{
			last = c;
		}
		
		return c;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			doing();
		}
	}
	
	private static void doing(){
		List<Thread> threadList = new ArrayList<Thread>();
		Thread thread = null;
		for (int i = 0; i < 10; i++) {
			thread = new Thread(new ThreadA());
			thread.start();
			threadList.add(thread);
		}
		
		
		boolean isdone = true;
		do{
			isdone = true;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Thread t : threadList) {
				isdone = isdone && !t.isAlive();
			}
		}while(!isdone);
		System.out.println("result : num=" + ThreadA.num + ", size=" + ThreadA.nums.size() + ", max=" + ThreadA.max);
		ThreadA.num = 0;
	}
}
