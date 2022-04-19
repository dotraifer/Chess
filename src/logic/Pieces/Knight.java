package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represent a knight, and it extends Piece class
 * @author dotanraif
 * @see logic.Pieces.Piece
 */
public class Knight extends Piece {
    final int[] move_mask = {-17, -15, -10, -6, 17, 15, 10, 6};

    private final static double[] WHITE_KNIGHT_PREFERRED_COORDINATES = {
            -0.5,-0.4,-0.3,-0.3,-0.3,-0.3,-0.4,-0.5,
            -0.4,-0.2,  0,  0,  0,  0,-0.2,-0.4,
            -0.3,  0, 0.1, 0.15, 0.15, 0.1,  0,-0.3,
            -0.3,  0.05, 0.15, 0.2, 0.2, 0.15,  0.05,-0.3,
            -0.3,  0, 0.15, 0.2, 0.2, 0.15,  0,-0.3,
            -0.3,  0.05, 0.1, 0.15, 0.15, 0.1,  0.05,-0.3,
            -0.4,-0.2,  0,  0.05,  0.05,  0,-0.2,-0.4,
            -0.5,-0.4,-0.3,-0.3,-0.3,-0.3,-0.4,-0.5
    };

    private final static double[] BLACK_KNIGHT_PREFERRED_COORDINATES = {
            -0.5,-0.4,-0.3,-0.3,-0.3,-0.3,-0.4,-0.5,
            -0.4,-0.2,  0,  0.05,  0.05,  0,-0.2,-0.4,
            -0.3,  0.05, 0.1, 0.15, 0.15, 0.1,  0.05,-0.3,
            -0.3,  0, 0.15, 0.20, 0.20, 0.15,  0,-0.30,
            -0.3,  0.05, 0.15, 0.20, 0.20, 0.15,  0.05,-0.30,
            -0.30,  0, 0.10, 0.15, 0.15, 0.10,  0,-0.30,
            -0.4,-0.2,  0,  0,  0,  0,-0.2,-0.4,
            -0.5,-0.4,-0.3,-0.3,-0.3,-0.3,-0.4,-0.5,
    };


    public Knight(int position, Color color, boolean isFirstMove) {
        super(position, color, isFirstMove);
        this.value = 3.2;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public double locationBonus() {
        return this.color == Color.White ? WHITE_KNIGHT_PREFERRED_COORDINATES[this.position] : BLACK_KNIGHT_PREFERRED_COORDINATES[this.position];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Move> getLegalMoves(Board board) {
        // TODO check if eating move needed
        int possible_coordinate;
        List<Move> legalMoves = new ArrayList<>();
        for (int mask : move_mask) {
            if (isExtremeCase(position, mask))
                continue;
            possible_coordinate = mask + position;
            if (isValidCoordinate(possible_coordinate))
                if (board.board_state.get(possible_coordinate) == null)
                    // regular move
                    legalMoves.add(new Move.MajorMove(board, this, possible_coordinate));
                else if (!isFriendlyPieceOnCoordinate(board, possible_coordinate)) {
                    legalMoves.add(new Move.AttackMove(board, this, possible_coordinate, board.getPieceAtCoordinate(possible_coordinate)));
                }
        }
        return legalMoves;
    }

    public boolean isExtremeCase(int coordinate, int mask) {
        return (isFirstColumnExtremeCase(coordinate, mask) || isSecondColumnExtremeCase(coordinate, mask)
                || isFirstRowExtremeCase(coordinate, mask) || isSecondRowExtremeCase(coordinate, mask));
    }

    public boolean isFirstColumnExtremeCase(int coordinate, int mask) {
        if (coordinate % 8 == 0 && (mask == -10 || mask == -17 || mask == 6 || mask == 15))
            return true;
        else return (coordinate + 1) % 8 == 0 && (mask == -15 || mask == -6 || mask == 17 || mask == 10);
    }

    public boolean isSecondColumnExtremeCase(int coordinate, int mask) {
        if ((coordinate - 1) % 8 == 0 && (mask == -10 || mask == 6))
            return true;
        else return (coordinate + 2) % 8 == 0 && (mask == -6 || mask == 10);
    }

    public boolean isFirstRowExtremeCase(int coordinate, int mask) {
        if ((coordinate >= 0 && coordinate <= 7) && (mask == -17 || mask == -15 || mask == -10 || mask == -6))
            return true;
        else return ((coordinate >= 56 && coordinate <= 63) && (mask == 17 || mask == 15 || mask == 10 || mask == 6));
    }

    public boolean isSecondRowExtremeCase(int coordinate, int mask) {
        if ((coordinate >= 8 && coordinate <= 15) && (mask == -17 || mask == -15))
            return true;
        else return ((coordinate >= 48 && coordinate <= 55) && (mask == 17 || mask == 15));
    }
}
