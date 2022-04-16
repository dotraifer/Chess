package logic.player.AI;

import logic.Pieces.Pawn;
import logic.Pieces.Piece;
import logic.player.Player;

import java.util.List;

public class PawnStruct {
    private static final double DOUBLE_PAWN_PUNISHMENT = -0.1;
    private static final double ISOLATED_PAWN_PUNISHMENT = -0.1;


    public static double pawnStruct(Player player, List<Piece> allActivePieces)
    {
        int[] pawnsArr = getPawnsPlacesArray(allActivePieces);
        return calculateDoublePawns(pawnsArr) + calculateIsolatedPawns((pawnsArr));

    }

    public static int[] getPawnsPlacesArray(List<Piece> allActivePieces)
    {
        int [] pawnsArr = {0, 0, 0, 0, 0, 0, 0, 0};
        for(Piece piece: allActivePieces)
        {
            if(piece.getClass() == Pawn.class)
                pawnsArr[piece.getPosition() % 8]++;
        }
        return pawnsArr;
    }

    public static double calculateDoublePawns(int[] pawnsArr)
    {
        int pawnDoubledTotal = 0;
        for (int i : pawnsArr) {
            // if more then one, for every couple add 1
            if (i > 1)
                pawnDoubledTotal += i - 1;
        }
        return pawnDoubledTotal * DOUBLE_PAWN_PUNISHMENT;
    }

    public static double calculateIsolatedPawns(int[] pawnsArr)
    {
        int numIsolatedPawns = 0;
        if(pawnsArr[0] > 0 && pawnsArr[1] == 0) {
            numIsolatedPawns += pawnsArr[0];
        }
        if(pawnsArr[7] > 0 && pawnsArr[6] == 0) {
            numIsolatedPawns += pawnsArr[7];
        }
        for(int i = 1; i < pawnsArr.length - 1; i++) {
            if((pawnsArr[i-1] == 0 && pawnsArr[i+1] == 0)) {
                numIsolatedPawns += pawnsArr[i];
            }
        }
        return numIsolatedPawns * ISOLATED_PAWN_PUNISHMENT;
    }

}
