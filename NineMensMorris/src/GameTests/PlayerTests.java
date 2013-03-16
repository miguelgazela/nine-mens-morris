package GameTests;

import static org.junit.Assert.*;
import org.junit.Test;

import GameLogic.HumanPlayer;
import GameLogic.Player;
import GameLogic.Player.InvalidPlayerId;

public class PlayerTests {
	@Test
	public void testConstructors() {
		try {
			Player p1 = new HumanPlayer("ExceptionExpected", 3);
		} catch (InvalidPlayerId e) {
			// expected
		}
	}
}
