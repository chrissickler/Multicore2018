import java.util.concurrent.locks.ReentrantLock;

public class PIncrementReentrant {
	static volatile int num;
	static ReentrantLock re;

	public static int parallelIncrement(int c, int numThreads) {
		MyThread t[] = new MyThread[numThreads];
		num = c;
		int mod = 1200000 % numThreads;
		for(int i =0;i<numThreads;i++) {
			if(mod!=0) {
				t[i] = new MyThread(1200000/numThreads + 1);
				mod--;
			}
			else
				t[i] = new MyThread(1200000/numThreads);
		}
		for(int i=0;i<t.length;i++) {
			t[i].run();
		}


		return num;
	}

	static class MyThread extends Thread {
		int times;
		Object obj = new Object();
		public MyThread(int times) {
			re = new ReentrantLock();
			this.times = times;
		}
		public void run() {
			while(times!=0){
				boolean isLocked = re.tryLock();
				if(isLocked) {
					re.lock();
					num++;
					times--;
					re.unlock();
				}


			}
		}
	}
}