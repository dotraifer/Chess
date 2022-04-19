package logic.player.AI;

import logic.Color;
import logic.Move;
import logic.Pieces.Bishop;
import logic.Pieces.Knight;
import logic.Pieces.Rook;
import logic.player.Player;

public class Mobility {
    private final static double ROOK_FORWARD_MOVES_MULTIPLIER = 0.015;
    private final static double ROOK_SIDE_MOVES_MULTIPLIER = 0.007;
    private final static double ROOK_BACKWARD_MOVES_MULTIPLIER = 0.002;
    private final static double BISHOP_FORWARD_MOVES_MULTIPLIER = 0.01;
    private final static double BISHOP_BACKWARD_MOVES_MULTIPLIER = 0.005;
    private final static double KNIGHT_FORWARD_MOVES_MULTIPLIER = 0.007;
    private final static double KNIGHT_BACKWARD_MOVES_MULTIPLIER = 0.003;
    /**
     * this function evaluate player mobility
     * @param player the player we calculate his mobility
     * @return evaluation of the player's mobility
     */
    public static double mobility(Player player) {
        double mobilityValue = 0;
        for(Move move : player.getLegalMoves()) {
            if (Rook.class.equals(move.getPieceMoved().getClass())) {
                mobilityValue += calcRookMoves(move, player);
            } else if (Bishop.class.equals(move.getPieceMoved().getClass())) {
                mobilityValue += calcBishopMoves(move, player);
            } else if (Knight.class.equals(move.getPieceMoved().getClass())) {
                mobilityValue += calcKnightMoves(move, player);
            } else
                mobilityValue += 0.001;
        }
        return mobilityValue;
    }

    public static double calcRookMoves(Move move, Player player)
    {
        int sideMove = 0;
        int forwardMove = 0;
        int backwardMove = 0;
        if(Math.abs(move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) <8 )
            sideMove++;
        else if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirectiron(player) >= 8)
            forwardMove++;
        else if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirectiron(player) <= -8)
            backwardMove++;
        return sideMove * ROOK_SIDE_MOVES_MULTIPLIER + forwardMove * ROOK_FORWARD_MOVES_MULTIPLIER + backwardMove * ROOK_BACKWARD_MOVES_MULTIPLIER;
    }

    public static double calcKnightMoves(Move move, Player player)
    {
        int forwardMove = 0;
        int backwardMove = 0;
        if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirectiron(player) >= 6)
            forwardMove++;
        else if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirectiron(player) <= -6)
            backwardMove++;
        return forwardMove * KNIGHT_FORWARD_MOVES_MULTIPLIER + backwardMove * KNIGHT_BACKWARD_MOVES_MULTIPLIER;
    }

    public static double calcBishopMoves(Move move, Player player)
    {
        int forwardMove = 0;
        int backwardMove = 0;
        if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirectiron(player) >= 7)
            forwardMove++;
        else if((move.getCoordinateMovedTo() - move.getPieceMoved().getPosition()) * getDirectiron(player) <= -7)
            backwardMove++;
        return forwardMove * BISHOP_FORWARD_MOVES_MULTIPLIER + backwardMove * BISHOP_BACKWARD_MOVES_MULTIPLIER;
    }

    public static int getDirectiron(Player player)
    {
        if(player.getColor() == Color.White)
            return -1;
        return 1;
    }
}
