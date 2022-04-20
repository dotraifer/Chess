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

    /**
     * this function evaluate the rook structure on the board for the player
     * @param board the board we analyze
     * @param playerActivePieces all the player's active pieces
     * @return evaluation of the rook structure of the player
     */
    public static double rookStruct(Board board, List<Piece> playerActivePieces)
    {
        List<Piece> rooks = findRooks(playerActivePieces);
        // if there are two rooks
        if(rooks.size() == 2) {
            Piece rook1 = rooks.get(0);
            Piece rook2 = rooks.get(1);
            return calculateConnectedRowRooks(rook1, rook2, board) + calculateConnectedColumnRooks(rook1, rook2, board);
        }
        return 0;

    }

    /**
     * this function find the rooks on the board
     * @param playerActivePieces all the player's active pieces
     * @return list of the rooks of the player
     */
    private static List<Piece> findRooks(List<Piece> playerActivePieces) {
        List<Piece> rooks = new ArrayList<>();
        for(Piece piece : playerActivePieces)
        {
            if(piece.getClass() == Rook.class)
                rooks.add(piece);
        }
        return rooks;
    }

    /**
     * this function evaluate the rook structure according to rooks connected on the Column
     * @param rook1 the first rook
     * @param rook2 the second rook
     * @param board the board we are in
     * @return the bonus if the rooks are connected on column, 0 otherwise
     */
    private static double calculateConnectedColumnRooks(Piece rook1, Piece rook2, Board board) {
        if(rook1.getPosition() % 8 == rook2.getPosition() % 8)
        {
            for(int i = Math.min(rook1.getPosition() + 8, rook2.getPosition());i < Math.max(rook1.getPosition(), rook2.getPosition());i+=8)
            {
                if(board.getPieceAtCoordinate(i) != null)
                    return 0;
            }
            return CONNECTED_COLUMN_ROOKS_BONUS;
        }
        return 0;
    }

    /**
     * this function evaluate the rook structure according to rooks connected on the Row
     * @param rook1 the first rook
     * @param rook2 the second rook
     * @param board the board we are in
     * @return the bonus if the rooks are connected on row, 0 otherwise
     */
    private static double calculateConnectedRowRooks(Piece rook1, Piece rook2, Board board) {
        if(rook1.getPosition() / 8 == rook2.getPosition() / 8)
        {
            for(int i = Math.min(rook1.getPosition() + 1, rook2.getPosition());i < Math.max(rook1.getPosition(), rook2.getPosition());i++)
            {
                if(board.getPieceAtCoordinate(i) != null)
                    return 0;
            }
            return CONNECTED_ROW_ROOKS_BONUS;
        }
        return 0;

    }




}
