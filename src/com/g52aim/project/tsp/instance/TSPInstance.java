package com.g52aim.project.tsp.instance;

import java.util.Random;
import com.g52aim.project.tsp.TSPObjectiveFunction;
import com.g52aim.project.tsp.interfaces.ObjectiveFunctionInterface;
import com.g52aim.project.tsp.interfaces.SolutionRepresentationInterface;
import com.g52aim.project.tsp.interfaces.TSPInstanceInterface;
import com.g52aim.project.tsp.solution.SolutionRepresentation;
import com.g52aim.project.tsp.solution.TSPSolution;

public class TSPInstance implements TSPInstanceInterface {
	
	private final Location[] locations;
	
	private final int numberOfCities;
	
	private final Random random;
	
	private ObjectiveFunctionInterface f = null;
	
	public TSPInstance(int numberOfCities, Location[] locations, Random random) {
		this.numberOfCities = numberOfCities;
		this.random = random;
		this.locations = locations;
		f = new TSPObjectiveFunction(this);
	}
	
	@Override
	public ObjectiveFunctionInterface getTSPObjectiveFunction() {
		return f;
	}

	@Override
	public int getNumberOfCities() {
		return numberOfCities;
	}

	@Override
	public Location getLocationForCity(int cityId) {
		// assuming cityId starts starts at 1
		return locations[cityId - 1];
	}

	@Override
	public TSPSolution createSolution(InitialisationMode mode) {
		// create a random perumation from 1..numberOfCities
		int[] ar = new int[locations.length];
		for(int i = 0; i < ar.length; i++) {
			ar[i] = i + 1;
		}
		
		for (int i = ar.length - 1; i > 0; i--) {
		      int index = random.nextInt(i + 1);
		      int a = ar[index];
		      ar[index] = ar[i];
		      ar[i] = a;
		}
		
		SolutionRepresentationInterface newSolution = new SolutionRepresentation(ar);
		return new TSPSolution(newSolution, f.getObjectiveFunctionValue(newSolution), newSolution.getNumberOfCities());
	}

}
