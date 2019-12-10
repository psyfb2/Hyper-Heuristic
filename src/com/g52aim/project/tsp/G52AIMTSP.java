package com.g52aim.project.tsp;


import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.g52aim.project.tsp.heuristics.AdjacentSwap;
import com.g52aim.project.tsp.heuristics.DavissHillClimbing;
import com.g52aim.project.tsp.heuristics.NextDescent;
import com.g52aim.project.tsp.heuristics.OX;
import com.g52aim.project.tsp.heuristics.PMX;
import com.g52aim.project.tsp.heuristics.Reinsertion;
import com.g52aim.project.tsp.heuristics.TwoOpt;
import com.g52aim.project.tsp.hyperheuristics.SR_IE_HH;
import com.g52aim.project.tsp.instance.InitialisationMode;
import com.g52aim.project.tsp.instance.Location;
import com.g52aim.project.tsp.instance.reader.TSPInstanceReader;
import com.g52aim.project.tsp.interfaces.HeuristicInterface;
import com.g52aim.project.tsp.interfaces.TSPInstanceInterface;
import com.g52aim.project.tsp.interfaces.TSPSolutionInterface;
import com.g52aim.project.tsp.interfaces.Visualisable;
import com.g52aim.project.tsp.interfaces.XOHeuristicInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

public class G52AIMTSP extends ProblemDomain implements Visualisable {

	private String[] instanceFiles = {
		"d1291", "d18512", "dj38", "usa13509", "qa194", "ch71009", "square", "circle", "plus", "dj38", "T81", "pcb1173"
	};
	
	private TSPSolutionInterface[] solutions;
	
	public TSPSolutionInterface bestSolution;
	
	public TSPInstanceInterface instance;
	
	private HeuristicInterface[] heuristics;
	
	public G52AIMTSP(long seed) {
		super(seed);
		
		// set default memory size and create the array of low-level heuristics
		setMemorySize(2);
		heuristics = new HeuristicInterface[7];
		heuristics[0] = new AdjacentSwap(rng);
		heuristics[1] = new TwoOpt(rng);
		heuristics[2] = new Reinsertion(rng);
		heuristics[3] = new NextDescent(rng);
		heuristics[4] = new DavissHillClimbing(rng);
		heuristics[5] = new OX(rng);
		heuristics[6] = new PMX(rng);
	}
	
	public TSPSolutionInterface getSolution(int index) {
		return solutions[index];
	}
	
	public TSPSolutionInterface getBestSolution() {
		return bestSolution;
	}

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {
		// apply heuristic and return the objective value of the candidate solution
		if(currentIndex != candidateIndex) {
			copySolution(currentIndex, candidateIndex);
		}

		double val = heuristics[hIndex].apply(solutions[candidateIndex], depthOfSearch, intensityOfMutation);
		
		updateBestSolution(candidateIndex);
		return val;
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		// apply heuristic and return the objective value of the candidate solution
		XOHeuristicInterface h =  (XOHeuristicInterface) heuristics[hIndex];
		double val = h.apply(solutions[parent1Index], solutions[parent2Index], solutions[candidateIndex], depthOfSearch, intensityOfMutation);
		
		updateBestSolution(candidateIndex);
		return val;
	}

	@Override
	public String bestSolutionToString() {
		return bestSolution.toString();
	}

	@Override
	public boolean compareSolutions(int a, int b) {
		return solutions[a].getObjectiveFunctionValue() < solutions[b].getObjectiveFunctionValue();
	}

	@Override
	public void copySolution(int a, int b) {
		// BEWARE this should copy the solution, not the reference to it!
		// That is, that if we apply a heuristic to the solution in index 'b',
		// then it does not modify the solution in index 'a' or vice-versa.
		solutions[b] = solutions[a].clone();
	}

	@Override
	public double getBestSolutionValue() {
		return bestSolution.getObjectiveFunctionValue();
	}
	
	@Override
	public double getFunctionValue(int index) {
		return solutions[index].getObjectiveFunctionValue();
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
		// hard coded since heuristics list won't be changing in the future
		int[] heuristicIndexes;
		if(type.equals(HeuristicType.CROSSOVER)) {
			heuristicIndexes = new int[2];
			heuristicIndexes[0] = 5;
			heuristicIndexes[1] = 6;
		} 
		else if(type.equals(HeuristicType.LOCAL_SEARCH)) {
			heuristicIndexes = new int[2];
			heuristicIndexes[0] = 3;
			heuristicIndexes[1] = 4;
		}
		else if(type.equals(HeuristicType.MUTATION)) {
			heuristicIndexes = new int[3];
			heuristicIndexes[0] = 0;
			heuristicIndexes[1] = 1;
			heuristicIndexes[2] = 2;
		} else {
			heuristicIndexes = new int[0];
		}
		
		return heuristicIndexes;
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		// hard coded since heuristics list won't be changing in the future
		int[] dosHeuristics = {3, 4};
		return dosHeuristics;
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		// hard coded since heuristics list won't be changing in the future
		int[] iomHeuristics = {0, 1, 2, 5, 6};
		return iomHeuristics;
	}

	@Override
	public int getNumberOfHeuristics() {
		// has to be hard-coded due to the design of the HyFlex framework
		return 7;
	}

	@Override
	public int getNumberOfInstances() {
		return instanceFiles.length;
	}

	@Override
	public void initialiseSolution(int index) {
		solutions[index] = instance.createSolution(InitialisationMode.RANDOM);
		
		// update best solution
		if(bestSolution == null) {
			bestSolution = solutions[index];
		} else if(solutions[index].getObjectiveFunctionValue() < bestSolution.getObjectiveFunctionValue()) {
			bestSolution = solutions[index];
		}
		
	}

	@Override
	public void loadInstance(int instanceId) {

		String SEP = FileSystems.getDefault().getSeparator();
		String instanceName = "instances" + SEP + "tsp" + SEP + instanceFiles [instanceId] + ".tsp";

		// load instance from file
		instance = new TSPInstanceReader().readTSPInstance(Paths.get(instanceName), rng);

		// set the objective function in each of the heuristics
		for(HeuristicInterface h : heuristics) {
			h.setObjectiveFunction(instance.getTSPObjectiveFunction());
		}
	}

	@Override
	public void setMemorySize(int size) {
		if(size <= 1) {
			return;
		}
		TSPSolutionInterface[] tempSolutions = new TSPSolutionInterface[size];
		
		if(solutions == null) {
			solutions = tempSolutions;
			return;
		}
		
		// copy over previous solutions
		for(int i = 0; i < solutions.length && i < size; i++) {
			tempSolutions[i] = solutions[i];
		}
		solutions = tempSolutions;
	}

	@Override
	public String solutionToString(int index) {
		return solutions[index].toString();
	}

	@Override
	public String toString() {
		return "PSYFB2's G52AIM TSP";
	}
	
	private void updateBestSolution(int index) {
		if(solutions[index].getObjectiveFunctionValue() < bestSolution.getObjectiveFunctionValue()) {
			bestSolution = solutions[index];
		}
	}
	
	@Override
	public TSPInstanceInterface getLoadedInstance() {

		return this.instance;
	}

	@Override
	public Location[] getRouteOrderedByLocations() {

		int[] city_ids = getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		Location[] route = Arrays.stream(city_ids).boxed().map(getLoadedInstance()::getLocationForCity).toArray(Location[]::new);
		return route;
	}

	public static void main(String [] args) {
		long seed = 527893l;
		long timeLimit = 10_000;
		G52AIMTSP tsp = new G52AIMTSP(seed);
		HyperHeuristic hh = new SR_IE_HH(seed);
		tsp.loadInstance( 0 );
		hh.setTimeLimit(timeLimit);
		hh.loadProblemDomain(tsp);
		hh.run();
		
		double best = hh.getBestSolutionValue();
		System.out.println("Best = " + best);
		
		// you will need to populate this based on your representation!
		int[] cities = tsp.getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		List<Location> routeLocations = new ArrayList<>();
		
		for(int i = 0; i < tsp.getBestSolution().getNumberOfCities(); i++) {
			routeLocations.add(tsp.instance.getLocationForCity(cities[i]));
		}
		//SolutionPrinter.printSolution(routeLocations);
	}


}
