package logic.player.AI;

import logic.Board;
import logic.Move;
import logic.Pieces.Piece;
import logic.Pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class RookStruct {
    private static final double CONNECTED_ROW_ROOKS_BONUS = 0.07;
    private static final double CONNECTED_COLUMN_ROOKS_BONUS = 0.12;

    public static double rookStruct(Board board, List<Piece> playerActivePieces)
    {
        List<Piece> rooks = findRooks(playerActivePieces);;
        if(rooks.size() == 2) {
            Piece rook1 = rooks.get(0);
            Piece rook2 = rooks.get(1);
            return calculateConnectedRowRooks(rook1, rook2, board) + calculateConnectedColumnRooks(rook1, rook2, board);
        }
        return 0;

    }

    private static List<Piece> findRooks(List<Piece> playerActivePieces) {
        List<Piece> rooks = new ArrayList<>();
        for(Piece piece : playerActivePieces)
        {
            if(piece.getClass() == Rook.class)
                rooks.add(piece);
        }
        return rooks;
    }

    private static double calculateConnectedColumnRooks(Piece rook1, Piece rook2, Board board) {
        if(rook1.getPosition() - 8 == rook2.getPosition() || rook1.getPosition() + 8 == rook2.getPosition())
            return CONNECTED_COLUMN_ROOKS_BONUS;
        for(Move move : rook1.getLegalMoves(board))
        {
            if(move.getCoordinateMovedTo() - 8 == rook2.getPosition() || move.getCoordinateMovedTo() + 8 == rook2.getPosition())
                return CONNECTED_COLUMN_ROOKS_BONUS;
        }
        return 0;
    }

    private static double calculateConnectedRowRooks(Piece rook1, Piece rook2, Board board) {
        if(rook1.getPosition() - 1 == rook2.getPosition() || rook1.getPosition() + 1 == rook2.getPosition())
            return CONNECTED_ROW_ROOKS_BONUS;
        for(Move move : rook1.getLegalMoves(board))
        {
            if(move.getCoordinateMovedTo() - 1 == rook2.getPosition() || move.getCoordinateMovedTo() + 1 == rook2.getPosition())
                return CONNECTED_ROW_ROOKS_BONUS;
        }
        return 0;
    }




}
