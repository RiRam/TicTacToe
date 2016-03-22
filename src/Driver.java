
public class Driver {
	/** 
	 * Driver main method 
	 */
	public static void main(String[] args) {		
		// Initialize the game-board and current status
		System.out.println("Welcome! Let's play a game of Tic-Tac-Toe.");	//greeting
		
		TicTacToe game = new TicTacToe();	//create TicTacToe instance
		System.out.println(game.rules());	//print rules
		
		game.printBoard();
		game.playGame();
	}
}
