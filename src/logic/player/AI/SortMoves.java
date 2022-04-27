package logic.player.AI;

import com.google.common.collect.ComparisonChain;
import logic.Move;

import java.util.Comparator;
/**
 * this class implements the Comparator interface and used for compare between moves
 * to make a Move ordering strategy
 */
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
                .compareTrueFirst(m1.isPawnPromotion(), m2.isPawnPromotion())
                .compareTrueFirst(m1.isAttack(), m2.isAttack())
                .compareTrueFirst(m1.isAttack() && ((Move.AttackMove)m1).getAttackedPiece().value >= m1.getPieceMoved().value,
                        m2.isAttack() && ((Move.AttackMove)m2).getAttackedPiece().value >= m2.getPieceMoved().value)
                .compareTrueFirst(m1.isCastle(), m2.isCastle())
                .compare(m2.getPieceMoved().value, m1.getPieceMoved().value)
                .compareTrueFirst(m1.getPieceMoved().getPosition() < m1.getCoordinateMovedTo(),
                        m2.getPieceMoved().getPosition() < m2.getCoordinateMovedTo())
                .result();
    }
}
