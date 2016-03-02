
public class Driver {
	/** 
	 * The entry main method (the program starts here) 
	 */
	public static void main(String[] args) {		
		// Initialize the game-board and current status
		System.out.println("Welcome! Let's play a game of Tic-Tac-Toe.");
		TicTacToe.pickSymbols();
		TicTacToe game = new TicTacToe(TicTacToe.pickFirstPlayer());
		System.out.println(game.rules());
		game.printBoard();
		game.playGame();
		game.replay();
	}
}
