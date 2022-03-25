package logic;

import logic.Pieces.*;
import logic.player.BlackPlayer;
import logic.player.Player;
import logic.player.WhitePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    public Map<Integer, Piece> board_state = new HashMap<Integer, Piece>();
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player turn;

    public Board() {
        this.board_state = createNewBoard();
        this.whitePieces = getActivePieces(board_state, Color.White);
        this.blackPieces = getActivePieces(board_state, Color.Black);
        List<Move> whiteLegalMoves = getAllLegalMoves(this.whitePieces);
        List<Move> blackLegalMoves = getAllLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteLegalMoves, blackLegalMoves);
        this.turn = null;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    public Player getOponnent()
    {
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
    public HashMap<Integer, Piece> createNewBoard()
    {
        HashMap<Integer, Piece> board_state = new HashMap<Integer, Piece>();
        // Black Layout
        board_state.put(0, new Rook(0, Color.Black));
        board_state.put(1, new Knight(1, Color.Black));
        board_state.put(2, new Bishop(2, Color.Black));
        board_state.put(3, new Queen(3, Color.Black));
        board_state.put(4, new King(4, Color.Black));
        board_state.put(5, new Bishop(5, Color.Black));
        board_state.put(6, new Knight(6, Color.Black));
        board_state.put(7, new Rook(7, Color.Black));
        board_state.put(8, new Pawn(8, Color.Black));
        board_state.put(9, new Pawn(9, Color.Black));
        board_state.put(10, new Pawn(10, Color.Black));
        board_state.put(11, new Pawn(11, Color.Black));
        board_state.put(12, new Pawn(12, Color.Black));
        board_state.put(13, new Pawn(13, Color.Black));
        board_state.put(14, new Pawn(14, Color.Black));
        board_state.put(15, new Pawn(15, Color.Black));
        // Empty boxes
        for (int i = 16; i < 48;i++)
            board_state.put(i, null);
        // White Layout
        board_state.put(48, new Pawn(48, Color.White));
        board_state.put(49, new Pawn(49, Color.White));
        board_state.put(50, new Pawn(50, Color.White));
        board_state.put(51, new Pawn(51, Color.White));
        board_state.put(52, new Pawn(52, Color.White));
        board_state.put(53, new Pawn(53, Color.White));
        board_state.put(54, new Pawn(54, Color.White));
        board_state.put(55, new Pawn(55, Color.White));
        board_state.put(56, new Rook(56, Color.White));
        board_state.put(57, new Knight(57, Color.White));
        board_state.put(58, new Bishop(58, Color.White));
        board_state.put(59, new Queen(59, Color.White));
        board_state.put(60, new King(60, Color.White));
        board_state.put(61, new Bishop(61, Color.White));
        board_state.put(62, new Knight(62, Color.White));
        board_state.put(63, new Rook(63, Color.White));
        return board_state;
    }
}
