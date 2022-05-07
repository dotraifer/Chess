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
    private static final int MAX_QUIESCENCE = 5000;
    private static Map<Long, CachedData> transpositionTable = new HashMap<>();
    private static long start = 0;
    private static final long maxTime = 10000;
    private static boolean timeout;


    /**
     * using the iterative deepening idea to go to the max calculating depth in the given time
     * @param board the board we're evaluating using the iterative deepening
     * @return the best Move
     */
    public static Move IterativeDeepening(Board board)
    {
        // init tt
        if(board.getMovesWithoutEat() == 0)
           transpositionTable.clear();
        Move bestMove;
        timeout = false;
        Move move = null;
        start = System.currentTimeMillis();
        for(int d = 1;;d++)
        {
            bestMove = move;
            move = MiniMaxAB(board, d);
            if(timeout) {
                System.out.println("calculated with depth of " + (d - 1) + "\n");
                break;
            }
        }
        return bestMove;
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
        Move bestMove = null;
        double bestValue = -PositionEvaluation.MATE-1;
        double currentValue;
        // sort the moves
        List<Move> SortedMoves = sortMoves(turn.getLegalMoves());
        // for every possible move
        long start = System.currentTimeMillis();
        for (final Move move : SortedMoves) {
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                // if the move is checkmate-return him
                if(moveTransition.getToBoard().getTurn().isInCheckMate())
                    return move;
                // if white turn, call min, else call max
                currentValue = -1* alphaBetaTT(moveTransition.getToBoard(),depth - 1, -PositionEvaluation.MATE, PositionEvaluation.MATE);
                // if white and we found bigger
                if (currentValue > bestValue) {
                    bestValue = currentValue;
                    bestMove = move;
                }
            }
        }
        long end = System.currentTimeMillis();
        //System.out.println((end - start) / 1000f);
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
        if(System.currentTimeMillis() - start > maxTime) {
            timeout = true;
            return alpha;
        }
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
            if(depth != 0)
                return PositionEvaluation.evaluate(board);
            quiescenceCount = 0;
            long start = System.currentTimeMillis();
            value = Quiescence(board, alpha, beta);
            if(board.getTurn().getColor() == Color.White)
            {
                if(value <= alpha)
                    transpositionTable.put(HashCode(board), new CachedData(depth, value, UPPERBOUND));
                else
                    transpositionTable.put(HashCode(board), new CachedData(depth, value, EXACT_VALUE));

            }
            else
            {
                if(value > alpha)
                    transpositionTable.put(HashCode(board), new CachedData(depth, value, LOWERBOUND));
                else
                    transpositionTable.put(HashCode(board), new CachedData(depth, value, EXACT_VALUE));
            }
            return value;
        }
        double best = -PositionEvaluation.MATE-1;
        for(Move move : sortMoves(board.getTurn().getLegalMoves()))
        {
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                value = -alphaBetaTT(moveTransition.getToBoard(), depth - 1,
                        -beta, -alpha);
                if (value > best)
                    best = value;
                if (best > alpha)
                    alpha = best;
                if (best >= beta)
                    break;
            }

        }
        if(board.getTurn().getColor() == Color.White)
        {
            if(best <= alpha)
                transpositionTable.put(HashCode(board), new CachedData(depth, best, UPPERBOUND));
            else
                transpositionTable.put(HashCode(board), new CachedData(depth, best, EXACT_VALUE));

        }
        else
        {
            if(best > alpha)
                transpositionTable.put(HashCode(board), new CachedData(depth, best, LOWERBOUND));
            else
                transpositionTable.put(HashCode(board), new CachedData(depth, best, EXACT_VALUE));
        }
        return best;
    }

    /**
     * calculate the none quite moves when we got to the end of the depth
     * @param board the board we are in
     * @param alpha the alpha index
     * @param beta the beta index
     * @return the evaluation for the board after going through all the none quite moves
     */
    private static double Quiescence(Board board, double alpha, double beta ) {
        double stand_pat = PositionEvaluation.evaluate(board);
        alpha = Math.max(alpha, stand_pat);

        if(alpha >= beta)
        {
            return stand_pat;
        }
        List<Move> sortedMoves = sortMoves(board.getTurn().getLegalMoves());
        for(Move move : sortedMoves)  {
            if(move.isAttack() || move.isPawnPromotion())
            {
                final MoveTransition moveTransition = board.getTurn().makeMove(move);
                if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE)
                {
                    double score = -Quiescence( moveTransition.getToBoard(), -beta, -alpha );

                    stand_pat = Math.max(stand_pat, score);

                    alpha = Math.max(alpha, stand_pat);

                    if(alpha >= beta)
                        break;
                }
            }
        }
        return stand_pat;
    }

}
