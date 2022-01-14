package logic;

import logic.Pieces.Piece;

import java.util.HashMap;
import java.util.Map;

public class Board {
    public HashMap<Integer, Piece> board_state = new HashMap<Integer, Piece>();
    public Board(HashMap<Integer, Piece> board_state) {
        this.board_state = board_state;
    }
}
