package pt.up.fe.ninemensmorris.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.up.fe.ninemensmorris.logic.Board;
import pt.up.fe.ninemensmorris.logic.GameException;
import pt.up.fe.ninemensmorris.logic.Position;
import pt.up.fe.ninemensmorris.logic.Token;

public class BoardTests {

	@Test
	public void testConstructor() {
		Board board = new Board();
		try {
			assertEquals(0, board.getNumberOfPiecesOfPlayer(Token.PLAYER_1));
			assertEquals(0, board.getNumberOfPiecesOfPlayer(Token.PLAYER_2));
			assertTrue(board.getNumTotalPiecesPlaced() == 0);
			assertTrue(board.positionIsAvailable(0));
			assertNotNull(board.getMillCombination(0));
		} catch(GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Test
	public void testPieces() {
		Board board = new Board();
		try {
			assertTrue(board.getNumTotalPiecesPlaced() == 0);
			board.incNumTotalPiecesPlaced();
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			assertFalse(board.getNumTotalPiecesPlaced() == 0);
			assertEquals(1, board.getNumberOfPiecesOfPlayer(Token.PLAYER_1));
			board.incNumPiecesOfPlayer(Token.NO_PLAYER);
			fail("The previous method should have sent an exception");
		} catch (GameException e) {
			// expected exception, do nothing
		}
	}
	
	@Test
	public void testMillCombinations() {
		Board board = new Board();
		try {
			Position[] millRow = board.getMillCombination(0);
			assertNotNull(millRow);
			assertEquals(0, millRow[0].getPositionIndex());
			assertEquals(1, millRow[1].getPositionIndex());
			assertEquals(2, millRow[2].getPositionIndex());
		} catch (GameException e) {
			e.printStackTrace();
		}
	}
}
