package logic;

import logic.Pieces.Piece;

public class Move {
    private Board board;
    private Piece pieceMoved;
    private int coordinateMovedTo;

    public Move(Board board, Piece pieceMoved, int coordinateMovedTo) {
        this.board = board;
        this.pieceMoved = pieceMoved;
        this.coordinateMovedTo = coordinateMovedTo;
    }
}
