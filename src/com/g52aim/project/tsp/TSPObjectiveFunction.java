package com.g52aim.project.tsp;

import com.g52aim.project.tsp.instance.Location;
import com.g52aim.project.tsp.interfaces.ObjectiveFunctionInterface;
import com.g52aim.project.tsp.interfaces.SolutionRepresentationInterface;
import com.g52aim.project.tsp.interfaces.TSPInstanceInterface;

public class TSPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final TSPInstanceInterface instance;
	
	public TSPObjectiveFunction(TSPInstanceInterface instance) {
		this.instance = instance;
	}

	@Override
	public double getObjectiveFunctionValue(SolutionRepresentationInterface solution) {
		// return the overall distance of the route including the distance from the last city back to the first!
		// delta evaluation built into the heuristics
		int[] solutionArr = solution.getSolutionRepresentation();	
		double cost = 0;
	
		for(int i = 0; i < solutionArr.length - 1; i++) {
			cost += getCost(solutionArr[i], solutionArr[i + 1]);
		}
		
		// add distance from last city back to first city
		cost += getCost(solutionArr[solutionArr.length - 1], solutionArr[0]);
		
		return cost;
	}

	@Override
	public double getCost(int location_a, int location_b) {
		Location a = instance.getLocationForCity(location_a);
		Location b = instance.getLocationForCity(location_b);
		return Math.round(Math.sqrt( Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2) ));
	}

}
