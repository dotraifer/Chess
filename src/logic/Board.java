package logic;

import gui.Result;
import logic.Pieces.*;
import logic.player.BlackPlayer;
import logic.player.Player;
import logic.player.WhitePlayer;

import java.util.*;
import java.util.Collections;

/**
 * this class represent a board of chess, and its attributes
 */
public class Board {
    /**
     * an HahMap of the board state
     */
    public final Map<Integer, Piece> board_state;
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private Player turn;
    private int movesWithoutEat;
    private final Move transitionMove;

    /**
     * Constructor for the Board class
     * @param builder a builder that contains the needed values for the new Board
     */
    public Board(BoardBuilder builder) {
        this.board_state = Collections.unmodifiableMap(builder.boardConfig);
        this.whitePieces = getActivePieces(builder.boardConfig, Color.White);
        this.blackPieces = getActivePieces(builder.boardConfig, Color.Black);
        List<Move> whiteLegalMoves = getAllLegalMoves(this.whitePieces);
        List<Move> blackLegalMoves = getAllLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves, builder.isWhiteAi, builder.whiteHasCastled);
        this.blackPlayer = new BlackPlayer(this, whiteLegalMoves, blackLegalMoves, builder.isBlackAi, builder.blackHasCastled);
        this.turn = getPlayerForColor(builder.nextMoveMaker);
        this.movesWithoutEat = builder.movesWithoutEat;
        this.transitionMove = builder.transitionMove != null ? builder.transitionMove : Move.MoveFactory.getNullMove();

    }

    // getter
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    // getter
    public Player getBlackPlayer() {
        return blackPlayer;
    }

    // getter
    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    // getter
    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    public int getMovesWithoutEat() {
        return movesWithoutEat;
    }

    public void setMovesWithoutEat(int movesWithoutEat) {
        this.movesWithoutEat = movesWithoutEat;
    }

    public Move getTransitionMove() {
        return transitionMove;
    }

    /**
     * return the player object for the given color
     * @param color color Enum
     * @return the player object of this color
     */
    public Player getPlayerForColor(Color color)
    {
        if(color == Color.White)
            return this.whitePlayer;
        else
            return this.blackPlayer;
    }

    /**
     * return the opponent of the player who it's his turn
     * @return the opponent player object
     */
    public Player getOpponent()
    {
        if(this.turn == null)
            return this.whitePlayer;
        if(this.turn.getClass() == WhitePlayer.class)
            return this.blackPlayer;
        return this.whitePlayer;
    }

    // getter
    public Player getTurn() {
        return turn;
    }

    // setter
    public void setTurn(Player turn) {
        this.turn = turn;
    }

    /**
     * look for the piece object on a given coordinate
     * @param coordinate the coordinate we look in
     * @return the piece object on the given coordinate, or null if no piece there
     */
    public Piece getPieceAtCoordinate(int coordinate)
    {
        if(board_state.containsKey(coordinate))
            return board_state.get(coordinate);
        return null;
    }
    @Override
    public String toString() {
        return "Board{" +
                "board_state=" + board_state +
                ", whitePieces=" + whitePieces +
                ", blackPieces=" + blackPieces +
                '}';
    }

    /**
     * get a list of all the active pieces for a given color
     * @param board_state the board state hash map
     * @param color the color we want to find the pieces for
     * @return list of all the pieces from this color
     */
    private List<Piece> getActivePieces(Map<Integer, Piece> board_state, Color color)
    {
        List<Piece> activePieces = new ArrayList<>();
        for (Piece piece : board_state.values())
        {
            if (piece != null && piece.getColor() == color)
                activePieces.add(piece);
        }
        return activePieces;
    }


    /**
     * find all the possible moves for a given list of pieces
     * @param Pieces the pieces we want to get all their legal moves
     * @return all the possible moves for this group of pieces
     */
    public List<Move> getAllLegalMoves(List<Piece> Pieces)
    {
        List<Move> possibleMoves = new ArrayList<>();
        for(Piece piece : Pieces)
        {
            possibleMoves.addAll(piece.getLegalMoves(this));
        }
        return possibleMoves;
    }

    /**
     * create the stating board
     * @return HashMap of the starting board
     */
    public static BoardBuilder createNewBoard(boolean isWhiteAi, boolean isBlackAi)
    {
        BoardBuilder builder = new BoardBuilder();
        HashMap<Integer, Piece> board_state = new HashMap<Integer, Piece>();
        // Black Layout
        builder.setPiece(new Rook(0, Color.Black, true));
        builder.setPiece(new Knight(1, Color.Black, true));
        builder.setPiece(new Bishop(2, Color.Black, true));
        builder.setPiece(new Queen(3, Color.Black, true));
        builder.setPiece(new King(4, Color.Black, true));
        builder.setPiece(new Bishop(5, Color.Black, true));
        builder.setPiece(new Knight(6, Color.Black, true));
        builder.setPiece(new Rook(7, Color.Black, true));
        builder.setPiece(new Pawn(8, Color.Black, true));
        builder.setPiece(new Pawn(9, Color.Black, true));
        builder.setPiece(new Pawn(10, Color.Black, true));
        builder.setPiece(new Pawn(11, Color.Black, true));
        builder.setPiece(new Pawn(12, Color.Black, true));
        builder.setPiece(new Pawn(13, Color.Black, true));
        builder.setPiece(new Pawn(14, Color.Black, true));
        builder.setPiece(new Pawn(15, Color.Black, true));
        // Empty boxes
        builder.setPiece(new Pawn(48, Color.White, true));
        builder.setPiece(new Pawn(49, Color.White, true));
        builder.setPiece(new Pawn(50, Color.White, true));
        builder.setPiece(new Pawn(51, Color.White, true));
        builder.setPiece(new Pawn(52, Color.White, true));
        builder.setPiece(new Pawn(53, Color.White, true));
        builder.setPiece(new Pawn(54, Color.White, true));
        builder.setPiece(new Pawn(55, Color.White, true));
        builder.setPiece(new Rook(56, Color.White, true));
        builder.setPiece(new Knight(57, Color.White, true));
        builder.setPiece(new Bishop(58, Color.White, true));
        builder.setPiece(new Queen(59, Color.White, true));
        builder.setPiece(new King(60, Color.White, true));
        builder.setPiece(new Bishop(61, Color.White, true));
        builder.setPiece(new Knight(62, Color.White, true));
        builder.setPiece(new Rook(63, Color.White, true));
        builder.setMoveMaker(Color.White);
        builder.isWhiteAi = isWhiteAi;
        builder.isBlackAi = isBlackAi;
        builder.whiteHasCastled = false;
        builder.blackHasCastled = false;
        builder.movesWithoutEat = 0;
        return builder;
    }

    /**
     * check if the game has been finished, and in what score if does
     * @return the Result of the game, NOT_FINISHED if the game still going
     * @see Result Enum
     */
    public Result gameResult()
    {
        if(this.whitePlayer.isInCheckMate())
            return Result.BLACK;
        if(this.blackPlayer.isInCheckMate())
            return Result.WHITE;
        if(this.getTurn().isInStaleMate() || this.getMovesWithoutEat() == 50 || notEnoughMaterial()
        || isThreeTimesPosition())
            return Result.DRAW;
        return Result.NOT_FINISHED;
    }

    /**
     * check if both of the players has not enough material to win(draw case)
     * @exemple king against king, king and knight against king and so on...
     * @return true if both can't win
     */
    private boolean notEnoughMaterial() {
        return isNotEnoughMaterialToWin(this.blackPieces) && isNotEnoughMaterialToWin(this.whitePieces);
    }

    /**
     * check if the active pieces are enough to win
     * @param activePieces the active pieces of a player
     * @return true if the player has not enough material to win, else if does
     */
    private boolean isNotEnoughMaterialToWin(List<Piece> activePieces)
    {
        boolean isPawn = false;
        int material = 0;
        for(Piece piece : activePieces)
        {
            if(piece.getClass() != King.class)
                material += piece.value;
            if(piece.getClass() == Pawn.class)
                isPawn = true;
        }
        return (material <= 3 && !isPawn);
    }

    /**
     * this function checks if we go back at the same position 3 times
     * @see <a href="https://en.wikipedia.org/wiki/Threefold_repetition">
     * @return true if the same position returned 3 times-false otherwise
     */
    private boolean isThreeTimesPosition()
    {
        int samePositionCounter = 0;
        // for every move in the last N moves without eat
        for(Move move : lastNMoves(movesWithoutEat))
        {
            // check if the move board was equal to the current board
            if (this.equals(move.getBoard())) {
                samePositionCounter++;
            }
        }
        // if we get back 3 times
        return samePositionCounter >= 3;
    }

    /**
     * this function return a list of the last N Moves in the game
     * @param N the number of last moves to find
     * @return list of Move of the last N moves in the game
     */
    public List<Move> lastNMoves(int N) {
        final List<Move> moveHistory = new ArrayList<>();
        Move currentMove = this.getTransitionMove();
        int i = 0;
        // while the not a nullMove and still not in N
        while(currentMove != Move.MoveFactory.getNullMove() && i < N) {
            // add the move
            moveHistory.add(currentMove);
            // go back another board
            currentMove = currentMove.getBoard().getTransitionMove();
            i++;
        }
        // return the list
        return Collections.unmodifiableList(moveHistory);
    }

    public static class BoardBuilder {

        Map<Integer, Piece> boardConfig;
        Color nextMoveMaker;
        Move transitionMove;
        boolean isWhiteAi;
        boolean isBlackAi;
        int movesWithoutEat;
        boolean whiteHasCastled;
        boolean blackHasCastled;

        public BoardBuilder() {
            this.boardConfig = new HashMap<>();
        }

        public BoardBuilder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPosition(), piece);
            return this;
        }

        public BoardBuilder setMoveMaker(final Color nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public BoardBuilder setMoveTransition(final Move transitionMove) {
            this.transitionMove = transitionMove;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board b = (Board) o;
        return board_state.equals(b.board_state) && turn.getColor().equals(b.turn.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(board_state, turn);
    }
}
