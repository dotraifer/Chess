package logic.player;

import logic.Board;
import logic.Move;
import logic.Pieces.Piece;

import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(Board board, List<Move> whiteLegalMoves, List<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public List<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

}
