package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.player.AI.GameStage;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represent a Rook, and it extends Piece class
 * @author dotanraif
 * @see logic.Pieces.Piece
 */
public class Rook extends Piece{
    final int[] move_mask = {1, 8, -8, -1};

    private final static double[] WHITE_ROOK_PREFERRED_COORDINATES = {
            0,  0,  0,  0,  0,  0,  0,  0,
            0.05, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2,  0.05,
            -0.05,  0,  0,  0,  0,  0,  0, -0.05,
            -0.05,  0,  0,  0,  0,  0,  0, -0.05,
            -0.05,  0,  0,  0,  0,  0,  0, -0.05,
            -0.05,  0,  0,  0,  0,  0,  0, -0.05,
            -0.05,  0,  0,  0,  0,  0,  0, -0.05,
            0,  0,  0,  0.05,  0.05,  0,  0,  0
    };

    private final static double[] BLACK_ROOK_PREFERRED_COORDINATES = {
            0,  0,  0,  0.05,  0.05,  0,  0,  0,
            -0.05,  0,  0,  0,  0,  0,  0, -0.05,
            -0.05,  0,  0,  0,  0,  0,  0, -0.05,
            -0.05,  0,  0,  0,  0,  0,  0, -0.05,
            -0.05,  0,  0,  0,  0,  0,  0, -0.05,
            -0.05,  0,  0,  0,  0,  0,  0, -0.05,
            0.05, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2,  0.05,
            0,  0,  0,  0,  0,  0,  0,  0,
    };

    public Rook(int position, Color color, boolean isFirstMove) {
        super(position, color, isFirstMove);
        this.value = 5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double locationBonus(GameStage gameStage) {
        return this.color == Color.White ? WHITE_ROOK_PREFERRED_COORDINATES[this.position] : BLACK_ROOK_PREFERRED_COORDINATES[this.position];
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
            while (isValidCoordinate(possible_coordinate))
            {
                if(isFirstColumnExtremeCase(possible_coordinate, mask))
                    break;
                possible_coordinate += mask;
                if(isValidCoordinate(possible_coordinate))
                {
                    if(board.board_state.get(possible_coordinate) == null)
                        // regular move
                        legalMoves.add(new Move.MajorMove(board, this, possible_coordinate));
                    else if(!isFriendlyPieceOnCoordinate(board, possible_coordinate)) {
                        // attack move
                        // TODO check if eating move needed
                        legalMoves.add(new Move.AttackMove(board, this, possible_coordinate, board.getPieceAtCoordinate(possible_coordinate)));
                        break;
                    }
                    else
                        // if friendly on dest
                        break;
                }
                else
                    // if out of board
                    break;
            }
        }
        return legalMoves;
    }

    public boolean isFirstColumnExtremeCase(int coordinate, int mask) {
        if (coordinate % 8 == 0 && (mask == -1))
            return true;
        else return ((coordinate + 1) % 8 == 0 && (mask == 1));
    }

}
