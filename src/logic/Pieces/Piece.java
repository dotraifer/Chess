package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;

import java.util.List;

public abstract class Piece {
    protected final Color color;
    protected int position;
    protected boolean isFirstMove;

    public Color getColor() {
        return color;
    }

    public int getPosition() {
        return position;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public Piece(int position, final Color color, boolean isFirstMove) {
        this.position = position;
        this.color = color;
        this.isFirstMove = isFirstMove;
    }
    public abstract List<Move> getLegalMoves(Board board);

    @Override
    public String toString() {
        return " " + this.getClass();
    }

    public boolean isValidCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate <= 63;
    }


    public boolean isFriendlyPieceOnCoordinate(Board board, int coordinate) {
        /*
        return false if the coordinate in empty, or it has enemy piece on it,
        and true if there is friendly piece
         */
        return board.board_state.get(coordinate) != null && board.board_state.get(coordinate).color == color;
    }

    public void movePiece(Move move)
    {
        this.position = move.getCoordinateMovedTo();
    }
}
