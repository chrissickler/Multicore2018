package q6.Synchronized;

public class PIncrement implements Runnable{

	static volatile int num;
	int times;

	public PIncrement(int times) {
		this.times=times;
	}

	public static int parallelIncrement(int c, int numThreads) {
		PIncrement t[] = new PIncrement[numThreads];
		num = c;
		int mod = 1200000 % numThreads;
		for(int i =0;i<numThreads;i++) {
			if(mod!=0) {
				t[i] = new PIncrement(1200000/numThreads + 1);
				mod--;
			}
			else
				t[i] = new PIncrement(1200000/numThreads);
		}
		for(int i=0;i<t.length;i++) {
			t[i].run();
		}


		return num;
	}

	@Override
	public void run() {
		while(times!=0) {
			synchronized(this) {
				num++;
			}
			this.times--;
		}
	}

//	static class MyThread extends Thread {
//		int times;
//		Object obj = new Object();
//		public MyThread(int times) {
//
//			this.times = times;
//		}
//		public void run() {
//			while(times!=0){
//				synchronized(this) {
//					num++;
//				}
//				times--;
//
//			}
//		}
//
//
//	}



}





