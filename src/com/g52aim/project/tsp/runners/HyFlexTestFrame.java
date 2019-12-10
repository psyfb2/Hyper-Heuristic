package com.g52aim.project.tsp.runners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class HyFlexTestFrame {
	
	protected final long[] SEEDS;

	// runs per instanceID for both hyper heuristics
	protected final int TOTAL_RUNS = 11;

	protected final long MILLISECONDS_IN_EACH_RUN = 60 * 1000; // 1 minute runs
	
	// IDs for T81, pcb1173, ch71009 
	protected final int[] instanceIDs = {10, 11, 5};

	public HyFlexTestFrame() {
		
		/*
		 * Generation of SEED values
		 */
		Random random = new Random(10022017L);
		SEEDS = new long[TOTAL_RUNS];
		
		for(int i = 0; i < TOTAL_RUNS; i++)
		{
			SEEDS[i] = random.nextLong();
		}
		
	}
	
	public int getTotalRuns() {
		return this.TOTAL_RUNS;
	}
	
	public long[] getSeeds() {
		return this.SEEDS;
	}
	
	
	public int[] getInstanceIDs() {
		return instanceIDs;
	}
	
	public long getRunTime() {
		return MILLISECONDS_IN_EACH_RUN;
	}
	
	public void saveData(String filePath, String data) {
		
		Path path = Paths.get("./" + filePath);
		if(!Files.exists(path)) {
			try {
				Files.createFile(path);
				
				//add header
				String header = "HH,Run Time,f_best,Trial,Instance ID";
				for(int i = 0; i < TOTAL_RUNS; i++) {
					
					header += ("," + i);
				}
				
				Files.write(path, (header + "\r\n" + data).getBytes());
				
			} catch (IOException e) {
				System.err.println("Could not create file at " + path.toAbsolutePath());
				System.err.println("Printing data to screen instead...");
				System.out.println(data);
			}
		} else {
			try {
				byte[] currentData = Files.readAllBytes(path);
				data = "\r\n" + data;
				byte[] newData = data.getBytes();
				byte[] writeData = new byte[currentData.length + newData.length];
				System.arraycopy(currentData, 0, writeData, 0, currentData.length);
				System.arraycopy(newData, 0, writeData, currentData.length, newData.length);
				Files.write(path, writeData);
				
			} catch (IOException e) {
				System.err.println("Could not create file at " + path.toAbsolutePath());
				System.err.println("Printing data to screen instead...");
				System.out.println(data);
			}
			
		}
	}
}
