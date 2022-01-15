package logic.player;

import logic.Board;
import logic.Move;

import java.util.List;

public class WhitePlayer extends Player{

    public WhitePlayer(Board board, List<Move> whiteLegalMoves, List<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }
}
