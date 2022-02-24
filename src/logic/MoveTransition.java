package logic;

public class MoveTransition {
    private Board fromBoard;
    private Board toBoard;
    private Move transitionMove;
    private MoveStatus moveStatus;

    public MoveTransition(Board fromBoard, Board toBoard, Move transitionMove, MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.toBoard = toBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return moveStatus;
    }
}
