package logic;

import logic.Pieces.Piece;
import logic.Pieces.Queen;
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

    // getter
    public int getCoordinateMovedTo() {
        return coordinateMovedTo;
    }

    // getter
    public Board getBoard() {
        return board;
    }

    // getter
    public Piece getPieceMoved() {
        return pieceMoved;
    }

    @Override
    public String toString() {
        return "Move{" +
                ", pieceMoved=" + pieceMoved +
                ", coordinateMovedTo=" + coordinateMovedTo +
                '}';
    }

    /**
     * this function return a board after to execute of this move
     * @return the Board object after making this move
     */
    public Board executeMove() {
        // create new builder
        final Board.UserBuilder builder = new Board.UserBuilder();
        // put back the unchanged attributes
        builder.isWhiteAi = board.getWhitePlayer().isAi;
        builder.isBlackAi = board.getBlackPlayer().isAi;
        this.board.getTurn().getActivePieces().stream().filter(piece -> !this.pieceMoved.equals(piece)).forEach(builder::setPiece);
        this.board.getOpponent().getActivePieces().forEach(builder::setPiece);
        // clone the moved piece
        Piece piece = pieceMoved.clone();
        // move the piece
        piece.movePiece(this);
        builder.setPiece(piece);
        // change turn
        builder.setMoveMaker(this.board.getOpponent().getColor());
        // set the move transition
        builder.setMoveTransition(this);
        return builder.build();
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

        /**
         * {@inheritDoc}
         */
        @Override
        public Board executeMove()
        {
            final Board.UserBuilder builder = new Board.UserBuilder();
            builder.isWhiteAi = board.getWhitePlayer().isAi;
            builder.isBlackAi = board.getBlackPlayer().isAi;
            System.out.println("third" + builder.isBlackAi);
            this.board.getTurn().getActivePieces().stream().filter(piece -> !this.pieceMoved.equals(piece)).forEach(builder::setPiece);
            this.board.getOpponent().getActivePieces().forEach(builder::setPiece);
            Piece piece = pieceMoved.clone();
            piece.movePiece(this);
            builder.setPiece(piece);
            if(isLastRow(coordinateMovedTo, piece.getColor()))
                builder.setPiece(new Queen(this.coordinateMovedTo, piece.getColor(), true));
            else
                builder.setPiece(piece);
            builder.setMoveMaker(this.board.getOpponent().getColor());
            builder.setMoveTransition(this);
            return builder.build();
        }
    }

    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(Board board, Piece pieceMoved, int coordinateMovedTo, Piece attackedPiece) {
            super(board, pieceMoved, coordinateMovedTo,attackedPiece);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Board executeMove()
        {
            final Board.UserBuilder builder = new Board.UserBuilder();
            builder.isWhiteAi = board.getWhitePlayer().isAi;
            builder.isBlackAi = board.getBlackPlayer().isAi;
            this.board.getTurn().getActivePieces().stream().filter(piece -> !this.pieceMoved.equals(piece)).forEach(builder::setPiece);
            this.board.getOpponent().getActivePieces().forEach(builder::setPiece);
            Piece piece = pieceMoved.clone();
            piece.movePiece(this);
            builder.setPiece(piece);
            if(isLastRow(coordinateMovedTo, pieceMoved.getColor()))
                builder.setPiece(new Queen(this.coordinateMovedTo, piece.getColor(), true));
            else
                builder.setPiece(piece);
            builder.setMoveMaker(this.board.getOpponent().getColor());
            builder.setMoveTransition(this);
            return builder.build();
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

        /**
         * {@inheritDoc}
         */
        @Override
        public Board executeMove() {
            final Board.UserBuilder builder = new Board.UserBuilder();
            builder.isWhiteAi = board.getWhitePlayer().isAi;
            builder.isBlackAi = board.getBlackPlayer().isAi;
            for (final Piece piece : this.board.getTurn().getActivePieces()) {
                if (!this.pieceMoved.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            this.board.getOpponent().getActivePieces().forEach(builder::setPiece);
            Piece piece = pieceMoved.clone();
            piece.movePiece(this);
            builder.setPiece(piece);
            //calling movePiece here doesn't work, we need to explicitly create a new Rook
            builder.setPiece(new Rook(this.castleRookDest, this.castleRook.getColor(), true));
            builder.boardConfig.get(castleRookDest).setFirstMove(false);
            builder.setMoveMaker(this.board.getOpponent().getColor());
            builder.setMoveTransition(this);
            return builder.build();
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

            for (final Move move : board.getTurn().getLegalMoves()) {
                if (move.pieceMoved.getPosition() == currentCoordinate &&
                        move.getCoordinateMovedTo() == destinationCoordinate) {
                    return move;
                }
            }
            return invalidMove;
        }
    }
    public boolean isLastRow(int coordinate, Color color)
    {
        if(coordinate >= 0 && coordinate <= 7 && color == Color.White)
            return true;
        else return coordinate >= 56 && coordinate <= 63 && color == Color.Black;
    }



}
