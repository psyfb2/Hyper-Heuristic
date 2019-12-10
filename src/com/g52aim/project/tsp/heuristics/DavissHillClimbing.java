package com.g52aim.project.tsp.heuristics;

import java.util.ArrayList;
import java.util.Random;

import com.g52aim.project.tsp.Test;
import com.g52aim.project.tsp.instance.Pair;
import com.g52aim.project.tsp.interfaces.HeuristicInterface;
import com.g52aim.project.tsp.interfaces.TSPSolutionInterface;


/**
 * 
 * @author Warren G. Jackson
 * Performs adjacent swap, returning the first solution with strict improvement
 *
 */
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {
	
	public DavissHillClimbing(Random random) {
		super(random);
	}

	@Override
	public double apply(TSPSolutionInterface solution, double dos, double iom) {
		int times = 1;
		if(dos < 0.2) {
			times = 1;
		} else if (dos < 0.4) {
			times = 2;
		} else if(dos < 0.6) {
			times = 3;
		} else if(dos < 0.8) {
			times = 4;
		} else if(dos < 1.0) {
			times = 5;
		} else if(dos == 1.0) {
			times = 6;
		}
		
		int[] cities = solution.getSolutionRepresentation().getSolutionRepresentation();
		ArrayList<Pair> possiblePairs = produceAllPossibleAdjacentPairs(cities);
		
		while(!possiblePairs.isEmpty() && times > 0) {
			// select two adjacent cities to be swapped randomly which have not been swapped previously
			int randPair = random.nextInt(possiblePairs.size());
			Pair p = possiblePairs.get(randPair);
			possiblePairs.remove(randPair);
			
			// calculate the fitness of the candidate solution using delta evaluation
			double delta = deltaEvaluationAdjacentSwap(p.getX(), cities);
			double currentFitness = solution.getObjectiveFunctionValue();
			
			if(currentFitness + delta < currentFitness) {
				// accept the solution 
				times--;
				possiblePairs = produceAllPossibleAdjacentPairs(cities);
				solution.setObjectiveFunctionValue(currentFitness + delta);
				swapCities(p.getX(), p.getX() + 1, cities);
			}
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
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return true;
	}
	
	public static void main(String[] args) {
		// for testing purposes
		Random r = new Random();
		Test.testMutation(new DavissHillClimbing(r), r, 0.1);
	}
}
