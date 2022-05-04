package logic.player.AI;

import logic.Board;
import logic.Color;
import logic.Move;
import logic.Pieces.King;
import logic.Pieces.Piece;
import logic.player.Player;

import java.util.List;
/**
 * this class contains static methods for evaluating a full position of a player
 */
public class PositionEvaluation {
    private static final int OPENING_MATERIAL_SUM = 60;
    private static final int MIDGAME_MATERIAL_SUM = 28;
    private static final double MOBILITY_VALUE_OPENING = 1;
    private static final double MOBILITY_VALUE_MIDGAME = 0.8;
    private static final double MOBILITY_VALUE_ENDING = 0.4;
    private static final double ATTACK_MULTIPLIER = 0.01;
    public static final double MATE = 10000;

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
        // the score of the white - the score of the black
        if(board.getTurn().getColor() == Color.White)
            return (score(board, board.getWhitePlayer(), gameStage) - score(board, board.getBlackPlayer(), gameStage)) + Material.material(board.getWhitePieces(), board.getBlackPieces());
        return -((score(board, board.getWhitePlayer(), gameStage) - score(board, board.getBlackPlayer(), gameStage)) + Material.material(board.getWhitePieces(), board.getBlackPieces()));

    }

    /**
     * this function calculate the game stage according to the material left og the board
     * @param board the board we want to Determine his game stage
     * @return Enum of the game stage(OPENING,MIDGAME OR ENDING)
     */
    private static GameStage calculateGameStage(Board board) {
        double materialLeft = calcMaterial(board);
        if(materialLeft > OPENING_MATERIAL_SUM)
            return GameStage.OPENING;
        else if(materialLeft <= OPENING_MATERIAL_SUM && materialLeft > MIDGAME_MATERIAL_SUM)
            return GameStage.MIDGAME;
        return GameStage.ENDING;
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
            // if the piece is not king,add her value
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
        List<Piece> enemyActivePieces = player.getRival().getActivePieces();
        return switch (gameStage) {
            // if opening game stage
            case OPENING ->
                    Mobility.mobility(player) * MOBILITY_VALUE_OPENING+
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    checkmate(player) + attacks(player) +
                    RookStruct.rookStruct(board, allActivePieces)+
                    CenterControl.centerControl(player, board)+
                    KingSafety.calculateKingSafety(player, board, gameStage) +
                    PieceLocation.pieceLocation(allActivePieces, gameStage)
            ;
            // if midgame game stage
            case MIDGAME ->
                    Mobility.mobility(player) * MOBILITY_VALUE_MIDGAME+
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    CenterControl.centerControl(player, board) +
                    checkmate(player) + attacks(player) +
                    RookStruct.rookStruct(board, allActivePieces)+
                    KingSafety.calculateKingSafety(player, board, gameStage) +
                    PieceLocation.pieceLocation(allActivePieces, gameStage)

            ;
            // if ending game stage
            case ENDING ->
                    Mobility.mobility(player) * MOBILITY_VALUE_ENDING +
                    PawnStruct.pawnStruct(player, allActivePieces) +
                    checkmate(player) + attacks(player) +
                    CenterControl.centerControl(player, board) +
                    RookStruct.rookStruct(board, allActivePieces)+
                    KingSafety.calculateKingSafety(player, board, gameStage) +
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
        return (player.getRival().isInCheckMate() ?  MATE :  0);
    }

    /**
     * this function evaluate good eating moves(where smaller piece eat a bigger piece)
     * @param player the player we look for good eating moves
     * @return the evaluation for good eating moves
     */
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

    /**
     * this function prints the evaluation Details
     * @param board the board we evaluate
     * @return String of the evaluation details
     */
    public static String evaluationDetails(final Board board) {
        List<Piece> WallActivePieces = board.getWhitePieces();
        List<Piece> BallActivePieces = board.getBlackPieces();
        return
                "\ngame stage" + calculateGameStage(board) + "\n" +
                ("White:\n material: "  + Material.material(WallActivePieces, BallActivePieces) + " \nmobility:" +
        Mobility.mobility(board.getWhitePlayer()) * MOBILITY_VALUE_OPENING+"\n pawns"+
                PawnStruct.pawnStruct(board.getWhitePlayer(), WallActivePieces) +"\nchackmate:"+
                checkmate(board.getWhitePlayer())) + "\n attack: " + attacks(board.getWhitePlayer()) +"\ncenter:"+
                CenterControl.centerControl(board.getWhitePlayer(), board)+"\nrooks:"+
                RookStruct.rookStruct(board, WallActivePieces ) +"\n"+
                        "kingtro :" + KingSafety.calculateKingTropism(board.getWhitePlayer()) + "\n" +
                        "saftey: " + KingSafety.calculateKingSafety(board.getWhitePlayer(), board, calculateGameStage(board)) + "\n"+
                        "PL:" + PieceLocation.pieceLocation(WallActivePieces, calculateGameStage(board)) +

                        "black +: \n material" + Material.material(BallActivePieces, WallActivePieces) + "\nmobility:" +
                        Mobility.mobility(board.getBlackPlayer()) * MOBILITY_VALUE_OPENING+"\n pawns"+
                        PawnStruct.pawnStruct(board.getBlackPlayer(), BallActivePieces) +"\n checkmate"+
                        checkmate(board.getBlackPlayer()) +"\n attack:" + attacks(board.getBlackPlayer()) +"\ncenter:"+
                CenterControl.centerControl(board.getBlackPlayer(), board)+"\nrooks:"+
                RookStruct.rookStruct(board, BallActivePieces ) +"\n"+
                        "kingtro"+ KingSafety.calculateKingTropism(board.getBlackPlayer()) + "\n" +
                        "saftey: " + KingSafety.calculateKingSafety(board.getBlackPlayer(), board, calculateGameStage(board)) + "\n" +
                        "casled" + board.getBlackPlayer().isHasCastled() +
                        "PL:" + PieceLocation.pieceLocation(BallActivePieces, calculateGameStage(board)) +

                        "Final Score = " + evaluate(board);

    }



}
