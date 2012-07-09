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
		
		TileGameState state = new TileGameState(users, 9, 5);
	
		
		createTiles(state);
		
		assignAdjs(state);
				
		assignTiles(state, users);
		
		
		return state;
	}
	
	
	
	
	private void assignAdjs(TileGameState state) {
		
		for (TileNode t : state.tiles.values()) {
			if (state.tiles.containsKey((t.tileX - 1) + "" + (t.tileY - 1))) {
				t.adj.add(state.tiles.get((t.tileX - 1) + "" + (t.tileY - 1)));
			}
			if (state.tiles.containsKey((t.tileX - 1) + "" + (t.tileY + 1))) {
				t.adj.add(state.tiles.get((t.tileX - 1) + "" + (t.tileY + 1)));
			}
			if (state.tiles.containsKey(t.tileX + "" + (t.tileY - 2))) {
				t.adj.add(state.tiles.get(t.tileX + "" + (t.tileY - 2)));
			}
			if (state.tiles.containsKey(t.tileX + "" + (t.tileY + 2))) {
				t.adj.add(state.tiles.get(t.tileX + "" + (t.tileY + 2)));
			}
			if (state.tiles.containsKey((t.tileX + 1) + "" + (t.tileY - 1))) {
				t.adj.add(state.tiles.get((t.tileX + 1) + "" + (t.tileY - 1)));
			}
			if (state.tiles.containsKey((t.tileX + 1) + "" + (t.tileY + 1))) {
				t.adj.add(state.tiles.get((t.tileX + 1) + "" + (t.tileY + 1)));
			}
		}

			
			
			
	}
	
		
	

	private void assignTiles(TileGameState state, Set<Integer> users) {
		ArrayList<Integer> userList = new ArrayList<Integer>(users);
		
		int p;
		int aCount = 0;
		int bCount = 0;
		
		for(TileNode t: state.tiles.values()) {
			p = rand.nextInt(2);
			
			
			//System.out.println(t.nodeID);
			
			if(aCount == state.tiles.size() / 2) {
				t.owner = userList.get(1);
				bCount++;
				
			} else if(bCount == state.tiles.size() / 2) {
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
		
		System.out.println("A: " + aCount);
		System.out.println("B: " + bCount);

		
		
	}
	
	private void createTiles(TileGameState state) {
		
		
		for(int i = 0; i < state.height; i ++) {
			if(i % 2 == 0) {
				for(int j = 1; j < state.width * 2 - 1; j+=2) {
					state.tiles.put("" + j + "" + i, new TileNode(i * 10 + j + 10, j, i));
					
					
				}
				
				
			} else {
				
				for(int j = 0; j < state.width * 2 - 1; j+=2) {
					state.tiles.put("" + j + "" + i, new TileNode(i * 10 + j + 10, j, i));
					
					
				}
				
			}
			
			
		}
	}



	@Override
	protected GameState createNewGame(Set<Integer> users, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	protected void printState(GameState state) {
		TileGameState s = (TileGameState) state;
		
		
		//print the adj set sizes
		for(TileNode t: s.tiles.values()) {
			System.out.println(t.nodeID + ": " + t.adj.size());		
		}
		
		
		
		//print the board
		for(int i = 0; i < s.height; i ++) {
			if(i % 2 == 0) {
				System.out.print("     ");
				for(int j = 1; j < s.width * 2 - 1; j+=2) {
					
					System.out.print("  ");
					System.out.print(s.tiles.get(i + "" + j).nodeID + ":" + s.tiles.get(i + "" + j).owner + ":");
					
					
					if(s.tiles.get(i + "" + j).active) {
						System.out.print("A");
					} else {
						System.out.print("N");
					}
					System.out.print("  ");
				}
				
				
			} else {
				
				for(int j = 0; j < s.width * 2 - 1; j+=2) {
					System.out.print("  ");
					System.out.print(s.tiles.get(i + "" + j).nodeID + ":" + s.tiles.get(i + "" + j).owner + ":");
					
					
					if(s.tiles.get(i + "" + j).active) {
						System.out.print("A");
					} else {
						System.out.print("N");
					}
					System.out.print("  ");
					
					
				}
				
			}
			System.out.print("\n\n");
			
			
		}
		
		
	}
	


	
	
}
