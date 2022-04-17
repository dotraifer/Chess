package logic.player.AI;

import logic.Board;
import logic.Move;
import logic.Pieces.Piece;
import logic.player.Player;

import java.util.List;

public class PositionEvaluation {
    private static double MOBILITY_VALUE_OPENING = 1;
    private static double MOBILITY_VALUE_MIDGAME = 0.8;
    private static double MOBILITY_VALUE_ENDING = 0.4;
    private static double ATTACK_MULTIPLIER = 0.01;
    public static double evaluate(Board board)
    {
        GameStage gameStage = calculateGameStage(board);
        return (score(board, board.getWhitePlayer(), gameStage) - score(board, board.getBlackPlayer(), gameStage
        ));
    }

    private static GameStage calculateGameStage(Board board) {
        double materialLeft = calcMaterial(board);
        if(materialLeft > 60)
            return GameStage.OPENING;
        else if(materialLeft <= 60 && materialLeft > 28)
            return GameStage.MIDGAME;
        else if(materialLeft <= 28)
            return GameStage.ENDING;
        return GameStage.OPENING;
    }

    private static double calcMaterial(Board board) {
        double materialSum = 0;
        for(Piece piece : board.board_state.values())
        {
            if(piece != null)
                materialSum += piece.value;

        }
        return materialSum;
    }

    public static double score(Board board, Player player, GameStage gameStage)
    {
        List<Piece> allActivePieces = player.getActivePieces();
        return switch (gameStage) {
            case OPENING -> Material.material(player, allActivePieces) +
                    Mobility.mobility(player) * MOBILITY_VALUE_OPENING+
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    checkmate(player) + attacks(player) + CenterControl.centerControl(player, board);
            case MIDGAME -> Material.material(player, allActivePieces) +
                    Mobility.mobility(player) * MOBILITY_VALUE_MIDGAME+
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    checkmate(player) + attacks(player);
            case ENDING -> Material.material(player, allActivePieces) +
                    Mobility.mobility(player) * MOBILITY_VALUE_ENDING +
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    checkmate(player) + attacks(player);
        };
    }

    private static double checkmate(Player player) {
        return (player.getRival().isInCheckMate() ?  10000 :  0);
    }

    private static double attacks(final Player player) {
        int attackScore = 0;
        for(final Move move : player.getLegalMoves()) {
            if(move instanceof Move.AttackMove) {
                final Piece movedPiece = move.getPieceMoved();
                final Piece attackedPiece = ((Move.AttackMove) move).getAttackedPiece();
                if(movedPiece.value <= attackedPiece.value) {
                    attackScore++;
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }



}
