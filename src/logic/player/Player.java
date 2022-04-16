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
    /**
     * find the king of the player
     * @param board the board to look on
     * @return the king of the player
     */
    protected abstract Piece findKing(Board board);

    /**
     * get the rival player
     * @return the rival player object
     */
    public abstract Player getRival();

    // getter
    public Piece getKing() {
        return king;
    }

    // getter
    public List<Move> getLegalMoves() {
        return legalMoves;
    }

    /**
     * will get every active piece for the player
     * @return list of the active pieces of the player
     */
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
    // getter
    public boolean isInCheck(){
        return isInCheck;
    }

    /**
     * check if this player is in checkmate position
     * @return true if in check mate, false otherwise
     */
    public boolean isInCheckMate() {
        // if in check and has no moves
        return isInCheck() && !isCanEscape();
    }

    /**
     * check if this player is in stalemate position
     * @return true if in stalemate, false otherwise
     */
    public boolean isInStaleMate(){
        // if not in check but has no moves
        return !isInCheck() && !isCanEscape();
    }

    // TODO : implement method that check if the king can escape

    /**
     * check if the player has legal moves
     * @return true if it has legal moves, false otherwise
     */
    private boolean isCanEscape() {
        for (Move move : this.legalMoves){
            MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus() == Move.MoveStatus.DONE)
                return true;
        }
        return false;
    }

    /**
     * the function make a given move, if the move is illegal it will return move transition with undone or left_in_check
     * status and won't change the destination board, if the move is legal it will return the move transition of the move.
     * @see MoveTransition
     * @param move the move to make
     * @return move transition of after the move has done
     */
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

    /**
     * check if the move is legal
     * @param move the move to check if legal
     * @return true if move legal, false otherwise
     */
    private boolean isMoveLegal(Move move) {
        return this.legalMoves.contains(move);
    }

    /**
     * get the color of the player
     * @return the color of the player
     */
    public abstract Color getColor();

    /**
     * this find the possible castles for the player
     * @param playerLegals list of the player legal moves
     * @param opponentLegals list of the rival player legal moves
     * @return list of the possible caste moves
     */
    public abstract List<Move> calculateCastles(List<Move> playerLegals, List<Move> opponentLegals);
}
