package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    final int[] move_mask = {-17, -15, -10, -6, 17, 15, 10, 6};

    public Knight(int position, Color color) {
        super(position, color);
    }

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
                if (!isFriendlyPieceOnCoordinate(board, possible_coordinate)) {
                    legalMoves.add(new Move(board, this, possible_coordinate));
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
