package hw1_6_a;
import java.util.Random;

interface Lock{
	public void requestCS(int pid,int role);
	public void releaseCS(int pid);
}

public class PIncrement 
{
	
	static volatile int [] flag;
	static volatile int [] turn;
	static volatile PetersonAlgorithm [] comp;
	static volatile int ctot = 0;
	static int threads;
	
	public static int parallelIncrement(int c, int numThreads)
	{
		threads = numThreads;
		flag = new int[numThreads];
		MyThread t[] = new MyThread[numThreads];
		int height;
		if(numThreads>4)
		{
			height = 3;
			comp = new PetersonAlgorithm[7];
			turn = new int[7];
		}
		else if(numThreads>2)
		{
			height = 2;
			comp = new PetersonAlgorithm[3];
			turn = new int[3];
		}
		else
		{
			height = 1;
			comp = new PetersonAlgorithm[1];
			turn = new int[1];
		}
		for(int i =0;i<comp.length;i++)
		{
			comp[i] = new PetersonAlgorithm(i);
		}
		int mod = 1200000 % numThreads;
		for(int i =0;i<numThreads;i++)
		{
			if(mod!=0)
			{
				t[i] = new MyThread(i,height,1200000/numThreads + 1);
				mod--;
			}
			else
				t[i] = new MyThread(i,height,1200000/numThreads);
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
	
	static class MyThread extends Thread
	{
		
		int myId;
		int height;
		int prevComp; // makes left and right easy
		int times;
		
		public MyThread(int id,int height, int times)
		{
			myId = id;
			flag[id] = 0;
			this.height = height;
			this.times = times;
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
					comp[prevComp].requestCS(myId,myId%2);
				}
				else
				{
					comp[(prevComp-1)/2].requestCS(myId,prevComp%2);
					prevComp = (prevComp-1)/2;
				}
				flag[myId]++;
				nonCriticalSection();
				}
				CriticalSection();
				times--;
				comp[prevComp].releaseCS(myId);

			}
		}
			
		
	}
	static class PetersonAlgorithm implements Lock {
		int id;
		public PetersonAlgorithm(int id)
		{
			this.id = id;
		}
		public void requestCS(int pid, int role){
            turn[id] = role;
            while(turn[id]==role && testOpponents(pid,id))
            {}             
            
		}
		public void releaseCS(int id)
		{
			flag[id]=0;

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





