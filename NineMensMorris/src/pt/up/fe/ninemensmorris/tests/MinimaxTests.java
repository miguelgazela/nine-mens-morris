package pt.up.fe.ninemensmorris.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.up.fe.ninemensmorris.logic.Board;
import pt.up.fe.ninemensmorris.logic.Game;
import pt.up.fe.ninemensmorris.logic.GameException;
import pt.up.fe.ninemensmorris.logic.IAPlayer;
import pt.up.fe.ninemensmorris.logic.MinimaxIAPlayer;
import pt.up.fe.ninemensmorris.logic.Move;
import pt.up.fe.ninemensmorris.logic.Token;

public class MinimaxTests {

	@Test
	public void testConstructor() {

		try {
			IAPlayer player = new MinimaxIAPlayer(Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER, 4);
			assertNotNull(player.getName());
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Test
	public void testPlacePiece() {

		try {
			IAPlayer player1 = new MinimaxIAPlayer(Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER, 4);
			IAPlayer player2 = new MinimaxIAPlayer(Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER, 4);
			Board board = new Board();
			int playIndex;
			assertTrue((playIndex = player1.getIndexToPlacePiece(board))>-1);
			board.setPositionAsPlayer(playIndex, Token.PLAYER_1);
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			board.incNumTotalPiecesPlaced();
			assertTrue((playIndex = player2.getIndexToPlacePiece(board))>-1);
			board.setPositionAsPlayer(playIndex, Token.PLAYER_2);
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	@Test
	public void testMovingPhase() {

		try {
			IAPlayer player1 = new MinimaxIAPlayer(Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER, 4);
			IAPlayer player2 = new MinimaxIAPlayer(Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER, 4);
			Board board = new Board();
			for(int i=0;i<9;i++) {
				int playIndex = player1.getIndexToPlacePiece(board);
				board.setPositionAsPlayer(playIndex, Token.PLAYER_1);
				board.incNumPiecesOfPlayer(Token.PLAYER_1);
				int removePiece;
				if((removePiece = player1.getIndexToRemovePieceOfOpponent(board))>-1 && (board.getPosition(removePiece).getPlayerOccupyingIt() == Token.PLAYER_2))
					{
						board.getPosition(removePiece).setAsUnoccupied();
						board.decNumPiecesOfPlayer(Token.PLAYER_2);
					}
				board.incNumTotalPiecesPlaced();
				
				playIndex = player2.getIndexToPlacePiece(board);
				board.setPositionAsPlayer(playIndex, Token.PLAYER_2);
				board.incNumPiecesOfPlayer(Token.PLAYER_2);
				if((removePiece = player2.getIndexToRemovePieceOfOpponent(board))>-1 && (board.getPosition(removePiece).getPlayerOccupyingIt() == Token.PLAYER_1))
				{
					board.getPosition(removePiece).setAsUnoccupied();
					board.decNumPiecesOfPlayer(Token.PLAYER_1);
				}
				board.incNumTotalPiecesPlaced();
			}
			
			assertSame(Game.MOVING_PHASE,((MinimaxIAPlayer) player1).getGamePhase(board, Token.PLAYER_1));
			
		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	@Test
	public void testGameOver() {

		try {
			IAPlayer player1 = new MinimaxIAPlayer(Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER, 4);
			Board board = new Board();
			for(int i=0;i<18;i++) {
				board.incNumTotalPiecesPlaced();
			}
			
			board.setPositionAsPlayer(0, Token.PLAYER_1);
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			board.setPositionAsPlayer(2, Token.PLAYER_1);
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			board.setPositionAsPlayer(4, Token.PLAYER_1);
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			board.setPositionAsPlayer(3, Token.PLAYER_2);
			board.incNumPiecesOfPlayer(Token.PLAYER_2);
			board.setPositionAsPlayer(5, Token.PLAYER_2);
			board.incNumPiecesOfPlayer(Token.PLAYER_2);
			board.setPositionAsPlayer(9, Token.PLAYER_2);
			board.incNumPiecesOfPlayer(Token.PLAYER_2);
			
			assertSame(Game.FLYING_PHASE,((MinimaxIAPlayer) player1).getGamePhase(board, Token.PLAYER_1));
			board.printBoard();
			Move move = player1.getPieceMove(board, Game.FLYING_PHASE);
			assertSame(4, move.srcIndex);
			assertSame(1, move.destIndex);
			assertTrue(move.removePieceOnIndex != -1);

		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
