package com.g52aim.project.tsp.runners;

import java.util.Random;

import com.g52aim.project.tsp.G52AIMTSP;
import com.g52aim.project.tsp.hyperheuristics.Psyfb2HH;
import com.g52aim.project.tsp.hyperheuristics.SR_IE_HH;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

@SuppressWarnings("unused")
public class SR_IE_Runner {
	
	public static void main(String [] args) {
		long seed = 15032018l;
		seed = new Random().nextInt();
		long timeLimit = 60_000l;
		ProblemDomain problem = new G52AIMTSP(seed);
		problem.loadInstance(11);
		
		//HyperHeuristic hh = new SR_IE_HH(seed);
		HyperHeuristic hh = new Psyfb2HH(seed);
		
		hh.setTimeLimit(timeLimit);
		hh.loadProblemDomain(problem);
		hh.run();
		
		System.out.println("f(s_best) = " + hh.getBestSolutionValue() + "\n");
	}

}
