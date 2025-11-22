
import java.util.ArrayList;

/**
 * CPSC 457 Fall 2025.
 * Code modified from Levi Meston's Prime Finder Skeleton code.
 */

class SharedIntegerArray {
	private ArrayList<Integer> primeNumbers;

	public SharedIntegerArray(int initialCapacity) {

		// TODO CONVERT TO SINGLETON DESIGN PATTERN
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
			System.out.print(primeNumbers.get(i) + " ");
		}
	}

	public synchronized int size() {
		/*
		Returns the number of elements in the shared array.
		@return The number of elements in the shared array.
		*/
		return primeNumbers.size();
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

		// Reduce thread count to be equal to range if more threads than numbers to check
		if (thread_count > (max_bound - min_bound + 1)) {
			thread_count = max_bound - min_bound + 1;
		}

		// Create thread array and shared array
		ArrayList<PrimeFinderThread> threads = new ArrayList<>(thread_count);
		SharedIntegerArray sharedPrimes = new SharedIntegerArray(128);

		// Find minimum subrange count and remainder 
		nums_per_thread = Math.floorDiv((max_bound - min_bound + 1) , thread_count);
		remainder = (max_bound - min_bound + 1) % thread_count;
		
		// Create threads to search subranges
		int threadLowerBound = min_bound;
		int threadUpperBound;

		for (int thread_index = 0; thread_index < thread_count; thread_index++) {
			// If there's a remainder, we'll equally distribute it across the threads. 

			// Calculate bounds the thread searches
			if (remainder > 0) {
				remainder--;
				threadUpperBound = threadLowerBound + nums_per_thread;
			}
			else {
				threadUpperBound = threadLowerBound + nums_per_thread - 1;
			}

			// Create and add thread to list
			PrimeFinderThread thread = new PrimeFinderThread(threadLowerBound, threadUpperBound, sharedPrimes);

			thread.start();
			threads.add(thread);

			System.out.println("Thread " + thread_index + " searching range: [" + threadLowerBound + ", " + threadUpperBound + "]");

			// Update lower bound for next thread
			threadLowerBound = threadUpperBound + 1;
		}

		// Wait on all threads to finish
		for (PrimeFinderThread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Sort and print results
		System.out.println("Main Thread: All workers finished. Primes found:\n");
		sharedPrimes.sort();
		sharedPrimes.print();

		System.out.println("\n\nMain Thread: " + sharedPrimes.size() + " prime numbers found.");
	}
	
}