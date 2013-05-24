package pt.up.fe.ninemensmorris.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.esotericsoftware.minlog.Log;

public class MinimaxIAPlayer extends IAPlayer {
	private int depth;
	private Token opponentPlayer;
	private Move currentBestMove;
	public int bestScore = 0;
	static final int maxScore = 1000000;
	public MinimaxIAPlayer(Token player, int numPiecesPerPlayer, int depth) throws GameException {
		super(player, numPiecesPerPlayer);
		if(depth < 1) {
			throw new GameException(""+getClass().getName()+" - Invalid Minimax Player Depth");
		}
		this.depth = depth;
		opponentPlayer = (player == Token.PLAYER_1) ? Token.PLAYER_2 : Token.PLAYER_1;
	}

	private void applyMove(Move move, Token player, Board gameBoard, int gamePhase) throws GameException {
		
		// Try this move for the current player
		Position position = gameBoard.getPosition(move.destIndex);
		position.setAsOccupied(player);
		
		if(gamePhase == Game.PLACING_PHASE) {
			gameBoard.incNumPiecesOfPlayer(player);
		} else {
			gameBoard.getPosition(move.srcIndex).setAsUnoccupied();
		}

		if(move.removePieceOnIndex != -1) { // this move removed a piece from opponent
			Position removed = gameBoard.getPosition(move.removePieceOnIndex);
			removed.setAsUnoccupied();
			gameBoard.decNumPiecesOfPlayer(getOpponentToken(player));
		}
	}
	
	private void undoMove(Move move, Token player, Board gameBoard, int gamePhase) throws GameException {
		// Undo move
		Position position = gameBoard.getPosition(move.destIndex);
		position.setAsUnoccupied();

		if(gamePhase == Game.PLACING_PHASE) {
			gameBoard.decNumPiecesOfPlayer(player);
		} else {
			gameBoard.getPosition(move.srcIndex).setAsOccupied(player);
		}

		if(move.removePieceOnIndex != -1) {
			Token opp = getOpponentToken(player);
			gameBoard.getPosition(move.removePieceOnIndex).setAsOccupied(opp);
			gameBoard.incNumPiecesOfPlayer(opp);
		}
	}
	
	private Token getOpponentToken(Token player) {
		if(player == playerToken) {
			return opponentPlayer;
		} else {
			return playerToken;
		}
	}
	
	@Override
	public int getIndexToPlacePiece(Board gameBoard) {
		numberOfMoves = 0; // TODO TESTING
		movesThatRemove = 0; // TODO TESTING

		try {
			List<Move> moves = generateMoves(gameBoard, playerToken, Game.PLACING_PHASE); // sorted already

			for(Move move : moves) {
				applyMove(move, playerToken, gameBoard, Game.PLACING_PHASE);
				move.score += alphaBeta(opponentPlayer, gameBoard, depth-1, Integer.MIN_VALUE, Integer.MAX_VALUE);
				undoMove(move, playerToken, gameBoard, Game.PLACING_PHASE);
			}
			
			Collections.sort(moves, new HeuristicComparatorMax());

			// if there are different moves with the same score it returns one of them randomly
			List<Move> bestMoves = new ArrayList<Move>();
			int bestScore = moves.get(0).score;
			bestMoves.add(moves.get(0));
			for(int i = 1; i < moves.size(); i++) {
				if(moves.get(i).score == bestScore) {
					bestMoves.add(moves.get(i));
				} else {
					break;
				}
			}
			currentBestMove = bestMoves.get(rand.nextInt(bestMoves.size()));
			return currentBestMove.destIndex;
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		Log.error("Should not get here");
		return -1;
	}

	@Override
	public int getIndexToRemovePieceOfOpponent(Board gameBoard) {
		return currentBestMove.removePieceOnIndex;
	}

	@Override
	public Move getPieceMove(Board gameBoard, int gamePhase) throws GameException {
		numberOfMoves = 0; // TODO TESTING
		movesThatRemove = 0; // TODO TESTING
		
		try {
			
			List<Move> moves = generateMoves(gameBoard, playerToken, getGamePhase(gameBoard, playerToken)); // sorted already

			for(Move move : moves) {
				applyMove(move, playerToken, gameBoard, Game.MOVING_PHASE);
				move.score += alphaBeta(opponentPlayer, gameBoard, depth-1, Integer.MIN_VALUE, Integer.MAX_VALUE);
				undoMove(move, playerToken, gameBoard, Game.MOVING_PHASE);
			}

			Collections.sort(moves, new HeuristicComparatorMax());

			// if there are different moves with the same score it returns one of them randomly
			List<Move> bestMoves = new ArrayList<Move>();
			int bestScore = moves.get(0).score;
			bestMoves.add(moves.get(0));
			for(int i = 1; i < moves.size(); i++) {
				if(moves.get(i).score == bestScore) {
					bestMoves.add(moves.get(i));
				} else {
					break;
				}
			}
			currentBestMove = bestMoves.get(rand.nextInt(bestMoves.size()));
			return currentBestMove;
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		Log.error("Should not get here");
		return null;
	}
	
	private int alphaBeta(Token player, Board gameBoard, int depth, int alpha, int beta) {

		int gameOver;
		List<Move> childMoves;

		try {
			int gamePhase = getGamePhase(gameBoard, player);

			if (depth == 0) { // depth reached, evaluate score
				return evaluate(gameBoard, gamePhase);
			} else if((gameOver = checkGameOver(gameBoard)) != 0) { // gameover
				return gameOver;
			} else if((childMoves = generateMoves(gameBoard, player, gamePhase)).isEmpty()) {
				if(player == playerToken) { // IT SHOULD RETURN DIFFERENT VALUES RIGHT? IF THE BOT DOESN'T HAVE ANY POSSIBLE MOVES, THEN THE PLAYER WINS, AND RETURNS MAX VALUE???
					return -maxScore;
				} else {
					return maxScore;
				}
			}  else {

				for (Move move : childMoves) {

					applyMove(move, player, gameBoard, gamePhase);

					if (player == playerToken) {  // maximizing player
						alpha = Math.max(alpha, alphaBeta(opponentPlayer, gameBoard, depth - 1, alpha, beta));

						if (beta <= alpha) {
							undoMove(move, player, gameBoard, gamePhase);
							break; // cutoff
						}
					} else {  //  minimizing player
						beta = Math.min(beta, alphaBeta(playerToken, gameBoard, depth - 1, alpha, beta));
						if (beta <= alpha) {
							undoMove(move, player, gameBoard, gamePhase);
							break; // cutoff
						}
					}
					undoMove(move, player, gameBoard, gamePhase);
				}

				if(player == playerToken) {
					return alpha;
				} else {
					return beta;
				}
			}
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		Log.error("SHOULD NOT GET HERE!");
		return -1;
	}

	private int evaluate(Board gameBoard, int gamePhase) throws GameException {
		int score = 0;
		int R1_numPlayerMills = 0, R1_numOppMills = 0;
		int R2_numPlayerTwoPieceConf = 0, R2_numOppTwoPieceConf = 0;

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
				R1_numPlayerMills++;
			} else if(playerPieces == 2 && emptyCells == 1) {
				R2_numPlayerTwoPieceConf++;
			} else if(playerPieces == 1 && emptyCells == 2) {
				score += 1;
			} else if(opponentPieces == 3) {
				R1_numOppMills++;
			} else if(opponentPieces == 2 && emptyCells == 1) {
				R2_numOppTwoPieceConf++;
			} else if(opponentPieces == 1 && emptyCells == 2) {
				score += -1;
			}
			
			Token playerInPos = gameBoard.getPosition(i).getPlayerOccupyingIt();
			if(i == 4 || i == 10 || i == 13 || i == 19) {
				if(playerInPos == playerToken) {
					score += 2;
				} else if(playerInPos != Token.NO_PLAYER) {
					score -= 2;
				}
			} else if(i == 1 || i == 9 || i == 14 || i == 22
						|| i == 7 || i == 11 || i == 12 || i == 16) {
				if(playerInPos == playerToken) {
					score += 1;
				} else if(playerInPos != Token.NO_PLAYER) {
					score -= 1;
				}
			}
		}

		/**
		 * Version 0.1
		 * Depth: 2, MAX_MOVES: 100 => 53% win vs 6% random win
		 * Depth: 3, MAX_MOVES: 100 => 82% win vs 0% random win
		 */
		//		score += 100*R1_numPlayerMills + 10*R2_numPlayerTwoPieceConf;
		//		score -= 100*R1_numOppMills + 10*R2_numOppTwoPieceConf;
		//		score += 10*R2_numPlayerTwoPieceConf;
		//		score -= 10*R2_numOppTwoPieceConf;

		/**
		 * Version 0.2
		 * Depth: 2, MAX_MOVES: 100 => 57% win vs 5% random win
		 * Depth: 3, MAX_MOVES: 100 => 83% win vs 0% random win
		 * Depth: 4, MAX_MOVES: 100 => 91% win vs 0% random win
		 */
		int coef;
		// number of mills
		if(gamePhase == Game.PLACING_PHASE) {
			coef = 80;
		} else if(gamePhase == Game.MOVING_PHASE) {
			coef = 120;
		} else {
			coef = 180;
		}
		score += coef*R1_numPlayerMills;
		score -= coef*R1_numOppMills;

		// number of pieces
		if(gamePhase == Game.PLACING_PHASE) {
			coef = 10;
		} else if(gamePhase == Game.MOVING_PHASE) {
			coef = 8;
		} else {
			coef = 6;
		}
		score += coef*gameBoard.getNumberOfPiecesOfPlayer(playerToken);
		score -= coef*gameBoard.getNumberOfPiecesOfPlayer(opponentPlayer);

		// number of 2 pieces and 1 free spot configuration
		if(gamePhase == Game.PLACING_PHASE) {
			coef = 12;
		} else {
			coef = 10;
		}
		score += coef*R2_numPlayerTwoPieceConf;
		score -= coef*R2_numOppTwoPieceConf;
		
		if(gamePhase == Game.PLACING_PHASE) {
			coef = 10;
		} else {
			coef = 25;
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

			if(playerPieces == 3 && selectedPiece) { // made a mill - select piece to remove
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

	private  List<Move> generateMoves(Board gameBoard, Token player, int gamePhase) throws GameException {
		List<Move> moves = new ArrayList<Move>();
		Position position, adjacentPos;

		try {
			if(gamePhase == Game.PLACING_PHASE) {
				for(int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) { // Search for empty cells and add to the List
					Move move = new Move(-7, -1, -1, Move.PLACING);

					if(!(position = gameBoard.getPosition(i)).isOccupied())	{
						position.setAsOccupied(player);
						move.destIndex = i;
						checkMove(gameBoard, player, moves, move);
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
								checkMove(gameBoard, player, moves, move);
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
						checkMove(gameBoard, player, moves, move);
						destPos.setAsUnoccupied();
					}
					srcPos.setAsOccupied(player);
				}
			}
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		/**
		 * => V.0.2
		 */
		// if depth > 3, rate the moves and sort them.
		// When depth is 3 or less, this overhead doesn't compensate the time lost
		if(depth > 3) {
			for(Move move : moves) {
				Token removedPlayer = Token.NO_PLAYER;
				position = gameBoard.getPosition(move.destIndex);

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

				move.score = evaluate(gameBoard, gamePhase);

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

			if(player == playerToken) {
				Collections.sort(moves, new HeuristicComparatorMax());
			} else {
				Collections.sort(moves, new HeuristicComparatorMin());
			}
		}

		/**
		 * V.0.2 <=
		 */
		numberOfMoves += moves.size();
		return moves;
	}

	private class HeuristicComparatorMax implements Comparator<Move> {

		@Override
		public int compare(Move t, Move t1) {
			return t1.score - t.score;
		}
	}

	private class HeuristicComparatorMin implements Comparator<Move> {

		@Override
		public int compare(Move t, Move t1) {
			return t.score - t1.score;
		}
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
					return -maxScore;
				}
				else if(gameBoard.getNumberOfPiecesOfPlayer(opponentPlayer) <= Game.MIN_NUM_PIECES) {
					return maxScore;
				}
				else {
					return 0;
				}
			} catch (GameException e) {
				e.printStackTrace();

			}
		}
		return 0;
	}
}
