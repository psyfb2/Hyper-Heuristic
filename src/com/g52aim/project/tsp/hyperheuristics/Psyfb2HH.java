package com.g52aim.project.tsp.hyperheuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.g52aim.project.tsp.G52AIMTSP;
import com.g52aim.project.tsp.SolutionPrinter;
import com.g52aim.project.tsp.instance.Location;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;
 
public class Psyfb2HH extends HyperHeuristic {

	public Psyfb2HH(long seed) {
		super(seed);
	}
	
	@Override
	protected void solve(ProblemDomain problem) {
		problem.initialiseSolution(0);
		double current = problem.getFunctionValue(0);
		int parentIndex = 0;
		int childIndex = 1;
		
		problem.setIntensityOfMutation(0.2);
		problem.setDepthOfSearch(1.0);
		
		@SuppressWarnings("unused")
		long iteration = 0;
		boolean accept;
		System.out.println("Iteration\tf(s)\tf(s')\tAccept");

		// contains indexes of local search heuristics
		int[] localSearchHeuristics = problem.getHeuristicsOfType(HeuristicType.LOCAL_SEARCH);
		long[] localSearchScores = new long[localSearchHeuristics.length];
		Arrays.fill(localSearchScores, 1);
		
		// stores indexes of mutation heuristics
		int[] mutationHeuristics = problem.getHeuristicsOfType(HeuristicType.MUTATION);
		long[] mutationScores = new long[mutationHeuristics.length];
		Arrays.fill(mutationScores, 1);
		
		double temperature = current;
		while(!hasTimeExpired()) {
			// perform mutation with highest score heuristic
			int bestM = max(mutationScores);
			
			double m = problem.applyHeuristic(mutationHeuristics[bestM], parentIndex, childIndex);
	
			if(m <= current) {
				mutationScores[bestM]++;
			} else {
				mutationScores[bestM]--;
			}
			
			// perform local search with highest score heuristic
			int bestL = max(localSearchScores);
			double candidate = problem.applyHeuristic(localSearchHeuristics[bestL], childIndex, childIndex);
			
			accept = candidate <= current || rng.nextDouble() < p(candidate - current, temperature);
			
			if(accept) {
				if(candidate < current) {
					localSearchScores[bestL]++;
				} else {
					localSearchScores[bestL]--;
				}
				int temp = parentIndex;
				parentIndex = childIndex;
				childIndex = temp;
				current = candidate;
			}
			
			temperature *= 0.99;
			System.out.println(iteration + "\t" + current + "\t" + candidate + "\t" + accept + "\t" + bestM + "\t" + bestL);
			iteration++;
		}
		
		int[] cities = ((G52AIMTSP) problem).getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		List<Location> routeLocations = new ArrayList<>();
		
		for(int i = 0; i < ((G52AIMTSP) problem).getBestSolution().getNumberOfCities(); i++) {
			routeLocations.add(((G52AIMTSP) problem).instance.getLocationForCity(cities[i]));
		}
		SolutionPrinter.printSolution(routeLocations);
		
	}
	
	private int max(long[] arr) {
		int best = 0;
		for(int i = 1; i < arr.length; i++) {
			if(arr[i] > arr[best]) {
				best = i;
			}
		}
		return best;
	}

	private double p(double delta, double temp) {
		return Math.exp(-delta/temp);
		
	}

	@Override
	public String toString() {
		return "PSYFB2 HH";
	}

}
