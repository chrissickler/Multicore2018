package q6.Tournament;
import java.util.Random;

public class PIncrement implements Runnable
{
	
	static volatile int [] flag;
	static volatile int [] turn;
	static volatile TournamentLock [] comp;
	static volatile int ctot = 0;
	static int threads;
	int myId;
	int height;
	int prevComp; // makes left and right easy
	int times;
	
	public PIncrement(int id,int height, int times)
	{
		myId = id;
		flag[id] = 0;
		this.height = height;
		this.times = times;
	}
	
	
	public static int parallelIncrement(int c, int numThreads)
	{
		threads = numThreads;
		flag = new int[numThreads];
		PIncrement t[] = new PIncrement[numThreads];
		int height;
		if(numThreads>4)
		{
			height = 3;
			comp = new TournamentLock[7];
			turn = new int[7];
		}
		else if(numThreads>2)
		{
			height = 2;
			comp = new TournamentLock[3];
			turn = new int[3];
		}
		else
		{
			height = 1;
			comp = new TournamentLock[1];
			turn = new int[1];
		}
		for(int i =0;i<comp.length;i++)
		{
			comp[i] = new TournamentLock(i);
		}
		int mod = 1200000 % numThreads;
		for(int i =0;i<numThreads;i++)
		{
			if(mod!=0)
			{
				t[i] = new PIncrement(i,height,1200000/numThreads + 1);
				mod--;
			}
			else
				t[i] = new PIncrement(i,height,1200000/numThreads);
		}
		
		for(int i=0;i<t.length;i++)
		{
			t[i].run();
		}
		while(ctot!=1200000)
		{}
		for(int i=0;i<t.length;i++)
		{
		}
		return ctot;
		
	}
	
	void nonCriticalSection()
	{
		//Util.mySleep(r.nextInt(1000));
	}
	void CriticalSection()
	{
		ctot++;
		//Util.mySleep(r.nextInt(1000));
	}
	@Override
	public void run()
	{
		while(times!=0)
		{
			while(flag[myId]!=height)
			{
			//lock.requestCS(myId);
			if(flag[myId]==0) //if on the first level of the tree
			{
				prevComp = ((int)Math.pow(2,height-1)) + (myId/2) -1;
				comp[prevComp].lock(myId);
			}
			else
			{
				comp[(prevComp-1)/2].lock(myId);
				prevComp = (prevComp-1)/2;
			}
			flag[myId]++;
			nonCriticalSection();
			}
			CriticalSection();
			times--;
			comp[prevComp].unlock(myId);

		}
	}
	
	static boolean testOpponents(int pid,int compid)//wait for all flags of potential opponents in x are <k;
	{
		
		if(threads==1)
		{
			return false;
		}
		if(threads < 3)
		{
			if(pid==1)
				return flag[0]==1;
			else
				return flag[1]==1;
		}
		if(threads < 5)
		{
			if(compid==0)
			{
			   for(int i=0;i<threads;i++)
			   {				   
				   if(flag[i]>=flag[pid] && i!=pid)
					   return true;
				   
			   }
			   return false;
			}
		}
		else
		{
			if(compid==0)
			{
			   for(int i=0;i<threads;i++)
			   {
				   if(flag[i]>=flag[pid] && i!=pid)
					   return true;
				   
			   }
			   return false;
			}
			else if(compid==1)
			{
				for(int i=0;i<threads || i< 4;i++)
				   {
					   if(flag[i]>=flag[pid] && i!=pid)
						   return true;
					   
				   }
				return false;
			}
			else if(compid==2)
			{
				for(int i=4;i<threads;i++)
				   {
					   if(flag[i]>=flag[pid] && i!=pid)
						   return true;
					   
				   }
				return false;
			}
		}
		return false;
	}

	
}





