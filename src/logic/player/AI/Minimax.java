package logic.player.AI;

import com.google.common.collect.Ordering;
import gui.Result;
import logic.Board;
import logic.Color;
import logic.Move;
import logic.MoveTransition;
import logic.player.Player;

import java.util.*;

import static logic.player.AI.CachedData.Type.*;

/**
 * this class contains static methods for the Minimax with alpha-beta pruning to find the best move for a player
 */
public class Minimax {
    private static int quiescenceCount = 0;
    private static final int MAX_QUIESCENCE = 5000 * 5;
    private static Map<Long, CachedData> transpositionTable = new HashMap<>();

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
        double bestValue = Double.NEGATIVE_INFINITY;
        double currentValue;
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
                // if white turn, call min, else call max
                currentValue = -1* alphaBetaTT(moveTransition.getToBoard(), depth - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                // if white and we found bigger
                if (currentValue > bestValue) {
                    bestValue = currentValue;
                    bestMove = move;
                }
            }
        }
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
                return 2;
            }
        }
        return depth - 1;
    }

    /**
     * this function generate a hash code key for a specific board to put or find in the transposition table
     * @param board the board to get his key
     * @return long of the key of the board for the transposition table
     */
    private static long HashCode(Board board)
    {
        return Zobrist.getKeyForBoard(board);
    }

    /**
     * this function implement the negaMax(minimax) white alpha-beta pruning and transposition table, and it returns an evaluation of the board with forward looking
     * @param board the board to find the best move in
     * @param depth the search depth in the tree
     * @param alpha the biggest value we saw
     * @param beta the lowest value we saw
     * @return the evaluation of the board
     */
    public static double alphaBetaTT(Board board, int depth, double alpha, double beta)
    {
        double value;
        CachedData tte = transpositionTable.get(HashCode(board));
        if(tte != null && tte.getDepth() >= depth)
        {
            if(tte.getType() == EXACT_VALUE) // stored value is exact
                return tte.getScore();
            if(tte.getType() == LOWERBOUND && tte.getScore() > alpha)
                alpha = tte.getScore(); // update lowerbound alpha if needed
            else if(tte.getType() == UPPERBOUND && tte.getScore() < beta)
                beta = tte.getScore(); // update upperbound beta if needed
            if(alpha >= beta)
                return tte.getScore(); // if lowerbound surpasses upperbound
        }
        if(depth == 0 || board.gameResult() != Result.NOT_FINISHED)
        {
            if(board.gameResult() == Result.DRAW)
                return 0;
            value = PositionEvaluation.evaluate(board);
            if(value <= alpha) // a lowerbound value
                transpositionTable.put(HashCode(board), new CachedData(depth, value, LOWERBOUND));
                //StoreTTEntry(board.getHashKey(), value, LOWERBOUND, depth);
            else if(value >= beta) // an upperbound value
                transpositionTable.put(HashCode(board), new CachedData(depth, value, UPPERBOUND));
                //StoreTTEntry(board.getHashKey(), value, UPPERBOUND, depth);
            else // a true minimax value
                transpositionTable.put(HashCode(board), new CachedData(depth, value, LOWERBOUND));
            return value;
        }
        double best = Double.NEGATIVE_INFINITY;
        for(Move move : sortMoves(board.getTurn().getLegalMoves()))
        {
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                value = -alphaBetaTT(moveTransition.getToBoard(), calculateQuiescenceDepth(moveTransition.getToBoard(), depth),
                        -beta, -alpha);
                if (value > best)
                    best = value;
                if (best > alpha)
                    alpha = best;
                if (best >= beta)
                    break;
            }

        }
        if(best <= alpha) // a lowerbound value
            transpositionTable.put(HashCode(board), new CachedData(depth, best, LOWERBOUND));
                    //board.getHashKey(), best, LOWERBOUND, depth);
        else if(best >= beta) // an upperbound value
            transpositionTable.put(HashCode(board), new CachedData(depth, best, UPPERBOUND));
            //StoreTTEntry(board.getHashKey(), best, UPPERBOUND, depth);
        else // a true minimax value
            transpositionTable.put(HashCode(board), new CachedData(depth, best, EXACT_VALUE));
        return best;
    }

}
