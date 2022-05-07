package logic.player.AI;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.MoveTransition;
import logic.Pieces.Bishop;
import logic.Pieces.King;
import logic.Pieces.Knight;
import logic.Pieces.Rook;
import logic.player.Player;

/**
 * this class contains static methods for evaluating a Mobility of a player
 */
public class Mobility {
    private final static double ROOK_FORWARD_MOVES_MULTIPLIER = 0.02;
    private final static double ROOK_SIDE_MOVES_MULTIPLIER = 0.012;
    private final static double ROOK_BACKWARD_MOVES_MULTIPLIER = 0.007;
    private final static double BISHOP_FORWARD_MOVES_MULTIPLIER = 0.014;
    private final static double BISHOP_BACKWARD_MOVES_MULTIPLIER = 0.009;
    private final static double KNIGHT_FORWARD_MOVES_MULTIPLIER = 0.012;
    private final static double KNIGHT_BACKWARD_MOVES_MULTIPLIER = 0.005;
    private final static double DETENTION_PUNISHMENT_MULTIPLIER = 0.1;
    /**
     * this function evaluate player mobility
     * @param player the player we calculate his mobility
     * @return evaluation of the player's mobility
     */
    public static double mobility(Player player, Board board) {
        double mobilityValue = 0;
        for(Move move : player.getLegalMoves()) {
            // according to the piece
            if (Rook.class.equals(move.getPieceMoved().getClass())) {
                mobilityValue += calcRookMoves(move, player);
            } else if (Bishop.class.equals(move.getPieceMoved().getClass())) {
                mobilityValue += calcBishopMoves(move, player);
            } else if (Knight.class.equals(move.getPieceMoved().getClass())) {
                mobilityValue += calcKnightMoves(move, player);
            } else
                mobilityValue += 0.001;
            // detention can be implemented
        }
        return mobilityValue;
    }

    /**
     * this function calculate the rook moves evaluation, according to the rook moves forward, backward or to sides
     * @param move the move we investigate
     * @param player the player we evaluate for
     * @return the evaluation for the rook mobility
     */
    public static double calcRookMoves(Move move, Player player)
    {
        int sideMove = 0;
        int forwardMove = 0;
        int backwardMove = 0;
        if(Math.abs(move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) <8 )
            sideMove++;
        else if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirection(player) >= 8)
            forwardMove++;
        else if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirection(player) <= -8)
            backwardMove++;
        return sideMove * ROOK_SIDE_MOVES_MULTIPLIER + forwardMove * ROOK_FORWARD_MOVES_MULTIPLIER + backwardMove * ROOK_BACKWARD_MOVES_MULTIPLIER;
    }

    /**
     * this function calculate the knight moves evaluation, according to the knight moves forward and backward
     * @param move the move we investigate
     * @param player the player we evaluate for
     * @return the evaluation for the knight mobility
     */
    public static double calcKnightMoves(Move move, Player player)
    {
        int forwardMove = 0;
        int backwardMove = 0;
        if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirection(player) >= 6)
            forwardMove++;
        else if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirection(player) <= -6)
            backwardMove++;
        return forwardMove * KNIGHT_FORWARD_MOVES_MULTIPLIER + backwardMove * KNIGHT_BACKWARD_MOVES_MULTIPLIER;
    }

    /**
     * this function calculate the bishop moves evaluation, according to the bishop moves forward and backward
     * @param move the move we investigate
     * @param player the player we evaluate for
     * @return the evaluation for the bishop mobility
     */
    public static double calcBishopMoves(Move move, Player player)
    {
        int forwardMove = 0;
        int backwardMove = 0;
        if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirection(player) >= 7)
            forwardMove++;
        else if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirection(player) <= -7)
            backwardMove++;
        return forwardMove * BISHOP_FORWARD_MOVES_MULTIPLIER + backwardMove * BISHOP_BACKWARD_MOVES_MULTIPLIER;
    }

    /**
     * get the movement direction for the player
     * @param player the player
     * @return the direction of the player
     */
    public static int getDirection(Player player)
    {
        if(player.getColor() == Color.White)
            return -1;
        return 1;
    }
}
