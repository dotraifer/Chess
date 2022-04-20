package logic.player.AI;

import com.google.common.collect.Ordering;
import gui.Result;
import logic.Board;
import logic.Color;
import logic.Move;
import logic.MoveTransition;
import logic.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Minimax {
    private static int quiescenceCount = 0;
    private static final int MAX_QUIESCENCE = 5000 * 5;


    /**
     * this function uses Minimax with Alpha-Beta to find the best move for a player in the given board
     * @param board the board we want to return the best move for
     * @param depth the depth we want to calculate the board positions
     * @return the best move by the computer for the given board
     * @see <a href="https://www.youtube.com/watch?v=l-hh51ncgDI">
     */
    public static Move MiniMaxAB(final Board board, int depth) {
        final long startTime = System.currentTimeMillis();
        final Player turn = board.getTurn();
        final Color color = turn.getColor();
        Move bestMove = null;
        double highestSeenValue = Double.NEGATIVE_INFINITY;
        double lowestSeenValue = Double.POSITIVE_INFINITY;
        double currentValue;
        List<Move> SortedMoves = sortMoves(turn.getLegalMoves());
        for (final Move move : SortedMoves) {
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                // check if works
                if(moveTransition.getToBoard().getTurn().isInCheckMate())
                    return move;
                currentValue = color == Color.White ?
                        min(moveTransition.getToBoard(), depth - 1, highestSeenValue, lowestSeenValue) :
                        max(moveTransition.getToBoard(), depth - 1, highestSeenValue, lowestSeenValue);
                if (color == Color.White && currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (color == Color.Black && currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                    System.out.println(lowestSeenValue + " " + move);
                }
            }
        }
        long executionTime = System.currentTimeMillis() - startTime;
        //System.out.printf("ececitoin" + executionTime);
        System.out.println("best" + bestMove);
        System.out.println("highest" + highestSeenValue);
        System.out.println("lowest" + lowestSeenValue);
        return bestMove;
    }

    /**
     * sort the move by order
     * @param legalMoves the list of moves we want to sort
     * @return the sorted list of moves
     */
    private static List<Move> sortMoves(Collection<Move> legalMoves) {
        SortMoves sortMoves = new SortMoves();
        return Ordering.from(sortMoves).immutableSortedCopy(legalMoves);
    }

    public static double max(final Board board,
                   final int depth,
                   final double highest,
                   final double lowest) {
        if (depth == 0 ||  board.gameResult() != Result.NOT_FINISHED) {
            if(board.gameResult() == Result.DRAW)
                return 0;
            return PositionEvaluation.evaluate(board);
        }
        double currentHighest = highest;
        List<Move> SortedMoves = sortMoves(board.getTurn().getLegalMoves());
        for (final Move move : SortedMoves) {
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                currentHighest = Math.max(currentHighest, min(moveTransition.getToBoard(),
                        calculateQuiescenceDepth(moveTransition.getToBoard(), depth), currentHighest, lowest));
                if (currentHighest >= lowest) {
                    return lowest;
                }
            }
        }
        return currentHighest;
    }

    public static double min(final Board board,
                   final int depth,
                   final double highest,
                   final double lowest) {
        if (depth == 0 ||  board.gameResult() != Result.NOT_FINISHED) {
            if(board.gameResult() == Result.DRAW)
                return 0;
            return PositionEvaluation.evaluate(board);
        }
        double currentLowest = lowest;
        List<Move> SortedMoves = sortMoves(board.getTurn().getLegalMoves());
        for (final Move move : SortedMoves) {
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                currentLowest = Math.min(currentLowest, max(moveTransition.getToBoard(),
                        calculateQuiescenceDepth(moveTransition.getToBoard(), depth), highest, currentLowest));
                if (currentLowest <= highest) {
                    return highest;
                }
            }
        }
        return currentLowest;
    }


    private static String calculateTimeTaken(final long start, final long end) {
        final long timeTaken = (end - start) / 1000000;
        return timeTaken + " ms";
    }

    private static int calculateQuiescenceDepth(final Board toBoard,
                                         final int depth) {
        if(depth == 1 && quiescenceCount < MAX_QUIESCENCE) {
            int activityMeasure = 0;
            if (toBoard.getTurn().isInCheck()) {
                activityMeasure += 1;
            }

            if(toBoard.getTransitionMove().isPawnPromotion())
                activityMeasure += 2;
            if(toBoard.getTransitionMove().isAttack())
                activityMeasure += 2;
            for(final Move move: toBoard.lastNMoves(3)) {
                if(move.isAttack()) {
                    activityMeasure += 1;
                }
            }
            if(activityMeasure >= 2) {
                quiescenceCount++;
                return 3;
            }
        }
        return depth - 1;
    }

}
