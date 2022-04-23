package logic.player.AI;

import logic.Pieces.Piece;

import java.util.List;
/**
 * this class contains static methods for evaluating a Piece location of a player
 */
public class PieceLocation {

    /**
     * this function evaluate the pieces' location on the board
     * @param allActivePieces all the player's active pieces
     * @param gameStage the game stage we are in
     * @return the piece location evaluation
     */
    public static double pieceLocation(List<Piece> allActivePieces, GameStage gameStage)
    {
        double locationBonus = 0;
        for(Piece piece : allActivePieces) {
            locationBonus += piece.locationBonus(gameStage);
        }
        return locationBonus;
    }
}
