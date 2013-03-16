package GameLogic;

public class HumanPlayer extends Player {
		
	public HumanPlayer(String name, int playerId) throws InvalidPlayerId {
		super(playerId);
		this.name = name;
	}

	@Override
	public boolean isIA() {
		return false;
	}
}
