package logic.player;

import logic.Board;
import logic.Move;
import logic.Pieces.Piece;

import java.util.List;

public class WhitePlayer extends Player{

    public WhitePlayer(Board board, List<Move> whiteLegalMoves, List<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public List<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }
}
