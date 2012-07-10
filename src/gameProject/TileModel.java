package gameProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	protected GameState makeMove(GameState state, String move) {
		TileGameState s = (TileGameState) state;
		//NEED TO FIX LATER SEE COMMENT BELOW ON NEXT LINE
		TileNode moveNode = s.tiles.get(String.valueOf(Integer.valueOf(move) - 10));//=10 here because that is how it is displayed on the screen
		
		//make the move 
		moveNode.active = true;
		s.scores.put(moveNode.owner, s.scores.get(moveNode.owner) + 1);
		
		
		for(TileNode adj: moveNode.adj) {
			if(adj.active && adj.owner != moveNode.owner) {
				s.scores.put(adj.owner, s.scores.get(adj.owner) - 1);//update Score
				s.scores.put(moveNode.owner, s.scores.get(moveNode.owner) + 1);
				adj.owner = moveNode.owner;
			}
			
			
		}
		//update scores
		
		//check if the game is over 
		
		return s;
	}



	@Override
	protected boolean isOver(GameState state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected GameState parseGameState(String s) {
		
		HashMap<String,String> args = new HashMap<String,String>();
		
		String[] splitString = s.split(",");
		
		for(String line: splitString) {
			System.out.println(line);
			String[] temp = line.split("=");
			
			args.put(temp[0], temp[1]);
		}
		
		System.out.println(args);
		HashSet<Integer> players = new HashSet<Integer>();
		
		TileGameState state = new TileGameState(players, Integer.valueOf(args.get("h")), Integer.valueOf(args.get("w")));
		
		//System.out.println(state.height + " " + state.width);
		
		String temp = args.get("players");
		//System.out.println(temp);
		
		for(String line: temp.split("\\|")) {
			String[] splitTemp = line.split("-");
			//System.out.println(line);
			
			state.players.add(Integer.valueOf(splitTemp[0]));
			state.scores.put(Integer.valueOf(splitTemp[0]), Integer.valueOf(splitTemp[1]));
			
		}

		parseBoard(state, args.get("board"));

		
		return state;
	}

	private void parseBoard(TileGameState state, String s) {
		String[] splitBoard = s.split("\\|");
		
		
		
		//make the nodes
		for(String nodeString: splitBoard) {
			String[] nodeInfo = nodeString.split("-");
			
			TileNode n = new TileNode(Integer.valueOf(nodeInfo[0]), 
									  Integer.valueOf(nodeInfo[1]),
									  Integer.valueOf(nodeInfo[2]));
			n.owner = Integer.valueOf(nodeInfo[3]);
			
			if(nodeInfo[4].equals("0")) n.active = false;
			else n.active = true;
			
			state.tiles.put(String.valueOf(n.nodeID), n);
			
			
			
		}
		
		//add adjs
		for(String nodeString: splitBoard) {
			String[] nodeInfo = nodeString.split("-");
			
			String[] adjs = nodeInfo[5].split("\\.");
			
			//System.out.println("the length: " + adjs.length);
			TileNode n = state.tiles.get(nodeInfo[0]);
			for(String adj: adjs) {
				n.adj.add(state.tiles.get(adj));
			
			}
			
		}
		
	}

	/* (non-Javadoc)
	 * the string looks like this:
	 * players=userID-score|userID-score|userID-score...,h=height,w=width,board=node|node|node|node...
	 * 
	 * a node looks like this:
	 * nodeID-x-y-owner-active-adjList
	 * 
	 * an adjList looks like this:
	 * nodeID.nodeID.nodeID.nodeID...
	 * 
	 * example: 
	 * players=1-100|2-20,h=9,w=7,board=10-1-1-1-0-20.21.32.40|11-1-2-2-1-21.22.33.49....
	 * 
	 */
	@Override
	protected String compressGameState(GameState state) {
		TileGameState s = (TileGameState) state;
		
		StringBuilder buf = new StringBuilder();
		
		
		buf.append("players=");
		for(int p: s.players) {
			buf.append(p + "-" + s.scores.get(p) +"|");
			
		}
		buf.deleteCharAt(buf.length() - 1);
		
		buf.append(",h=" + s.height);
		buf.append(",w=" + s.width);

		
		
		buf.append(",board=");
		for(TileNode t: s.tiles.values()) {
			buf.append(t.nodeID + "-" + t.tileX + "-" + t.tileY + "-" +
					t.owner + "-");
			
			if(t.active) buf.append(1);
			else buf.append(0);
			
			buf.append("-");
			
			for(TileNode adj: t.adj) {
				buf.append(adj.nodeID + ".");
				
			}
			buf.deleteCharAt(buf.length() - 1);
			buf.append("|");
		}
		buf.deleteCharAt(buf.length() - 1);
		
		return buf.toString();
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
	
		for(int p: state.players) {
			state.scores.put(p, 0);//start with scores set to 0
		}
		
		createTiles(state);
		
		assignAdjs(state);
				
		assignTiles(state, users);
		
		
		return state;
	}
	
	
	
	
	private void assignAdjs(TileGameState state) {
		
		for (TileNode t : state.tiles.values()) {
			if (t.tileX == 0) {
				if (state.tiles.containsKey( "" + (t.tileY - 1))) {
					t.adj.add(state.tiles.get( "" + (t.tileY - 1)));
				}
				if (state.tiles.containsKey("" + (t.tileY + 1))) {
					t.adj.add(state.tiles.get("" + (t.tileY + 1)));
				}
				if (state.tiles.containsKey("" + (t.tileY - 2))) {
					t.adj.add(state.tiles.get("" + (t.tileY - 2)));
				}
				if (state.tiles.containsKey("" + (t.tileY + 2))) {
					t.adj.add(state.tiles.get("" + (t.tileY + 2)));
				}
				if (state.tiles.containsKey("" + (t.tileY - 1))) {
					t.adj.add(state.tiles.get( "" + (t.tileY - 1)));
				}
				if (state.tiles.containsKey("" + (t.tileY + 1))) {
					t.adj.add(state.tiles.get("" + (t.tileY + 1)));
				}
			} else {
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
					if(i ==0) {
						state.tiles.put( "" + j, new TileNode(i * 10 + j , j, i));
					} else {
						state.tiles.put("" + i + "" + j, new TileNode(i * 10 + j , j, i));
					}
					
					
					
					
				}
				
				
			} else {
				
				for(int j = 0; j < state.width * 2 - 1; j+=2) {
					
					
					state.tiles.put("" + i + "" + j, new TileNode(i * 10 + j , j, i));
					
					
				}
				
			}
			
			
		}
	}



	@Override
	protected GameState createNewGame(Set<Integer> users, String type) {
		// this is doing nothing right now just passing to the other 
		//signature 
		return createNewGame(users);
	}

	
	@Override
	protected void printState(GameState state) {
		TileGameState s = (TileGameState) state;
		
		
		//print the adj set sizes
		/*for(TileNode t: s.tiles.values()) {
			System.out.println(t.nodeID + ": " + t.adj.size());		
		}*/
		
		
		//print scores
		
		System.out.print("\n                    SCORES\n                ");
		
		for(int p: s.players) {
			System.out.print(p + ":" +s.scores.get(p) + "   ");
		}
		
		System.out.print("\n\n");
		
		//print the board
		for(int i = 0; i < s.height; i ++) {
			if(i % 2 == 0) {
				System.out.print("     ");
				for(int j = 1; j < s.width * 2 - 1; j+=2) {
					
					System.out.print("  ");
					
					if(i == 0) {
						System.out.print(s.tiles.get("" + j).nodeID + 10 +  ":" 
								+ s.tiles.get("" + j).owner + ":");
						
						if(s.tiles.get("" + j).active) {
							System.out.print("A");
						} else {
							System.out.print("n");
						}
						System.out.print("  ");
						
					} else {
						System.out.print(s.tiles.get(i + "" + j).nodeID + 10 +  ":" 
								+ s.tiles.get(i + "" + j).owner + ":");
						
						if(s.tiles.get(i + "" + j).active) {
							System.out.print("A");
						} else {
							System.out.print("n");
						}
						System.out.print("  ");
					}

				}
				
				
			} else {
				
				for(int j = 0; j < s.width * 2 - 1; j+=2) {
					System.out.print("  ");
					System.out.print(s.tiles.get(i + "" + j).nodeID + 10 + ":" + s.tiles.get(i + "" + j).owner + ":");
					
					
					if(s.tiles.get(i + "" + j).active) {
						System.out.print("A");
					} else {
						System.out.print("n");
					}
					System.out.print("  ");
					
					
				}
				
			}
			System.out.print("\n\n");
			
			
		}
		
		
	}

	@Override
	protected String whatGame() {
		
		return "tileGame";
	}
	


	
	
}
