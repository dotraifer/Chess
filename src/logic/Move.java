package logic;

import logic.Pieces.Piece;

public class Move {
    private Board board;
    private Piece pieceMoved;
    private int coordinateMovedTo;

    public Move(Board board, Piece pieceMoved, int coordinateMovedTo) {
        this.board = board;
        this.pieceMoved = pieceMoved;
        this.coordinateMovedTo = coordinateMovedTo;
    }

    public int getCoordinateMovedTo() {
        return coordinateMovedTo;
    }

    @Override
    public String toString() {
        return "Move{" +
                ", pieceMoved=" + pieceMoved +
                ", coordinateMovedTo=" + coordinateMovedTo +
                '}';
    }

    public Board executeMove() {
        Board new_board = new Board();
        for(Piece piece : board.getTurn().getActivePieces())
        {
            // TODO : is override equals needed?
            if(!pieceMoved.equals(piece))
                new_board.board_state.put(piece.getPosition(), piece);
        }
        for(Piece piece : board.getOponnent().getActivePieces())
        {
            new_board.board_state.put(piece.getPosition(), piece);
        }
        // 22
        new_board.board_state.put(pieceMoved.getPosition(), null);
        new_board.board_state.put(this.coordinateMovedTo, pieceMoved);
        pieceMoved.movePiece(this);
        new_board.setTurn(board.getOponnent());
        return new_board;
    }
}
