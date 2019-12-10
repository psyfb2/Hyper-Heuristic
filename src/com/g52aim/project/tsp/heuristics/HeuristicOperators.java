package com.g52aim.project.tsp.heuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.g52aim.project.tsp.instance.Pair;
import com.g52aim.project.tsp.interfaces.ObjectiveFunctionInterface;

/**
 * 
 * @author Warren G. Jackson
 * 
 * TODO you can add any common functionality here
 *		to save having to re-implement them in all 
 *		your other heuristics!
 *		( swapping two cities seems to be popular )
 *
 *
 * If this this concept it new to you, you may want
 * to read this article on inheritance:
 * https://www.tutorialspoint.com/java/java_inheritance.htm 
 */
public class HeuristicOperators {
	
	protected final Random random;
	protected ObjectiveFunctionInterface f;
	
	public HeuristicOperators(Random random) {
	
		this.random = random;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		// store the objective function so we can use it later!
		this.f = f;
	}
	
	protected void swapCities(int index_a, int index_b, int[] cities) {
		int temp = cities[index_a];
		cities[index_a] = cities[index_b];
		cities[index_b] = temp;
	}
	
	protected void reverseSubArray(int start, int end, int[] cities) {
		if(start < end) {
			do {
				swapCities(start++, end--, cities);
			} while(start < end);
		} else {
			// allow start to be more than end. Need to reverse array around the end.
			int[] arr = new int[(cities.length - (start - 1) + end)];
			// make a new array containing values from start to end
			int index = 0;
			for(int i = 0; i < cities.length - start; i++) {
				arr[index++] = cities[i + start];
			}
			for(int i = 0; i < end + 1; i++) {
				arr[index++] = cities[i];
			}

			reverseSubArray(0, arr.length - 1, arr);

			// copy back into original array
			for(int i = 0; i < arr.length; i++) {
				cities[(start + i) % cities.length] = arr[i];
			}
		}
	}
	
	protected ArrayList<Pair> produceAllPossibleAdjacentPairs(int[] cities) {
		// produces pairs of all possible adjacent city indexes 
		ArrayList<Pair> possiblePairs = new ArrayList<Pair>();
		for(int i = 0; i < cities.length - 1; i++) {
			possiblePairs.add(new Pair(i, i + 1));
		}
		return possiblePairs;
	}
	
	/**
	 * Returns delta value (change in distance) for adjacent swap given cities array BEFORE swap
	 * @param indexToSwap assuming will swap indexToSwap with indexToSwap + 1
	 * @param cities cities array BEFORE swap
	 * @return delta (change in distance)
	 */
	protected double deltaEvaluationAdjacentSwap(int indexToSwap, int[] cities) {
		// cost of index + 1 -> index + 2 (this path is removed)
		double oldPathA = f.getCost(cities[(indexToSwap + 1) % cities.length], cities[(indexToSwap + 2) % cities.length]); 
		// cost of index - 1 -> index     (this path is removed)
		double oldPathB = f.getCost(cities[Math.floorMod(indexToSwap - 1, cities.length)], cities[indexToSwap]); 
		// cost of index -> index + 2     (new path)
		double newPathA = f.getCost(cities[indexToSwap], cities[(indexToSwap + 2) % cities.length]); 
		 // cost of index - 1 -> index + 1 (new path)
		double newPathB = f.getCost(cities[Math.floorMod(indexToSwap - 1, cities.length)], cities[(indexToSwap + 1) % cities.length]);
		
		return (newPathA + newPathB) - (oldPathA + oldPathB);
	}
	
	public static void main(String[] args) {
		// for testing purposes
		int[] arr = new int[10];
		for(int i = 0; i < arr.length; i++) {
			arr[i] = i + 1;
		}
		System.out.println("Before: " + Arrays.toString(arr));
		new HeuristicOperators(new Random()).reverseSubArray(9, 0, arr);
		System.out.println("After:  " + Arrays.toString(arr));
	}
}
