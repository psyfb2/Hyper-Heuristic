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
public class OX extends CrossoverHeuristicOperators implements XOHeuristicInterface {
	
	public OX(Random random) {
		
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
		
		int[] parent1 = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] parent2 = p2.getSolutionRepresentation().getSolutionRepresentation();
		
		for(int i = 0; i < times; i++) {
			int[] c1 = new int[parent1.length];
			int[] c2 = new int[parent2.length];
			Arrays.fill(c1, -1);
			Arrays.fill(c2, -1);
			
			oX(parent1, parent2, c1);
			oX(parent2, parent1, c2);
			
			// make the children of this iteration the parents of the next
			parent1 = c1;
			parent2 = c2;
		}
		
		int[] childToUse;
		if(random.nextBoolean()) {
			childToUse = parent1;
		} else {
			childToUse = parent2;
		}
		c.getSolutionRepresentation().setSolutionRepresentation(childToUse);
		c.setObjectiveFunctionValue(f.getObjectiveFunctionValue(c.getSolutionRepresentation()));
		return c.getObjectiveFunctionValue();
	}
	
	private void oX(int[] p1Cities, int[] p2Cities, int[] child) {
		int left = random.nextInt(p1Cities.length);
		int right;
		
		do {
			right = random.nextInt(p1Cities.length - 1);
		} while(left == right);
		
		// left must be less than right
		if(left > right) {
			int temp = left;
			left = right;
			right = temp;
		}
		
		//System.out.println("Left =  " + left);
		//System.out.println("Right = " + right);
		
		for(int i = left; i <= right; i++) {
			child[i] = p1Cities[i];
		}
		
		// stores elements in p1 that are not in child
		int[] p1NotInChild = new int[p1Cities.length - (right - left) - 1];
		int j = 0;
		for(int i = 0; i < p1Cities.length; i++) {
			 if(!subArrayContains(left, right, child, p1Cities[i])) {
				 p1NotInChild[j++] = p1Cities[i];
			 }
		}
		
		// rotate parent 2 so that element after right is first element
		int[] p2CitiesRotated = p2Cities.clone(); // don't want to change p2Cities
		rotateRight(p2CitiesRotated, p2Cities.length - right - 1);
		
		// order elements in p1NotInChild by the order in p2Cities
		int[] replacement = new int[p1Cities.length - (right - left) - 1];
		j = 0;
		for(int i = 0; i < p1Cities.length; i++) {
			if(subArrayContains(0, p1NotInChild.length - 1, p1NotInChild, p2CitiesRotated[i])) {
				replacement[j++] = p2CitiesRotated[i];
			}
		}
		
		// p1NotInChild now has the correct order (according to the order they appear in p2Cities)
		
		// copy elements in replacement into remaining elements in child
		for(int i = 0; i < replacement.length; i++) {
			int index = (right + i + 1) % child.length;
			child[index] = replacement[i];
		}
	}
	
	private boolean subArrayContains(int left, int right, int[] arrToCheck, int val) {
		for(int i = left; i <= right; i++) {
			if(arrToCheck[i] == val) {
				return true;
			}
		}
		return false;
	}
	
	public void rotateRight(int[] arr, int places) {
		int[] tempArr = new int[arr.length];
		System.arraycopy(arr, arr.length - places, tempArr, 0, places);
		System.arraycopy(arr, 0, arr, places, arr.length - places);
		System.arraycopy(tempArr, 0, arr, 0, places);
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
	
	public static void main(String[] args) {
		Random r = new Random();
		Test.testCrossover(new OX(r), r, 0.1);
	}
}