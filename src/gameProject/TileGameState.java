package gameProject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TileGameState implements GameState {
	Set<Integer> players = new HashSet<Integer>();
	TileNode board;
	HashMap<String,TileNode> tiles;
	
	
	public TileGameState(Set<Integer> players) {
		this.players = players;
		tiles = new HashMap<String,TileNode>();
	}
	
}
