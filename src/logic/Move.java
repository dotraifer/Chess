package logic;

import logic.Pieces.Piece;
import logic.Pieces.Queen;
import logic.Pieces.Rook;

/**
 * this class represent a Move on the board, and its attributes
 */
public abstract class Move {
    protected Board board;
    protected Piece pieceMoved;
    protected int coordinateMovedTo;

    /**
     * Constructor for the Move class
     * @param board the board we make the move in
     * @param pieceMoved the piece that moved in this Move
     * @param coordinateMovedTo the coordinate on the board that the Piece moved to
     * @see Board
     */
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

    public boolean isAttack()
    {
        return false;
    }

    public boolean isCastle()
    {
        return false;
    }

    public boolean isPawnPromotion(){return false;}

    public boolean isPawnThreat(){return false;}
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
        final Board.BoardBuilder builder = new Board.BoardBuilder();
        // put back the unchanged attributes
        builder.isWhiteAi = board.getWhitePlayer().isAi;
        builder.isBlackAi = board.getBlackPlayer().isAi;
        builder.whiteHasCastled = board.getWhitePlayer().isHasCastled();
        builder.blackHasCastled = board.getBlackPlayer().isHasCastled();
        builder.movesWithoutEat = board.getMovesWithoutEat() + 1;
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

        @Override
        public boolean isAttack() {
            return false;
        }
    }
    public static class AttackMove extends Move{
        Piece attackedPiece;
        public AttackMove(Board board, Piece pieceMoved, int coordinateMovedTo, Piece
                          attackedPiece) {
            super(board, pieceMoved, coordinateMovedTo);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board executeMove() {
            Board board = super.executeMove();
            board.setMovesWithoutEat(0);
            return board;
        }

        public Piece getAttackedPiece() {
            return attackedPiece;
        }

        @Override
        public boolean isAttack() {
            return true;
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
            final Board.BoardBuilder builder = new Board.BoardBuilder();
            builder.isWhiteAi = board.getWhitePlayer().isAi;
            builder.isBlackAi = board.getBlackPlayer().isAi;
            builder.whiteHasCastled = board.getWhitePlayer().isHasCastled();
            builder.blackHasCastled = board.getBlackPlayer().isHasCastled();
            builder.movesWithoutEat = 0;
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
        @Override
        public boolean isPawnPromotion() {
            return isLastRow(coordinateMovedTo, pieceMoved.getColor());
        }

        @Override
        public boolean isPawnThreat() {
            return board.getPieceAtCoordinate(coordinateMovedTo + (9 * pieceMoved.getColor().getDirection())) != null ||
                    board.getPieceAtCoordinate(coordinateMovedTo + (7 * pieceMoved.getColor().getDirection())) != null;

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
            final Board.BoardBuilder builder = new Board.BoardBuilder();
            builder.isWhiteAi = board.getWhitePlayer().isAi;
            builder.isBlackAi = board.getBlackPlayer().isAi;
            builder.movesWithoutEat = 0;
            builder.whiteHasCastled = board.getWhitePlayer().isHasCastled();
            builder.blackHasCastled = board.getBlackPlayer().isHasCastled();
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

        @Override
        public boolean isPawnPromotion() {
            return isLastRow(coordinateMovedTo, pieceMoved.getColor());
        }
    }
    public static final class EnPassantPawnAttackMove extends PawnAttackMove{

        public EnPassantPawnAttackMove(Board board, Piece pieceMoved, int coordinateMovedTo, Piece attackedPiece) {
            super(board, pieceMoved, coordinateMovedTo, attackedPiece);
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
        public boolean isCastle() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Board executeMove() {
            final Board.BoardBuilder builder = new Board.BoardBuilder();
            builder.isWhiteAi = board.getWhitePlayer().isAi;
            builder.isBlackAi = board.getBlackPlayer().isAi;
            builder.whiteHasCastled = board.getWhitePlayer().isHasCastled();
            builder.blackHasCastled = board.getBlackPlayer().isHasCastled();
            if(board.getTurn().getColor() == Color.White)
                builder.whiteHasCastled = true;
            else
                builder.blackHasCastled = true;
            builder.movesWithoutEat = board.getMovesWithoutEat() + 1;
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

        public static Move getNullMove() {
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

    /**
     * this function check if a given coordinate is the last row for a given color
     * @param coordinate the coordinate to check
     * @param color the color we check for
     * @return true if it is the last row, else otherwise
     * @see PawnMove
     * @see PawnAttackMove
     */
    public boolean isLastRow(int coordinate, Color color)
    {
        if(coordinate >= 0 && coordinate <= 7 && color == Color.White)
            return true;
        else return coordinate >= 56 && coordinate <= 63 && color == Color.Black;
    }



}
