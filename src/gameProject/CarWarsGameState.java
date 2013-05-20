package gameProject;

import java.util.HashMap;
import java.util.Set;


public class CarWarsGameState implements GameState{
	
	Set<Integer> players;
	int width;
	int height;
	int numVehicles;
	boolean over = false;
	
	HashMap<String,Vehicle> vehicles;
	HashMap<Integer,Integer> scores;
	
	
	
	public CarWarsGameState(Set<Integer> players, int height, int width,int numVehicles) {
		
		this.players=players;
		this.height = height;
		this.width = width;
		this.numVehicles=numVehicles;
		
		
		vehicles = new HashMap<String,Vehicle>();
		scores = new HashMap<Integer,Integer>();
		
	}
	
	
	
}
