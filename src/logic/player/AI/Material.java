package logic.player.AI;

import logic.Pieces.Bishop;
import logic.Pieces.Piece;

import java.util.List;

public class Material {
    private final static double TWO_BISHOPS_BONUS = 0.25;

    /**
     * this function evaluate the material value of the given pieces
     * @param allActivePieces the pieces we want to evaluate their values
     * @return material evaluation for the given pieces
     */
    public static double material(List<Piece> allActivePieces)
    {
        int numberOfBishops = 0;
        double materialValue = 0;
        for(Piece piece : allActivePieces) {
            materialValue += piece.getValue() + piece.locationBonus();
            if (piece.getClass() == Bishop.class)
                numberOfBishops++;
        }
        return materialValue;
                //+ (numberOfBishops == 2 ? TWO_BISHOPS_BONUS : 0);
    }
}
