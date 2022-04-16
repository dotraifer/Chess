package logic.player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import logic.*;
import logic.Pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    protected Board board;
    protected List<Move> legalMoves;
    protected Piece king;
    protected boolean isInCheck;
    public boolean isAi;

    public Player(Board board, List<Move> legalMoves, List<Move> enemyLegalMoves, boolean isAI) {
        this.board = board;
        this.king = findKing(board);
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateCastles(legalMoves, enemyLegalMoves)));
        this.isInCheck = !getAttacksOnBox(king.getPosition(), enemyLegalMoves).isEmpty();
        this.isAi = isAI;
    }
    protected abstract Piece findKing(Board board);

    public abstract Player getRival();

    public Piece getKing() {
        return king;
    }

    public List<Move> getLegalMoves() {
        return legalMoves;
    }

    public abstract List<Piece> getActivePieces();


    /**
     * find all the possible attack moves on a given box, and return list of it
     * @param boxPos the coordinate of the box
     * @param moves list of moves to check
     * @return the possible attack moves to a box on the board
     */
    protected static List<Move> getAttacksOnBox(int boxPos, List<Move> moves){
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
            if (transition.getMoveStatus() == Move.MoveStatus.DONE)
                return true;
        }
        return false;
    }

    public MoveTransition makeMove(Move move) {
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, this.board, move, Move.MoveStatus.UNDONE);
        }
        Board transitionBoard = move.executeMove();
        List<Move> attacksOnKing = getAttacksOnBox(findKing(transitionBoard).getPosition(), transitionBoard.getTurn().legalMoves);
        if(!attacksOnKing.isEmpty()) {
            return new MoveTransition(this.board, this.board, move, Move.MoveStatus.LEFT_IN_CHECK);
        }
        return new MoveTransition(this.board, transitionBoard, move, Move.MoveStatus.DONE);
    }

    private boolean isMoveLegal(Move move) {
        return this.legalMoves.contains(move);
    }

    public abstract Color getColor();

    public abstract List<Move> calculateCastles(List<Move> playerLegals, List<Move> opponentLegals);
}
