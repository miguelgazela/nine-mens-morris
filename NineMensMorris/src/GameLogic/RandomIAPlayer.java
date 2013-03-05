package GameLogic;

public class RandomIAPlayer extends IAPlayer {

	public RandomIAPlayer(int playerId) {
		super(playerId);
	}

	@Override
	public int getIndexToPlacePiece(Board gameBoard) {
		while(true) {
			int index = rand.nextInt(24);
			if(!gameBoard.boardPositions[index].isOccupied) {
				return index;
			}
		}
	}

	@Override
	public int getIndexToRemovePieceOfOpponent(Board gameBoard) {
		while(true) {
			int index = rand.nextInt(24);
			int playerOccupying = gameBoard.boardPositions[index].playerOccupying;
			if(playerOccupying != Position.NO_PLAYER && playerOccupying != this.playerId) {
				return index;
			}
		}
	}

	@Override
	public Move getPieceMove(Board gameBoard) {
		while(true) {
			int index = rand.nextInt(24);
			Position position = gameBoard.boardPositions[index];
			if(position.playerOccupying == this.playerId) {
				int[] adjacents = position.adjacentPositions;
				for(int i = 0; i < adjacents.length; i++) {
					if(!gameBoard.boardPositions[adjacents[i]].isOccupied) {
						return new Move(index, adjacents[i]);
					}
				}
			}
		}
	}

	
}
