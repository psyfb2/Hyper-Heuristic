package com.g52aim.project.tsp.heuristics;

import java.util.Random;

import com.g52aim.project.tsp.interfaces.ObjectiveFunctionInterface;

/**
 * 
 * @author Warren G. Jackson
 * 
 * TODO you can add any common functionality here
 *		to save having to re-implement them in all 
 *		your other heuristics!
 *
 *
 * If this this concept it new to you, you may want
 * to read this article on inheritance:
 * https://www.tutorialspoint.com/java/java_inheritance.htm 
 */
public class CrossoverHeuristicOperators {
	
	protected final Random random;
	protected ObjectiveFunctionInterface f;
	
	public CrossoverHeuristicOperators(Random random) {
	
		this.random = random;
	}

	
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		this.f = f;
	}
	
	/**
	 * swap c1Cities[i..j] with c2Cities[i..j]
	 * @param i
	 * @param j
	 * @param c1Cities
	 * @param c2Cities
	 */
	protected void swapSubArray(int i, int j, int[] c1Cities, int[] c2Cities) {
		for(int k = i; k <= j; k++) {
			int temp = c1Cities[k];
			c1Cities[k] = c2Cities[k];
			c2Cities[k] = temp;
		}
	}
}
