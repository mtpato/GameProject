package gameProject;

import java.util.Set;

public abstract class GameModel {
	
	/**
	 * given a string representing the state of the game and a move to be made 
	 * this function makes the move and then returns a string that represents 
	 * the new state of the game 
	 * 
	 * @param state
	 * @param move
	 * @return the new state of the game after the move
	 */
	protected abstract String makeMove(GameState state, String move);
	
	/**
	 * given the state of a game it returns if the game is over 
	 * 
	 * @param state
	 * @return whether the game is over 
	 */
	protected abstract boolean isOver(GameState state);
	
	/**
	 * given the string that represents the state 
	 * this function creates the game state object for the game that is 
	 * being played
	 * 
	 * @param state
	 * @return the parsed GameState
	 */
	protected abstract GameState parseGameState(String s);
	
	/**
	 * given a game state it compresses it in the the string that represents 
	 * the state for storage and communication with the client
	 * 
	 * @param s
	 * @return String  the compress GameState
	 */
	protected abstract String compressGameState(GameState state);
	
	/**
	 * returns the userID of the player that won the game 
	 * 
	 * @param state
	 * @return the userID of the winner
	 */
	protected abstract int winner(GameState state);
	
	
	/**
	 * given a set of userIDs this function creates a game with those users
	 * 
	 * @param users
	 * @return the GameState
	 */
	protected abstract GameState createNewGame(Set<Integer> users);
	
	
	
	/**
	 * given a set of users and a game type (this is where things like difficulty 
	 * or level type or who knows what else could be specified). it creates a
	 * new game and returns the GameState object that represents that game;
	 * 
	 * @param users
	 * @param type
	 * @return the new GameState
	 */
	protected abstract GameState createNewGame(Set<Integer> users, String type);
	

}
