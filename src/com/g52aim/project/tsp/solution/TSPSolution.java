package com.g52aim.project.tsp.solution;

import com.g52aim.project.tsp.interfaces.SolutionRepresentationInterface;
import com.g52aim.project.tsp.interfaces.TSPSolutionInterface;

public class TSPSolution implements TSPSolutionInterface {

	private SolutionRepresentationInterface representation;
	
	private double objectiveFunctionValue;
	
	private int numberOfVariables;
	
	public TSPSolution(SolutionRepresentationInterface representation, double objectiveFunctionValue, int numberOfVariables) {
		
		this.representation = representation;
		this.objectiveFunctionValue = objectiveFunctionValue;
		this.numberOfVariables = numberOfVariables;
	}

	@Override
	public double getObjectiveFunctionValue() {
		return objectiveFunctionValue;
	}

	@Override
	public void setObjectiveFunctionValue(double objectiveFunctionValue) {
		this.objectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public SolutionRepresentationInterface getSolutionRepresentation() {
		return representation;
	}
	
	@Override
	public TSPSolutionInterface clone() {
		// make a deep copy of the solution
		return new TSPSolution(representation.clone(), objectiveFunctionValue, numberOfVariables);
	}

	@Override
	public int getNumberOfCities() {
		return numberOfVariables;
	}
}
