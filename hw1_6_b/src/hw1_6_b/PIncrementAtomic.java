package hw1_6_b;
import java.util.concurrent.atomic.AtomicInteger;

interface Lock{
	public void requestCS(int pid,int role);
	public void releaseCS(int pid);
}

public class PIncrementAtomic 
{
	
	static volatile AtomicInteger ctot = new AtomicInteger();
	static int threads;
	
	public static int parallelIncrement(int c, int numThreads)
	{
		ctot.set(0);
		threads = numThreads;
		MyThread t[] = new MyThread[numThreads];
		int mod = 1200000 % numThreads;
		for(int i =0;i<numThreads;i++)
		{
			if(mod!=0)
			{
				t[i] = new MyThread(1200000/numThreads + 1);
				mod--;
			}
			else
				t[i] = new MyThread(1200000/numThreads);
		}
		
		for(int i=0;i<t.length;i++)
		{
			t[i].run();
		}
		while(ctot.get()!=1200000)
		{}
		return ctot.get();
		
	}
	
	static class MyThread extends Thread
	{
		int times;
		public MyThread(int times)
		{

			this.times = times;
		}
		public void run()
		{
			while(times!=0)
			{
				int expect = ctot.get();
				if(ctot.compareAndSet(expect, expect+1))
					times--;

			}
		}
			
		
	}
	
}





