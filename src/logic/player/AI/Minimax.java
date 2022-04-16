package logic.player.AI;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.MoveTransition;
import logic.player.Player;


public class Minimax {
    public static Move execute(final Board board, int depth) {
        final Player turn = board.getTurn();
        final Color color = turn.getColor();
        Move bestMove = null;
        double highestSeenValue = Double.NEGATIVE_INFINITY;
        double lowestSeenValue = Double.POSITIVE_INFINITY;
        double currentValue;
        for (final Move move : turn.getLegalMoves()) {
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                currentValue = color == Color.White ?
                        min(moveTransition.getToBoard(), depth - 1, highestSeenValue, lowestSeenValue) :
                        max(moveTransition.getToBoard(), depth - 1, highestSeenValue, lowestSeenValue);
                if (color == Color.White && currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (color == Color.Black && currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }

    public static double max(final Board board,
                   final int depth,
                   final double highest,
                   final double lowest) {
        if (depth == 0 ||  board.getTurn().isInCheckMate() ||
                board.getTurn().isInStaleMate()) {
            return PositionEvaluation.evaluate(board);
        }
        double currentHighest = highest;
        for (final Move move : board.getTurn().getLegalMoves()) {
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                currentHighest = Math.max(currentHighest, min(moveTransition.getToBoard(),
                        depth-1, currentHighest, lowest));
                if (lowest <= currentHighest) {
                    break;
                }
            }
        }
        return currentHighest;
    }

    public static double min(final Board board,
                   final int depth,
                   final double highest,
                   final double lowest) {
        if (depth == 0 ||  board.getTurn().isInCheckMate() ||
                board.getTurn().isInStaleMate()) {
            return PositionEvaluation.evaluate(board);
        }
        double currentLowest = lowest;
        for (final Move move : board.getTurn().getLegalMoves()) {
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                currentLowest = Math.min(currentLowest, max(moveTransition.getToBoard(),
                        depth - 1, highest, currentLowest));
                if (currentLowest <= highest) {
                    break;
                }
            }
        }
        return currentLowest;
    }
}
