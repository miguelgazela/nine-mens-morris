package pt.up.fe.ninemensmorris.logic;

import java.util.ArrayList;
import java.util.List;

public class MinimaxIAPlayer extends IAPlayer {
	private int depth;
	private Token opponentPlayer;
	private int pieceToRemove;
	private Move bestMove;

	public int numberOfMoves = 0; // TODO TESTING
	public int movesThatRemove = 0;
	public int bestScore = 0;

	public MinimaxIAPlayer(Token player, int numPiecesPerPlayer, int depth) throws GameException {
		super(player, numPiecesPerPlayer);
		if(depth < 1) {
			throw new GameException(""+getClass().getName()+" - Invalid Minimax Player Depth");
		}
		this.depth = depth;
		opponentPlayer = (player == Token.PLAYER_1) ? Token.PLAYER_2 : Token.PLAYER_1;
		pieceToRemove = -1;
	}

	@Override
	public int getIndexToPlacePiece(Board gameBoard) {
		numberOfMoves = 0; // TODO TESTING
		movesThatRemove = 0; // TODO TESTING
		try {
			bestMove = new Move(-1, -1, -1, Move.PLACING);
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		bestScore = minimax(playerToken,depth, gameBoard, Integer.MIN_VALUE, Integer.MAX_VALUE);
		//pieceToRemove = minimax[3];
		//return minimax[2];
		return bestMove.destIndex;
	}

	@Override
	public int getIndexToRemovePieceOfOpponent(Board gameBoard) {
		//return pieceToRemove;
		return bestMove.removePieceOnIndex;
	}

	@Override
	public Move getPieceMove(Board gameBoard, int gamePhase) throws GameException {
		numberOfMoves = 0; // TODO TESTING
		movesThatRemove = 0; // TODO TESTING
		try {
			bestMove = new Move(-1, -1, -1, Move.MOVING);
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		bestScore = minimax(playerToken, depth, gameBoard, Integer.MIN_VALUE, Integer.MAX_VALUE);
		//pieceToRemove = minimax[3];
		//return new Move(minimax[1], minimax[2], minimax[3], Move.MOVING);
		return bestMove;
	}

	private int evaluate(Board gameBoard, int gamePhase) throws GameException {
		int score = 0;
		for(int i = 0; i < Board.NUM_MILL_COMBINATIONS; i++) {
			int playerPieces = 0, emptyCells = 0, opponentPieces = 0;

			try {
				Position[] row = gameBoard.getMillCombination(i);
				for(int j = 0; j < Board.NUM_POSITIONS_IN_EACH_MILL; j++) {
					if(row[j].getPlayerOccupyingIt() == playerToken) {
						playerPieces++;
					} else if(row[j].getPlayerOccupyingIt() == Token.NO_PLAYER) {
						emptyCells++;
					} else { 
						opponentPieces++;
					}
				}
			} catch (GameException e) {
				e.printStackTrace();
			}

			if(playerPieces == 3) {
				score += 100;
			} else if(playerPieces == 2 && emptyCells == 1) {
				score += 10;
			} else if(playerPieces == 1 && emptyCells == 2) {
				score += 1;
			} else if(opponentPieces == 3) {
				score += -100;
			} else if(opponentPieces == 2 && emptyCells == 1) {
				score += -10;
			} else if(opponentPieces == 1 && emptyCells == 2) {
				score += -1;
			}
		}
		return score;
	}
	
	private void checkMove(Board gameBoard, Token player, List<Move> moves, Move move) throws GameException {
		boolean madeMill = false;
		
		for(int i = 0; i < Board.NUM_MILL_COMBINATIONS; i++) { //check if piece made a mill
			int playerPieces = 0; 
			boolean selectedPiece = false;
			Position[] row = gameBoard.getMillCombination(i);

			for(int j = 0; j < Board.NUM_POSITIONS_IN_EACH_MILL; j++) {

				if(row[j].getPlayerOccupyingIt() == player) {
					playerPieces++;
				}
				if(row[j].getPositionIndex() == move.destIndex) {
					selectedPiece = true;
				}
			}
			if(playerPieces==3 && selectedPiece) { // made a mill - select piece to remove
				madeMill = true;
				for(int l = 0; l < Board.NUM_POSITIONS_OF_BOARD; l++) {
					Position pos = gameBoard.getPosition(l);
					
					if(pos.getPlayerOccupyingIt() != player && pos.getPlayerOccupyingIt() != Token.NO_PLAYER) {
						move.removePieceOnIndex = l;

						// add a move for each piece that can be removed, this way it will check what's the best one to remove
						moves.add(move);
						movesThatRemove++; // TODO TESTING
					}
				}
			}
			selectedPiece = false;					
		}

		if(!madeMill) { // don't add repeated moves
			moves.add(move);
		} else {
			madeMill = false;
		}
	}

	private  List<Move> generateMoves(Board gameBoard, Token player, int gamePhase) {
		List<Move> nextMoves = new ArrayList<Move>();
		Position position, adjacentPos;
		boolean madeMill = false;

		try {
			if(gamePhase == Game.PLACING_PHASE) {
				for(int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) { // Search for empty cells and add to the List
					Move move = new Move(-1, -1, -1, Move.PLACING);

					if(!(position = gameBoard.getPosition(i)).isOccupied())	{
						position.setAsOccupied(player);
						move.destIndex = i;
						checkMove(gameBoard, player, nextMoves, move);
						position.setAsUnoccupied();
					}
				}
			} else if (gamePhase == Game.MOVING_PHASE) {
				for(int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) {

					if((position = gameBoard.getPosition(i)).getPlayerOccupyingIt() == player) { // for each piece of the player
						int[] adjacent = position.getAdjacentPositionsIndexes();

						for(int j = 0; j < adjacent.length; j++) { // check valid moves to adjacent positions
							Move move = new Move(i, -1, -1, Move.MOVING);
							adjacentPos = gameBoard.getPosition(adjacent[j]);

							if(!adjacentPos.isOccupied()) {
								adjacentPos.setAsOccupied(player);
								move.destIndex = adjacent[j];
								position.setAsUnoccupied();
								checkMove(gameBoard, player, nextMoves, move);
								position.setAsOccupied(player);
								adjacentPos.setAsUnoccupied();
							}
						}
					}
				}
			} else if (gamePhase == Game.FLYING_PHASE) {
				List<Integer> freeSpaces = new ArrayList<Integer>();
				List<Integer> playerSpaces = new ArrayList<Integer>();
				
				for(int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) {
					if((position = gameBoard.getPosition(i)).getPlayerOccupyingIt() == player) {
						playerSpaces.add(i);
					} else if(!position.isOccupied()) {
						freeSpaces.add(i);
					}
				}

				// for every piece the player has on the board
				for(int n = 0; n < playerSpaces.size(); n++) {
					Position srcPos =  gameBoard.getPosition(playerSpaces.get(n));
					srcPos.setAsUnoccupied();
					
					// each empty space is a valid move
					for(int j = 0; j < freeSpaces.size(); j++) {
						Move move = new Move(srcPos.getPositionIndex(), -1, -1, Move.MOVING);
						Position destPos = gameBoard.getPosition(freeSpaces.get(j));
						destPos.setAsOccupied(player);
						move.destIndex = freeSpaces.get(j);
						checkMove(gameBoard, player, nextMoves, move);
						destPos.setAsUnoccupied();
					}
					srcPos.setAsOccupied(player);
				}
			}
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return nextMoves;
	}

	public int minimax(Token player, int depth, Board gameBoard, int alpha, int beta) {

		int bestScore = (player == playerToken) ? (Integer.MIN_VALUE+1) : (Integer.MAX_VALUE-1);
		int currentScore = 0, bestPosDest = -1, bestPosSrc = -1, removePos = -1, gameOver;
		Token removedPlayer = Token.NO_PLAYER;
		List<Move> nextMoves = null;

		try {
			int gamePhase = getGamePhase(gameBoard, player);

			if (depth == 0) { // depth reached, evaluate score
				bestScore = evaluate(gameBoard, gamePhase);
			} else if((gameOver = checkGameOver(gameBoard)) != 0) { // gameover
				bestScore = gameOver;
			} else if((nextMoves = generateMoves(gameBoard, player, gamePhase)).isEmpty()) {
				if(player == playerToken) { // IT SHOULD RETURN DIFFERENT VALUES RIGHT? IF THE BOT DOESN'T HAVE ANY POSSIBLE MOVES, THEN THE PLAYER WINS, AND RETURNS MAX VALUE???
					bestScore = Integer.MIN_VALUE;
				} else {
					bestScore = Integer.MAX_VALUE;
				}
			}  else {
				numberOfMoves += nextMoves.size();


				for (Move move : nextMoves) {
					Position position = gameBoard.getPosition(move.destIndex);

					// Try this move for the current player
					position.setAsOccupied(player);

					if(gamePhase == Game.PLACING_PHASE) {
						gameBoard.incNumPiecesOfPlayer(player);
					} else {
						gameBoard.getPosition(move.srcIndex).setAsUnoccupied();
					}

					if(move.removePieceOnIndex != -1) { // this move removed a piece from opponent
						Position removed = gameBoard.getPosition(move.removePieceOnIndex);
						removedPlayer = removed.getPlayerOccupyingIt();
						removed.setAsUnoccupied();
						gameBoard.decNumPiecesOfPlayer(removedPlayer);
					}

					if (player == playerToken) {  // maximizing player
						currentScore = minimax(opponentPlayer, depth - 1, gameBoard, alpha, beta);
						if (currentScore > bestScore) {
							bestScore = currentScore;
							bestPosDest = move.destIndex;
							bestPosSrc = move.srcIndex;
							removePos = move.removePieceOnIndex;
						}
					} else {  //  minimizing player
						currentScore = minimax(this.playerToken, depth - 1, gameBoard, alpha, beta);
						if (currentScore < bestScore) {
							bestScore = currentScore;
							bestPosDest = move.destIndex;
							bestPosSrc = move.srcIndex;
							removePos = move.removePieceOnIndex;
						}
					}

					/*
					if(player == playerToken) { // maximizing player
						currentScore = minimax(opponentPlayer, depth-1, gameBoard, alpha, beta)[0];

						if(currentScore > alpha) { // we have found a better best move
							bestScore = currentScore;
							alpha = currentScore;
							bestPosDest = move.destIndex;
							bestPosSrc = move.srcIndex;
							removePos = move.removePieceOnIndex;
						}
						if(alpha >= beta) {
							break;
						}
					} else {
						currentScore = minimax(playerToken, depth-1, gameBoard, alpha, beta)[0];

						if(currentScore < beta) {
							bestScore = currentScore;
							beta = currentScore;
							bestPosDest = move.destIndex;
							bestPosSrc = move.srcIndex;
							removePos = move.removePieceOnIndex;
						}
						if(beta <= alpha) {
							break;
						}
					}
					 */

					// Undo move
					position.setAsUnoccupied();

					if(gamePhase == Game.PLACING_PHASE) {
						gameBoard.decNumPiecesOfPlayer(player);
					} else {
						gameBoard.getPosition(move.srcIndex).setAsOccupied(player);
					}

					if(move.removePieceOnIndex != -1) {
						gameBoard.getPosition(move.removePieceOnIndex).setAsOccupied(removedPlayer);
						gameBoard.incNumPiecesOfPlayer(removedPlayer);
					}
				}
			}
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		bestMove.destIndex = bestPosDest;
		bestMove.srcIndex = bestPosSrc;
		bestMove.removePieceOnIndex = removePos;
		return bestScore;
	}

	public int getGamePhase(Board gameBoard, Token player) 
	{
		int gamePhase = Game.PLACING_PHASE;
		try {
			if(gameBoard.getNumTotalPiecesPlaced() == (Game.NUM_PIECES_PER_PLAYER * 2))
			{
				gamePhase = Game.MOVING_PHASE;
				if(gameBoard.getNumberOfPiecesOfPlayer(player) <= 3) {
					gamePhase = Game.FLYING_PHASE;
				}
			}
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return gamePhase;		
	}

	private int checkGameOver(Board gameBoard) {
		if(gameBoard.getNumTotalPiecesPlaced() == (Game.NUM_PIECES_PER_PLAYER * 2))
		{
			try {
				if(gameBoard.getNumberOfPiecesOfPlayer(playerToken) <= Game.MIN_NUM_PIECES) {
					return Integer.MIN_VALUE;
				}
				else if(gameBoard.getNumberOfPiecesOfPlayer(opponentPlayer) <= Game.MIN_NUM_PIECES) {
					return Integer.MAX_VALUE;
				}
				else {
					/* THIS IS NOT NECESSARY RIGHT? THE GENERATE MOVES FUNCTION WILL RETURN AN EMPTY LIST RIGHT?
					boolean playerHasValidMove = false, opponentPlayerHasValidMove = false;
					Token player; 
					
					// check if each player has at least one valid move
					for(int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) {
						Position position = gameBoard.getPosition(i);
						if((player = position.getPlayerOccupyingIt()) != Token.NO_PLAYER) {
							int[] adjacent = position.getAdjacentPositionsIndexes();
							for(int j = 0; j < adjacent.length; j++) {
								Position adjacentPos = gameBoard.getPosition(adjacent[j]);
								if(!adjacentPos.isOccupied()) {
									if(!playerHasValidMove) { // must only change if boolean is false
										playerHasValidMove = (player == playerToken);
									}
									if(!opponentPlayerHasValidMove) {
										opponentPlayerHasValidMove = (player == opponentPlayer);
									}
									break;
								}
							}
						}
						
						if(playerHasValidMove && opponentPlayerHasValidMove) {
							return 0;
						} else if(!playerHasValidMove) {
							return Integer.MIN_VALUE;
						} else if(!opponentPlayerHasValidMove) {
							return Integer.MAX_VALUE;
						}
					}
					*/
					return 0;
				}
			} catch (GameException e) {
				e.printStackTrace();

			}
		}
		return 0;
	}
}
