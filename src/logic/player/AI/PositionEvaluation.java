package logic.player.AI;

import logic.Board;
import logic.Pieces.Piece;
import logic.player.Player;

public class PositionEvaluation {
    public static GameStage gameStage;
    public static double evaluate(Board board)
    {
        gameStage = calculateGameStage(board);
        System.out.println(gameStage);
        return (score(board, board.getWhitePlayer()) - score(board, board.getBlackPlayer()));
    }

    private static GameStage calculateGameStage(Board board) {
        double materialLeft = material(board.getWhitePlayer()) + material(board.getBlackPlayer());
        materialLeft -= 20000;
        System.out.println(materialLeft);
        if(materialLeft > 54)
            return GameStage.OPENING;
        else if(materialLeft <= 54 && materialLeft > 28)
            return GameStage.MIDGAME;
        else if(materialLeft <= 28)
            return GameStage.ENDING;
        return gameStage;
    }

    public static double score(Board board, Player player)
    {
        return material(player) + mobility(player) + checkmate(player);
    }

    private static double checkmate(Player player) {
        return (player.getRival().isInCheckMate() ?  10000 :  0);
    }

    private static double mobility(Player player) {
        return player.getLegalMoves().size() * 0.01;
    }

    public static double material(Player player)
    {
        double materialValue = 0;
        for(Piece piece : player.getActivePieces()) {
            materialValue += piece.getValue();
        }
        return materialValue;
    }

}
