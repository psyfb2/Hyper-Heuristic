package com.g52aim.project.tsp.heuristics;

import java.util.Arrays;
import java.util.Random;

import com.g52aim.project.tsp.Test;
import com.g52aim.project.tsp.interfaces.TSPSolutionInterface;
import com.g52aim.project.tsp.interfaces.XOHeuristicInterface;

/**
 * 
 * @author Warren G. Jackson
 *
 */
public class PMX extends CrossoverHeuristicOperators implements XOHeuristicInterface {
	
	public PMX(Random random) {
		
		super(random);
	}

	@Override
	public double apply(TSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		// invalid operation, return the same solution!
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public double apply(TSPSolutionInterface p1, TSPSolutionInterface p2,
			TSPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
		int times = 1;
		if(intensityOfMutation < 0.2) {
			times = 1;
		} else if(intensityOfMutation < 0.4) {
			times = 2;
		} else if(intensityOfMutation < 0.6) {
			times = 3;
		} else if(intensityOfMutation < 0.8) {
			times = 4;
		} else if(intensityOfMutation < 1.0) {
			times = 5;
		} else if(intensityOfMutation == 1.0) {
			times = 6;
		}
		
		int[] p1Cities = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] p2Cities = p2.getSolutionRepresentation().getSolutionRepresentation();
		int[] c1Cities = null;
		int[] c2Cities = null;
		
		for(int t = 0; t < times; t++) {
			// chose random sub array in both parents from i to j where i < j < p.length
			int i = random.nextInt(p1Cities.length);
			int j;
			
			do {
				j = random.nextInt(p1Cities.length - 1);
			} while(i == j);
			
			// i must be less than j
			if(i > j) {
				int temp = i;
				i = j;
				j = temp;
			}
			
			//System.out.println("i: " + i);
			//System.out.println("j: " + j);
	
			
			c1Cities = p1Cities.clone();
			c2Cities = p2Cities.clone();
			
			// swap sub array of p1 with p2 so that c1[i..j] = p2[i..j] and c2[i..j] = p1[1..j]
			// and produce a replacement mapping for child 1 and child 2
			int[] c1Replacements = new int[c1Cities.length + 1];
			int[] c2Replacements = new int[c2Cities.length + 1];
			Arrays.fill(c1Replacements, -1);
			Arrays.fill(c2Replacements, -1);
			for(int k = i; k <= j; k++) {
				c1Cities[k] = p2Cities[k];
				c2Cities[k] = p1Cities[k];
				
				c1Replacements[p2Cities[k]] = p1Cities[k];
				c2Replacements[p1Cities[k]] = p2Cities[k];
			}
			
			fillChildren(p1Cities, p2Cities, c1Replacements, c2Replacements, c1Cities, c2Cities, 0, i);
			fillChildren(p1Cities, p2Cities, c1Replacements, c2Replacements, c1Cities, c2Cities, j + 1, p1Cities.length);
			
			// make the children of this iteration the parents of the next
			p1Cities = c1Cities;
			p2Cities = c2Cities;
		}

		int[] childToUse;
		if(random.nextBoolean()) {
			childToUse = c1Cities;
		} else {
			childToUse = c2Cities;
		}
		c.getSolutionRepresentation().setSolutionRepresentation(childToUse);
		c.setObjectiveFunctionValue(f.getObjectiveFunctionValue(c.getSolutionRepresentation()));
		return c.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}
	
	private void fillChildren(int[] p1Cities, int[] p2Cities, int[] c1Replacements, int[] c2Replacements, int[] c1Cities, int[] c2Cities, int start, int end) {
		// use replacement arrays to fill in elements with indexes not between i and j
		for(int k = start; k < end; k++) {
			int val1 = p1Cities[k];
			int replacementVal1 = c1Replacements[val1];
			
			int val2 = p2Cities[k];
			int replacementVal2 = c2Replacements[val2];
			
			// while loop in order to chain mappings e.g. 1<->2, 1<->4 = 2<->1<->4
			while(replacementVal1 != -1) {
				val1 = replacementVal1;
				replacementVal1 = c1Replacements[replacementVal1];
			}
			
			while(replacementVal2 != -1) {
				val2 = replacementVal2;
				replacementVal2 = c2Replacements[replacementVal2];
			}
			
			c1Cities[k] = val1;
			c2Cities[k] = val2;
		}
	}
	
	public static void main(String[] args) {
		Random r = new Random();
		Test.testCrossover(new PMX(r), r, 0.1);
	}
}
