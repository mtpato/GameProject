package gameProject;

import java.util.Set;


/**
 * @author michaelpato
 *
 *
 *
 */
public class TileModel extends GameModel{

	@Override
	protected String makeMove(GameState state, String move) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isOver(GameState state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected GameState parseGameState(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String compressGameState(GameState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int winner(GameState state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected GameState createNewGame(Set<Integer> users) {
		System.out.println("CREATEING GAME ");
		
		TileGameState state = new TileGameState(users);
		
		
		
		
		createTiles(state);
		
		//next need to set up adjs for the tiles 
		
		//then GET THE RANDOM ASSIGNMENT OF TILES


		
	
		
		
		return state;
	}


	@Override
	protected GameState createNewGame(Set<Integer> users, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void printState(GameState state) {
		TileGameState s = (TileGameState) state;
		
		
		for(int i = 0; i < 5; i ++) {
			if(i % 2 == 0) {
				System.out.print("     ");
				for(int j = 0; j < 3; j++) {
					
					System.out.print("  ");
					System.out.print(s.tiles.get(i + "" + j).nodeID + ":" + s.tiles.get(i + "" + j).owner + ":");
					
					
					if(s.tiles.get(i + "" + j).active) {
						System.out.print("A");
					} else {
						System.out.print("O");
					}
					System.out.print("  ");
				}
				
				
			} else {
				
				for(int j = 0; j < 4; j++) {
					System.out.print("  ");
					System.out.print(s.tiles.get(i + "" + j).nodeID + ":" + s.tiles.get(i + "" + j).owner + ":");
					
					
					if(s.tiles.get(i + "" + j).active) {
						System.out.print("A");
					} else {
						System.out.print("O");
					}
					System.out.print("  ");
					
					
				}
				
			}
			System.out.print("\n\n");
			
			
		}
		
		
	}
	
	private void createTiles(TileGameState state) {
		int n = 1;
		
		for(int i = 0; i < 5; i ++) {
			if(i % 2 == 0) {
				for(int j = 0; j < 3; j++) {
					state.tiles.put("" + i + "" + j, new TileNode(n + 10, i, j));
					n++;
					
				}
				
				
			} else {
				
				for(int j = 0; j < 4; j++) {
					state.tiles.put("" + i + "" + j, new TileNode(n + 10, i, j));
					n++;
					
				}
				
			}
			
			
		}
	}

	
	
}
