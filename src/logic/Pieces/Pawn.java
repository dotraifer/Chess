package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    final int[] move_mask = {8};

    public Pawn(int position, Color color) {
        super(position, color);
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        int possible_coordinate;
        List<Move> legalMoves = new ArrayList<>();
        for (int mask : move_mask) {
            possible_coordinate = mask + (position * getDirection(color));
            if (isValidCoordinate(possible_coordinate)) {
                if (mask == 8 && board.board_state.get(possible_coordinate) == null) {
                    legalMoves.add(new Move(board, this, possible_coordinate * getDirection(color)));
                }
            }
        }
        return legalMoves;
    }

    public int getDirection(Color color) {
        if (color == Color.White)
            return -1;
        return 1;
    }
}
