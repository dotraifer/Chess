package logic.player.AI;

import logic.Board;
import logic.Pieces.Piece;
import logic.player.Player;

import java.util.List;

public class PositionEvaluation {
    private static double MOBILITY_VALUE_OPENING = 1;
    private static double MOBILITY_VALUE_MIDGAME = 0.8;
    private static double MOBILITY_VALUE_ENDING = 0.4;
    public static double evaluate(Board board)
    {
        GameStage gameStage = calculateGameStage(board);
        return (score(board, board.getWhitePlayer(), gameStage) - score(board, board.getBlackPlayer(), gameStage
        ));
    }

    private static GameStage calculateGameStage(Board board) {
        double materialLeft = Material.material(board.getWhitePlayer(), board.getWhitePlayer().getActivePieces()) + Material.material(board.getBlackPlayer(), board.getBlackPlayer().getActivePieces());
        System.out.println(materialLeft);
        if(materialLeft > 60)
            return GameStage.OPENING;
        else if(materialLeft <= 60 && materialLeft > 28)
            return GameStage.MIDGAME;
        else if(materialLeft <= 28)
            return GameStage.ENDING;
        return GameStage.OPENING;
    }

    public static double score(Board board, Player player, GameStage gameStage)
    {
        List<Piece> allActivePieces = player.getActivePieces();
        System.out.println(Material.material(player, allActivePieces) + " n" + player.getColor() + " " + Mobility.mobility(player) + "" + PawnStruct.pawnStruct(player, allActivePieces));
        return switch (gameStage) {
            case OPENING -> Material.material(player, allActivePieces) +
                    Mobility.mobility(player) * MOBILITY_VALUE_OPENING+
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    checkmate(player);
            case MIDGAME -> Material.material(player, allActivePieces) +
                    Mobility.mobility(player) * MOBILITY_VALUE_MIDGAME+
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    checkmate(player);
            case ENDING -> Material.material(player, allActivePieces) +
                    Mobility.mobility(player) * MOBILITY_VALUE_ENDING +
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    checkmate(player);
        };
    }

    private static double checkmate(Player player) {
        return (player.getRival().isInCheckMate() ?  10000 :  0);
    }



}
