import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

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
	 * TicTacToe - Constructor
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
	 * 
	 * @return
	 */
	public int getBestMove() {
		/*
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
		int spot = -1;
		
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
			/*
			int n = ThreadLocalRandom.current().nextInt(0, availableSpaces.size());
			return Integer.parseInt(availableSpaces.get(n));
			*/
			System.out.println("No more moves!");
			return -1;
		}
	}
	
	/**
	 * 
	 * @return
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
	 * 
	 * @return
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
	 * 
	 * @return
	 */
	public static boolean checkCenter()
	{
		return isEmptySpace(4);
	}
	
	/**
	 * 
	 * @return
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
	 * 
	 * @return
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
	 * @return String
	 */
	public String nextPlayer() {
		if (currentPlayer == playerSymbol) {
			return opponentSymbol;
		} else {
			return playerSymbol;
		}
	}
	
	/** 
	 * Return true if it is a draw (no more empty cells) 
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
	 * printBoard() - Print the game board 
	 */
	public void printBoard() {
		System.out.println(" " + board[0] + " | " + board[1] + " | " + board[2] 
				+ "\n===+===+===\n" + " " + board[3] + " | " + board[4] + " | " 
				+ board[5] + "\n===+===+===\n" + " " + board[6] + " | " + board[7] 
				+ " | " + board[8] + "\n"); // print all the board cells
	}

	@Override
	public String rules() {
		return "Rules:\nTic-tac-toe is a paper-and-pencil game for two players, who take\n"
				+ "turns marking the spaces in a 3×3 grid. The player who succeeds in placing\n"
				+ "three of their marks in a horizontal, vertical, or diagonal row wins the game.\n";
		
	}

	@Override
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
	
	public static boolean isEmptySpace(int spot)
	{
		if(board[spot].equals(playerSymbol) || board[spot].equals(opponentSymbol))
			return false;
		else
			return true;
	}

	@Override
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
