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

	
}
