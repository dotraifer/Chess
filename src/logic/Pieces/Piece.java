package logic.Pieces;

import logic.Board;
import logic.Color;
import logic.Move;

import java.util.List;
import java.util.Objects;

/**
 * this class is an abstract class of a piece on the board, and it extends by the spastic class of every piece
 * @author dotanraif
 */
public abstract class Piece implements Cloneable {
    protected final Color color;
    protected int position;
    protected boolean isFirstMove;
    public double value;

    // getter
    public Color getColor() {
        return color;
    }

    // getter
    public int getPosition() {
        return position;
    }

    // getter
    public boolean isFirstMove() {
        return isFirstMove;
    }

    public Piece(int position, final Color color, boolean isFirstMove) {
        this.position = position;
        this.color = color;
        this.isFirstMove = isFirstMove;
        this.value = getValue();
    }

    // getter
    public double getValue() {
        return value;
    }

    // setter
    public void setFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    /**
     * get the bonus for the piece location according to her Piece-Square Tables
     * @return the bonus to add
     */
    public abstract double locationBonus();

    /**
     * finds all the legal moves for a piece in a given board
     * @param board the board we check in
     * @return List of all the legal moves for a piece(some special moves not include)
     */
    public abstract List<Move> getLegalMoves(Board board);

    @Override
    public String toString() {
        return " " + this.getClass();
    }

    /**
     * check if a given coordinate is valid(in the board, between 0 and 63)
     * @param coordinate coordinate as number
     * @return true if the coordinate is valid, false if not
     */
    public boolean isValidCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate <= 63;
    }

    /**
     *  check if the coordinate is not empty, and it has friendly piece on it
     * @param board the board we check on
     * @param coordinate the coordinate we check on
     * @return true if the piece is friendly, else otherwise
     */
    public boolean isFriendlyPieceOnCoordinate(Board board, int coordinate) {
        return board.board_state.get(coordinate) != null && board.board_state.get(coordinate).color == this.color;
    }

    /**
     * changes the piece that been moved to the current attributes
     * @param move the move we make
     */
    public void movePiece(Move move)
    {
        this.position = move.getCoordinateMovedTo();
        this.isFirstMove = false;
    }

    /**
     * equals override, check if object o is equal to this piece
     * @param o object to check if equal
     * @return true if all attributes equal, else false
     */
    @Override
    public boolean equals(Object o) {
        // if same
        if (this == o) return true;
        // if not the right class
        if (o == null || getClass() != o.getClass()) return false;
        // casting
        Piece piece = (Piece) o;
        // check if all attributes equal
        return position == piece.position && isFirstMove == piece.isFirstMove && color == piece.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, position, isFirstMove);
    }

    /**
     * shallow cloning for piece object
     * @return return clone of the object
     */
    @Override
    public Piece clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Piece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
