package logic.player.AI;

import logic.Board;
import logic.Move;
import logic.Pieces.King;
import logic.Pieces.Piece;
import logic.player.Player;

import java.util.List;

public class PositionEvaluation {
    private static final double MOBILITY_VALUE_OPENING = 1;
    private static final double MOBILITY_VALUE_MIDGAME = 0.8;
    private static final double MOBILITY_VALUE_ENDING = 0.4;
    private static final double ATTACK_MULTIPLIER = 0.01;

    /**
     * evaluate the given board, by subtracting the white player evaluation with the black player evaluation
     * the biggest it will return-the better for white, the smallest-better for black
     * @param board the board we evaluate
     * @return the evaluation of the board
     * @see #score(Board, Player, GameStage)
     */
    public static double evaluate(Board board)
    {
        GameStage gameStage = calculateGameStage(board);
        return (score(board, board.getWhitePlayer(), gameStage) - score(board, board.getBlackPlayer(), gameStage
        ));
    }

    /**
     * this function calculate the game stage according to the material left og the board
     * @param board the board we want to Determine his game stage
     * @return Enum of the game stage(OPENING,MIDGAME OR ENDING)
     */
    private static GameStage calculateGameStage(Board board) {
        double materialLeft = calcMaterial(board);
        if(materialLeft > 60)
            return GameStage.OPENING;
        else if(materialLeft <= 60 && materialLeft > 28)
            return GameStage.MIDGAME;
        else if(materialLeft <= 28)
            return GameStage.ENDING;
        return GameStage.OPENING;
    }

    /**
     * calculate the material on the board
     * @param board the board we want to calculate on
     * @return the material value on this board
     */
    private static double calcMaterial(Board board) {
        double materialSum = 0;
        for(Piece piece : board.board_state.values())
        {
            if(piece != null && piece.getClass() != King.class)
                materialSum += piece.value;

        }
        return materialSum;
    }

    /**
     * this function evaluate the score of the board for the given player
     * @param board the board we calculate on
     * @param player the player we calculate the score for
     * @param gameStage Enum of the game stage
     * @return the evaluation of the player position on this board
     * @see GameStage
     */
    public static double score(Board board, Player player, GameStage gameStage)
    {
        List<Piece> allActivePieces = player.getActivePieces();
        return switch (gameStage) {
            case OPENING -> Material.material(allActivePieces) +
                    Mobility.mobility(player) * MOBILITY_VALUE_OPENING+
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    checkmate(player) + attacks(player) +
                    RookStruct.rookStruct(board, allActivePieces)+
                    PieceLocation.pieceLocation(allActivePieces, gameStage)+
                    CenterControl.centerControl(player, board)
            ;
            case MIDGAME -> Material.material(allActivePieces) +
                    Mobility.mobility(player) * MOBILITY_VALUE_MIDGAME+
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    checkmate(player) + attacks(player) +
                    RookStruct.rookStruct(board, allActivePieces) +
                    PieceLocation.pieceLocation(allActivePieces, gameStage)
            ;
            case ENDING -> Material.material(allActivePieces) +
                    Mobility.mobility(player) * MOBILITY_VALUE_ENDING +
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    checkmate(player) + attacks(player) +
                    RookStruct.rookStruct(board, allActivePieces)+
                    PieceLocation.pieceLocation(allActivePieces, gameStage)
            ;
        };
    }

    /**
     * this function checks if the player rival is in checkmate
     * @param player the player we check if gave checkmate
     * @return true if the rival on checkmate, false otherwise
     */
    private static double checkmate(Player player) {
        return (player.getRival().isInCheckMate() ?  10000 :  0);
    }

    private static double attacks(final Player player) {
        int attackScore = 0;
        for(final Move move : player.getLegalMoves()) {
            if(move instanceof Move.AttackMove) {
                final Piece movedPiece = move.getPieceMoved();
                final Piece attackedPiece = ((Move.AttackMove) move).getAttackedPiece();
                if(movedPiece.value <= attackedPiece.value) {
                    attackScore++;
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }

    public static String evaluationDetails(final Board board) {
        List<Piece> WallActivePieces = board.getWhitePieces();
        List<Piece> BallActivePieces = board.getBlackPieces();
        return
                "\ngame stage" + calculateGameStage(board) + "\n" +
                ("White:\n material: " + Material.material(WallActivePieces) + " \nmobility:" +
        Mobility.mobility(board.getWhitePlayer()) * MOBILITY_VALUE_OPENING+"\n pawns"+
                PawnStruct.pawnStruct(board.getWhitePlayer(), WallActivePieces) +"\nchackmate:"+
                checkmate(board.getWhitePlayer())) + "\n attack: " + attacks(board.getWhitePlayer()) +"\ncenter:"+
                CenterControl.centerControl(board.getWhitePlayer(), board)+"\nrooks:"+
                RookStruct.rookStruct(board, WallActivePieces ) +"\n"+
                        "black +: \n material" + Material.material(BallActivePieces) + "\nmobility:" +
                        Mobility.mobility(board.getBlackPlayer()) * MOBILITY_VALUE_OPENING+"\n pawns"+
                        PawnStruct.pawnStruct(board.getBlackPlayer(), BallActivePieces) +"\n checkmate"+
                        checkmate(board.getBlackPlayer()) +"\n attack:" + attacks(board.getBlackPlayer()) +"\ncenter:"+
                CenterControl.centerControl(board.getBlackPlayer(), board)+"\nrooks:"+
                RookStruct.rookStruct(board, BallActivePieces ) +"\n"+

                        "Final Score = " + evaluate(board);

    }



}
