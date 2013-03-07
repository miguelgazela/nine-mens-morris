package GameLogic;

import java.util.ArrayList;
import java.util.List;

public class MinimaxIAPlayer extends IAPlayer {
	private int depth;
	private int oppPlayer;
	public MinimaxIAPlayer(int playerId,int depth) {
		super(playerId);
		this.depth = depth;
		if(playerId==1)
			oppPlayer=2;
		else
			oppPlayer=1;
	}

	@Override
	public int getIndexToPlacePiece(Board gameBoard) {
		return minimax(playerId,depth,gameBoard)[1];
	}

	@Override
	public int getIndexToRemovePieceOfOpponent(Board gameBoard) {
		// TODO Auto-generated method stub
		return 0;
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
					if(gameBoard.winningPositions[i][j].playerOccupying == playerId) 
						playerPieces++;
					else if(gameBoard.winningPositions[i][j].playerOccupying == -1)
						emptyCells++;
					else opponentPieces++;
				}
				
				if(playerPieces == 3)
					score += 100;
				else if(playerPieces == 2 && emptyCells == 1)
					score += 10;
				else if(playerPieces == 1 && emptyCells == 2)
					score += 1;
				else if(opponentPieces == 3)
					score += -100;
				else if(opponentPieces == 2 && emptyCells == 1)
					score += -10;
				else if(opponentPieces == 1 && emptyCells == 2)
					score += -1;
			}
		 return score;
	 }
	 
	 private  List<Integer> generateMoves(Board gameBoard) {
		 List<Integer> nextMoves = new ArrayList<Integer>();
	 
	      // If gameover, i.e., no next move
	    //  if (hasWon(mySeed) || hasWon(oppSeed)) {
	       //  return nextMoves;   // return empty list
	     // }
	 
	      // Search for empty cells and add to the List
	     for(int i = 0; i < gameBoard.boardPositions.length; i++)  //falta para quando só dá para mover para uma adjacente
			if(!gameBoard.boardPositions[i].isOccupied)   
				nextMoves.add(i);
	   
	     return nextMoves;
	 }
	 
	 private int[] minimax(int player, int depth,Board gameBoard) {
		// Generate possible next moves in a List of Positions.
	      List<Integer> nextMoves = generateMoves(gameBoard);
	 
	     
	      int bestScore = (player == playerId) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
	      int currentScore;
	      int bestPos = -1;
	 
	      if (nextMoves.isEmpty() || depth == 0) {
	         // Gameover or depth reached, evaluate score
	         bestScore = evaluate(gameBoard);
	      } else {
	         for (int pos : nextMoves) {
	            // Try this move for the current "player"
	        	 gameBoard.boardPositions[pos].playerOccupying = player;
	        	 gameBoard.boardPositions[pos].isOccupied = true;
	            if (player == playerId) {  // maximizing player
	               currentScore = minimax(oppPlayer,depth - 1,gameBoard)[0];
	               if (currentScore > bestScore) {
	                  bestScore = currentScore;
	                  bestPos = pos;
	               }
	            } else {  //  minimizing player
	               currentScore = minimax(playerId,depth - 1,gameBoard)[0];
	               if (currentScore < bestScore) {
	                  bestScore = currentScore;
	                  bestPos = pos;
	               }
	            }
	            // Undo move
	            gameBoard.boardPositions[pos].isOccupied = false;
	            gameBoard.boardPositions[pos].playerOccupying = -1;
	         }
	      }
	      return new int[] {bestScore, bestPos};
	 }
}
