import java.util.Scanner;
//import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Tic-Tac-Toe: Two-player console version.
 * 
 * @author Erica Ram
 */
public class Game {
	
	// The game board and the game status
	public static String[] board = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
	public static int currentState;  // the current state of the game
	public static String currentPlayer; // the current player
	
	//String values containing the chosen value for each player
	public static String playerSymbol;
	public static String opponentSymbol;
	
	//potential win conditions, used for checking if the game is over
	public static int[][] winConditions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
	
	public static Scanner input = new Scanner(System.in); // the input Scanner
	
	/** 
	 * The entry main method (the program starts here) 
	 */
	public static void main(String[] args) {		
		// Initialize the game-board and current status
		System.out.println("Welcome! Let's play a game of Tic-Tac-Toe.");
		pickSymbols();
		initGame();
		printBoard();
		playGame();
		replay();
	}
	
	/**
	 * freshBoard() - clears the board for a replay
	 */
	public static void freshBoard()
	{
		board[0] = "0";
		board[1] = "1"; 
		board[2] = "2"; 
		board[3] = "3"; 
		board[4] = "4"; 
		board[5] = "5"; 
		board[6] = "6"; 
		board[7] = "7"; 
		board[8] = "8";
	}
	
	/**
	 * replay() - prompts the player to replay the game
	 */
	public static void replay()
	{
		boolean validInput = false;
		
		do	{
			System.out.print("Play again? (y/n)\n");	//prints this twice, have yet to figure out why
			String replay = input.nextLine();
			if(replay.equals("y") || replay.equals("Y"))	{
				freshBoard();
				pickSymbols();
				initGame();
				printBoard();
				playGame();
				validInput = true;
			}
			else if(replay.equals("n") || replay.equals("N"))	{
				System.out.println("Have a nice day!");
				validInput = true;
			}
		} while (!validInput);
	}
	
	public static void playGame()
	{
		do {
			getHumanSpot();
			if (!gameIsOver() && !tie()) {
				evalBoard();
			}
		} while (!gameIsOver() && !tie()); // repeat if not game-over
		
		if(tie())
			System.out.println("It's a tie!");
		
		System.out.println("Game over.\n");
	}
	
	/** 
	 * pickSymbols() - allows user to select a symbol for the 
	 * 				player and for the opponent.
	 */
	public static void pickSymbols() {
		boolean validInput = false;
		
		do {
			System.out.print("Enter a symbol for the player: ");
			String symbol = input.next();
			if (symbol.length() == 1) {
				playerSymbol = symbol;
				validInput = true;  // input okay, exit loop	
			}
		} while (!validInput);  // repeat until input is valid
		
		validInput = false;
		
		do {
			System.out.print("Enter a symbol for the opponent: ");
			String symbol = input.next();
			if (symbol.length() == 1 && !symbol.equals(playerSymbol)) {
				opponentSymbol = symbol;
				validInput = true;  // input okay, exit loop
			}
			else
				continue;
		} while (!validInput);  // repeat until input is valid
	}

	/** 
	 * Initializes the game
	 */
	public static void initGame() {
		currentState = 0; // "playing" or ready to play
		currentPlayer = playerSymbol;  // cross plays first
	}

	/**
	 * Returns the next player
	 * 
	 * @return String
	 */
	public static String nextPlayer() {
		if (currentPlayer == playerSymbol) {
			return opponentSymbol;
		} else {
			return playerSymbol;
		}
	}

	/** 
	 * Update global variables "board" and "currentPlayer". 
	 */
	public static void getHumanSpot() {
		int spot;
		boolean validInput = false;  // for input validation
		do {
			System.out.print("Enter [0-8]:\n");
			String scanIn = input.next();
			
			/* Resolves issue:
			 * "The game skips turn on bad input."
			 */
			try {
				spot = Integer.parseInt(scanIn);
			}
			catch (NumberFormatException e) {	//catches if input cannot be an int
				System.out.println("Error: Please input 0-8.");
				continue;
			}

			
			try {
				if (board[spot] != playerSymbol && board[spot] != opponentSymbol) {
					board[spot] = playerSymbol;  // update game-board content
					printBoard();
					validInput = true;  // input okay, exit loop
				}
			}
			catch (ArrayIndexOutOfBoundsException e) {	//catches if input < 0 or input > 8
				System.out.println("Error: Please input 0-8.");
				continue;
			}

			currentPlayer = nextPlayer();  // cross plays first
		} while (!validInput);  // repeat until input is valid
	}

	public static void evalBoard() {
		boolean foundSpot = false;
		do {
			if (board[4] == "4") {
				board[4] = opponentSymbol;
				foundSpot = true;
			} else {
				int spot = getBestMove();
				if (board[spot] != playerSymbol && board[spot] != opponentSymbol) {
					foundSpot = true;
					board[spot] = opponentSymbol;
				} else {
					foundSpot = false;
				}
			}
		} while (!foundSpot);
		printBoard();
	}

	/** Return true if the game was just won */
	public static boolean gameIsOver() {
		for ( int[] w : winConditions )           
        {
            if (board[w[0]] == playerSymbol
                && board[w[1]] == playerSymbol
                && board[w[2]] == playerSymbol)	{
                return true;
            }
            else if(board[w[0]] == opponentSymbol
                    && board[w[1]] == opponentSymbol
                    && board[w[2]] == opponentSymbol)	{
            	return true;
            }
        }
		return false;
	}


	public static int getBestMove() {
		ArrayList<String> availableSpaces = new ArrayList<String>();
		boolean foundBestMove = false;
		int spot = 100;
		for (String s: board) {
			if (s != playerSymbol && s != opponentSymbol) {
				availableSpaces.add(s);
			}
		}
		for (String as: availableSpaces) {
			spot = Integer.parseInt(as);
			board[spot] = opponentSymbol;
			if (gameIsOver()) {
				foundBestMove = true;
				board[spot] = as;
				return spot;
			} else {
				board[spot] = playerSymbol;
				if (gameIsOver()) {
					foundBestMove = true;
					board[spot] = as;
					return spot;
				} else {
					board[spot] = as;
				}
			}
		}
		if (foundBestMove) {
			return spot;
		} else {
			int n = ThreadLocalRandom.current().nextInt(0, availableSpaces.size());
			return Integer.parseInt(availableSpaces.get(n));
		}
	}

	/** 
	 * Return true if it is a draw (no more empty cells) 
	 */
	public static boolean tie() {
		/*
		 * "Easier" than the previous tie-checking method.
		 * For each loop runs through the board checking for spaces not containing 
		 * either the player's symbol or the opponent's symbol.
		 * (Previously returned whether any of the board positions were empty)
		 */
		for(String b : board)
		{
			if(!b.equals(playerSymbol) && !b.equals(opponentSymbol))
				return false;
		}
		return true;
	}

	/** 
	 * Print the game board 
	 */
	public static void printBoard() {
		System.out.println(" " + board[0] + " | " + board[1] + " | " + board[2] + "\n===+===+===\n" + " " + board[3] + " | " + board[4] + " | " + board[5] + "\n===+===+===\n" + " " + board[6] + " | " + board[7] + " | " + board[8] + "\n"); // print all the board cells
	}

}
