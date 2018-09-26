package q2.c;

import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement {
	
	static int count = 0;
	static myThread [] arr;
	static volatile boolean[] Available;
	
	static AtomicInteger tailSlot = new AtomicInteger(0);
	

    public static int parallelIncrement(int c, int numThreads){
    	//tailSlot.set(0);
    	arr = new myThread[numThreads];
    	Available = new boolean[numThreads];
    	Available[0] = true;
    	for(int i=0;i<numThreads;i++)
    	{
    		arr[i] = new myThread(i,120000/numThreads);
    	}
        for(int i=0;i<arr.length;i++)
        {
        	arr[i].increment();
        }
        while(count!=120000) {}
        return count;
    }
    
    static class myThread extends Thread
    {
    	int times;
    	int mySlot;
    	int id;
    	public myThread(int id, int times)
    	{
    		this.id = id;
    		this.times = times;
    	}
    	public void increment()
    	{
    		while(times!=0)
    		{
    			lock();
    			CS();
    			unlock();
    		}
    	}
    	public void lock()
    	{
    		mySlot = (tailSlot.getAndIncrement() % Available.length);
    		while(!Available[mySlot]) {}
    	}
    	public void CS()
    	{
    		count++;
    		times--;
    	}
    	public void unlock()
    	{
    		Available[mySlot] = false;
    		Available[(mySlot+1)%Available.length] = true;
    	}
    	
    }

}