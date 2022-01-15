import logic.Board;
import logic.Move;
import logic.Pieces.Piece;

public class Program {
    public static void main(String[] args){

        Board board = new Board();
        for(Piece piece : board.board_state.values())
        {
            if(piece != null)
                System.out.println(piece.toString());
        }
        for(Move move : board.getAllLegalMoves(board.getWhitePieces()))
        {
            System.out.println(move.toString());
        }

    }
}
