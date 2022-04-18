package logic.player.AI;

import com.google.common.collect.ComparisonChain;
import logic.Move;

import java.util.Comparator;

public class SortMoves implements Comparator<Move> {
    /**
     * this function is used to sort the list of moves by their possibility to be good
     * the function compare between the first move and the second
     * @param m1 first move
     * @param m2 second move
     * @return the int value of the compartment
     */
    @Override
    public int compare(Move m1, Move m2) {
        return ComparisonChain.start()
                .compareTrueFirst(m1.isAttack(), m2.isAttack())
                .compareTrueFirst(m1.isCastle(), m2.isCastle())
                .compareTrueFirst(m1.getPieceMoved().getPosition() < m1.getCoordinateMovedTo(),
                        m2.getPieceMoved().getPosition() < m2.getCoordinateMovedTo())
                .compare(m2.getPieceMoved().value, m1.getPieceMoved().value)
                .result();
    }
}
