package gameProject;

import java.util.ArrayList;
import java.util.Collections;
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
		TileNode moveNode = s.tiles.get(String.valueOf(Integer.valueOf(move)));
		
		
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

		
		//check if the game is over 
		
		if(isOver(s)) s.over = true;
		
		return s;
	}



	@Override
	protected boolean isOver(GameState state) {
		//here we get the win state we are still deciding what that is
		//for now it will be when all the tiles are active
		TileGameState s = (TileGameState) state;
		
		
		int totalScore = 0;
		
		for(int points: s.scores.values()) {
			totalScore = totalScore + points;
		}
		
		if(totalScore == s.tiles.size()){
			return true;
		}

		
		
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
		
		if(args.get("over").equals("0")) state.over = false;
		else state.over = true;

		
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
	 * players=userID-score|userID-score|userID-score...,h=height,w=width,board=node|node|node|node...,over=0or1
	 * 
	 * a node looks like this:
	 * nodeID-x-y-owner-active-adjList
	 * 
	 * an adjList looks like this:
	 * nodeID.nodeID.nodeID.nodeID...
	 * 
	 * example: 
	 * players=1-100|2-20,h=9,w=7,board=10-1-1-1-0-20.21.32.40|11-1-2-2-1-21.22.33.49....,over=0
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
		
		buf.append(",over=");
		
		if(s.over) buf.append(1);
		else buf.append(0);
		
		return buf.toString();
	}

	@Override
	protected int winner(GameState state) {
		TileGameState s = (TileGameState) state;
		
		
		int winner = -1;
		
		int max = -1;
		
		
		for(int player: s.scores.keySet()) {
			if(s.scores.get(player) > max) {
				winner = player;
			}
		}
		
		
		
		return winner;
	}

	@Override
	protected GameState createNewGame(Set<Integer> users) {
		System.out.println("CREATEING GAME ");
		
		TileGameState state = new TileGameState(users, 9, 9);
	
		for(int p: state.players) {
			state.scores.put(p, 0);//start with scores set to 0
		}
		
		createTiles(state);
		
		assignAdjs(state);
				
		assignTiles(state, users);
		
		
		return state;
	}
	
	
	
	
	private void assignAdjs(TileGameState state) {
		HashMap<String,TileNode> pointToNode = new HashMap<String,TileNode>();
		
		
		for(TileNode n: state.tiles.values()) {
			pointToNode.put(n.tileX + "," + n.tileY  , n);
		}
		
		
		
		
		for (TileNode n : pointToNode.values()) {
			
			checkNodeAddHash(n, n.tileX - 2, n.tileY + 0, pointToNode);
			checkNodeAddHash(n, n.tileX - 1, n.tileY - 1, pointToNode);
			checkNodeAddHash(n, n.tileX + 1, n.tileY - 1, pointToNode);
			checkNodeAddHash(n, n.tileX + 2, n.tileY + 0, pointToNode);
			checkNodeAddHash(n, n.tileX + 1, n.tileY + 1, pointToNode);
			checkNodeAddHash(n, n.tileX - 1, n.tileY + 1, pointToNode);
			

		
		}

			
			
			
	}
	
		
	

	private void checkNodeAddHash(TileNode n, int x, int y, HashMap<String, TileNode> hash) {
		if(hash.containsKey(x + "," + y)) {
			n.adj.add(hash.get(x + "," + y));
		}
		
	}



	/**
	 * this assigns 10 random tiles to the players 
	 * 
	 * 
	 * @param state
	 * @param users
	 */
	private void assignTiles(TileGameState state, Set<Integer> users) {
		ArrayList<Integer> userList = new ArrayList<Integer>(users);
		ArrayList<TileNode> tiles = new ArrayList<TileNode>(state.tiles.values());
		
		
		int startNum = 10; //the number of tiles each player gets at the start
		
		int numPlayers = userList.size();
		int p;
		
		
		for(int i = 0; i < startNum * numPlayers; i++) {
			p = rand.nextInt(tiles.size());
			
			TileNode t = tiles.remove(p);
			t.owner = userList.get(i % numPlayers);
	
		}
		
	}
	
	private void createTiles(TileGameState state) {
		int n = 0;
		
		
		
		for(int y = 0; y < state.height; y ++) {
			
			
			if(y % 2 == 0) {
				for(int x = 1; x < state.width; x += 2) {
					
					state.tiles.put( n + "", new TileNode(n, x, y));
					n++;
					
				}
			} else {
				for(int x = 0; x < state.width; x += 2) {
					
					state.tiles.put( n + "", new TileNode(n, x, y));
					n++;
					
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
		
		System.out.println("PRINTING STATE");
		TileGameState s = (TileGameState) state;
		
		ArrayList<TileNode> nodes = new ArrayList<TileNode>(s.tiles.values());
		
		Collections.sort(nodes, new TileNodeComparator());
		
		//print the adj set sizes
		for(TileNode t: nodes) {
			System.out.print(t.nodeID + ":x=" + t.tileX + ":y=" +t.tileY + ":");		
			
			for(TileNode tn: t.adj) {
				System.out.print(tn.nodeID + ".");
			}
			System.out.print("\n");
		}
		
		
		//print scores
		
		System.out.print("\n                    SCORES\n                  ");
		
		for(int p: s.players) {
			System.out.print(p + ":" +s.scores.get(p) + "   ");
		}
		
		
		
		System.out.print("\n\n");
		
		System.out.print("Game over: " + s.over + "\n");
		
		//print the board
		
		
		//int n = 0;
		
		
		for(TileNode n : nodes) {
			
			if(n.tileY % 2 == 0) {
				if(n.tileX == 1) {
					System.out.print("\n\n        ");
				}
				
			} 
			
			if(n.tileY % 2 == 1) {
				if(n.tileX == 0) {
					System.out.print("\n\n   ");
				}
				
			}
			
			
			if(n.nodeID < 10) {
				System.out.print("0");
			}
			
			System.out.print(n.nodeID + ":" + n.owner + ":");
			
			if(n.active) {
				System.out.print("A");
			} else {
				System.out.print("n");
			}
			
			System.out.print("    ");
			

		}
		
		System.out.print("\n\n");
		
		
		
	}

	@Override
	protected String whatGame() {
		
		return "tileGame";
	}
	


	
	
}
