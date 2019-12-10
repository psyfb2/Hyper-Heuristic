package com.g52aim.project.tsp;

import java.util.Arrays;
import java.util.Random;

import com.g52aim.project.tsp.instance.Location;
import com.g52aim.project.tsp.instance.TSPInstance;
import com.g52aim.project.tsp.interfaces.HeuristicInterface;
import com.g52aim.project.tsp.interfaces.SolutionRepresentationInterface;
import com.g52aim.project.tsp.interfaces.XOHeuristicInterface;
import com.g52aim.project.tsp.solution.SolutionRepresentation;
import com.g52aim.project.tsp.solution.TSPSolution;

public class Test {
	public static void testMutation(HeuristicInterface test, Random r, double times) {
		// for testing purposes
		int[] p1Arr = {2, 3, 1, 4, 6, 5, 9, 8, 7, 10};
		
		Location[] testLocation = new Location[p1Arr.length];
		for(int i = 0; i < testLocation.length; i++) {
			testLocation[i] = new Location(i, i + 2);
		}

		TSPObjectiveFunction f = new TSPObjectiveFunction(new TSPInstance(p1Arr.length, testLocation, r));
		
		SolutionRepresentationInterface rep = new SolutionRepresentation(p1Arr);
		TSPSolution p1 = new TSPSolution(
				rep, f.getObjectiveFunctionValue(rep), p1Arr.length);
		
		test.setObjectiveFunction(f);
		double delta;
		double trueVal;
		System.out.println("Before: " + Arrays.toString(p1.getSolutionRepresentation().getSolutionRepresentation()));
		test.apply(p1, times, times);
		System.out.println("After:  " + Arrays.toString(p1.getSolutionRepresentation().getSolutionRepresentation()));
		
		delta = p1.getObjectiveFunctionValue();
		trueVal = f.getObjectiveFunctionValue(p1.getSolutionRepresentation());
		System.out.println("Delta Eval  = " + delta);
		System.out.println("Normal Eval = " + trueVal);
		
	}
	
	public static void testCrossover(XOHeuristicInterface test, Random r, double times) {
		// for testing purposes
		int[] p1Arr = {1, 2, 3, 5, 4, 6, 8, 7, 9};
		int[] p2Arr = {2, 3, 4, 6, 5, 1, 9, 7, 8};
		TSPSolution p1 = new TSPSolution(
				new SolutionRepresentation(p1Arr), 0.0, p1Arr.length);
		TSPSolution p2 = new TSPSolution(
				new SolutionRepresentation(p2Arr), 0.0, p2Arr.length);
		TSPSolution c = new TSPSolution(
				new SolutionRepresentation(p2Arr), 0.0, p2Arr.length);
		

		Location[] testLocation = new Location[p1Arr.length];
		for(int i = 0; i < testLocation.length; i++) {
			testLocation[i] = new Location(i, i + 2);
		}
		test.setObjectiveFunction(new TSPObjectiveFunction(new TSPInstance(p1Arr.length, testLocation, r)));
		
		test.apply(p1, p2, c, times, times);
		System.out.println("Parent 1: " + Arrays.toString(p1.getSolutionRepresentation().getSolutionRepresentation()));
		System.out.println("Parent 2: " + Arrays.toString(p2.getSolutionRepresentation().getSolutionRepresentation()));
		System.out.println("\nChild:    " + Arrays.toString(c.getSolutionRepresentation().getSolutionRepresentation()));
	}
}
