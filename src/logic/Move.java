package logic;

import logic.Pieces.Piece;
import logic.Pieces.Rook;

public abstract class Move {
    protected Board board;
    protected Piece pieceMoved;
    protected int coordinateMovedTo;


    public Move(Board board, Piece pieceMoved, int coordinateMovedTo) {
        this.board = board;
        this.pieceMoved = pieceMoved;
        this.coordinateMovedTo = coordinateMovedTo;
    }
    public enum MoveStatus {
        DONE, UNDONE, LEFT_IN_CHECK
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

        for(Piece piece : board.getTurn().getActivePieces())
        {
            // TODO : is override equals needed?
            if(!pieceMoved.equals(piece))
                board.board_state.put(piece.getPosition(), piece);
        }
        for(Piece piece : board.getOponnent().getActivePieces())
        {
            board.board_state.put(piece.getPosition(), piece);
        }
        // 22
        board.board_state.put(pieceMoved.getPosition(), null);
        board.board_state.put(this.coordinateMovedTo, pieceMoved);
        pieceMoved.movePiece(this);
        board.setTurn(board.getOponnent());
        return board;
    }
    public static class MajorMove extends Move{

        public MajorMove(Board board, Piece pieceMoved, int coordinateMovedTo) {
            super(board, pieceMoved, coordinateMovedTo);
        }
    }
    public static class AttackMove extends Move{
        Piece attackedPiece;
        public AttackMove(Board board, Piece pieceMoved, int coordinateMovedTo, Piece
                          attackedPiece) {
            super(board, pieceMoved, coordinateMovedTo);
            this.attackedPiece = attackedPiece;
        }
    }
    public static class PawnMove extends Move{

        public PawnMove(Board board, Piece pieceMoved, int coordinateMovedTo) {
            super(board, pieceMoved, coordinateMovedTo);
        }
    }

    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(Board board, Piece pieceMoved, int coordinateMovedTo, Piece attackedPiece) {
            super(board, pieceMoved, coordinateMovedTo,attackedPiece);
        }
    }
    public static final class EnPassantPawnAttackMove extends PawnAttackMove{

        public EnPassantPawnAttackMove(Board board, Piece pieceMoved, int coordinateMovedTo, Piece attackedPiece) {
            super(board, pieceMoved, coordinateMovedTo, attackedPiece);
        }
    }

    public static final class PawnJump extends Move{

        public PawnJump(Board board, Piece pieceMoved, int coordinateMovedTo) {
            super(board, pieceMoved, coordinateMovedTo);
        }
    }

    static abstract class CastleMove extends Move{
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDest;

        CastleMove(Board board, Piece pieceMoved, int coordinateMovedTo, final Rook castleRook,
                          final int castleRookStart, final int castleRookDest) {
            super(board, pieceMoved, coordinateMovedTo);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDest = castleRookDest;
        }

        @Override
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
            new_board.board_state.put(coordinateMovedTo, pieceMoved);
            new_board.board_state.put(castleRookStart, null);
            new_board.board_state.put(castleRookDest, pieceMoved);
            pieceMoved.movePiece(this);
            new_board.setTurn(board.getOponnent());
            return new_board;
        }
    }

    public static final class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(Board board, Piece pieceMoved, int coordinateMovedTo, final Rook castleRook,
                                  final int castleRookStart, final int castleRookDest) {
            super(board, pieceMoved, coordinateMovedTo, castleRook, castleRookStart,
                   castleRookDest);
        }
    }
    public static final class QueenSideCastleMove extends CastleMove{

        public QueenSideCastleMove(Board board, Piece pieceMoved, int coordinateMovedTo, final Rook castleRook,
                                   final int castleRookStart, final int castleRookDest) {
            super(board, pieceMoved, coordinateMovedTo, castleRook, castleRookStart,
                    castleRookDest);
        }
    }
    public final static class InvalidMove extends Move{

        public InvalidMove() {
            super(null, null, -1);
        }
    }
    public static class MoveFactory {

        private static final Move invalidMove = new InvalidMove();

        public MoveFactory() {
            throw new RuntimeException("Not instantiatable!");
        }

        public Move getNullMove() {
            return invalidMove;
        }

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {

            System.out.println(board.getTurn().getLegalMoves());
            for (final Move move : board.getTurn().getLegalMoves()) {
                if (move.pieceMoved.getPosition() == currentCoordinate &&
                        move.getCoordinateMovedTo() == destinationCoordinate) {
                    return move;
                }
            }
            return invalidMove;
        }
    }



}
