package logic.player.AI;

import logic.Board;
import logic.Color;
import logic.Pieces.Piece;
import logic.Pieces.Queen;

import java.util.List;
/**
 * this class contains static methods for evaluating a Piece location of a player
 */
public class PieceLocation {

    private static final double EARLY_QUEEN_DEVELOPMENT_PUNISHMENT = -0.5;
    private static final double PIECE_SQUARE_TABLE_REDUCER = 0.8;
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
        return locationBonus * PIECE_SQUARE_TABLE_REDUCER + earlyQueenDevelopment(allActivePieces, gameStage);
    }

    /**
     * this function return the early development of a queen evaluation-development(not in the first 2 rows)
     * @param allActivePieces all the player active pieces
     * @param gameStage the game stage we are currently in
     * @return evaluation to queen early development
     */
    private static double earlyQueenDevelopment(List<Piece> allActivePieces, GameStage gameStage)
    {
        Piece queen = findQueen(allActivePieces);
        if(queen != null && gameStage == GameStage.OPENING)
        {
            int loc = queen.getPosition();
            if(queen.getColor() == Color.White)
            {
                if(!(Board.size - loc - 1 >= 0 && Board.size - loc - 1 <= 15))
                    return EARLY_QUEEN_DEVELOPMENT_PUNISHMENT;
            }
            if(queen.getColor() == Color.Black)
            {
                if(!(loc >= 0 && loc <= 15))
                    return EARLY_QUEEN_DEVELOPMENT_PUNISHMENT;
            }
        }
        return 0;
    }

    /**
     * find the queen for the player
     * @param allActivePieces all the player active pieces
     * @return the queen of the player, or null if no queen was found
     */
    private static Piece findQueen(List<Piece> allActivePieces)
    {
        for(Piece piece : allActivePieces)
        {
            if(piece.getClass() == Queen.class)
                return piece;
        }
        return null;
    }
}
