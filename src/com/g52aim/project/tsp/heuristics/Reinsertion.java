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
public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

	public Reinsertion(Random random) {

		super(random);
	}

	@Override
	public double apply(TSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
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
		
		int[] cities = solution.getSolutionRepresentation().getSolutionRepresentation();
		for(int i = 0; i < times; i ++) {
			int select = random.nextInt(cities.length);
			int into;
			
			do {
				into = random.nextInt(cities.length);
			} while(select == into);
			
			if((select == 0 && into == cities.length - 1) || (select == cities.length - 1 && into == 0)) {
				insert(select, into, cities);
				// no need to perform delta evaluation, cost has not changed
				continue;
			}
			// delta evaluation
			
			// cost of path from select - 1 -> select
			double oldPathA = f.getCost(cities[Math.floorMod(select - 1, cities.length)], cities[select]);
			// cost of path from select -> select + 1
			double oldPathB= f.getCost(cities[select], cities[(select + 1) % cities.length]); 
			// cost of path from into -> into + 1 or if select > into (indexes shifted to the right) then into - 1 -> into
			double oldPathC = (select < into) ? f.getCost(cities[into], cities[(into + 1) % cities.length]) : f.getCost(cities[Math.floorMod(into - 1, cities.length)], cities[into]); 
			
			// cost of path from select - 1 -> select + 1
			double newPathA = f.getCost(cities[Math.floorMod(select - 1, cities.length)], cities[(select + 1) % cities.length]); 
			// cost of path from into -> select
			double newPathB = f.getCost(cities[into], cities[select]); 
			// cost of path from select -> into + 1 or if select > into (indexes shifted to the right) then into - 1 -> s
			double newPathC = (select < into) ? f.getCost(cities[select], cities[(into + 1) % cities.length]) : f.getCost(cities[Math.floorMod(into - 1, cities.length)], cities[select]); 
			
			
			solution.setObjectiveFunctionValue(
					solution.getObjectiveFunctionValue() + (newPathA + newPathB + newPathC) - (oldPathA + oldPathB + oldPathC));
			
			insert(select, into, cities);
		}
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
	
	private void insert(int select, int into, int[] cities) {
		// insert select into index into and push elements behind into back to fill gap left by moving select
		if(select < into) {
			// push elements back by 1 to fill gap
			int temp = cities[select];
			for(int i = 0; i < into - select; i ++) {
				cities[select + i] = cities[select + i + 1];
			}
			cities[into] = temp;
		} else {
			// push elements forward by 1 to fill gap
			int temp = cities[select];
			for(int i = 0; i < select - into; i++) {
				cities[select - i] = cities[select - i - 1];
			}
			cities[into] = temp;
		}
	}
	
	public static void main(String[] args) {
		// for testing purposes
		Random r = new Random();
		Test.testMutation(new Reinsertion(r), r, 0.1);
	}

}
