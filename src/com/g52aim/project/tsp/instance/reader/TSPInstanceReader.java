package com.g52aim.project.tsp.instance.reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import com.g52aim.project.tsp.instance.Location;
import com.g52aim.project.tsp.instance.TSPInstance;
import com.g52aim.project.tsp.interfaces.TSPInstanceInterface;
import com.g52aim.project.tsp.interfaces.TSPInstanceReaderInterface;


public class TSPInstanceReader implements TSPInstanceReaderInterface {

	@Override
	public TSPInstanceInterface readTSPInstance(Path path, Random random) {
		// this function reads a .tsp file and returns TSPInstance object 
		ArrayList<Location> locations = new ArrayList<Location>();
		BufferedReader tspFile;
		
		try {
			tspFile = Files.newBufferedReader(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		String line;
		String[] twoCoords;
		boolean dataReached = false;
		try {
			while((line = tspFile.readLine()) != null) {
				if(line.equals("NODE_COORD_SECTION")) {
					dataReached = true;
				}
				else if(dataReached && !line.equals("EOF")) {
					// remove duplicate spacing and also any initial spacing
					line = line.replaceAll("\\s+"," ").trim();
					twoCoords = line.split(" ");
					if(twoCoords.length == 3) {
						// ignore index
						locations.add(new Location(Double.parseDouble(twoCoords[1]), Double.parseDouble(twoCoords[2])));
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		Location[] locationsArr = locations.toArray(new Location[locations.size()]);
		return new TSPInstance(locations.size(), locationsArr, random);
	}
	
	public static void main(String args[]) {
		new TSPInstanceReader().readTSPInstance(Paths.get("./instances/tsp/d1291.tsp"), null);
	}
}
