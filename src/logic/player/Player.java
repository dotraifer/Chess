package logic.player;

import logic.*;
import logic.Pieces.King;
import logic.Pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    protected Board board;
    protected List<Move> legalMoves;
    protected Piece king;
    protected boolean isInCheck;

    public Player(Board board, List<Move> legalMoves, List<Move> enemyLegalMoves) {
        this.board = board;
        this.legalMoves = legalMoves;
        this.king = getKing();
        this.isInCheck = !getAttacksOnBox(king.getPosition(), enemyLegalMoves).isEmpty();
    }

    private Piece getKing() {
        for (Piece piece : getActivePieces()) {
            if(piece.getClass() == King.class)
                return piece;
        }
        return null;
    }

    public abstract List<Piece> getActivePieces();


    /**
     * find all the possible attack moves on a given box, and return list of it
     * @param boxPos the coordinate of the box
     * @param moves list of moves to check
     * @return the possible attack moves to a box on the board
     */
    private List<Move> getAttacksOnBox(int boxPos, List<Move> moves){
        List<Move> attackMoves = new ArrayList<>();
        for (Move move : moves)
        {
            if(boxPos == move.getCoordinateMovedTo())
                attackMoves.add(move);
        }
        return attackMoves;
    }
    public boolean isInCheck(){
        return isInCheck;
    }

    public boolean isInCheckMate() {
        // if in check and has no moves
        return isInCheck() && !isCanEscape();
    }

    public boolean isInStaleMate(){
        // if not in check but has no moves
        return !isInCheck() && !isCanEscape();
    }

    // TODO : implement method that check if the king can escape
    private boolean isCanEscape() {
        for (Move move : this.legalMoves){
            MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus() == MoveStatus.DONE)
                return true;
        }
        return false;
    }

    private MoveTransition makeMove(Move move) {
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.UNDONE);
        }
        Board transitionBoard = move.executeMove();
        List<Move> attacksOnKing = getAttacksOnBox(getKing().getPosition(), transitionBoard.getTurn().legalMoves);

        if(!attacksOnKing.isEmpty())
            return new MoveTransition(this.board, this.board, move, MoveStatus.LEFT_IN_CHECK);
        return new MoveTransition(this.board, transitionBoard, move, MoveStatus.DONE);
    }

    private boolean isMoveLegal(Move move) {
        return this.legalMoves.contains(move);
    }

    public abstract Color getColor();
}
