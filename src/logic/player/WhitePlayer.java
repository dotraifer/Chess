package logic.player;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.Pieces.King;
import logic.Pieces.Piece;
import logic.Pieces.Rook;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represent a black player, and it extends the Player class
 * @author dotanraif
 * @see logic.player.Player
 */
public class WhitePlayer extends Player{

    /**
     * {@inheritDoc}
     */
    public WhitePlayer(Board board, List<Move> whiteLegalMoves, List<Move> blackLegalMoves, boolean isAi, boolean hasCastled) {
        super(board, whiteLegalMoves, blackLegalMoves, isAi, hasCastled);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected Piece findKing(Board board) {
        for (Piece piece : board.getWhitePieces()) {
            if(piece.getClass() == King.class)
                return piece;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getRival() {
        return board.getBlackPlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getColor() {
        return Color.White;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Move> calculateCastles(List<Move> playerLegals, List<Move> opponentLegals) {
        List<Move> Castles = new ArrayList<>();
        if(this.king.isFirstMove() && this.king.getPosition() == 60 && Player.getAttacksOnBox(60, opponentLegals).isEmpty())
        {
            // if not occupied
            if(this.board.getPieceAtCoordinate(61) == null && this.board.getPieceAtCoordinate(62) == null)
            {
                Piece rook = this.board.getPieceAtCoordinate(63);
                // if rook and first move
                if(rook != null && rook.getClass() == Rook.class && rook.isFirstMove())
                {
                    // if tiles are not under attack
                    if(Player.getAttacksOnBox(61, opponentLegals).isEmpty()
                    && Player.getAttacksOnBox(62, opponentLegals).isEmpty())
                    {
                        Castles.add(new Move.KingSideCastleMove(this.board, this.king, 62, (Rook) rook, 63, 61));
                    }
                }
            }
            // same for queen castle
            if(this.board.getPieceAtCoordinate(59) == null &&
                    this.board.getPieceAtCoordinate(58) == null &&
                    this.board.getPieceAtCoordinate(57) == null)
            {
                Piece rook = this.board.getPieceAtCoordinate(56);
                if(rook != null && rook.getClass() == Rook.class && rook.isFirstMove()) {
                    if (Player.getAttacksOnBox(59, opponentLegals).isEmpty()
                            && Player.getAttacksOnBox(58, opponentLegals).isEmpty()) {
                        Castles.add(new Move.QueenSideCastleMove(this.board, this.king, 58, (Rook)rook, 56, 59));
                    }
                }
            }
        }
        return Castles;
    }


}
