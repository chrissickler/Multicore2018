package q5;

import java.util.concurrent.locks.*;

public class CoarseGrainedListSet implements ListSet {
// you are free to add members
	
  ReentrantLock lock = new ReentrantLock();
  Node head = null;
  public CoarseGrainedListSet() {
	// implement your constructor here	  
  }
  
  public boolean add(int value) {
	// implement your add method here
	lock.lock();
	try
	{
		if(head==null)
		{
			head = new Node(value);
			return true;
		}
		else if(head!=null && head.value>=value)
		{
			if(head.value==value)
				return false;
			Node save = head;
			head = new Node(value);
			head.next = save;
			return true;
		}
		else
		{
			Node prev = null;
			Node current = head;
			while(current.next!=null && current.next.value<=value )
			{
				prev = current;
				current = current.next;
				
			}
			if(current.value == value)
				return false;
			else
			{

			    Node save = current.next;
				current.next = new Node(value);
				current.next.next = save;
				return true;
			}
		}
	}
	finally
	{
		lock.unlock();
	}
  }
  
  public boolean remove(int value) {
	// implement your remove method here	
	lock.lock();
    try
    {
    	if(head==null)
		{
			return false;
		}
		else
		{
			Node prev = null;
			Node current = head;
			//boolean found = false;
			while(current.next!=null && current.value<=value)
			{
				prev = current;
				current = current.next;
			}
			if(current.value==value)
			{
				if(prev!=null)
				{
					prev.next = current.next;
				}
				else
				{
					head = current.next;
				}
				return true;
			}
			return false;
		}		
	}
	finally
	{
		lock.unlock();
	}
  }
  
  public boolean contains(int value) {
	// implement your contains method here	
    lock.lock();
	try
	{
		if(head==null)
		{
			return false;
		}
		else
		{
			Node current = head;
			//boolean found = false;
			while(current.next!=null && current.value<=value)
			{
				current = current.next;
			}
			if(current.value==value)
			{
				return true;
			}
			return false;
		}		
	}
	finally
	{
		lock.unlock();
	}
  }
  
  protected class Node {
	  public Integer value;
	  public Node next;
		    
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
    Node curr = head;
    while(curr!=null)
    {
    	ret += curr.value +",";
    	curr = curr.next;
    }
    return ret;
  }
}