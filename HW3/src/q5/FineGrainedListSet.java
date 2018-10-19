package q5;

import java.util.concurrent.locks.ReentrantLock;

import q5.CoarseGrainedListSet.Node;

public class FineGrainedListSet implements ListSet {
// you are free to add members
	
  Node head;
  Node tail;
	
	
  public FineGrainedListSet() {
    // implement your constructor here	
	  head  = new Node(-1);
	  tail  = head;
  }
	  
  public boolean add(int value) {
    // implement your add method here
	Node node = new Node(value);
    Node prev = null;
    Node curr = head;
    while(true)
    {
    	if(this.contains(value))
    		return false;
    	while(curr.next!=null && curr.next.value<=value)
    	{
    		prev = curr;
    		curr = curr.next;
    	}
    	if(prev!=null)
    		prev.lock.lock();
    	curr.lock.lock();
    	try{
    		
    	
    	if(curr.value==value || (curr.next!=null && curr.next.value==value))
    	    return false;
    	if(curr.value>value)
    		continue;
    	else
		{
		    Node save = curr.next;
			curr.next = node;
			node.next = save;
			return true;
		}
    	}
    	finally
    	{
    		if(prev!=null)
    			prev.lock.unlock();
    		curr.lock.unlock();
    	}
    }
    	
	
  }
	  
  public boolean remove(int value) {
    // implement your remove method here	
	  Node prev = null;
	  Node curr = head;
	    while(true)
	    {
	    	if(!this.contains(value))
	    		return false; 
	    	while(curr.next!=null && curr.next.value<=value)
	    	{
	    		prev = curr;
	    		curr = curr.next;
	    	}
	    	if(prev!=null)
	    		prev.lock.lock();
	    	curr.lock.lock();
	    	try{
	    		
	    	
	    	if(curr.value==value)
	    	{
	    		return false;
	    	}
	    	else if(curr.value>value)
	    		continue;
	    	else
			{
	    		if(prev!=null)
				{
					prev.next = curr.next;
				}
				else
				{
					head = curr.next;
				}
	    		return true;
			}
	    	}
	    	finally
	    	{
	    		if(prev!=null)
	    			prev.lock.unlock();
	    		curr.lock.unlock();
	    	}
	    }
  }
	  
  public boolean contains(int value) {
    // implement your contains method here
	  if(head==null)
	  {
			return false;
	  }
	  else
	  {
		Node current = head;
		//boolean found = false;
		while(current!= null && current.next!=null && current.value<=value)
		{
			current = current.next;
		}
		if(current!=null && current.value==value)
		{
				return true;
		}
		return false;
	  }		
  }
	  
  protected class Node {
    public Integer value;
    public Node next;
    public boolean isDeleted = false;
    ReentrantLock lock = new ReentrantLock();
			    
  	public Node(Integer x) {
  		value = x;
  		next = null;
  	}
  }

  /*
  return the string of list, if: 1 -> 2 -> 3, then return "1,2,3,"
  check simpleTest for more info
  */
  public String toString() {
	  String ret  =  "";
	    Node curr = head.next;
	    while(curr!=null)
	    {
	    	ret += curr.value +",";
	    	curr = curr.next;
	    }
	    return ret;
  }
}