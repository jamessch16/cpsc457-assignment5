/**
 * CPSC 457 Fall 2025.
 * Example starter code for Assignment 5 Part 2. You may build off of this provided code or start from scratch.
 * Most of the provided example starter code is taken from Levi Meston's PrimeFinder skeleton code.
 * 
 * NOTE: The rest of Levi's prime finder exercise was not intended to follow the shared buffer constraints of this assignment. 
 * It is your responsibility to ensure that your implementation follows the assignment specifications.
 * Please ensure you also take note of all of the other constraints in the specification.
 */
public class PrimeFinder {
	
	/**
	 * Simple prime number checker.
	 * @param num The number to check
	 * @return True if num is a prime number, False otherwise.
	 */
	public static boolean isPrime(int num) {
		if (num < 2) {
			return false;
		}
		for (int i = 2; i <= Math.sqrt(num); i++) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Most of the provided example starter code is taken from Levi Meston's Prime Finder Skeleton code.
	 * 
	 * NOTE: The rest of Levi's prime finder exercise was not intended to follow the shared buffer constraints of this assignment.
	 * It is your responsibility to ensure that your implementation follows the assignment specifications.
	 * Additionally, please ensure you also take note of all of the other constraints in the specification.
	 */
	public static void main(String [] args) {
		/* Constant variables */
		final int MIN_BOUND_ARG = 0;
		final int MAX_BOUND_ARG = 1;
		final int THREAD_COUNT_ARG = 2;
		final int MIN_ARGS = 3;
		
		/* Variables to hold arguments */
		int min_bound;
		int max_bound;
		int thread_count;
		
		/* Variables to compute subranges */
		int nums_per_thread;
		int remainder;
		
		/* Ensure we have a valid number of args. */
		if (args.length < MIN_ARGS) {
			System.out.println ("Usage: java PrimeFinder <min> <max> <thread_count>");
			System.exit (0);
		}
		
		/* Parse args. */
		min_bound = Integer.parseInt (args [MIN_BOUND_ARG]);
		max_bound = Integer.parseInt (args [MAX_BOUND_ARG]);
		thread_count = Integer.parseInt (args [THREAD_COUNT_ARG]);
		
		/* Find minimum subrange count and remainder */
		nums_per_thread = Math.floorDiv((max_bound - min_bound + 1) , thread_count);
		remainder = (max_bound - min_bound + 1) % thread_count;
		
		/* Incomplete basic thread loop */
		for (int thread_index = 0; thread_index < thread_count; thread_index++) {
			/* If there's a remainder, we'll equally distribute it across the threads. */
			if (remainder > 0) {
				remainder--;
			}
			/* Otherwise, we'll just setup the base bounds. */
			else {
				
			}
		}
	}
	
}