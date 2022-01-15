package logic.player;

import logic.Board;
import logic.Move;

import java.util.List;

public abstract class Player {
    protected Board board;
    protected List<Move> legalMoves;

    public Player(Board board, List<Move> legalMoves, List<Move> enemyLegalMoves) {
        this.board = board;
        this.legalMoves = legalMoves;
    }
}
