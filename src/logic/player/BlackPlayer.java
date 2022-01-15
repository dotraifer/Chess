package logic.player;

import logic.Board;
import logic.Move;

import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(Board board, List<Move> whiteLegalMoves, List<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }
}
