package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    private final static double[] WHITE_PAWN_PREFERRED_COORDINATES = {
            0,  0,  0,  0,  0,  0,  0,  0,
            0.75, 0.75, 0.75, 0.75, 0.75, 0.75, 0.75, 0.75,
            0.25, 0.25, 0.29, 0.29, 0.29, 0.29, 0.25, 0.25,
            0.05,  0.05, 0.1, 0.55, 0.55, 0.1,  0.05,  0.05,
            0,  0,  0, 0.2, 0.2,  0,  0,  0,
            0.05, -0.05,-0.1,  0,  0,-0.1, -0.05,  0.05,
            0.05, 0.1, 0.1,-0.2,-0.2, 0.1, 0.1,  0.05,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    private final static double[] BLACK_PAWN_PREFERRED_COORDINATES = {
            0,  0,  0,  0,  0,  0,  0,  0,
            0.05, 0.1, 0.1,-0.2,-0.2, 0.1, 0.1,  0.05,
            0.05, -0.05,-0.1,  0,  0,-0.1, -0.05,  0.05,
            0,  0,  0, 0.2, 0.2,  0,  0,  0,
            0.05,  0.05, 0.1, 0.55, 0.55, 0.1,  0.05,  0.05,
            0.25, 0.25, 0.29, 0.29, 0.29, 0.29, 0.25, 0.25,
            0.75, 0.75, 0.75, 0.75, 0.75, 0.75, 0.75, 0.75,
            0,  0,  0,  0,  0,  0,  0,  0
    };
    final int[] move_mask = {8, 16, 7 , 9};

    public Pawn(int position, Color color, boolean isFirstMove) {
        super(position, color, isFirstMove);
        this.value = 1;
    }

    @Override
    public double locationBonus() {
        return this.color == Color.White ? WHITE_PAWN_PREFERRED_COORDINATES[this.position] : BLACK_PAWN_PREFERRED_COORDINATES[this.position];
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        int possible_coordinate;
        List<Move> legalMoves = new ArrayList<>();
        for (int mask : move_mask) {
            possible_coordinate = position + (mask * getDirection(color));
            if (isValidCoordinate(possible_coordinate)) {
                if(isFirstColumnExtremeCase(this.position, mask, color))
                    continue;
                if (mask == 8 && board.board_state.get(possible_coordinate) == null) {
                    legalMoves.add(new Move.PawnMove(board, this, possible_coordinate));
                }
                else if (mask == 16 && board.board_state.get(possible_coordinate) == null &&
                        board.board_state.get(possible_coordinate - 8 * getDirection(color)) == null && isFirstMove)
                {
                    legalMoves.add(new Move.PawnMove(board, this, possible_coordinate));
                }
                else if(mask == 7 && board.board_state.get(possible_coordinate) != null && !isFriendlyPieceOnCoordinate(board, possible_coordinate))
                {
                    legalMoves.add(new Move.PawnAttackMove(board, this, possible_coordinate, board.getPieceAtCoordinate(possible_coordinate)));
                }
                else if(mask == 9 && board.board_state.get(possible_coordinate) != null && !isFriendlyPieceOnCoordinate(board, possible_coordinate))
                {
                    legalMoves.add(new Move.PawnAttackMove(board, this, possible_coordinate, board.getPieceAtCoordinate(possible_coordinate)));
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
    public boolean isFirstColumnExtremeCase(int coordinate, int mask, Color color) {
        // TODO possible problem with direction
        if (coordinate % 8 == 0 && ((mask == 7 && color == Color.Black)||(mask == 9 && color == Color.White)))
            return true;
        else return ((coordinate + 1) % 8 == 0 && ((mask == 9 && color == Color.Black)||(mask == 7 && color == Color.White)));
    }


}
