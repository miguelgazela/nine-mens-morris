package GameLogic;

public abstract class Game {
	
	static public final int NUM_PIECES_PER_PLAYER = 9;
	
	protected static final int MIN_NUM_PIECES = 2;
	static public final int PLACING_PHASE = 1;
	static public final int MOVING_PHASE = 2;
	static public final int FLYING_PHASE = 3;
	
	protected Board gameBoard;
	protected int gamePhase;
	protected boolean gameIsOver;
	
	public Game() {
		gameBoard = new Board();
		gamePhase = Game.PLACING_PHASE;
		gameIsOver = false;
	}
	
	public int getCurrentGamePhase() {
		return gamePhase;
	}
	
	public boolean positionIsAvailable(int boardIndex) throws GameException {
        return gameBoard.positionIsAvailable(boardIndex);
}
	
	public boolean validMove(int currentPositionIndex, int nextPositionIndex) throws GameException {
		Position currentPos = gameBoard.getPosition(currentPositionIndex);
		if(currentPos.isAdjacentToThis(nextPositionIndex) && !gameBoard.getPosition(nextPositionIndex).isOccupied()) {
			return true;
		}
		return false;
	}
	
	public void movePieceFromTo(int src, int dest, Token player) throws GameException {
		gameBoard.getPosition(src).setAsUnoccupied();
		gameBoard.getPosition(dest).setAsOccupied(player);
	}
	
	public boolean placePieceOfPlayer(int boardPosIndex, Token player) throws GameException {
		if(gameBoard.positionIsAvailable(boardPosIndex)) {
			gameBoard.getPosition(boardPosIndex).setAsOccupied(player);
			gameBoard.incNumPiecesOfPlayer(player);
			if(gameBoard.incNumTotalPiecesPlaced() == (NUM_PIECES_PER_PLAYER * 2)) {
				gamePhase = Game.MOVING_PHASE;
			}
			return true;
		}
		return false;
	}
	
	public boolean madeAMill(int dest, Token player) throws GameException {
		int maxNumPlayerPiecesInRow = 0;
		for(int i = 0; i < Board.NUM_MILL_COMBINATIONS; i++) {
			Position[] row = gameBoard.getMillCombination(i);
			for(int j = 0; j < Board.NUM_POSITIONS_IN_EACH_MILL; j++) {
				if(row[j].getPositionIndex() == dest) {
					int playerPiecesInThisRow = numPiecesFromPlayerInRow(row, player);
					if(playerPiecesInThisRow > maxNumPlayerPiecesInRow) {
						maxNumPlayerPiecesInRow = playerPiecesInThisRow;
					}
				}
			}
		}
		return (maxNumPlayerPiecesInRow == 3);
	}
	
	private int numPiecesFromPlayerInRow(Position[] pos, Token player) {
		int counter = 0;
		for(int i = 0; i < pos.length; i++) {
			if(pos[i].getPlayerOccupyingIt() == player) {
				counter++;
			}
		}
		return counter;
	}
	
	public boolean positionHasPieceOfPlayer(int boardIndex, Token player) throws GameException {
		return (gameBoard.getPosition(boardIndex).getPlayerOccupyingIt() == player);
	}
	
	public void printGameBoard() {
		gameBoard.printBoard();
	}

	public boolean gameIsOver() {
		return gameIsOver;
	}
	
	public abstract boolean removePiece(int index, Token playerId) throws GameException;
	public abstract void checkGameIsOver();
}
