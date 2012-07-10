package gameProject;


import java.util.HashSet;

public class TileNode {
	int nodeID;
	int tileState;// -1 for empty or the userID for who has it
	int tileX;//just for testing
	int tileY;//just for testing 
	int owner = 1;
	boolean active = false;
	
	HashSet<TileNode> adj;
	
	public TileNode(int nodeID, int x, int y) {
		tileX = x;
		tileY = y;
		this.nodeID = nodeID;
		tileState = -1;
		adj = new HashSet<TileNode>();
		
	}


	
	
	
}
