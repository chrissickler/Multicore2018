import java.util.concurrent.*;
import java.util.Arrays; 
import java.util.*;
public class Frequency { 
	//static int [] arr;
	
	
    public static int parallelFreq(int x, int[] A, int numThreads) throws Exception {
        // your implementation goes here. 
    	if(numThreads<1)
    	{
    		return -1;
    	}
    	if(A==null)
    	{
    		return -1;
    	}
    	if(A.length==0)
    	{
    		return 0;    		
    	}
    	
    	ExecutorService exserv = Executors.newFixedThreadPool(numThreads);
    	ArrayList<Future<Integer>> list = new ArrayList<Future<Integer>>();
    	CallableThread [] threads = new CallableThread[numThreads];
    	int div = A.length/numThreads;
    	int mod = A.length % numThreads;
    	int ptr = 0;
    	for(int i =0;i<numThreads;i++)
    	{
    		 if(mod!=0)
    		 {
    			 threads[i] = new CallableThread(Arrays.copyOfRange(A,ptr,ptr+div+1),x);
    			 ptr = ptr + div +1;
    			 mod --;
    			 Future<Integer> future = exserv.submit(threads[i]);
        		 list.add(future);
    		 }
    		 else if(div!=0)
    		 {

    			 threads[i] = new CallableThread(Arrays.copyOfRange(A,ptr,ptr+div),x);
    			 ptr = ptr + div;
    			 Future<Integer> future = exserv.submit(threads[i]);
        		 list.add(future);
    		 }
    	}
    	
    	int total = 0;
    	for(Future<Integer> future : list)
    	{
    		total += future.get();
    	}
    	return total;
     } 
    
}
class CallableThread implements Callable{
	int [] array;
	int x;
	public CallableThread(int [] arr, int x)
	{
       array = arr;
       this.x = x;
	}
	public Integer call() throws Exception
	{
		int count = 0;
	    for(int i = 0;i<array.length;i++)
	    {
	    	if(array[i]==x)
	    	{
	    		count++;
	    	}
	    }
		return count;
	}
}
