package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    final int[] move_mask = {8, 16, 7 , 9};
    private boolean isFirstMove;

    public Pawn(int position, Color color) {
        super(position, color, true);
        this.isFirstMove = true;
    }

    @Override
    public List<Move> getLegalMoves(Board board) {
        int possible_coordinate;
        List<Move> legalMoves = new ArrayList<>();
        for (int mask : move_mask) {
            possible_coordinate = position + (mask * getDirection(color));
            if (isValidCoordinate(possible_coordinate)) {
                if (mask == 8 && board.board_state.get(possible_coordinate) == null) {
                    legalMoves.add(new Move.PawnMove(board, this, possible_coordinate));
                }
                else if (mask == 16 && board.board_state.get(possible_coordinate) == null && isFirstMove)
                {
                    legalMoves.add(new Move.PawnMove(board, this, possible_coordinate));
                }
                if(isFirstColumnExtremeCase(this.position, mask, color))
                    break;
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
