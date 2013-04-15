package GameLogic;

import java.util.ArrayList;
import java.util.List;

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
		int minimax[] = minimax(playerToken,depth,gameBoard, Game.PLACING_PHASE);
		pieceToRemove = minimax[3];
		return minimax[2];
	}

	@Override
	public int getIndexToRemovePieceOfOpponent(Board gameBoard) {
		return pieceToRemove;
	}

	@Override
	public Move getPieceMove(Board gameBoard, int gamePhase) {
		numberOfMoves = 0; // TODO TESTING
		movesThatRemove = 0; // TODO TESTING
		int minimax[] = minimax(playerToken, depth, gameBoard, gamePhase);
		pieceToRemove = minimax[3];
		return new Move(minimax[1], minimax[2], minimax[3]);
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
			
			// TODO Don't we need to evaluate other situations?
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
				Move move = new Move(-1, -1, -1);
				Position position;
				
				if(!(position = gameBoard.getPosition(i)).isOccupied())	{
					position.setAsOccupied(player);
					move.dest = i;

					for(int k = 0; k < Board.NUM_MILL_COMBINATIONS; k++) { //check if piece made a mill
						int playerPieces = 0; 
						boolean selectedPiece = false;
						Position[] row = gameBoard.getMillCombination(k);
						
						for(int j = 0; j < Board.NUM_POSITIONS_IN_EACH_MILL; j++) {
							
							if(row[j].getPlayerOccupyingIt() == player) {
								playerPieces++;
							}
							if(row[j].getPositionIndex() == move.dest) {
								selectedPiece = true;
							}
						}
						if(playerPieces==3 && selectedPiece) { // made a mill - select piece to remove
							madeMill = true;
							for(int l = 0; l < Board.NUM_POSITIONS_OF_BOARD; l++) {
								Position pos = gameBoard.getPosition(l);
								if(pos.getPlayerOccupyingIt() != player && pos.getPlayerOccupyingIt() != Token.NO_PLAYER) {
									move.remove = l; // index of piece to remove
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
						Move move = new Move(i, -1, -1);
						Position adjacentPos = gameBoard.getPosition(adjacent[j]);
						
						if(!adjacentPos.isOccupied()) {
							adjacentPos.setAsOccupied(player);
							move.dest = adjacent[j];
							
							// TODO THE NEXT BLOCK OF CODE IS DUPLICATED. MOVE IT TO A SEPARATE FUNCTION IF POSSIBLE
							
							for(int k = 0; k < Board.NUM_POSITIONS_OF_BOARD; k++) { //check if piece made a mill
								int playerPieces = 0; 
								boolean selectedPiece = false;
								Position[] row = gameBoard.getMillCombination(k);

								for(int l = 0; l < Board.NUM_POSITIONS_IN_EACH_MILL; l++) {
									if(row[l].getPlayerOccupyingIt() == player) {
										playerPieces++;
									}
									if(row[l].getPositionIndex() == move.dest) {
										selectedPiece = true;
									}
								}
								
								if(playerPieces==3 && selectedPiece) { // made a mill - select piece to remove
									madeMill = true;
									for(int l = 0; l < Board.NUM_POSITIONS_OF_BOARD; l++) {
										Position pos = gameBoard.getPosition(l);
										if(pos.getPlayerOccupyingIt() != player && pos.getPlayerOccupyingIt() != Token.NO_PLAYER) {
											move.remove = l; // index of piece to remove
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
							position.setAsUnoccupied();
						}
					}
				}
			}
		}
		} catch (GameException e) {
			e.printStackTrace();
		}
		return nextMoves;
	}

	private int[] minimax(Token player, int depth,Board gameBoard, int gamePhase) {
		
		int bestScore = (player == this.playerToken) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int currentScore = 0, bestPosDest = -1, bestPosSrc = -1, removePos = -1;
		Token removedPlayer = Token.NO_PLAYER;
		List<Move> nextMoves;

		if (depth == 0 || (nextMoves = generateMoves(gameBoard, player, gamePhase)).isEmpty()) { // gameover or depth reached, evaluate score
			bestScore = evaluate(gameBoard);
		} else {
			numberOfMoves += nextMoves.size();
			
			try {
				for (Move move : nextMoves) {
					Position position = gameBoard.getPosition(move.dest);

					// Try this move for the current player
					position.setAsOccupied(player);

					if(move.remove != -1) { // this move removed a piece from opponent
						Position removed = gameBoard.getPosition(move.remove);
						removedPlayer = removed.getPlayerOccupyingIt();
						removed.setAsUnoccupied();
					}

					if (player == this.playerToken) {  // maximizing player
						currentScore = minimax(opponentPlayer, depth - 1,gameBoard, gamePhase)[0];
						if (currentScore > bestScore) {
							bestScore = currentScore;
							bestPosDest = move.dest;
							bestPosSrc = move.src;
							removePos = move.remove;
						}
					} else {  //  minimizing player
						currentScore = minimax(this.playerToken, depth - 1,gameBoard, gamePhase)[0];
						if (currentScore < bestScore) {
							bestScore = currentScore;
							bestPosDest = move.dest;
							bestPosSrc = move.src;
							removePos = move.remove;
						}
					}

					// Undo move
					position.setAsUnoccupied();
					if(move.remove != -1) {
						gameBoard.getPosition(move.remove).setAsOccupied(removedPlayer);
					}
				}
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
		return new int[] {bestScore, bestPosSrc, bestPosDest, removePos};
	}
}
