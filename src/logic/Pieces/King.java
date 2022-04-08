package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    final int[] move_mask = {-9, -8, -7, -1, 1, 7, 8, 9};
    public King(int position, Color color, boolean isFirstMove) {
        super(position, color, isFirstMove);
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        int possible_coordinate;
        List<Move> legalMoves = new ArrayList<>();
        for (int mask : move_mask) {
            if (isFirstColumnExtremeCase(position, mask))
                continue;
            possible_coordinate = mask + position;
            if (isValidCoordinate(possible_coordinate)) {
                if (board.board_state.get(possible_coordinate) == null)
                    // regular move
                    legalMoves.add(new Move.MajorMove(board, this, possible_coordinate));
                else if (!isFriendlyPieceOnCoordinate(board, possible_coordinate)) {
                    legalMoves.add(new Move.AttackMove(board, this, possible_coordinate, board.getPieceAtCoordinate(possible_coordinate)));
                }
            }
        }
        return legalMoves;

    }
    public boolean isFirstColumnExtremeCase(int coordinate, int mask) {
        if (coordinate % 8 == 0 && (mask == -1 || mask == -9 || mask == 7))
            return true;
        else return ((coordinate + 1) % 8 == 0 && (mask == 1 || mask == -7 || mask == 9));
    }
}
