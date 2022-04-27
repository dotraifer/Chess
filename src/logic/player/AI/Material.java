package logic.player.AI;

import logic.Pieces.Bishop;
import logic.Pieces.King;
import logic.Pieces.Pawn;
import logic.Pieces.Piece;

import java.util.List;

/**
 * this class contains static methods for evaluating a Material of a player
 */
public class Material {
    private final static double TWO_BISHOPS_BONUS = 0.05;

    /**
     * this function evaluate the material value of the given pieces
     * @param allActivePieces the pieces we want to evaluate their values
     * @return material evaluation for the given pieces
     */
    public static double material(List<Piece> allActivePieces, List<Piece> allEnemyActivePieces)
    {
        /**int numberOfBishops = 0;
        double materialValue = 0;
        for(Piece piece : allActivePieces) {
            materialValue += piece.getValue();
            if (piece.getClass() == Bishop.class)
                numberOfBishops++;
        }
        return materialValue
                + (numberOfBishops == 2 ? TWO_BISHOPS_BONUS : 0);*/
        return getTotalAdvantage(allActivePieces, allEnemyActivePieces);
    }


    /**
     * evaluating the material board, in more  efficient way, that
     * modify the material difference by a "trade down" bonus that encourages the winning side to trade pieces but no pawns
     * @param allWhiteActivePieces
     * @param allBlackActivePieces
     * @return the total material advantage- minus to black and plus to white
     * the way to calculate the material advantage:
     * @see <a href="https://www.chessprogramming.org/Material_Hash_Table"></a>
     */
    public static double getTotalAdvantage(List<Piece> allWhiteActivePieces, List<Piece> allBlackActivePieces)
    {
        double whiteSum = 0;
        double blackSum = 0;
        int whitePawns = 0;
        int blackPawns = 0;
        for(Piece piece : allWhiteActivePieces)
        {
            if(piece.getClass() != King.class) {
                whiteSum += piece.getValue();
                if(piece.getClass() == Pawn.class)
                    whitePawns++;
            }
        }
        for(Piece piece : allBlackActivePieces)
        {
            if(piece.getClass() != King.class) {
                blackSum += piece.getValue();
                if(piece.getClass() == Pawn.class)
                    blackPawns++;
            }

        }
        double md = Math.abs(whiteSum - blackSum);
        double pa = whiteSum > blackSum ? whitePawns : blackPawns;
        double ms = Math.min(24, md) + ((md * pa * (80 - (whiteSum + blackSum))) / (64 * (pa + 1)));
        double totalAdv = Math.min(31, ms);
        return whiteSum >= blackSum ? totalAdv : -1*totalAdv;
    }
}
