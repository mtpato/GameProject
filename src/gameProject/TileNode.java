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
		this.nodeID = nodeID;
		tileState = -1;
		adj = new HashSet<TileNode>();
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nodeID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileNode other = (TileNode) obj;
		if (nodeID != other.nodeID)
			return false;
		return true;
	}
	
	
	
}
