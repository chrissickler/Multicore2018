package hw1_6_b;

public class main {

	public static void main(String[] args)
	{
		
	  System.out.printf("Threads: %d c: %d\n", 1,PIncrementAtomic.parallelIncrement(0, 1));

	  System.out.printf("Threads: %d c: %d\n", 2,PIncrementAtomic.parallelIncrement(0, 2));

	  System.out.printf("Threads: %d c: %d\n", 3,PIncrementAtomic.parallelIncrement(0, 3));

	  System.out.printf("Threads: %d c: %d\n", 4,PIncrementAtomic.parallelIncrement(0, 4));
	      
	  System.out.printf("Threads: %d c: %d\n", 5,PIncrementAtomic.parallelIncrement(0, 5));

 	  System.out.printf("Threads: %d c: %d\n", 6,PIncrementAtomic.parallelIncrement(0, 6));

	  System.out.printf("Threads: %d c: %d\n", 7,PIncrementAtomic.parallelIncrement(0, 7));

      System.out.printf("Threads: %d c: %d\n", 8,PIncrementAtomic.parallelIncrement(0, 8));
	}

}
