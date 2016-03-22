
/**
 * Tic-Tac-Toe: Two-player console version.
 * 
 * @author Erica Ram
 */
public abstract class Game {
	
	/**
	 * rules() - returns the rules (to be printed)
	 */
	public abstract String rules();
	
	/**
	 * setup() - sets up for the game
	 */
	public abstract void setup();
	
	/**
	 * replay() - prompts the player to replay the game
	 */
	public abstract boolean replay();
	
	/**
	 * playGame() - play the game
	 */
	public abstract void playGame();


	/** 
	 * gameIsOver() - checks if the game is over
	 */
	public abstract boolean gameIsOver();

}
