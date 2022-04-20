package logic;

/**
 * this class represent a move transition, and the influences of a move
 */
public class MoveTransition {
    private Board fromBoard;
    private Board toBoard;
    public Move transitionMove;
    private Move.MoveStatus moveStatus;

    /**
     * a constructor for the MoveTransition class
     * @param fromBoard the board we came from before the move
     * @param toBoard the board we go to after the move
     * @see Board
     * @param transitionMove the Move we made
     * @see Move
     * @param moveStatus the move status of the MoveTransition
     * @see logic.Move.MoveStatus
     */
    public MoveTransition(Board fromBoard, Board toBoard, Move transitionMove, Move.MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.toBoard = toBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
    }

    // getter
    public Move.MoveStatus getMoveStatus() {
        return moveStatus;
    }

    // getter
    public Board getToBoard() {
        return toBoard;
    }
}
