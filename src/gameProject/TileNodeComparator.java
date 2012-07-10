package gameProject;

import java.util.Comparator;

public class TileNodeComparator implements Comparator{

	@Override
	public int compare(Object arg0, Object arg1) {
		
		return ((TileNode) arg0).nodeID - ((TileNode) arg1).nodeID;
	}

}
