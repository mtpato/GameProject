package gameProject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;


/**
 * @author michaelpato
 *
 *
 *
 */
public class TileModel extends GameModel{

	Random rand = new Random();
	
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
		
		assignTiles(state, users);


		
	
		
		
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
		
		
		for(int i = 0; i < 7; i ++) {
			if(i % 2 == 0) {
				System.out.print("     ");
				for(int j = 0; j < 5; j++) {
					
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
				
				for(int j = 0; j < 6; j++) {
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
	
	private void assignTiles(TileGameState state, Set<Integer> users) {
		ArrayList<Integer> userList = new ArrayList<Integer>(users);
		
		int p;
		int aCount = 0;
		int bCount = 0;
		
		for(TileNode t: state.tiles.values()) {
			p = rand.nextInt(2);
			
			
			System.out.println(t.nodeID);
			
			if(aCount == state.tiles.size() / 2) {
				t.owner = userList.get(1);
				bCount++;
				
			} else if(aCount == state.tiles.size() / 2) {
				t.owner = userList.get(0);
				aCount++;
				
			} else if(p == 0) {
				t.owner = userList.get(0);
				aCount++;
				
			} else {
				t.owner = userList.get(1);
				bCount++;
			}
			
			
			
		}

		
		
	}
	
	private void createTiles(TileGameState state) {
		int n = 1;
		
		for(int i = 0; i < 7; i ++) {
			if(i % 2 == 0) {
				for(int j = 0; j < 5; j++) {
					state.tiles.put("" + i + "" + j, new TileNode(n + 10, i, j));
					n++;
					
				}
				
				
			} else {
				
				for(int j = 0; j < 6; j++) {
					state.tiles.put("" + i + "" + j, new TileNode(n + 10, i, j));
					n++;
					
				}
				
			}
			
			
		}
	}

	
	
}
