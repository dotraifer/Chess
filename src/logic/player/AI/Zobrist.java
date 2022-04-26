package logic.player.AI;

import logic.Board;
import logic.Color;
import logic.Pieces.*;

import java.util.Random;


public class Zobrist {
    private static final long[][] ZobristTable = initTable();
    
    private static int indexOf(Piece piece)
    {
        if (piece.getClass() == Pawn.class && piece.getColor() == Color.White)
            return 0;
        if (piece.getClass() == Knight.class && piece.getColor() == Color.White)
            return 1;
        if (piece.getClass() == Bishop.class && piece.getColor() == Color.White)
            return 2;
        if (piece.getClass() == Rook.class && piece.getColor() == Color.White)
            return 3;
        if (piece.getClass() == Queen.class && piece.getColor() == Color.White)
            return 4;
        if (piece.getClass() == King.class && piece.getColor() == Color.White)
            return 5;
        if (piece.getClass() == Pawn.class  && piece.getColor() == Color.Black)
            return 6;
        if (piece.getClass() == Knight.class  && piece.getColor() == Color.Black)
            return 7;
        if (piece.getClass() == Bishop.class  && piece.getColor() == Color.Black)
            return 8;
        if (piece.getClass() == Rook.class  && piece.getColor() == Color.Black)
            return 9;
        if (piece.getClass() == Queen.class  && piece.getColor() == Color.Black)
            return 10;
        if (piece.getClass() == King.class  && piece.getColor() == Color.Black)
            return 11;
        else
            return -1;
    }

    // Initializes the table
    private static long[][] initTable()
    {
        Random rand = new Random();
        long[][] tempTable = new long[64][12];
        for (int i = 0; i<64; i++) {
            for (int j = 0; j < 12; j++)
                tempTable[i][j] = rand.nextLong();
        }
        return tempTable;
    }
    static Random random = new Random();
    public static final long whiteKingMoved = random.nextLong();
    public static final long whiteQueenSideRookMoved = random.nextLong();;
    public static final long whiteKingSideRookMoved = random.nextLong();;
    public static final long blackKingMoved = random.nextLong();;
    public static final long blackQueenSideRookMoved = random.nextLong();;
    public static final long blackKingSideRookMoved = random.nextLong();;
    public static final long[] passantColumn =
            {0x70cc73d90bc26e24L, 0xe21a6b35df0c3ad7L, 0x3a93d8b2806962L, 0x1c99ded33cb890a1L, 0xcf3145de0add4289L, 0xd0e4427a5514fb72L, 0x77c621cc9fb3a483L, 0x67a34dac4356550bL,};
    public static final long whiteMove = random.nextLong();;

    public static long getKeyForBoard(Board board) {
        long key = 0;

        for (int loc = 0; loc < 64; loc++) {
            if(board.getPieceAtCoordinate(loc) != null)
                key ^= ZobristTable[loc][indexOf(board.getPieceAtCoordinate(loc))];
        }

        if (findCoordinatePieceFirstMove(board, 60, "King"))
            key ^= whiteKingMoved;
        if (findCoordinatePieceFirstMove(board, 63, "Rook"))
            key ^= whiteKingSideRookMoved;
        if (findCoordinatePieceFirstMove(board, 56, "Rook"))
            key ^= whiteQueenSideRookMoved;
        if (findCoordinatePieceFirstMove(board, 4, "King"))
            key ^= blackKingMoved;
        if (findCoordinatePieceFirstMove(board, 7, "Rook"))
            key ^= blackKingSideRookMoved;
        if (findCoordinatePieceFirstMove(board, 0, "Rook"))
            key ^= blackQueenSideRookMoved;

        //if (b.enPassantLoc != -1)
        //    key ^= passantColumn[BBUtils.getLocCol(b.enPassantLoc)];

        if (board.getTurn().getColor() == Color.White)
            key ^= whiteMove;

        return key;
    }

    private static boolean findCoordinatePieceFirstMove(Board board, int pieceCoordinate, String pieceClass)
    {
        Piece piece = board.getPieceAtCoordinate(pieceCoordinate);
        return piece != null && piece.getClass().getSimpleName().equals(pieceClass) && piece.isFirstMove();

    }
}
