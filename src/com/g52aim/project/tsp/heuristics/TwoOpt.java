package com.g52aim.project.tsp.heuristics;

import java.util.Random;

import com.g52aim.project.tsp.Test;
import com.g52aim.project.tsp.interfaces.HeuristicInterface;
import com.g52aim.project.tsp.interfaces.TSPSolutionInterface;

/**
 * 
 * @author Warren G. Jackson
 *
 */
public class TwoOpt extends HeuristicOperators implements HeuristicInterface {
	
	public TwoOpt(Random random) {
	
		super(random);
	}

	@Override
	public double apply(TSPSolutionInterface solution, double dos, double intensityOfMutation) {
		// pick i, j (i can be more than j in which case reverse around the array)
		// then reverse sub array cities[i..j] within cities
		int numOfSwaps = 1;
		if(intensityOfMutation < 0.2) {
			numOfSwaps = 1;
		} else if(intensityOfMutation < 0.4) {
			numOfSwaps = 2;
		} else if(intensityOfMutation < 0.6) {
			numOfSwaps = 3;
		} else if (intensityOfMutation < 0.8) {
			numOfSwaps = 4;
		} else if(intensityOfMutation < 1.0) {
			numOfSwaps = 5;
		} else if(intensityOfMutation == 1.0) {
			numOfSwaps = 6;
		}
		
		int[] cities = solution.getSolutionRepresentation().getSolutionRepresentation();
		
		for(int k = 0; k < numOfSwaps; k++) {
			int i = random.nextInt(solution.getNumberOfCities());
			int j;
			
			do {
				j = random.nextInt(solution.getNumberOfCities());
			} while(i == j || i == j + 1);
			
			if(i == 0 && j == cities.length - 1 || j == 0 && i == cities.length) {
				reverseSubArray(i, j, cities);
				// no need to perform delta evaluation, cost has not changed
				continue;
			}
			
			// delta evaluation
			// cost of i - 1 -> i (this path is removed)
			double oldPathA = f.getCost(cities[Math.floorMod((i - 1), cities.length)], cities[i]);
			// cost of j -> j + 1 (this path is removed)
			double oldPathB = f.getCost(cities[j], cities[(j + 1) % cities.length]); 
			
			// cost of i - 1 -> j (new path)
			double newPathA = f.getCost(cities[Math.floorMod(i - 1, cities.length)], cities[j]); 
			// cost of i -> j + 1 (new path)
			double newPathB = f.getCost(cities[i], cities[(j + 1) % cities.length]); 
			
			solution.setObjectiveFunctionValue(
					solution.getObjectiveFunctionValue() + (newPathA + newPathB) - (oldPathA + oldPathB));
			
			// allows i to be more than j
			reverseSubArray(i, j, cities);
		}
		
		
		solution.getSolutionRepresentation().setSolutionRepresentation(cities);
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {
		return false;
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
		// for testing purposes
		Random r = new Random();
		Test.testMutation(new TwoOpt(r), r, 0.1);
	}
}
