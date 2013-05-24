package pt.up.fe.ninemensmorris.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.up.fe.ninemensmorris.logic.Board;
import pt.up.fe.ninemensmorris.logic.Game;
import pt.up.fe.ninemensmorris.logic.GameException;
import pt.up.fe.ninemensmorris.logic.IAPlayer;
import pt.up.fe.ninemensmorris.logic.MinimaxIAPlayer;
import pt.up.fe.ninemensmorris.logic.Move;
import pt.up.fe.ninemensmorris.logic.Position;
import pt.up.fe.ninemensmorris.logic.Token;

public class MinimaxTests {
//
//	@Test
//	public void testConstructor() {
//
//		try {
//			IAPlayer player = new MinimaxIAPlayer(Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER, 4);
//			assertNotNull(player.getName());
//		} catch (GameException e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
//	}
//
//	@Test
//	public void testPlacePiece() {
//
//		try {
//			IAPlayer player1 = new MinimaxIAPlayer(Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER, 4);
//			IAPlayer player2 = new MinimaxIAPlayer(Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER, 4);
//			Board board = new Board();
//			int playIndex;
//			assertTrue((playIndex = player1.getIndexToPlacePiece(board))>-1);
//			board.setPositionAsPlayer(playIndex, Token.PLAYER_1);
//			board.incNumPiecesOfPlayer(Token.PLAYER_1);
//			board.incNumTotalPiecesPlaced();
//			assertTrue((playIndex = player2.getIndexToPlacePiece(board))>-1);
//			board.setPositionAsPlayer(playIndex, Token.PLAYER_2);
//		} catch (GameException e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
//	}
//	
//	@Test
//	public void testMovingPhase() {
//
//		try {
//			IAPlayer player1 = new MinimaxIAPlayer(Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER, 4);
//			IAPlayer player2 = new MinimaxIAPlayer(Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER, 4);
//			Board board = new Board();
//			for(int i=0;i<9;i++) {
//				int playIndex = player1.getIndexToPlacePiece(board);
//				board.setPositionAsPlayer(playIndex, Token.PLAYER_1);
//				board.incNumPiecesOfPlayer(Token.PLAYER_1);
//				int removePiece;
//				if((removePiece = player1.getIndexToRemovePieceOfOpponent(board))>-1 && (board.getPosition(removePiece).getPlayerOccupyingIt() == Token.PLAYER_2))
//					{
//						board.getPosition(removePiece).setAsUnoccupied();
//						board.decNumPiecesOfPlayer(Token.PLAYER_2);
//					}
//				board.incNumTotalPiecesPlaced();
//				
//				playIndex = player2.getIndexToPlacePiece(board);
//				board.setPositionAsPlayer(playIndex, Token.PLAYER_2);
//				board.incNumPiecesOfPlayer(Token.PLAYER_2);
//				if((removePiece = player2.getIndexToRemovePieceOfOpponent(board))>-1 && (board.getPosition(removePiece).getPlayerOccupyingIt() == Token.PLAYER_1))
//				{
//					board.getPosition(removePiece).setAsUnoccupied();
//					board.decNumPiecesOfPlayer(Token.PLAYER_1);
//				}
//				board.incNumTotalPiecesPlaced();
//			}
//			
//			assertSame(Game.MOVING_PHASE,((MinimaxIAPlayer) player1).getGamePhase(board, Token.PLAYER_1));
//			
//		} catch (GameException e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
//	}
	
//	@Test
//	public void testGameOver() {
//
//		try {
//			IAPlayer player1 = new MinimaxIAPlayer(Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER, 4);
//			Board board = new Board();
//			for(int i=0;i<18;i++) {
//				board.incNumTotalPiecesPlaced();
//			}
//			
//			board.setPositionAsPlayer(0, Token.PLAYER_1);
//			board.incNumPiecesOfPlayer(Token.PLAYER_1);
//			board.setPositionAsPlayer(2, Token.PLAYER_1);
//			board.incNumPiecesOfPlayer(Token.PLAYER_1);
//			board.setPositionAsPlayer(4, Token.PLAYER_1);
//			board.incNumPiecesOfPlayer(Token.PLAYER_1);
//			board.setPositionAsPlayer(3, Token.PLAYER_2);
//			board.incNumPiecesOfPlayer(Token.PLAYER_2);
//			board.setPositionAsPlayer(5, Token.PLAYER_2);
//			board.incNumPiecesOfPlayer(Token.PLAYER_2);
//			board.setPositionAsPlayer(9, Token.PLAYER_2);
//			board.incNumPiecesOfPlayer(Token.PLAYER_2);
//			
//			assertSame(Game.FLYING_PHASE,((MinimaxIAPlayer) player1).getGamePhase(board, Token.PLAYER_1));
//			board.printBoard();
//			Move move = player1.getPieceMove(board, Game.FLYING_PHASE);
//			assertSame(4, move.srcIndex);
//			assertSame(1, move.destIndex);
//			assertTrue(move.removePieceOnIndex != -1);
//
//		} catch (GameException e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
//	}
	
	@Test
	public void testGameOver() {
		try {
			IAPlayer player2 = new MinimaxIAPlayer(Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER, 5);
			Board board = new Board();
			for(int i=0;i<Game.NUM_PIECES_PER_PLAYER*2;i++) {
				board.incNumTotalPiecesPlaced();
			}
			
			/*board.setPositionAsPlayer(0, Token.PLAYER_1);
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
>>>>>>> 8ec224d8da2c764e42c0d7da3a59d22594ecc494
			board.setPositionAsPlayer(2, Token.PLAYER_1);
			board.setPositionAsPlayer(9, Token.PLAYER_1);
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			
			board.setPositionAsPlayer(10, Token.PLAYER_2);
			board.setPositionAsPlayer(12, Token.PLAYER_2);
			board.setPositionAsPlayer(13, Token.PLAYER_2);
			board.setPositionAsPlayer(14, Token.PLAYER_2);
			board.incNumPiecesOfPlayer(Token.PLAYER_2);
			board.incNumPiecesOfPlayer(Token.PLAYER_2);
<<<<<<< HEAD
=======
			board.setPositionAsPlayer(9, Token.PLAYER_2);
			board.incNumPiecesOfPlayer(Token.PLAYER_2);*/
			
			board.setPositionAsPlayer(0, Token.PLAYER_1);
			board.setPositionAsPlayer(2, Token.PLAYER_1);
			board.setPositionAsPlayer(9, Token.PLAYER_1);
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			board.incNumPiecesOfPlayer(Token.PLAYER_1);
			
			board.setPositionAsPlayer(10, Token.PLAYER_2);
			board.setPositionAsPlayer(12, Token.PLAYER_2);
			board.setPositionAsPlayer(13, Token.PLAYER_2);
			board.setPositionAsPlayer(14, Token.PLAYER_2);
			board.incNumPiecesOfPlayer(Token.PLAYER_2);
			board.incNumPiecesOfPlayer(Token.PLAYER_2);
			board.incNumPiecesOfPlayer(Token.PLAYER_2);
			board.incNumPiecesOfPlayer(Token.PLAYER_2);
			board.incNumPiecesOfPlayer(Token.PLAYER_2);
			
			
			//assertSame(Game.FLYING_PHASE,((MinimaxIAPlayer) player1).getGamePhase(board, Token.PLAYER_1));
			board.printBoard();
			Move move = player2.getPieceMove(board, Game.MOVING_PHASE);
			assertSame(4, move.srcIndex);
			assertSame(1, move.destIndex);
			assertTrue(move.removePieceOnIndex != -1);

		} catch (GameException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void applyMove(Move move, Token player, Board gameBoard, int gamePhase) throws GameException {

		// Try this move for the current player
		Position position = gameBoard.getPosition(move.destIndex);
		position.setAsOccupied(player);

		if(gamePhase == Game.PLACING_PHASE) {
			gameBoard.incNumPiecesOfPlayer(player);
		} else {
			gameBoard.getPosition(move.srcIndex).setAsUnoccupied();
		}

		if(move.removePieceOnIndex != -1) { // this move removed a piece from opponent
			Position removed = gameBoard.getPosition(move.removePieceOnIndex);
			removed.setAsUnoccupied();
			if(player == Token.PLAYER_1) 
				gameBoard.decNumPiecesOfPlayer(Token.PLAYER_2);
			else
				gameBoard.decNumPiecesOfPlayer(Token.PLAYER_1);
		}
	}
}
