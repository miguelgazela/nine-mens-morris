package GameLogic;

import java.util.ArrayList;
import java.util.List;

public class MinimaxIAPlayer extends IAPlayer {
	private int depth;
	private int oppPlayer;
	private int pieceToRemove;
	public MinimaxIAPlayer(int playerId,int depth) {
		super(playerId);
		this.depth = depth;
		oppPlayer = (playerId == PLAYER_1) ? PLAYER_2 : PLAYER_1;
		pieceToRemove = -1;
	}

	@Override
	public int getIndexToPlacePiece(Board gameBoard) {
		int minimax[] = minimax(playerId,depth,gameBoard);
		pieceToRemove = minimax[2];
		return minimax[1];
	}

	@Override
	public int getIndexToRemovePieceOfOpponent(Board gameBoard) {
		return pieceToRemove;
	}

	@Override
	public Move getPieceMove(Board gameBoard) {
		// TODO Auto-generated method stub
		return null;
	}

	private int evaluate(Board gameBoard) {
		int score = 0;
		for(int i = 0; i < gameBoard.winningPositions.length; i++) {
			int playerPieces = 0, emptyCells = 0, opponentPieces = 0; 

			for(int j = 0; j < gameBoard.winningPositions[i].length; j++) {
				if(gameBoard.winningPositions[i][j].playerOccupying == playerId) {
					playerPieces++;
				} else if(gameBoard.winningPositions[i][j].playerOccupying == -1) {
					emptyCells++;
				} else opponentPieces++;
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

	private  List<int[]> generateMoves(Board gameBoard, int playerId) {
		List<int[]> nextMoves = new ArrayList<int[]>();

		// If gameover, i.e., no next move
		//  if (hasWon(mySeed) || hasWon(oppSeed)) {
		//  return nextMoves;   // return empty list
		// }

		
		for(int i = 0; i < gameBoard.boardPositions.length; i++) // Search for empty cells and add to the List
		{
			int move[]= {-1,-1};
			
			if(!gameBoard.boardPositions[i].isOccupied)   
				{
				gameBoard.boardPositions[i].playerOccupying = playerId;
				move[0] = i;
				
				for(int k = 0; k < gameBoard.winningPositions.length; k++) { //check if piece made a mill
					int playerPieces = 0; 
					boolean selectedPiece = false;
					for(int j = 0; j < gameBoard.winningPositions[k].length; j++) {
						
						if(gameBoard.winningPositions[k][j].playerOccupying == playerId) 
							playerPieces++;
						if(gameBoard.winningPositions[k][j].position == move[0])
							selectedPiece = true;
					}
					if(playerPieces==3 && selectedPiece) //made a mill - select piece to remove
					{
						for(int l = 0; l < gameBoard.boardPositions.length; l++)
							if(gameBoard.boardPositions[l].playerOccupying!=playerId && gameBoard.boardPositions[l].playerOccupying!=-1)
								{
									move[1] = l;
									nextMoves.add(move);
								}
					}
					else{
						nextMoves.add(move);
					}
					
					selectedPiece = false;					
				}
				gameBoard.boardPositions[i].playerOccupying = -1;
				}
		}
		return nextMoves;
	}

	private int[] minimax(int player, int depth,Board gameBoard) {
		List<int[]> nextMoves = generateMoves(gameBoard,player); // Generate possible next moves in a List of Positions.
		int bestScore = (player == playerId) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int currentScore = 0;
		int bestPos = -1;
		
		int removedPlayer = 0, removePos = -1;
		
		if (nextMoves.isEmpty() || depth == 0) { // Gameover or depth reached, evaluate score
			bestScore = evaluate(gameBoard);
		} else {
			for (int[] pos : nextMoves) {
				// Try this move for the current player
				gameBoard.boardPositions[pos[0]].playerOccupying = player;
				gameBoard.boardPositions[pos[0]].isOccupied = true;
				
				if(pos[1]!=-1) {
					removedPlayer = gameBoard.boardPositions[pos[1]].playerOccupying;
					gameBoard.boardPositions[pos[1]].playerOccupying = -1;
					gameBoard.boardPositions[pos[1]].isOccupied = false;
				}
					
				if (player == playerId) {  // maximizing player
					currentScore = minimax(oppPlayer,depth - 1,gameBoard)[0];
					if (currentScore > bestScore) {
						bestScore = currentScore;
						bestPos = pos[0];
						removePos = pos[1];
					}
				} else {  //  minimizing player
					currentScore = minimax(playerId,depth - 1,gameBoard)[0];
					if (currentScore < bestScore) {
						bestScore = currentScore;
						bestPos = pos[0];
						removePos = pos[1];
					}
				}
				
				// Undo move
				gameBoard.boardPositions[pos[0]].isOccupied = false;
				gameBoard.boardPositions[pos[0]].playerOccupying = Position.NO_PLAYER;
				if(pos[1]!=-1)
				{
					gameBoard.boardPositions[pos[1]].isOccupied = true;
					gameBoard.boardPositions[pos[1]].playerOccupying = removedPlayer;
				}
					
			}
		}
		return new int[] {bestScore, bestPos,removePos};
	}
}
