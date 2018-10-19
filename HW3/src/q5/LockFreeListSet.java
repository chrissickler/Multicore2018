package q5;

import q5.FineGrainedListSet.Node;
import java.util.concurrent.atomic.*;

public class LockFreeListSet implements ListSet {
// you are free to add members
	
  AtomicMarkableReference<Node> head = new AtomicMarkableReference<Node>(null, false);
	  
  public LockFreeListSet() {
    // implement your constructor here
	  Node a  = new Node(-2);
	  a.next = new AtomicMarkableReference<Node>(new Node(-1),false);
	  head.set(a, false);
	  
  }
	  
  public boolean add(int value) {
	// implement your add method here
      Node node = new Node(value);
      
      while(true)
      {
    	  AtomicMarkableReference<Node> prev = head;
          AtomicMarkableReference<Node> curr = head.getReference().next;
          if(this.contains(value))
        	  return false;
    	  while(curr.getReference().next!=null && curr.getReference().value<=value)
      	  {
      		prev = curr;
      	    curr = curr.getReference().next;
      	    System.out.print(".");
      	  }
    	  //check if prev is deleted
    	  node.next = curr;
    	  if(curr.getReference()==null)
    	  {
    		  if(prev.getReference().next.compareAndSet(null,node,false,false))
    		    {System.out.println(node.value);

                return true;
    		    }
    	  }
    	  else if(prev.getReference().next.compareAndSet(curr.getReference(), node, false, false))
    	  {
    		  System.out.println(curr.getReference().value);
            return true;
            
    	  }
      }
  }
	  
  public boolean remove(int value) {
    // implement your remove method here	
	  return false;
  }
	/*  Node node = new Node(value);
      Node prev = null;
      Node curr = head.get();
      int i = 0;
      while(true)
      {
    	while(curr.next!=null && curr.next.value<=value)
      	{
    		i++;
      		prev = curr;
      		curr = curr.next;
      	}
    	if(curr.value==node.value)
    	{
    		AtomicReference<Node> toCheck = head;
    		if(prev!=null)
    		{
    		for(int j=0;j<i-1;j++)
    			toCheck.set(toCheck.get().next);
    		Node toReplace = new Node(prev.value);
    		toReplace.next = curr.next;
    		if(toCheck.compareAndSet(prev, toReplace))
    			return true;
    		else
    		{
    			continue;
    		}
    		}
    		else
    		{
    			if(toCheck.compareAndSet(head.get(), toCheck.get().next))
    				return true;
    			continue;
    			
    		}
    	}
    	else
    	{
    		
    		return false;
    	}
    	
      }
  }*/
	  
  public boolean contains(int value) {
	  if(head==null)
	  {
			return false;
	  }
	  else
	  {
		AtomicMarkableReference<Node> current = head;
		//boolean found = false;
		while(current.getReference().next.getReference()!=null && current.getReference().value<=value)
		{
			current = current.getReference().next;
		}
		if(current.getReference().value==value)
		{
			System.out.println("Found");
				return true;
		}
		System.out.println("No" + value);
		return false;
	  }	
  }
	  
  protected class Node {
  	public Integer value;
  	public AtomicMarkableReference<Node> next;
  	public Node(Integer x) {
  	  value = x;
  	  next = new AtomicMarkableReference<Node>(null,false);
  	}
  }

  /*
  return the string of list, if: 1 -> 2 -> 3, then return "1,2,3,"
  check simpleTest for more info
  */
  public String toString() {
	  String ret  =  "";
	    Node curr = head.getReference().next.getReference();
	    curr = curr.next.getReference();
	    while(curr!=null)
	    {
	    	ret += curr.value +",";
	    	curr = curr.next.getReference();
	    }
	    return ret;
  }
}