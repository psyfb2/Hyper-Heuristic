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
public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	public AdjacentSwap(Random random) {
		super(random);
	}

	@Override
	public double apply(TSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		int numOfSwaps = 1;
		if(intensityOfMutation < 0.2) {
			numOfSwaps = 1;
		} else if(intensityOfMutation < 0.4) {
			numOfSwaps = 2;
		} else if(intensityOfMutation < 0.6) {
			numOfSwaps = 4;
		} else if (intensityOfMutation < 0.8) {
			numOfSwaps = 8;
		} else if(intensityOfMutation < 1.0) {
			numOfSwaps = 16;
		} else if(intensityOfMutation == 1.0) {
			numOfSwaps = 32;
		}
		
		int[] cities = solution.getSolutionRepresentation().getSolutionRepresentation();
		for(int i = 0; i < numOfSwaps; i++) {
			int indexToSwap = random.nextInt(solution.getNumberOfCities() - 1);
			
			// use delta evaluation to calculate the new cost (by using cost of old and new paths)
			double delta = deltaEvaluationAdjacentSwap(indexToSwap, cities);
			solution.setObjectiveFunctionValue(
					solution.getObjectiveFunctionValue() + delta);
			
			swapCities(indexToSwap, indexToSwap + 1, cities);
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
		Test.testMutation(new AdjacentSwap(r), r, 0.1);
	}
}
