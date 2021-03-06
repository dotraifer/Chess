package logic.player.AI;

import logic.Board;
import logic.Color;
import logic.Pieces.Pawn;
import logic.Pieces.Piece;
import logic.player.Player;

import java.util.List;

/**
 * this class contains static methods for evaluating a Pawn structure of a player
 */
public class PawnStruct {
    private static final double DOUBLE_PAWN_PUNISHMENT = -0.35;
    private static final double PAWN_ISLAND_PUNISHMENT = -0.1;
    private static final double BACKWARD_PAWN_PUNISHMENT = -0.15;
    private static final double ISOLATED_PAWN_PUNISHMENT = BACKWARD_PAWN_PUNISHMENT-0.03;
    private static final double OPEN_FILE_BACKWARD_PAWN_PUNISHMENT = -0.08;

    /**
     * this function evaluate the player pawn struct, according to pawns stack and isolated pawns
     * @param player the player we want to analyze his pawn structure
     * @param allActivePieces list of all the active pieces of the player
     * @return double that represent the pawn structure evaluation of the player
     */
    public static double pawnStruct(Player player, List<Piece> allActivePieces)
    {
        int[] pawnsArr = getPawnsPlacesArray(allActivePieces);
        return calculateDoublePawns(pawnsArr) + calculateIsolatedPawns(pawnsArr) + PawnIslands(pawnsArr)
                + BackwardPawns(player.getBoard(), allActivePieces);

    }

    /**
     * this function evaluate the backward pawns of the player in the board
     * @param board the board we are in
     * @param allActivePieces all the players active pieces
     * @return the backward evaluation
     */
    private static double BackwardPawns(Board board, List<Piece> allActivePieces) {
// some time passes
        boolean isBackward;
        boolean isOpenFile;
        int backwardCounter = 0;
        int openFileCounter = 0;
        for (Piece piece : allActivePieces) {
            isBackward = true;
            isOpenFile = true;
            if (piece.getClass() == Pawn.class) {
                int pawnLoc = piece.getPosition();
                int pawnCol = pawnLoc % 8;
                int dif;
                long start = System.currentTimeMillis();
                if (pawnCol != 7 && pawnCol != 0) {
                    for (int i = pawnCol + 1; i < EvaluationAssistants.size; i += 8) {
                        Piece pieceChecked = board.getPieceAtCoordinate(i);
                        if (pieceChecked != null && pieceChecked.getClass() == Pawn.class && pieceChecked.getColor() == piece.getColor()){
                            dif = (pawnLoc - i) * piece.getColor().getDirection();
                            if (dif >= -1)
                                isBackward = false;
                        }
                    }
                    for (int i = pawnCol - 1; i < EvaluationAssistants.size; i += 8) {
                        Piece pieceChecked = board.getPieceAtCoordinate(i);
                        if (pieceChecked != null && pieceChecked.getClass() == Pawn.class && pieceChecked.getColor() == piece.getColor()) {
                            dif = (pawnLoc - i) * piece.getColor().getDirection();
                            if (dif >= -1)
                                isBackward = false;
                        }
                    }
                }
                if (pawnCol == 0) {
                    for (int i = pawnCol + 1; i < EvaluationAssistants.size; i += 8) {
                        Piece pieceChecked = board.getPieceAtCoordinate(i);
                        if (pieceChecked != null && pieceChecked.getClass() == Pawn.class && pieceChecked.getColor() == piece.getColor()) {
                            dif = (pawnLoc - i) * piece.getColor().getDirection();
                            if (dif >= -1)
                                isBackward = false;
                        }
                    }
                }
                if (pawnCol == 7) {
                    for (int i = pawnCol - 1; i < EvaluationAssistants.size; i += 8) {
                        if (board.getPieceAtCoordinate(i) != null && board.getPieceAtCoordinate(i).getClass() == Pawn.class) {
                            dif = (pawnLoc - i) * piece.getColor().getDirection();
                            if (dif >=-1)
                                isBackward = false;
                        }
                    }
                }

                for(int i = pawnLoc;i < EvaluationAssistants.size && i > 0;i+= 8 * piece.getColor().getDirection() )
                {
                    Piece pieceChecked = board.getPieceAtCoordinate(i);
                    if(pieceChecked != null && pieceChecked.getClass() == Pawn.class && pieceChecked.getColor() != piece.getColor())
                        isOpenFile = false;
                }
                if(isBackward && isOpenFile) {
                    backwardCounter++;
                    openFileCounter++;
                }
                else if(isBackward)
                    backwardCounter++;
            }
        }
        return backwardCounter * BACKWARD_PAWN_PUNISHMENT + openFileCounter * OPEN_FILE_BACKWARD_PAWN_PUNISHMENT;
    }

    /**
     * this function count the number of pawns of the player in every column, and put the number in the place in the array
     * it returns
     * @param allActivePieces all the active pieces of the player
     * @return an array that represent the number of pawns in every column
     */
    public static int[] getPawnsPlacesArray(List<Piece> allActivePieces)
    {
        int [] pawnsArr = {0, 0, 0, 0, 0, 0, 0, 0};
        for(Piece piece: allActivePieces)
        {
            if(piece.getClass() == Pawn.class)
                pawnsArr[piece.getPosition() % 8]++;
        }
        return pawnsArr;
    }
    /**
     * this function count the pawns islands on the board, and punish the evaluation according to it
     * @param pawnsArr array of the number of pawns in every column of the player in the board
     * @return evaluation of the pawns Islands on the board for this pawns
     * @see <a href="https://www.chessprogramming.org/Pawn_Islands">
     */
    private static double PawnIslands(int[] pawnsArr) {
        int pawnsIslands = 0;
        for(int i = 0;i < pawnsArr.length; i++)
        {
            if(pawnsArr[i] > 0)
            {
                while( i < pawnsArr.length && pawnsArr[i] > 0)
                    i++;
                pawnsIslands++;
            }
        }
        return pawnsIslands * PAWN_ISLAND_PUNISHMENT;
    }


    /**
     * this function count the pawns stack on the board, and punish the evaluation according to it
     * @param pawnsArr array of the number of pawns in every column of the player in the board
     * @return evaluation of the pawns stack on the board for this pawns
     * @see <a href="https://www.chessprogramming.org/Doubled_Pawn"</a>
     */
    public static double calculateDoublePawns(int[] pawnsArr)
    {
        int pawnDoubledTotal = 0;
        for (int i : pawnsArr) {
            // if more then one, for every couple add 1
            if (i > 1)
                pawnDoubledTotal += i - 1;
        }
        return pawnDoubledTotal * DOUBLE_PAWN_PUNISHMENT;
    }

    /**
     * this function count the isolated pawns on the board, and punish the evaluation according to it
     * @param pawnsArr array of the number of pawns in every column of the player in the board
     * @return evaluation of the isolated pawns on the board for this pawns
     * @see <a href="https://www.chessprogramming.org/Isolated_Pawn"</a>
     */
    public static double calculateIsolatedPawns(int[] pawnsArr)
    {
        int numIsolatedPawns = 0;
        // if isolated on rightest column
        if(pawnsArr[0] > 0 && pawnsArr[1] == 0) {
            numIsolatedPawns += pawnsArr[0];
        }
        // if isolated on leftest column
        if(pawnsArr[7] > 0 && pawnsArr[6] == 0) {
            numIsolatedPawns += pawnsArr[7];
        }
        for(int i = 1; i < pawnsArr.length - 1; i++) {
            // if isolated from both sides
            if((pawnsArr[i-1] == 0 && pawnsArr[i+1] == 0)) {
                numIsolatedPawns += pawnsArr[i];
            }
        }
        return numIsolatedPawns * ISOLATED_PAWN_PUNISHMENT;
    }

}
