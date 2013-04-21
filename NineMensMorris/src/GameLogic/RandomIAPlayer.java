package GameLogic;

public class RandomIAPlayer extends IAPlayer {

	public RandomIAPlayer(Token player, int numPiecesPerPlayer) throws GameException {
		super(player, numPiecesPerPlayer);
	}

	@Override
	public int getIndexToPlacePiece(Board gameBoard) {
		while(true) {
			int index = rand.nextInt(Board.NUM_POSITIONS_OF_BOARD);
			try {
				if(!gameBoard.getPosition(index).isOccupied()) {
					return index;
				}
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getIndexToRemovePieceOfOpponent(Board gameBoard) {
		while(true) {
			try {
				int index = rand.nextInt(Board.NUM_POSITIONS_OF_BOARD);
				Token playerOccupying = gameBoard.getPosition(index).getPlayerOccupyingIt();
				if(playerOccupying != Token.NO_PLAYER && playerOccupying != this.playerToken) {
					return index;
				}
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Move getPieceMove(Board gameBoard, int gamePhase) {
		while(true) {
			try {
				int srcIndex = rand.nextInt(Board.NUM_POSITIONS_OF_BOARD);
				Position position = gameBoard.getPosition(srcIndex);
				if(position.getPlayerOccupyingIt() == this.playerToken) {
					int[] adjacents = position.getAdjacentPositionsIndexes();
					for(int i = 0; i < adjacents.length; i++) {
						if(!gameBoard.getPosition(adjacents[i]).isOccupied()) {
							return new Move(srcIndex, adjacents[i], -1, Move.MOVING);
						}
					}
				}
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
	}

	
}
