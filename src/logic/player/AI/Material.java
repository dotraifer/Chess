package logic.player.AI;

import logic.Pieces.Bishop;
import logic.Pieces.King;
import logic.Pieces.Piece;
import logic.player.Player;

import java.util.List;

public class Material {
    private final static double TWO_BISHOPS_BONUS = 0.25;
    public static double material(Player player, List<Piece> allActivePieces)
    {
        int numberOfBishops = 0;
        double materialValue = 0;
        for(Piece piece : allActivePieces) {
            materialValue += piece.getValue() + piece.locationBonus();
            if (piece.getClass() == Bishop.class)
                numberOfBishops++;
        }
        return materialValue + (numberOfBishops == 2 ? TWO_BISHOPS_BONUS : 0);
    }
}
