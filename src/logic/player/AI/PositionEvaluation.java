package logic.player.AI;

import logic.Board;
import logic.Pieces.Piece;
import logic.player.Player;

public class PositionEvaluation {
    public static double evaluate(Board board)
    {
        return (score(board, board.getWhitePlayer()) - score(board, board.getBlackPlayer()));
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
