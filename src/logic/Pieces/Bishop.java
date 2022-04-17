package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    final int[] move_mask = {7, 9, -7, -9};

    private final static double[] WHITE_BISHOP_PREFERRED_COORDINATES = {
            -0.2,-0.1,-0.1,-0.1,-0.1,-0.1,-0.1,-0.2,
            -0.1,  0,  0,  0,  0,  0,  0,-0.1,
            -0.1,  0,  0.05, 0.1, 0.1,  0.05,  0,-0.1,
            -0.1,  0.05,  0.05, 0.1, 0.1,  0.05,  0.05,-0.1,
            -0.1,  0, 0.1, 0.1, 0.1, 0.1,  0,-0.1,
            -0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,-0.1,
            -0.1,  0.05,  0,  0,  0,  0,  0.05,-0.1,
            -0.2,-0.1,-0.1,-0.1,-0.1,-0.1,-0.1,-0.2
    };

    private final static double[] BLACK_BISHOP_PREFERRED_COORDINATES = {
            -0.2,-0.1,-0.1,-0.1,-0.1,-0.1,-0.1,-0.2,
            -0.1,  0.05,  0,  0,  0,  0,  0.05,-0.1,
            -0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,-0.1,
            -0.1,  0, 0.1, 0.1, 0.1, 0.1,  0,-0.1,
            -0.1,  0.05,  0.05, 0.1, 0.1,  0.05,  0.05,-0.1,
            -0.1,  0,  0.05, 0.1, 0.1,  0.05,  0,-0.1,
            -0.1,  0,  0,  0,  0,  0,  0,-0.1,
            -0.2,-0.1,-0.1,-0.1,-0.1,-0.1,-0.1,-0.2
    };

    public Bishop(int position, Color color, boolean isFirstMove) {
        super(position, color, isFirstMove);
        this.value = 3;
    }

    @Override
    public double locationBonus() {
        return this.color == Color.White ? WHITE_BISHOP_PREFERRED_COORDINATES[this.position] : BLACK_BISHOP_PREFERRED_COORDINATES[this.position];
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        int possible_coordinate;
        List<Move> legalMoves = new ArrayList<>();
        for (int mask : move_mask) {
            possible_coordinate = position;
            while (isValidCoordinate(possible_coordinate)) {
                if (isFirstColumnExtremeCase(possible_coordinate, mask))
                    break;
                possible_coordinate += mask;
                if (isValidCoordinate(possible_coordinate)) {
                    if (board.board_state.get(possible_coordinate) == null)
                        // regular move
                        legalMoves.add(new Move.MajorMove(board, this, possible_coordinate));
                    else if (!isFriendlyPieceOnCoordinate(board, possible_coordinate)) {
                        // attack move
                        // TODO check if eating move needed
                        legalMoves.add(new Move.AttackMove(board, this, possible_coordinate, board.getPieceAtCoordinate(possible_coordinate)));
                        break;
                    } else
                        // if friendly on dest
                        break;
                } else
                    // if out of board
                    break;
            }
        }
        return legalMoves;
    }

    public boolean isFirstColumnExtremeCase(int coordinate, int mask) {
        if (coordinate % 8 == 0 && (mask == -9 || mask == 7))
            return true;
        else return ((coordinate + 1) % 8 == 0 && (mask == -7 || mask == 9));
    }
}
