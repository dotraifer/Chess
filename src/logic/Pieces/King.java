package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;

import java.util.List;

public class King extends Piece {
    public King(int position, Color color) {
        super(position, color);
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        return null;
    }
}
