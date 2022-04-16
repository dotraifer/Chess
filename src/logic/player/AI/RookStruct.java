package logic.player.AI;

import logic.Pieces.Piece;
import logic.Pieces.Rook;
import logic.player.Player;

import java.util.List;

public class RookStruct {
    private static final double CONNECTED_ROW_ROOKS_BONUS = 0.07;
    private static final double CONNECTED_COLUMN_ROOKS_BONUS = 0.12;

    public static double rookStruct(Player player, List<Piece> allActivePieces)
    {
        int[] rookRowArr = getRooksRowPlacesArray(allActivePieces);
        int[] rookColumnArr = getRooksColumnPlacesArray(allActivePieces);
        return calculateConnectedRowRooks(rookRowArr) + calculateConnectedCoulmnRooks(rookColumnArr);

    }

    private static double calculateConnectedCoulmnRooks(int[] rookColumnArr) {
        for (int i: rookColumnArr)
        {
            if (i > 1)
                return CONNECTED_COLUMN_ROOKS_BONUS;
        }
        return 0;
    }

    private static double calculateConnectedRowRooks(int[] rookRowArr) {
        for (int i: rookRowArr)
        {
            if (i > 1)
                return CONNECTED_ROW_ROOKS_BONUS;
        }
        return 0;
    }

    private static int[] getRooksColumnPlacesArray(List<Piece> allActivePieces) {
        int [] rooksArr = {0, 0, 0, 0, 0, 0, 0, 0};
        for(Piece piece: allActivePieces)
        {
            if(piece.getClass() == Rook.class)
                rooksArr[piece.getPosition() % 8]++;
        }
        return rooksArr;
    }

    private static int[] getRooksRowPlacesArray(List<Piece> allActivePieces) {
        int [] rooksArr = {0, 0, 0, 0, 0, 0, 0, 0};
        for(Piece piece: allActivePieces)
        {
            if(piece.getClass() == Rook.class)
                rooksArr[piece.getPosition() / 8]++;
        }
        return rooksArr;
    }


}
