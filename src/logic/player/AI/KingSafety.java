package logic.player.AI;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.Pieces.Pawn;
import logic.Pieces.Piece;
import logic.player.Player;

import java.util.Collection;

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
        return calculateCastleValue(player, board);
    }
    public static double calculateKingTropism(final Player player) {
        final int playerKingSquare = player.getKing().getPosition();
        final Collection<Move> enemyMoves = player.getRival().getLegalMoves();
        Piece closestPiece = null;
        int closestDistance = Integer.MAX_VALUE;
        for(final Move move : enemyMoves) {
            final int currentDistance = calculateChebyshevDistance(playerKingSquare, move.getCoordinateMovedTo());
            if(currentDistance < closestDistance) {
                closestDistance = currentDistance;
                closestPiece = move.getPieceMoved();
            }
        }
        return closestPiece.value / 10 * closestDistance;
    }

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
        final int[][] pawnsBestShield = {pawnsBestPos1, pawnsBestPos2, pawnsBestPos3, pawnsBestPos4};
        int kingCoordinate = player.getKing().getPosition();
        boolean isAllShieldTrue = true;
        for(int[] shield : pawnsBestShield)
        {
            isAllShieldTrue = true;
            for(int i : shield)
            {
                if(board.getPieceAtCoordinate(kingCoordinate + i * getDirection(player.getColor())) == null ||
                !(board.getPieceAtCoordinate(kingCoordinate + i * getDirection(player.getColor())).getClass() ==
                        Pawn.class))
                    isAllShieldTrue = false;
            }
            if(isAllShieldTrue)
                return GOOD_PAWNS_SHIELD_BONUS;
        }
        return BED_PAWNS_SHIELD_PUNISHMENT;

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
