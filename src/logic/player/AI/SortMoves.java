package logic.player.AI;

import com.google.common.collect.ComparisonChain;
import logic.Move;

import java.util.Comparator;

public class SortMoves implements Comparator<Move> {
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
