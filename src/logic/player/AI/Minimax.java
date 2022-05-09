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
    private static final long maxTime = 20000;
    private static boolean timeout;
    // bigger then this value is surely mate
    private static final double BIGGER_IS_MATE = PositionEvaluation.MATE- 1000;


    /**
     * using the iterative deepening idea to go to the max calculating depth in the given time
     * @param board the board we're evaluating using the iterative deepening
     * @return the best Move
     */
    public static Move IterativeDeepening(Board board)
    {
        // init tt when eating or pawn move
        if(board.getMovesWithoutEat() == 0)
           transpositionTable.clear();
        Move bestMove;
        timeout = false;
        Move move = null;
        start = System.currentTimeMillis();
        for(int d = 1;;d++)
        {
            // put in the best move the last minimax result
            bestMove = move;
            // put in move the minimax with the current board
            move = MiniMaxAB(board, d);
            // if timeout
            if(timeout) {
                System.out.println("calculated with depth of " + (d - 1) + "\n");
                break;
            }
        }
        // return the move from the one before the last minimax call(the move from the highest depth that didn't cause timeout)
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
                // call the negaMax recursion
                currentValue = -1* alphaBetaTT(moveTransition.getToBoard(),depth - 1, -PositionEvaluation.MATE, PositionEvaluation.MATE, 0);
                // if we found bigger value
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
     * @param distanceFromRoot the distance of the current position from the root position
     * @see <a href="https://en.wikipedia.org/wiki/Talk:Negamax"></a>
     * @return the evaluation of the board
     */
    public static double alphaBetaTT(Board board, int depth, double alpha, double beta, int distanceFromRoot)
    {
        // if we run out of time
        if(System.currentTimeMillis() - start > maxTime) {
            timeout = true;
            return alpha;
        }
        double value;
        // get the tt
        CachedData tte = transpositionTable.get(HashCode(board));
        // if the board is end position
        if(board.gameResult() != Result.NOT_FINISHED)
        {
            return PositionEvaluation.evaluate(board, distanceFromRoot);
        }
        // if the tt found and his calculation depth bigger then the current
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
        // if the depth is finished
        if(depth == 0)
        {
            quiescenceCount = 0;
            // evaluate the quiescence
            value = Quiescence(board, alpha, beta, distanceFromRoot);
            if(board.getTurn().getColor() == Color.White)
            {
                // if lower equal than the alpha score
                if(value <= alpha)
                    transpositionTable.put(HashCode(board), new CachedData(depth, value, UPPERBOUND));
                else
                    transpositionTable.put(HashCode(board), new CachedData(depth, value, EXACT_VALUE));

            }
            else
            {
                // if bigger equal then the biggest score
                if(value > alpha)
                    transpositionTable.put(HashCode(board), new CachedData(depth, value, LOWERBOUND));
                else
                    transpositionTable.put(HashCode(board), new CachedData(depth, value, EXACT_VALUE));
            }
            return value;
        }
        double best = -PositionEvaluation.MATE-1;
        // for every move
        for(Move move : sortMoves(board.getTurn().getLegalMoves()))
        {
            // make the move
            final MoveTransition moveTransition = board.getTurn().makeMove(move);
            if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE) {
                // call the recursion
                value = -alphaBetaTT(moveTransition.getToBoard(), depth - 1,
                        -beta, -alpha, distanceFromRoot + 1);
                if (value > best)
                    best = value;
                if (best > alpha)
                    alpha = best;
                // cut-off
                if (best >= beta)
                    break;
            }

        }
        // if white, and the score ss not for mate(we don't store mates positions)
        if(board.getTurn().getColor() == Color.White && best < BIGGER_IS_MATE && best > -BIGGER_IS_MATE)
        {
            // if lower than the biggest score
            if(best <= alpha)
                transpositionTable.put(HashCode(board), new CachedData(depth, best, UPPERBOUND));
            else
                transpositionTable.put(HashCode(board), new CachedData(depth, best, EXACT_VALUE));

        }
        // if black, and the score ss not for mate(we don't store mates positions)
        else if(best < BIGGER_IS_MATE && best > -BIGGER_IS_MATE)
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
     * @param distanceFromRoot the distance of the current position from the root position
     * @return the evaluation for the board after going through all the none quite moves
     */
    private static double Quiescence(Board board, double alpha, double beta , int distanceFromRoot) {
        // evaluate the position
        double stand_pat = PositionEvaluation.evaluate(board, distanceFromRoot);
        // alpha is max
        alpha = Math.max(alpha, stand_pat);
        if(alpha >= beta)
        {
            return stand_pat;
        }
        // sort the moves
        List<Move> sortedMoves = sortMoves(board.getTurn().getLegalMoves());
        for(Move move : sortedMoves)  {
            if(move.isAttack() || move.isPawnPromotion())
            {
                final MoveTransition moveTransition = board.getTurn().makeMove(move);
                if (moveTransition.getMoveStatus() == Move.MoveStatus.DONE)
                {
                    // call the recursion again
                    double score = -Quiescence( moveTransition.getToBoard(), -beta, -alpha, distanceFromRoot + 1 );

                    stand_pat = Math.max(stand_pat, score);

                    alpha = Math.max(alpha, stand_pat);
                    // cut-off
                    if(alpha >= beta)
                        break;
                }
            }
        }
        return stand_pat;
    }

}
