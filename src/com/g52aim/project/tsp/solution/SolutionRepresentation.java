package com.g52aim.project.tsp.solution;

import com.g52aim.project.tsp.interfaces.SolutionRepresentationInterface;

/**
 * 
 * @author Warren G. Jackson
 * 
 *
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {
	
	private int[] representation;

	public SolutionRepresentation(int[] representation) {
		this.representation = representation;
	}
	
	@Override
	public int[] getSolutionRepresentation() {
		return representation;
	}

	@Override
	public void setSolutionRepresentation(int[] solution) {
		representation = solution;
	}

	@Override
	public int getNumberOfCities() {
		return representation.length;
	}

	@Override
	public SolutionRepresentationInterface clone() {
		return new SolutionRepresentation(representation.clone());
	}

}
