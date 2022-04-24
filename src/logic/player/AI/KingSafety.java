package logic.player.AI;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.Pieces.King;
import logic.Pieces.Pawn;
import logic.Pieces.Piece;
import logic.player.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * this class contains static methods for evaluating a King safety of a player
 */
public class KingSafety {
    private static final double CASTLE_BONUS = 0.2;
    private static final double NONE_CASTLE_PUNISHMENT = -0.1;
    private static final double GOOD_PAWNS_SHIELD_BONUS = 0.3;
    private static final double BED_PAWNS_SHIELD_PUNISHMENT = -0.3;


    /**
     * evaluate the board according to king safety
     * @param player the player we evaluate for
     * @param board the current board
     * @return the value of the player's king safety if the current board
     */
    public static double calculateKingSafety(Player player, Board board)
    {
        return calculateCastleValue(player, board) + calculateKingTropism(player);
    }

    /**
     * this function calculate the king tropism for the player
     * @param player the player we calculate his king tropism
     * @return the king tropism evaluation
     */
    public static double calculateKingTropism(final Player player) {
        final int playerKingSquare = player.getKing().getPosition();
        final List<Move> enemyMoves = player.getRival().getLegalMoves();
        // the closest distance move
        int currentDistance;
        // the closest piece
        Piece closestPiece = null;
        int closestDistance = Integer.MAX_VALUE;
        for(final Move move : enemyMoves) {
            // calculate the distance between the move to the king
            currentDistance = calculateChebyshevDistance(playerKingSquare, move.getCoordinateMovedTo());
            if(move.getPieceMoved().getClass() != King.class && currentDistance < closestDistance) {
                closestDistance = currentDistance;
                closestPiece = move.getPieceMoved();
            }
        }
        if(closestPiece != null)
            return -1*(closestPiece.value / 200 * (10 - closestDistance));
        return 0;
    }

    /**
     * this function calculate the Chebyshev Distance between the king coordinate to a piece move coordinate
     * @param playerKingSquare the king coordinate
     * @param coordinateMovedTo the piece coordinate
     * @return return the Chebyshev distance to the king
     */
    private static int calculateChebyshevDistance(int playerKingSquare, int coordinateMovedTo) {
        int kingColumn =  playerKingSquare % 8;
        int kingRow = playerKingSquare / 8;
        int enemyColumn = coordinateMovedTo % 8;
        int enemyRow = coordinateMovedTo / 8;

        int columnDistance = Math.abs(kingColumn - enemyColumn);
        int rowDistance = Math.abs(kingRow - enemyRow);

        return Math.max(columnDistance, rowDistance);


    }

    /**
     * this function evaluate the castle and possibility to castle of a player
     * @param player the player we calculate castling values
     * @param board the current board
     * @return the value of castling for the player
     */
    public static double calculateCastleValue(Player player, Board board) {
        if (player.isHasCastled()) {
            return CASTLE_BONUS + calcPawnsShield(player, board);
        }
        else if(!player.getKing().isFirstMove())
            return NONE_CASTLE_PUNISHMENT;
        return 0;
    }

    /**
     * this function calculate the king position evaluation according to his pawn shield
     * @param player the player we calculate his king pos
     * @param board thr current board
     * @return the evaluation of the pawn shield
     */
    private static double calcPawnsShield(Player player, Board board) {
        final int[] pawnsBestPos1 = {9, 8, 7};
        final int[] pawnsBestPos2 = {9, 8, 15};
        final int[] pawnsBestPos3 = {9, 16, 7};
        final int[] pawnsBestPos4 = {17, 8, 7};
        final int[] pawnsBestPos5 = {1, 8, -1};
        final int[] pawnsBestPos6 = {8, 7};
        final int[] pawnsBestPos7 = {8, 9};
        final int[][] pawnsBestShield = {pawnsBestPos1, pawnsBestPos2, pawnsBestPos3, pawnsBestPos4, pawnsBestPos5,
                pawnsBestPos6, pawnsBestPos7
        };
        int kingCoordinate = player.getKing().getPosition();
        boolean isAllShieldTrue = true;
        // for every possible shield
        for(int[] shield : pawnsBestShield)
        {
            isAllShieldTrue = true;
            // for every spot in the shield
            for(int i : shield)
            {
                // if there is no pawn in the spot, and the king is not if first column case with the mask
                if((board.getPieceAtCoordinate(kingCoordinate + i * getDirection(player.getColor())) == null ||
                (board.getPieceAtCoordinate(kingCoordinate + i * getDirection(player.getColor())).getClass() !=
                        Pawn.class)) &&
                        !isPawnFromOtherSide(kingCoordinate, kingCoordinate + i * getDirection(player.getColor())))
                    isAllShieldTrue = false;
            }
            if(isAllShieldTrue && isOnFirstOrSecond(kingCoordinate, player.getColor()))
                return GOOD_PAWNS_SHIELD_BONUS;
        }
        return BED_PAWNS_SHIELD_PUNISHMENT;

    }

    private static boolean isOnFirstOrSecond(int kingCoordinate, Color playerColor)
    {
        if(playerColor == Color.White)
            return kingCoordinate <= 63 && kingCoordinate >= 48;
        return kingCoordinate <= 15 && kingCoordinate >= 0;
    }

    private static boolean isPawnFromOtherSide(int kingCoordinate, int pawnCoordinate)
    {
        if (kingCoordinate % 8 == 0 && ((pawnCoordinate + 1) % 8 == 0))
            return true;
        else return ((kingCoordinate + 1) % 8 == 0 && (pawnCoordinate % 8 == 0));
    }


    /**
     * get the movement direction
     * @param color the color
     * @return the direction multiplier
     */
    private static int getDirection(Color color)
    {
        if(color == Color.White)
            return -1;
        return 1;
    }
}
