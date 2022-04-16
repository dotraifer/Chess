package logic;

import logic.Pieces.*;
import logic.player.AI.PositionEvaluation;
import logic.player.BlackPlayer;
import logic.player.Player;
import logic.player.WhitePlayer;

import java.util.*;
import java.util.Collections;

public class Board {
    public final Map<Integer, Piece> board_state;
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private Player turn;

    public Board(UserBuilder builder) {
        this.board_state = Collections.unmodifiableMap(builder.boardConfig);
        this.whitePieces = getActivePieces(builder.boardConfig, Color.White);
        this.blackPieces = getActivePieces(builder.boardConfig, Color.Black);
        List<Move> whiteLegalMoves = getAllLegalMoves(this.whitePieces);
        List<Move> blackLegalMoves = getAllLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves, builder.isWhiteAi);
        this.blackPlayer = new BlackPlayer(this, whiteLegalMoves, blackLegalMoves, builder.isBlackAi);
        this.turn = getPlayerForColor(builder.nextMoveMaker);

    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }
    public Player getPlayerForColor(Color color)
    {
        if(color == Color.White)
            return this.whitePlayer;
        else
            return this.blackPlayer;
    }
    public Player getOponnent()
    {
        if(this.turn == null)
            return this.whitePlayer;
        if(this.turn.getClass() == WhitePlayer.class)
            return this.blackPlayer;
        return this.whitePlayer;
    }

    public Player getTurn() {
        return turn;
    }

    public void setTurn(Player turn) {
        this.turn = turn;
    }
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
    public static UserBuilder createNewBoard(boolean isWhiteAi, boolean isBlackAi)
    {
        UserBuilder builder = new UserBuilder();
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
        for (int i = 16; i < 48;i++)
            builder.boardConfig.put(i, null);
        // White Layout
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
        return builder;
    }

    public static class UserBuilder {

        Map<Integer, Piece> boardConfig;
        Color nextMoveMaker;
        Move transitionMove;
        boolean isWhiteAi;
        boolean isBlackAi;

        public UserBuilder() {
            this.boardConfig = new HashMap<>();
        }

        public UserBuilder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPosition(), piece);
            return this;
        }

        public UserBuilder setMoveMaker(final Color nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public UserBuilder setMoveTransition(final Move transitionMove) {
            this.transitionMove = transitionMove;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }

}
