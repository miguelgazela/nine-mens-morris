package GameLogic;

public class HumanPlayer extends Player {
		
	public HumanPlayer(String name, Token player, int numPiecesPerPlayer) throws GameException {
		super(player, numPiecesPerPlayer);
		this.name = name;
	}

	@Override
	public boolean isIA() {
		return false;
	}
}
