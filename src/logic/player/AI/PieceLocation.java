package logic.player.AI;

import logic.Pieces.Piece;

import java.util.List;

public class PieceLocation {

    public static double pieceLocation(List<Piece> allActivePieces, GameStage gameStage)
    {
        double locationBonus = 0;
        for(Piece piece : allActivePieces) {
            locationBonus += piece.locationBonus(gameStage);
        }
        return locationBonus;
    }
}
