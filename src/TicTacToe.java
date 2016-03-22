//import java.util.ArrayList;
import java.util.Scanner;
//import java.util.concurrent.ThreadLocalRandom;

public class TicTacToe extends Game {
	
	// The game board and the game status
	public static String[] board = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
	public static String currentPlayer; // the current player
	
	//String values containing the chosen value for each player
	public static String playerSymbol;
	public static String opponentSymbol;
	
	//potential win conditions, used for checking if the game is over
	public static int[][] winConditions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
	
	//used for checking for a win or a block
	public static int[][] twoInARow = {{0, 1}, {1, 2}, {3, 4}, {4, 5}, {6, 7}, {7, 8},	//horizontal two in a row
										{0, 3}, {3, 6}, {1, 4}, {4, 7}, {2, 5}, {5, 8},	//vertical two in a row 
										{0, 4}, {4, 8}, {2, 4}, {4, 6}};	//diagonal two in a row
	
	public static Scanner input = new Scanner(System.in); // the input Scanner
	
	/** 
	 * TicTacToe() - Constructor
	 * 
	 * Initializes the game (replacing initGame())
	 */
	public TicTacToe()
	{
		this.setup();
	}
	
	/**
	 * pickFirstPlayer() - asks user to choose which player goes first
	 */
	public static String pickFirstPlayer()
	{
		boolean validInput = false;
		
		do {
			System.out.print("Who goes first? (" + playerSymbol + "/" + opponentSymbol + ")");
			String first = input.next();
			if (first.equals(playerSymbol)) {
				validInput = true;
				return playerSymbol;
			}
			else if(first.equals(opponentSymbol)) {
				validInput = true;
				return opponentSymbol;
			}
		} while (!validInput);  // repeat until input is valid
		
		return playerSymbol;
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
	 * getPlayerSpot() - Update global variables "board" and "currentPlayer". 
	 */
	public void getPlayerSpot() {
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

			currentPlayer = nextPlayer(); 
		} while (!validInput);  // repeat until input is valid
	}
	
	/**
	 * evalBoard() - evaluates the board for the computer's move
	 */
	public void evalBoard() {
		boolean foundSpot = false;
		do {
			int spot = getBestMove();
			if (board[spot] != playerSymbol && board[spot] != opponentSymbol) {
				foundSpot = true;
				board[spot] = opponentSymbol;
			} else {
				foundSpot = false;
			}
		} while (!foundSpot);
		currentPlayer = nextPlayer();
		printBoard();
	}
	
	/**
	 * getBestMove() - uses strategy to determine the next best available move
	 * 
	 * @return int - position in the board for the select move
	 */
	public int getBestMove()  {
		/*
		 * provided code, has been replaced with new logic
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
		*/
		
		int spot = -1;	/* using -1 instead of throwing an exception
						 * because of how gameIsOver() and tie() will end 
						 * the game before -1 could be returned
						 * (Could possibly be redone with exceptions)
						 */
		
		
		if(checkWin() != -1)
		{
			spot = checkWin();
			System.out.println("checkWin() at " + spot);
		}
		else if(checkBlock() != -1)
		{
			spot = checkBlock();
			System.out.println("checkBlock() at " + spot);
		}
		else if(checkCenter())
		{
			spot = 4;
			System.out.println("checkCenter() at " + spot);
		}
		else if(checkOppositeCorner() != -1)
		{
			spot = checkOppositeCorner();
			System.out.println("checkOppositeCorner() at " + spot);
		}
		else if(checkEmptyCorner() != -1)
		{
			spot = checkEmptyCorner();
			System.out.println("checkEmptyCorner() at " + spot);
		}
		else if(checkEmptySide() != -1)	
		{
			spot = checkEmptySide();
			System.out.println("checkEmptySide() at " + spot);
		}
			
		if (spot != -1) {
			return spot;
		} else {
			/* provided code, has been replaced
			int n = ThreadLocalRandom.current().nextInt(0, availableSpaces.size());
			return Integer.parseInt(availableSpaces.get(n));
			*/
			System.out.println("No more moves!");
			return -1;
		}
	}
	
	/**
	 * checkWin() - checks if the computer has a move to win
	 * 
	 * @return int - position to win, -1 if no winning move
	 */
	public static int checkWin()
	{
		for ( int[] t : winConditions )           
        {
            if (board[t[0]] == opponentSymbol
                && board[t[1]] == opponentSymbol
                && isEmptySpace(t[2]))	{
                return t[2];
            }
            else if(isEmptySpace(t[0])
                    && board[t[1]] == opponentSymbol
                    && board[t[2]] == opponentSymbol)	{
            	return t[0];
            }
            else if(board[t[0]] == opponentSymbol
                    && isEmptySpace(t[1])
                    && board[t[2]] == opponentSymbol)	{
            	return t[1];
            }
        }
		return -1;
	}
	
	/**
	 * checkBlock() - checks if the computer can block a player win
	 * 
	 * @return int - position to block, -1 if no blocking move
	 */
	public static int checkBlock()
	{
		for ( int[] t : winConditions )           
        {
            if (board[t[0]] == playerSymbol
                && board[t[1]] == playerSymbol
                && isEmptySpace(t[2]))	{
                return t[2];
            }
            else if(isEmptySpace(t[0])
                    && board[t[1]] == playerSymbol
                    && board[t[2]] == playerSymbol)	{
            	return t[0];
            }
            else if(board[t[0]] == playerSymbol
                    && isEmptySpace(t[1])
                    && board[t[2]] == playerSymbol)	{
            	return t[1];
            }
        }
		return -1;
	}
	
	/**
	 * checkCenter() - checks if the center is an available move for computer
	 * 
	 * @return boolean - true if center is available, false if not
	 */
	public static boolean checkCenter()
	{
		return isEmptySpace(4);
	}
	
	/**
	 * checkOppositeCorner() - checks to see if a corner opposite the player is available
	 * 
	 * @return int - position in opposite corner, -1 if no opposite corners available
	 */
	public static int checkOppositeCorner()
	{
		if(board[0].equals(playerSymbol) && isEmptySpace(8))
			return 8;
		else if(board[2].equals(playerSymbol) && isEmptySpace(6))
			return 6;
		else if(board[8].equals(playerSymbol) && isEmptySpace(0))
			return 0;
		else if(board[6].equals(playerSymbol) && isEmptySpace(2))
			return 2;
		else
			return -1;
	}
	
	/**
	 * checkEmptyCorner() - checks for an empty corner position for the computer (0,2,6,8)
	 * 
	 * @return int - position in empty corner, -1 if no corners available
	 */
	public static int checkEmptyCorner()
	{
		if(isEmptySpace(0))
			return 0;
		else if(isEmptySpace(2))
			return 2;
		else if(isEmptySpace(6))
			return 6;
		else if(isEmptySpace(8))
			return 8;
		else
			return -1;
	}
	
	/**
	 * checkEmptySide() - checks for an empty side position for the computer (1,3,5,7)
	 * 
	 * @return int - position in empty side, -1 if no sides available
	 */
	public static int checkEmptySide()
	{
		if(isEmptySpace(1))
			return 1;
		else if(isEmptySpace(3))
			return 3;
		else if(isEmptySpace(5))
			return 5;
		else if(isEmptySpace(7))
			return 7;
		else
			return -1;
	}
	
	/**
	 * nextPlayer() - returns the next player
	 * 
	 * @return String - symbol of the player whose turn it is next
	 */
	public String nextPlayer() {
		if (currentPlayer == playerSymbol) {
			return opponentSymbol;
		} else {
			return playerSymbol;
		}
	}
	
	/** 
	 * tie() - return true if it is a draw (no more empty cells) 
	 */
	public boolean tie() {
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
	 * printBoard() - print the game board 
	 */
	public void printBoard() {
		System.out.println(" " + board[0] + " | " + board[1] + " | " + board[2] 
				+ "\n===+===+===\n" + " " + board[3] + " | " + board[4] + " | " 
				+ board[5] + "\n===+===+===\n" + " " + board[6] + " | " + board[7] 
				+ " | " + board[8] + "\n"); // print all the board cells
	}

	@Override
	/**
	 * rules() - returns the rules (to be printed)
	 */
	public String rules() {
		return "Rules:\nTic-tac-toe is a paper-and-pencil game for two players, who take\n"
				+ "turns marking the spaces in a 3×3 grid. The player who succeeds in placing\n"
				+ "three of their marks in a horizontal, vertical, or diagonal row wins the game.\n";
		
	}

	@Override
	/**
	 * setup() - sets up for TicTacToe by filling board positions with initial values ¨0¨-¨8¨
	 */
	public void setup() {
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
	 * isEmptySpace(int spot) - checks if a position in the board is empty
	 * 
	 * @param spot - position in board to check
	 * @return boolean - true if the position is empty, false otherwise
	 */
	public static boolean isEmptySpace(int spot)
	{
		if(board[spot].equals(playerSymbol) || board[spot].equals(opponentSymbol))
			return false;
		else
			return true;
	}

	@Override
	/**
	 * replay() - prompts the player to replay the game, processes replays
	 * 
	 * !!! currently issue with stopping after replaying 2+ times, GitHub Issue #2
	 */
	public boolean replay() {
		boolean validInput = false;
		System.out.print("Play again? (y/n)\n");
		do	{
			//System.out.print("Play again? (y/n)\n");	//prints this twice, have yet to figure out why
			String replay = input.nextLine();
			if(replay.equals("y") || replay.equals("Y"))	{
				this.setup();
				this.playGame();
				validInput = true;
				return true;
			}
			else if(replay.equals("n") || replay.equals("N"))	{
				System.out.println("Have a nice day!");
				validInput = true;
				return false;
			}
		} while (!validInput);
		return false;
	}

	@Override
	/**
	 * playGame() - play the game
	 */
	public void playGame() {
		pickSymbols();
		currentPlayer = pickFirstPlayer();
		do {
			do {
				if(currentPlayer.equals(playerSymbol))
					getPlayerSpot();
				else if(currentPlayer.equals(opponentSymbol))
					evalBoard();
			} while (!gameIsOver() && !tie()); // repeat if not game-over
			
			if(tie())
				System.out.println("It's a tie!");
			
			System.out.println("Game over.\n");
		} while(replay());
	}

	@Override
	/** 
	 * gameIsOver() - checks if the game is over
	 * 
	 * @return boolean - true if game is over, false otherwise
	 */
	public boolean gameIsOver() {
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
}
