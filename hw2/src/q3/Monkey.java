package q3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monkey {

	static ReentrantLock Rope = new ReentrantLock();
	static Condition right = Rope.newCondition();
	static Condition left = Rope.newCondition();
	static Condition kong  = Rope.newCondition();
	
    static volatile int count  = 0;
    static volatile int ropeDirection;
    
    int direction;
    public Monkey() {
    	
    }

    public void ClimbRope(int direction) throws InterruptedException {
    	this.direction = direction;
    	
    	Rope.lock();
    	try
    	{
    		if(direction == -1)
    		{
    			while(count!=0)
    			{
    				kong.await();
    			}
    		} else if (direction == 0) {
    			
    		    while (count >= 3 || (ropeDirection != direction && count!=0))
    			{
    				left.await();
    				
    			}
    		} else {
    			while (count >= 3 || (ropeDirection != direction && count!=0))
    			{
    				right.await();
    			}
    			
    		}
    		ropeDirection = direction;
    		count++;
    	}
    	finally
    	{
    		Rope.unlock();
    	}

    }

    public void LeaveRope() {
    	
       Rope.lock();
       try
       {
       count--;
       if(Rope.hasWaiters(kong))
       	{
    	   if(count==0)
    		   kong.signal();
       	}
       else
       	{
    	   
    	   if(direction == 0){ //left
    		   if (count == 0 && Rope.hasWaiters(right)) {
    			 right.signal();
    			   
    		   } else {
    			   left.signal();
    		   }
    	   } else if (direction == 1 ){
    		   if (count == 0 && Rope.hasWaiters(left)) {
    			   left.signal();
    		   } else {
    			   right.signal();
    		   }
    	   } else {
    		   
    		   if(Rope.hasWaiters(left))
    			   left.signal();
    		   else
    			   right.signal();
    		   
    	   }
       		
       	}
       }
       finally
       {
    	   Rope.unlock();
       }
    }

    /**
     * Returns the number of monkeys on the rope currently for test purpose.
     *
     * @return the number of monkeys on the rope
     *
     * Positive Test Cases:
     * case 1: when normal monkey (0 and 1) is on the rope, this value should <= 3, >= 0
     * case 2: when Kong is on the rope, this value should be 1
     */
    public int getNumMonkeysOnRope() {
        return count;
    }

}