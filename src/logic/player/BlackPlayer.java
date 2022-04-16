package logic.player;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.Pieces.King;
import logic.Pieces.Piece;
import logic.Pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(Board board, List<Move> whiteLegalMoves, List<Move> blackLegalMoves, boolean isAi) {
        super(board, blackLegalMoves, whiteLegalMoves, isAi);
        this.king = findKing(board);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Piece findKing(Board board) {
        for (Piece piece : board.getBlackPieces()) {
            if(piece.getClass() == King.class)
                return piece;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Player getRival() {
        return board.getWhitePlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getColor() {
        return Color.Black;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Move> calculateCastles(List<Move> playerLegals, List<Move> opponentLegals) {
        List<Move> Castles = new ArrayList<>();
        if(this.king.isFirstMove() && this.king.getPosition() == 4 && !this.isInCheck)
        {
            // if not occupied
            if(this.board.getPieceAtCoordinate(5) == null && this.board.getPieceAtCoordinate(6) == null)
            {
                Piece rook = this.board.getPieceAtCoordinate(7);
                // if rook and first move
                if(rook != null && rook.getClass() == Rook.class && rook.isFirstMove())
                {
                    // if tiles are not under attack
                    if(Player.getAttacksOnBox(5, opponentLegals).isEmpty()
                            && Player.getAttacksOnBox(6, opponentLegals).isEmpty())
                    {
                        Castles.add(new Move.KingSideCastleMove(this.board, this.king, 6, (Rook)rook, 7, 5));
                    }
                }
            }
            // same for queen castle
            if(this.board.getPieceAtCoordinate(1) == null &&
                    this.board.getPieceAtCoordinate(2) == null &&
                    this.board.getPieceAtCoordinate(3) == null)
            {
                Piece rook = this.board.getPieceAtCoordinate(0);
                if(rook != null && rook.getClass() == Rook.class && rook.isFirstMove()) {
                    if (Player.getAttacksOnBox(2, opponentLegals).isEmpty()
                            && Player.getAttacksOnBox(3, opponentLegals).isEmpty()) {
                        Castles.add(new Move.QueenSideCastleMove(this.board, this.king, 2, (Rook)rook, 0, 3));
                    }
                }
            }
        }
        return Castles;
    }

}
