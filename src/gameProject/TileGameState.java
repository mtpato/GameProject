package gameProject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TileGameState implements GameState {
	Set<Integer> players = new HashSet<Integer>();
	TileNode board;
	HashMap<String,TileNode> tiles;
	int width;
	int height;
	HashMap<Integer,Integer> scores;
	//HashMap<Integer,String> usersNames;
	boolean over = false;
	int turn;
	
	
	public TileGameState(Set<Integer> players, int height, int width) {
		this.players = players;
		this.height = height;
		this.width = width;
		
		//usersNames = new HashMap<Integer,String>();
		tiles = new HashMap<String,TileNode>();
		scores = new HashMap<Integer,Integer>();
		
	}
	
}
