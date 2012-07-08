package gameProject;

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
	protected abstract String makeMove(String state, String move);
	
	/**
	 * given the state of a game it returns if the game is over 
	 * 
	 * @param state
	 * @return whether the game is over 
	 */
	protected abstract boolean isOver(String state);
	

}
