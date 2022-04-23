package logic.player.AI;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.player.Player;

import java.util.List;

/**
 * this class contains static methods for evaluating a Center control of a player
 */
public class CenterControl {
    private static final double ATTACKS_ON_ENEMY_CENTER_MULTIPLIER = 0.06;
    private static final double ATTACKS_ON_SELF_CENTER_MULTIPLIER = 0.025;
    private static final double PAWNS_ON_CENTER_MULTIPLIER = 0.08;
    private static final int[] CENTER_COORDINATE = {27, 28, 35, 36};


    public static double centerControl(Player player, Board board)
    {
        List<Move> allPlayersLegalMoves = player.getLegalMoves();
        //System.out.println("\n center: " + "\n Enemycenter:" + calculateAttacksOnEnemyCenter(player, allPlayersLegalMoves) +
               // "\nself center: " + calculateAttacksOnSelfCenter(player, allPlayersLegalMoves) + "\n pawns on center: " +
               // pawnsOnCenter(player, board));
        return calculateAttacksOnEnemyCenter(player, allPlayersLegalMoves) +
                calculateAttacksOnSelfCenter(player, allPlayersLegalMoves) +
                pawnsOnCenter(player, board);
    }

    /**
     * this function calculate the pawns on the center evaluation
     * @param player the player we calculate his pawns on the center evaluation
     * @param board the current board
     * @return the evaluation of the pawns in the center for the player
     */
    private static double pawnsOnCenter(Player player, Board board) {
        int numberOfPawnsOnCenter = 0;
        for(int i : CENTER_COORDINATE)
        {
            if(board.getPieceAtCoordinate(i) != null && board.getPieceAtCoordinate(i).getColor() == player.getColor())
                numberOfPawnsOnCenter++;
        }
        return numberOfPawnsOnCenter * PAWNS_ON_CENTER_MULTIPLIER;
    }

    /**
     * this function calculate the attacks on the enemy center evaluation
     * @param player the player we calculate his attacks on the enemy center on the center evaluation
     * @param allPlayersLegalMoves all the player's legal moves
     * @return the evaluation of the attacks on enemy center the player
     */
    public static double calculateAttacksOnEnemyCenter(Player player, List<Move> allPlayersLegalMoves)
    {
        double attacksOnBoxVal = 0;
        if(player.getColor() == Color.White) {
            // calculate attacks on the white enemy center
            attacksOnBoxVal = Player.getAttacksOnBox(27, allPlayersLegalMoves).size() + Player.getAttacksOnBox(28, allPlayersLegalMoves).size();

        }
        else if(player.getColor() == Color.Black) {
            // calculate attacks on the black enemy center
            attacksOnBoxVal = Player.getAttacksOnBox(35, allPlayersLegalMoves).size() + Player.getAttacksOnBox(36, allPlayersLegalMoves).size();
        }
        return attacksOnBoxVal * ATTACKS_ON_ENEMY_CENTER_MULTIPLIER;
    }

    /**
     * this function calculate the attacks on the self center evaluation
     * @param player the player we calculate his attacks on the self center on the center evaluation
     * @param allPlayersLegalMoves all the player's legal moves
     * @return the evaluation of the attacks on self center the player
     */
    public static double calculateAttacksOnSelfCenter(Player player, List<Move> allPlayersLegalMoves)
    {
        double attacksOnBoxVal = 0;
        if(player.getColor() == Color.White) {
            // calculate attacks on the white self center
            attacksOnBoxVal = Player.getAttacksOnBox(35, allPlayersLegalMoves).size() + Player.getAttacksOnBox(36, allPlayersLegalMoves).size();

        }
        else if(player.getColor() == Color.Black) {
            // calculate attacks on the black self center
            attacksOnBoxVal = Player.getAttacksOnBox(27, allPlayersLegalMoves).size() + Player.getAttacksOnBox(28, allPlayersLegalMoves).size();
        }
        return attacksOnBoxVal * ATTACKS_ON_SELF_CENTER_MULTIPLIER;
    }


}
