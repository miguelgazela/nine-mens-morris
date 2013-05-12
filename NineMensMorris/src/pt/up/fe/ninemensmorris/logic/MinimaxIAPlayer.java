package pt.up.fe.ninemensmorris.logic;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.minlog.Log;

public class MinimaxIAPlayer extends IAPlayer {
	private int depth;
	private Token opponentPlayer;
	private int pieceToRemove;

	public int numberOfMoves = 0; // TODO TESTING
	public int movesThatRemove = 0;

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
		int minimax[] = minimax(playerToken,depth, gameBoard);
		pieceToRemove = minimax[3];
		return minimax[2];
	}

	@Override
	public int getIndexToRemovePieceOfOpponent(Board gameBoard) {
		return pieceToRemove;
	}

	@Override
	public Move getPieceMove(Board gameBoard, int gamePhase) throws GameException {
		numberOfMoves = 0; // TODO TESTING
		movesThatRemove = 0; // TODO TESTING
		int minimax[] = minimax(playerToken, depth, gameBoard);
		pieceToRemove = minimax[3];
		return new Move(minimax[1], minimax[2], minimax[3], Move.MOVING);
	}

	private int evaluate(Board gameBoard) {
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

	private  List<Move> generateMoves(Board gameBoard, Token player, int gamePhase) {
		List<Move> nextMoves = new ArrayList<Move>();
		boolean madeMill = false;

		try {
			if(gamePhase == Game.PLACING_PHASE) {
				for(int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) { // Search for empty cells and add to the List
					Move move = new Move(-1, -1, -1, Move.PLACING);
					Position position;

					if(!(position = gameBoard.getPosition(i)).isOccupied())	{
						position.setAsOccupied(player);
						move.destIndex = i;

						for(int k = 0; k < Board.NUM_MILL_COMBINATIONS; k++) { //check if piece made a mill
							int playerPieces = 0; 
							boolean selectedPiece = false;
							Position[] row = gameBoard.getMillCombination(k);

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
										nextMoves.add(move);
										movesThatRemove++; // TODO TESTING
									}
								}
							}
							selectedPiece = false;					
						}

						if(!madeMill) { // don't add repeated moves
							nextMoves.add(move);
						} else {
							madeMill = false;
						}
						position.setAsUnoccupied();
					}
				}
			} else if (gamePhase == Game.MOVING_PHASE) {
				/* por cada peca do jogador, verifica para onde se consegue mover e 
				 * adiciona esse movimento a lista. Se consegue remover uma peca do oponente
				 * gracas a esse movimento, adiciona a peca removida ao movimento e adiciona tbm a lista
				 */
				for(int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) {
					Position position = gameBoard.getPosition(i);

					if(position.getPlayerOccupyingIt() == player) {
						int[] adjacent = position.getAdjacentPositionsIndexes();

						for(int j = 0; j < adjacent.length; j++) {
							Move move = new Move(i, -1, -1, Move.MOVING);
							Position adjacentPos = gameBoard.getPosition(adjacent[j]);

							if(!adjacentPos.isOccupied()) {
								adjacentPos.setAsOccupied(player);
								move.destIndex = adjacent[j];
								position.setAsUnoccupied();
								// TODO THE NEXT BLOCK OF CODE IS DUPLICATED. MOVE IT TO A SEPARATE FUNCTION IF POSSIBLE

								for(int k = 0; k < Board.NUM_MILL_COMBINATIONS; k++) { //check if piece made a mill
									int playerPieces = 0; 
									boolean selectedPiece = false;
									Position[] row = gameBoard.getMillCombination(k);

									for(int l = 0; l < Board.NUM_POSITIONS_IN_EACH_MILL; l++) {
										if(row[l].getPlayerOccupyingIt() == player) {
											playerPieces++;
										}
										if(row[l].getPositionIndex() == move.destIndex) {
											selectedPiece = true;
										}
									}

									if(playerPieces == 3 && selectedPiece) { // made a mill - select piece to remove
										madeMill = true;
										for(int m = 0; m < Board.NUM_POSITIONS_OF_BOARD; m++) {
											Position pos = gameBoard.getPosition(m);
											if(pos.getPlayerOccupyingIt() != player && pos.getPlayerOccupyingIt() != Token.NO_PLAYER) {
												move.removePieceOnIndex = m;
												nextMoves.add(move); // add a move for each piece that can be removed, this way it will check what's the best one to remove
												movesThatRemove++; // TODO REMOVE
											}
										}
									}
									selectedPiece = false;					
								}

								if(!madeMill) { // don't add repeated moves
									nextMoves.add(move);
								} else {
									madeMill = false;
								}
								position.setAsOccupied(player);
								adjacentPos.setAsUnoccupied();
							}
						}
					}
				}
			} else if (gamePhase == Game.FLYING_PHASE) {
				/* por cada peca do jogador, verifica para onde se consegue mover e 
				 * adiciona esse movimento a lista. Se consegue remover uma peca do oponente
				 * gracas a esse movimento, adiciona a peca removida ao movimento e adiciona tbm a lista
				 */
				List<Integer> freeSpaces = new ArrayList<Integer>();
				List<Integer> playerSpaces = new ArrayList<Integer>();
				for(int i = 0; i < Board.NUM_POSITIONS_OF_BOARD; i++) {
					Position position = gameBoard.getPosition(i);

					if(position.getPlayerOccupyingIt() == player)
						playerSpaces.add(i);
					else if(!position.isOccupied())
						freeSpaces.add(i);
				}

				for(int n=0;n<playerSpaces.size();n++) {
					Position srcPos =  gameBoard.getPosition(playerSpaces.get(n));
					srcPos.setAsUnoccupied();
					for(int j = 0; j < freeSpaces.size(); j++) {
						Move move = new Move(srcPos.getPositionIndex(), -1, -1, Move.MOVING);
						Position destPos = gameBoard.getPosition(freeSpaces.get(j));

						destPos.setAsOccupied(player);
						move.destIndex = freeSpaces.get(j);
						
						// TODO THE NEXT BLOCK OF CODE IS DUPLICATED. MOVE IT TO A SEPARATE FUNCTION IF POSSIBLE

						for(int k = 0; k < Board.NUM_MILL_COMBINATIONS; k++) { //check if piece made a mill
							int playerPieces = 0; 
							boolean selectedPiece = false;
							Position[] row = gameBoard.getMillCombination(k);

							for(int l = 0; l < Board.NUM_POSITIONS_IN_EACH_MILL; l++) {
								if(row[l].getPlayerOccupyingIt() == player) {
									playerPieces++;
								}
								if(row[l].getPositionIndex() == move.destIndex) {
									selectedPiece = true;
								}
							}

							if(playerPieces==3 && selectedPiece) { // made a mill - select piece to remove
								madeMill = true;
								for(int m = 0; m < Board.NUM_POSITIONS_OF_BOARD; m++) {
									Position pos = gameBoard.getPosition(m);
									if(pos.getPlayerOccupyingIt() != player && pos.getPlayerOccupyingIt() != Token.NO_PLAYER) {
										move.removePieceOnIndex = m;
										nextMoves.add(move); // add a move for each piece that can be removed, this way it will check what's the best one to remove
										movesThatRemove++; // TODO TESTING
									}
								}
							}
							selectedPiece = false;					
						}

						if(!madeMill) { // don't add repeated moves
							nextMoves.add(move);
						} else {
							madeMill = false;
						}
						
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

	private int[] minimax(Token player, int depth,Board gameBoard) {

		int bestScore = (player == this.playerToken) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int currentScore = 0, bestPosDest = -1, bestPosSrc = -1, removePos = -1, gameOver;
		Token removedPlayer = Token.NO_PLAYER;
		List<Move> nextMoves = null;

		int gamePhase = getGamePhase(gameBoard, player);

		if (depth == 0) { // gameover or depth reached, evaluate score
			bestScore = evaluate(gameBoard);
		} else if((gameOver = checkGameOver(gameBoard,player)) != 0) {
			bestScore = gameOver;
		} else if((nextMoves = generateMoves(gameBoard, player, gamePhase)).isEmpty()) {
			bestScore = Integer.MIN_VALUE;
		}  else {
			numberOfMoves += nextMoves.size();

			try {
				for (Move move : nextMoves) {
					Position position = gameBoard.getPosition(move.destIndex);

					// Try this move for the current player
					position.setAsOccupied(player);

					if(gamePhase == Game.PLACING_PHASE)
						gameBoard.incNumPiecesOfPlayer(player);
					else
						gameBoard.getPosition(move.srcIndex).setAsUnoccupied();

					if(move.removePieceOnIndex != -1) { // this move removed a piece from opponent
						Position removed = gameBoard.getPosition(move.removePieceOnIndex);
						removedPlayer = removed.getPlayerOccupyingIt();
						removed.setAsUnoccupied();
						gameBoard.decNumPiecesOfPlayer(removedPlayer);
					}

					if (player == this.playerToken) {  // maximizing player
						currentScore = minimax(opponentPlayer, depth - 1, gameBoard)[0];
						if (currentScore > bestScore) {
							bestScore = currentScore;
							bestPosDest = move.destIndex;
							bestPosSrc = move.srcIndex;
							removePos = move.removePieceOnIndex;
						}
					} else {  //  minimizing player
						currentScore = minimax(this.playerToken, depth - 1, gameBoard)[0];
						if (currentScore < bestScore) {
							bestScore = currentScore;
							bestPosDest = move.destIndex;
							bestPosSrc = move.srcIndex;
							removePos = move.removePieceOnIndex;
						}
					}

					// Undo move
					position.setAsUnoccupied();

					if(gamePhase == Game.PLACING_PHASE)
						gameBoard.decNumPiecesOfPlayer(player);
					else
						gameBoard.getPosition(move.srcIndex).setAsOccupied(player);
					if(move.removePieceOnIndex != -1) {
						gameBoard.getPosition(move.removePieceOnIndex).setAsOccupied(removedPlayer);
						gameBoard.incNumPiecesOfPlayer(removedPlayer);
					}
				}
			} catch (GameException e) {
				e.printStackTrace();

				System.exit(-1);
			}
		}
		return new int[] {bestScore, bestPosSrc, bestPosDest, removePos};
	}

	public int getGamePhase(Board gameBoard, Token player) 
	{
		int gamePhase = Game.PLACING_PHASE;
		try {
			if(gameBoard.getNumTotalPiecesPlaced() == (Game.NUM_PIECES_PER_PLAYER * 2))
			{
				gamePhase = Game.MOVING_PHASE;
				if(gameBoard.getNumberOfPiecesOfPlayer(player)<=3)
					gamePhase = Game.FLYING_PHASE;
			}
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return gamePhase;		
	}

	private int checkGameOver(Board gameBoard, Token player) {
		if(gameBoard.getNumTotalPiecesPlaced() == (Game.NUM_PIECES_PER_PLAYER * 2))
		{
			Token oppPlayer;

			if(player==Token.PLAYER_1)
				oppPlayer = Token.PLAYER_2;
			else
				oppPlayer = Token.PLAYER_1;

			try {
				if(gameBoard.getNumberOfPiecesOfPlayer(player)<=Game.MIN_NUM_PIECES)
					return Integer.MIN_VALUE;
				else if(gameBoard.getNumberOfPiecesOfPlayer(oppPlayer)<=Game.MIN_NUM_PIECES)
					return Integer.MAX_VALUE;
				else
					return 0;
			} catch (GameException e) {
				e.printStackTrace();

			}
		}
		return 0;
	}
}
