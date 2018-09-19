package q6.AtomicInteger;
import java.util.concurrent.atomic.AtomicInteger;

interface Lock{
	public void requestCS(int pid,int role);
	public void releaseCS(int pid);
}

public class PIncrement implements Runnable
{
	
	static volatile AtomicInteger ctot = new AtomicInteger();
	int times;
	
	public PIncrement(int times) {
		this.times=times;
}
	
	public static int parallelIncrement(int c, int numThreads)
	{
		ctot.set(0);
		PIncrement t[] = new PIncrement[numThreads];
		int mod = 1200000 % numThreads;
		for(int i =0;i<numThreads;i++)
		{
			if(mod!=0)
			{
				t[i] = new PIncrement(1200000/numThreads + 1);
				mod--;
			}
			else
				t[i] = new PIncrement(1200000/numThreads);
		}
		
		for(int i=0;i<t.length;i++)
		{
			t[i].run();
		}
		while(ctot.get()!=1200000)
		{}
		return ctot.get();
		
	}

	@Override
	public void run() {
		while(times!=0)
		{
			int expect = ctot.get();
			if(ctot.compareAndSet(expect, expect+1))
				times--;			
		}
	}
	
}





