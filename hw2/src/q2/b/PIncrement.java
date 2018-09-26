package q2.b;

public class PIncrement {

	static volatile int [] flag; //0:down 1:up
	static volatile int X = -1;
	static volatile int Y = -1;
	
	static int count = 0;
	static myThread [] arr;
	
    public static int parallelIncrement(int c, int numThreads) {
    	
    	flag = new int[numThreads];
    	arr = new myThread[numThreads];
    	for(int i=0;i<numThreads;i++)
    	{
    		arr[i] = new myThread(i,120000/numThreads);
    	}
        
        
        while(count!=120000)
        {
        	for(int i=0;i<arr.length;i++)
        	{
        		arr[i].acquire();
        	}
        }
        return count;
    }
    
    
    static class myThread extends Thread
    {
    	int id;
    	int times;
    	public myThread(int id, int times)
    	{
    		this.id = id;
    		this.times = times;
    	}
    	
    	public void acquire()
    	{
    		while(true)
    		{
    			flag[id] = 1;
    			X = id;
    			if(Y!=-1)
    			{
    				flag[id] = 0;
    				while(Y!=-1){}
    				continue;
    			}
    			else
    			{
    				Y = id;
    				if(X==id)
    				{
    					enterCS();
    					return;
    				}
    				else
    				{
    					flag[id] = 0;
    					for(int j=0;j<arr.length;j++)
    					{
    						while(flag[j]==1){}
    					}
    					if(Y==id)
    					{
    						enterCS();
    						return;
    					}
    					else
    					{
    						while(Y!=-1)
    						{}
    						continue;
    					}
    				}
    			}
    		}
    		
    	}
    	public void enterCS()
    	{
    		if(times!=0)
    		{
    			count++;
    			times--;
    		}
    		System.out.printf("%d",count);
    		release();
    	}
    	
    	public void release()
    	{
    		Y = -1;
    		flag[id] = 0;
    	}
    }
    

}

