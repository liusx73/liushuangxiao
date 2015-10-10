package thread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ThreadB implements Runnable {

	public static List<Integer> num = new LinkedList<Integer>();
	
	@Override
	public void run() {
		for (int i = 0; i < 1000; i++) {
			plusD();
		}
	}
	
	private synchronized void plusD(){
		num.add(1);
	}
	
	private void plusA(){
		num.add(1);
	}
	
	private static synchronized void plusB(){
		num.add(1);
	}
	
	public void plusC(){
		synchronized(num) {
			num.add(1);
		}
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
			thread = new Thread(new ThreadB());
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
		
		System.out.println(ThreadB.num.size());
		ThreadB.num = new LinkedList<Integer>();
	}
}
