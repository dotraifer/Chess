package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;

import java.util.List;

public abstract class Piece {
    protected final Color color;
    protected int position;

    public Color getColor() {
        return color;
    }

    public int getPosition() {
        return position;
    }


    public Piece(int position, final Color color) {
        this.position = position;
        this.color = color;
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
}
