package com.g52aim.project.tsp.runners;

import com.g52aim.project.tsp.G52AIMTSP;
import com.g52aim.project.tsp.hyperheuristics.Psyfb2HH;
import com.g52aim.project.tsp.hyperheuristics.SR_IE_HH;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

@SuppressWarnings("unused")
public class ComparisonRunner {
	final HyFlexTestFrame config;
	final int[] INSTANCE_IDs;
	final long RUN_TIME;
	final long[] SEEDS;
	final int TOTAL_RUNS;
	
	public ComparisonRunner(HyFlexTestFrame config) {
		this.config = config;
		
		this.TOTAL_RUNS = config.getTotalRuns();
		this.INSTANCE_IDs = config.getInstanceIDs();
		this.SEEDS = config.getSeeds();
		this.RUN_TIME = config.getRunTime();
	}
	
	/**
	 * Compare SR_IE_HH with Psyfb2HH by
	 * running 11 trials (each 1 minute) for one of 3 tsp instances
	 * each trial has its own seed which SR_IE_HH and Psyfb2HH share for that trial
	 * then print and write to file f_best for SR_IE, f_best for psyfb2HH, trial number, instanceID
	 * @param instanceIndex
	 */
	public void runTests(int instanceIndex) {
		for(int i = 0; i < TOTAL_RUNS; i++) {
			long seed = SEEDS[i];
			ProblemDomain problem = new G52AIMTSP(seed);
			problem.loadInstance(INSTANCE_IDs[instanceIndex]);
			
			//HyperHeuristic sr_ie = new SR_IE_HH(seed);
			//sr_ie.setTimeLimit(RUN_TIME);
			//sr_ie.loadProblemDomain(problem);
			//sr_ie.run();
			
			//System.out.println(sr_ie.toString() + " f_best = " + sr_ie.getBestSolutionValue() + ", Trial = " + i + ", InstanceID = " + instanceIndex + "\n");
			// HH,Run Time,f_best,Trial,Instance ID
			//config.saveData("test.csv", sr_ie.toString() + "," + RUN_TIME + "," + sr_ie.getBestSolutionValue() + "," + i + "," + instanceIndex);
			
			HyperHeuristic psyfb2 = new Psyfb2HH(seed);
			psyfb2.setTimeLimit(RUN_TIME);
			psyfb2.loadProblemDomain(problem);
			psyfb2.run();
			
			System.out.println(psyfb2.toString() + " f_best = " + psyfb2.getBestSolutionValue() + ", Trial = " + i + ", InstanceID = " + instanceIndex + "\n");
			config.saveData("test.csv", psyfb2.toString() + "," + RUN_TIME + "," + psyfb2.getBestSolutionValue() + "," + i + "," + instanceIndex);
		}
	}
	
	public void runAllTests() {
		for(int instance = 0; instance < INSTANCE_IDs.length; instance++) {
			runTests(instance);
		}
	}
	
	public static void main(String[] args) {
		ComparisonRunner r = new ComparisonRunner(new HyFlexTestFrame());
		
		//r.runTests(0);
		//r.runTests(1);
		r.runTests(2);
		
		//r.runAllTests();
	}
}
