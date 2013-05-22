package pt.up.fe.ninemensmorris.logic;

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
						Position adjacentPos = gameBoard.getPosition(adjacents[i]);
						
						if(!adjacentPos.isOccupied()) {
							adjacentPos.setAsOccupied(playerToken);
							position.setAsUnoccupied();
							
							Move move = new Move(srcIndex, adjacents[i], -1, Move.MOVING);

							for(int p = 0; p < Board.NUM_MILL_COMBINATIONS; p++) { //check if piece made a mill
								int playerPieces = 0; 
								boolean selectedPiece = false;
								Position[] row = gameBoard.getMillCombination(p);

								for(int j = 0; j < Board.NUM_POSITIONS_IN_EACH_MILL; j++) {

									if(row[j].getPlayerOccupyingIt() == playerToken) {
										playerPieces++;
									}
									if(row[j].getPositionIndex() == move.destIndex) {
										selectedPiece = true;
									}
								}
								if(playerPieces==3 && selectedPiece) { // made a mill - select piece to remove
									move.removePieceOnIndex = getIndexToRemovePieceOfOpponent(gameBoard);
									break;
								}
							}
							position.setAsOccupied(playerToken);
							adjacentPos.setAsUnoccupied();
							return move;
						}
					}
				}
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
	}


}
