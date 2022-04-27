package logic.player.AI;

import com.google.common.collect.Ordering;
import com.google.common.hash.HashCode;
import gui.Result;
import logic.Board;
import logic.Color;
import logic.Move;
import logic.MoveTransition;
import logic.player.Player;

import java.util.*;

/**
 * this class contains static methods for the Minimax with alpha-beta pruning to find the best move for a player
 */
public class Minimax {
    private static int quiescenceCount = 0;
    private static final int MAX_QUIESCENCE = 5000 * 5;
    private static Map<Long, CachedData> transpositionTable = new HashMap<>();

    public enum Flag
    {
        VALUE, UPPERBOUND, LOWERBOUND
    }

    /**
     * this function uses Minimax with Alpha-Beta to find the best move for a player in the given board
     * @param board the board we want to return the best move for
     * @param depth the depth we want to calculate the board positions
     * @return the best move by the computer for the given board
     * @see <a href="https://www.youtube.com/watch?v=l-hh51ncgDI">
     */
    public static Move MiniMaxAB(final Board board, int depth) {
        final Player turn = board.getTurn();
        final Color color = turn.getColor();
        Move bestMove = null;
        double highestSeenValue = Double.NEGATIVE_INFINITY;
        double lowestSeenValue = Double.POSITIVE_INFINITY;
        double currentValue = color == Color.White ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        quiescenceCount = 0;
        // sort the moves
        List<Move> SortedMoves = sortMoves(turn.getLegalMoves());
        // for every possible move
        for (final Move move : SortedMoves) {
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                // if the move is checkmate-return him
                if(moveTransition.getToBoard().getTurn().isInCheckMate())
                    return move;
                long boardIndex = HashCode(moveTransition.getToBoard());
                CachedData key = transpositionTable.get(boardIndex);
                //if(key != null && key.getDepth() >= depth)
                //{
                //    currentValue = color == Color.White ? Math.max(key.getScore(), currentValue) :
                //    Math.min(key.getScore(), currentValue);
                //}
                // if white turn, call min, else call max
                //else {
                    CachedData cd = new CachedData();
                    cd.setTurnColor(moveTransition.getToBoard().getTurn().getColor());
                    cd.setDepth(depth);
                    currentValue = color == Color.White ?
                            min(moveTransition.getToBoard(), depth - 1, highestSeenValue, lowestSeenValue) :
                            max(moveTransition.getToBoard(), depth - 1, highestSeenValue, lowestSeenValue);
                    cd.setScore(currentValue);
                    transpositionTable.put(boardIndex, cd);

                //}
                // if white and we found bigger
                if (color == Color.White && currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                    // if black and we found bigger
                } else if (color == Color.Black && currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        CachedData cd = new CachedData();
        cd.setTurnColor(board.getTurn().getColor());
        cd.setDepth(depth);
        if (board.getTurn().getColor() == Color.White) {
            cd.setScore(highestSeenValue);
        } else {
            cd.setScore(lowestSeenValue);
        }
        transpositionTable.put(HashCode(board), cd);
        return bestMove;
    }

    /**
     * sort the move by order
     * @param legalMoves the list of moves we want to sort
     * @return the sorted list of moves
     */
    private static List<Move> sortMoves(Collection<Move> legalMoves) {
        SortMoves sortMoves = new SortMoves();
        // sort the moves by order
        return Ordering.from(sortMoves).immutableSortedCopy(legalMoves);
    }

    /**
     * this function finds the best move for the max player
     * @param board the board we are in
     * @param depth the tree depth left
     * @param highest the highest value we have seen
     * @param lowest the lowest value we have seen
     * @return the highest move evaluation for the max player
     */
    public static double max(final Board board,
                   final int depth,
                   final double highest,
                   final double lowest) {

        CachedData cd = new CachedData();
        // depth end of the depth search or game came to result
        if (depth == 0 ||  board.gameResult() != Result.NOT_FINISHED) {
            // if the game is draw-return 0;
            if(board.gameResult() == Result.DRAW)
                return 0;
            // return the position evaluation
            return PositionEvaluation.evaluate(board);
        }
        double currentHighest = highest;
        // sort the moves
        List<Move> SortedMoves = sortMoves(board.getTurn().getLegalMoves());
        // for every move
        for (final Move move : SortedMoves) {
            // make the move
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            // if the move legal
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                long boardIndex = HashCode(moveTransition.getToBoard());
                CachedData key = transpositionTable.get(boardIndex);
                //if(key != null && key.getDepth() >= depth)
                //{
                //    currentHighest = Math.max(currentHighest, key.getScore());
                //}
                //else {
                cd = new CachedData();
                cd.setTurnColor(moveTransition.getToBoard().getTurn().getColor());
                cd.setDepth(depth);
                currentHighest = Math.max(currentHighest, min(moveTransition.getToBoard(),
                        calculateQuiescenceDepth(moveTransition.getToBoard(), depth), currentHighest, lowest));
                cd.setScore(currentHighest);
                //}
                if (currentHighest >= lowest) {
                    cd.setScore(lowest);
                    return lowest;
                }
                transpositionTable.put(boardIndex, cd);
            }
        }
        return currentHighest;
    }

    /**
     * this function finds the best move for the min player
     * @param board the board we are in
     * @param depth the tree depth left
     * @param highest the highest value we have seen
     * @param lowest the lowest value we have seen
     * @return the minimum move evaluation for the min player
     */
    public static double min(final Board board,
                   final int depth,
                   final double highest,
                   final double lowest) {
        CachedData cd = new CachedData();
        // depth end of the depth search or game came to result
        if (depth == 0 ||  board.gameResult() != Result.NOT_FINISHED) {
            if(board.gameResult() == Result.DRAW)
                // if the game is draw-return 0;
                return 0;
            // return the position evaluation
            return PositionEvaluation.evaluate(board);
        }
        double currentLowest = lowest;
        // sort the moves
        List<Move> SortedMoves = sortMoves(board.getTurn().getLegalMoves());
        // for every move
        for (final Move move : SortedMoves) {
            // make the move
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            // if the move legal
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                long boardIndex = HashCode(moveTransition.getToBoard());
// some time passes
                CachedData key = transpositionTable.get(boardIndex);

                //if(key != null && key.getDepth() >= depth){
                //    currentLowest = Math.min(currentLowest, key.getScore());
                //}
                //else {
                cd = new CachedData();
                cd.setTurnColor(moveTransition.getToBoard().getTurn().getColor());
                cd.setDepth(depth);
                currentLowest = Math.min(currentLowest, max(moveTransition.getToBoard(),
                        calculateQuiescenceDepth(moveTransition.getToBoard(), depth), highest, currentLowest));
                cd.setScore(currentLowest);
                //}

                if (currentLowest <= highest) {
                    cd.setScore(lowest);
                    return highest;
                }
                transpositionTable.put(boardIndex, cd);

            }
        }
        return currentLowest;
    }


    private static String calculateTimeTaken(final long start, final long end) {
        final long timeTaken = (end - start) / 1000000;
        return timeTaken + " ms";
    }

    /**
     * this function calculate the quiescence needed depth
     * @param toBoard the board we move to
     * @param depth the current depth
     * @return the quiescence new depth
     */
    private static int calculateQuiescenceDepth(final Board toBoard,
                                         final int depth) {
        // if the depth is 1, and we didn't pass the Max quiescence limit, check for non quit moves
        if(depth == 1 && quiescenceCount < MAX_QUIESCENCE) {
            int activityMeasure = 0;
            if (toBoard.getTurn().isInCheck()) {
                activityMeasure += 1;
            }

            if(toBoard.getTransitionMove().isPawnPromotion())
                activityMeasure += 2;
            if(toBoard.getTransitionMove().isAttack())
                activityMeasure += 2;
            if(toBoard.getTransitionMove().isPawnThreat())
                activityMeasure += 1;
            for(final Move move: toBoard.lastNMoves(3)) {
                if(move.isAttack()) {
                    activityMeasure += 1;
                }
            }
            // if the activity measure is bigger or equal to 2, add depth
            if(activityMeasure >= 2) {
                quiescenceCount++;
                return 3;
            }
        }
        return depth - 1;
    }

    private static long HashCode(Board board)
    {
        return Zobrist.getKeyForBoard(board);
    }

}
