package logic.player.AI;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.player.Player;

import java.util.List;

public class CenterControl {
    private static final double ATTACKS_ON_ENEMY_CENTER_MULTIPLIER = 0.1;
    private static final double ATTACKS_ON_SELF_CENTER_MULTIPLIER = 0.05;
    private static final double PAWNS_ON_CENTER_MULTIPLIER = 0.15;
    private static final int[] CENTER_COORDINATE = {27, 28, 35, 36};


    public static double centerControl(Player player, Board board)
    {
        List<Move> allPlayersLegalMoves = player.getLegalMoves();
        System.out.println("\n center: " + "\n Enemycenter:" + calculateAttacksOnEnemyCenter(player, allPlayersLegalMoves) +
                "\nself center: " + calculateAttacksOnSelfCenter(player, allPlayersLegalMoves) + "\n pawns on center: " +
                pawnsOnCenter(player, board));
        return calculateAttacksOnEnemyCenter(player, allPlayersLegalMoves) +
                calculateAttacksOnSelfCenter(player, allPlayersLegalMoves) +
                pawnsOnCenter(player, board);
    }

    private static double pawnsOnCenter(Player player, Board board) {
        int numberOfPawnsOnCenter = 0;
        for(int i : CENTER_COORDINATE)
        {
            if(board.getPieceAtCoordinate(i) != null && board.getPieceAtCoordinate(i).getColor() == player.getColor())
                numberOfPawnsOnCenter++;
        }
        return numberOfPawnsOnCenter * PAWNS_ON_CENTER_MULTIPLIER;
    }

    public static double calculateAttacksOnEnemyCenter(Player player, List<Move> allPlayersLegalMoves)
    {
        double attacksOnBoxVal = 0;
        if(player.getColor() == Color.White) {
            attacksOnBoxVal = Player.getAttacksOnBox(27, allPlayersLegalMoves).size() + Player.getAttacksOnBox(28, allPlayersLegalMoves).size();

        }
        else if(player.getColor() == Color.Black) {
            attacksOnBoxVal = Player.getAttacksOnBox(35, allPlayersLegalMoves).size() + Player.getAttacksOnBox(36, allPlayersLegalMoves).size();
        }
        return attacksOnBoxVal * ATTACKS_ON_ENEMY_CENTER_MULTIPLIER;
    }

    public static double calculateAttacksOnSelfCenter(Player player, List<Move> allPlayersLegalMoves)
    {
        double attacksOnBoxVal = 0;
        if(player.getColor() == Color.White) {
            attacksOnBoxVal = Player.getAttacksOnBox(35, allPlayersLegalMoves).size() + Player.getAttacksOnBox(36, allPlayersLegalMoves).size();

        }
        else if(player.getColor() == Color.Black) {
            attacksOnBoxVal = Player.getAttacksOnBox(27, allPlayersLegalMoves).size() + Player.getAttacksOnBox(28, allPlayersLegalMoves).size();
        }
        return attacksOnBoxVal * ATTACKS_ON_SELF_CENTER_MULTIPLIER;
    }


}
