package pt.up.fe.ninemensmorris.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.up.fe.ninemensmorris.logic.Position;
import pt.up.fe.ninemensmorris.logic.Token;

public class PositionTests {

	@Test
	public void testConstructor() {
		Position pos = new Position(1);
		assertFalse(pos.isOccupied());
		assertEquals(1, pos.getPositionIndex());
		assertTrue(pos.getPlayerOccupyingIt() == Token.NO_PLAYER);
		assertNull(pos.getAdjacentPositionsIndexes());
	}
	
	@Test
	public void testOccupied() {
		Position pos = new Position(3);
		pos.setAsOccupied(Token.PLAYER_1);
		assertTrue(pos.getPlayerOccupyingIt() == Token.PLAYER_1);
		assertTrue(pos.isOccupied());
		pos.setAsUnoccupied();
		assertFalse(pos.isOccupied());
		assertEquals(Token.NO_PLAYER, pos.getPlayerOccupyingIt());
	}

	@Test
	public void testAdjacentPositions() {
		Position pos = new Position(2);
		pos.addAdjacentPositionsIndexes(1, 3);
		assertNotNull(pos.getAdjacentPositionsIndexes());
		assertTrue(pos.getAdjacentPositionsIndexes().length == 2);
		assertEquals(1, pos.getAdjacentPositionsIndexes()[0]);
	}
}
