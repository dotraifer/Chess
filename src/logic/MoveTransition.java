package logic;

public class MoveTransition {
    private Board fromBoard;
    private Board toBoard;
    private Move transitionMove;
    private Move.MoveStatus moveStatus;

    public MoveTransition(Board fromBoard, Board toBoard, Move transitionMove, Move.MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.toBoard = toBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
    }

    public Move.MoveStatus getMoveStatus() {
        return moveStatus;
    }

    public Board getToBoard() {
        return toBoard;
    }
}
