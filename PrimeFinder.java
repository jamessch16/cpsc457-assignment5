
import java.util.ArrayList;

/**
 * CPSC 457 Fall 2025.
 * Example starter code for Assignment 5 Part 2. You may build off of this provided code or start from scratch.
 * Most of the provided example starter code is taken from Levi Meston's PrimeFinder skeleton code.
 * 
 * NOTE: The rest of Levi's prime finder exercise was not intended to follow the shared buffer constraints of this assignment. 
 * It is your responsibility to ensure that your implementation follows the assignment specifications.
 * Please ensure you also take note of all of the other constraints in the specification.
 */

class SharedIntegerArray {
	private static ArrayList<Integer> primeNumbers;

	static {
		primeNumbers = new ArrayList<>();
	}

	public SharedIntegerArray(int initialCapacity) {

		// TODO SINGLETON DESIGN PATTERN


		this.primeNumbers = new ArrayList<>(initialCapacity);
	}

	public synchronized void add(int num) {
		/*
		Adds the given number to the first open spot in the shared array.
		@param num The number to add.
		*/
		primeNumbers.add(num);
	}

	public synchronized int get(int index) {
		/*
		Gets the number at the specified index in the shared array.
		@param index The index to get.
		@return The number at the given index.
		*/
		return primeNumbers.get(index);
	}

	public synchronized void sort() {
		/*
		Sorts the shared array in ascending order.
		*/
		primeNumbers.sort(null);
	}

	public synchronized void print() {
		/*
		Prints the contents of the shared array.
		*/
		for (int i = 0; i < primeNumbers.size(); i++) {
			System.out.print(i + " ");
		}
	}
}


class PrimeFinderThread extends Thread {
	private int lowerBound;
	private int upperBound;
	private SharedIntegerArray sharedPrimes;

	public PrimeFinderThread(int lowerBound, int upperBound, SharedIntegerArray sharedPrimes) {
		if (lowerBound > upperBound) {
			throw new IllegalArgumentException("Lower bound must be less than or equal to upper bound.");
		}

		if (lowerBound < 1 || upperBound < 1) {
			throw new IllegalArgumentException("Bounds must be non-negative.");
		}

		// TODO VALIDATE INPUTS
		
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.sharedPrimes = sharedPrimes;
	}

	@Override
	public void run() {

		// Finds all prime numbers in the range [lowerBound, upperBound] and writes them to the next spot in sharedPrimes
		for (int num = lowerBound; num <= upperBound; num++) {
			if (PrimeFinder.isPrime(num)) {
				sharedPrimes.add(num);
			}
		}
	}
}


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

		final int MIN_BOUND_ARG = 0;
		final int MAX_BOUND_ARG = 1;
		final int THREAD_COUNT_ARG = 2;
		final int MIN_ARGS = 3;
		
		int min_bound;
		int max_bound;
		int thread_count;
		
		int nums_per_thread;
		int remainder;

		
		// Ensure we have a valid number of args.
		if (args.length < MIN_ARGS) {
			System.out.println ("Usage: java PrimeFinder <min> <max> <thread_count>");
			System.exit (0);
		}
		
		// Parse args.
		min_bound = Integer.parseInt (args [MIN_BOUND_ARG]);
		max_bound = Integer.parseInt (args [MAX_BOUND_ARG]);
		thread_count = Integer.parseInt (args [THREAD_COUNT_ARG]);

		// Validate Inputs
		if (max_bound < min_bound) {
			System.err.println("Error: max must be >= min");
			System.exit(1);
		}

		if (thread_count < 1) {
			System.err.println("Error: must have at least one thread");
			System.exit(1);
		}



		// TODO Handle case N > Range as in specifications
		// TODO handle case that bounds are negative ie lower < 0 orupper < 0

		ArrayList<PrimeFinderThread> threads = new ArrayList<>(thread_count);
		SharedIntegerArray sharedPrimes = new SharedIntegerArray(128);

		// Find minimum subrange count and remainder 
		nums_per_thread = Math.floorDiv((max_bound - min_bound + 1) , thread_count);
		remainder = (max_bound - min_bound + 1) % thread_count;

		// Initialize shared array
		// TODO Singleton
		
		// Create threads with searching the subranges
		for (int thread_index = 0; thread_index < thread_count; thread_index++) {
			// If there's a remainder, we'll equally distribute it across the threads. 

			int threadLowerBound = -1;
			int threadUpperBound = -1;

			// Calculate bounds the thread searches
			if (remainder > 0) {
				remainder--;
				threadLowerBound = min_bound + thread_index * nums_per_thread + Math.min(thread_index, remainder);	// TODO FIX INDEXING
				threadUpperBound = min_bound + (thread_index + 1) * nums_per_thread
			}
			else {
				threadLowerBound = min_bound + thread_index * nums_per_thread + Math.min(thread_index, remainder);	// TODO FIX INDEXING
				threadUpperBound = min_bound + (thread_index + 1) * nums_per_thread + Math.min(thread_index + 1, remainder) - 1;
			}

			// Create and add thread to list
			threads.add(new PrimeFinderThread(		// TODO FIX INDEXING
				min_bound + thread_index * nums_per_thread + Math.min(thread_index, remainder),
				min_bound + (thread_index + 1) * nums_per_thread + Math.min(thread_index + 1, remainder) - 1,
				sharedPrimes
			));

			System.out.println("Thread " + thread_index + " searching range: [" +
				(min_bound + thread_index * nums_per_thread + Math.min(thread_index, remainder)) + ", " +
				(min_bound + (thread_index + 1) * nums_per_thread + Math.min(thread_index + 1, remainder) - 1 + "]")
			);
		}

		// Wait on all threads to finish
	}
	
}