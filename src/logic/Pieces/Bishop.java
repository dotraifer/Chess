package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.player.AI.GameStage;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represent a bishop, and it extends Piece class
 * @author dotanraif
 * @see logic.Pieces.Piece
 */
public class Bishop extends Piece {
    final int[] move_mask = {7, 9, -7, -9};

    // piece square table for white bishop
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

    // piece square table for black bishop
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
        this.value = 3.3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double locationBonus(GameStage gameStage) {
        return this.color == Color.White ? WHITE_BISHOP_PREFERRED_COORDINATES[this.position] : BLACK_BISHOP_PREFERRED_COORDINATES[this.position];
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * this function use to check that a piece is not going out from one side of the board to the other
     * because it's on first column
     * @param coordinate the coordinate the piece is in
     * @param mask the mask we check in
     * @return true if the move is illegal-false otherwise
     */
    public boolean isFirstColumnExtremeCase(int coordinate, int mask) {
        if (coordinate % 8 == 0 && (mask == -9 || mask == 7))
            return true;
        else return ((coordinate + 1) % 8 == 0 && (mask == -7 || mask == 9));
    }

    @Override
    public String toString() {
        return "B";
    }
}
