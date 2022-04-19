package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.player.AI.GameStage;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represent a king, and it extends Piece class
 * @author dotanraif
 * @see logic.Pieces.Piece
 */
public class King extends Piece {
    final int[] move_mask = {-9, -8, -7, -1, 1, 7, 8, 9};

    private final static double[] WHITE_KING_PREFERRED_COORDINATES_STARTMID = {
            -0.3,-0.4,-0.4,-0.5,-0.5,-0.4,-0.4,-0.3,
            -0.3,-0.4,-0.4,-0.5,-0.5,-0.4,-0.4,-0.3,
            -0.3,-0.4,-0.4,-0.5,-0.5,-0.4,-0.4,-0.3,
            -0.3,-0.4,-0.4,-0.5,-0.5,-0.4,-0.4,-0.3,
            -0.2,-0.3,-0.3,-0.4,-0.4,-0.3,-0.3,-0.2,
            -0.1,-0.2,-0.2,-0.2,-0.2,-0.2,-0.2,-0.1,
            0.2, 0.2,  0,  0,  0,  0, 0.2, 0.2,
            0.2, 0.3, 0.1,  0,  0, 0.1, 0.3, 0.2
    };

    private final static double[] BLACK_KING_PREFERRED_COORDINATES_STARTMID = {
            0.2, 0.3, 0.1,  0,  0, 0.1, 0.3, 0.2,
            0.2, 0.2,  0,  0,  0,  0, 0.2, 0.2,
            -0.1,-0.2,-0.2,-0.2,-0.2,-0.2,-0.2,-0.1,
            -0.2,-0.3,-0.3,-0.4,-0.4,-0.3,-0.3,-0.2,
            -0.3,-0.4,-0.4,-0.5,-0.5,-0.4,-0.4,-0.3,
            -0.3,-0.4,-0.4,-0.5,-0.5,-0.4,-0.4,-0.3,
            -0.3,-0.4,-0.4,-0.5,-0.5,-0.4,-0.4,-0.3,
            -0.3,-0.4,-0.4,-0.5,-0.5,-0.4,-0.4,-0.3
    };
    public King(int position, Color color, boolean isFirstMove) {
        super(position, color, isFirstMove);
        this.value = 10000;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double locationBonus(GameStage gameStage) {
        if(gameStage == GameStage.OPENING || gameStage == GameStage.MIDGAME)
            return this.color == Color.White ? WHITE_KING_PREFERRED_COORDINATES_STARTMID[this.position] : BLACK_KING_PREFERRED_COORDINATES_STARTMID[this.position];
        else
            return this.color == Color.White ? WHITE_KING_PREFERRED_COORDINATES_STARTMID[this.position] : BLACK_KING_PREFERRED_COORDINATES_STARTMID[this.position];

    }

    /**
     * {@inheritDoc}
     */
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
