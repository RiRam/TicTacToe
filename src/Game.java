
/**
 * Tic-Tac-Toe: Two-player console version.
 * 
 * @author Erica Ram
 */
public abstract class Game {
	
	/**
	 * rules - prints the rules
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
	 * gameIsOver() - Return true if the game was just won 
	 */
	public abstract boolean gameIsOver();

}
