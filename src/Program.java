import gui.GameScreen;
import logic.Board;
import logic.Move;
import logic.Pieces.Piece;

public class Program {
    public static void main(String[] args){

        Board board = new Board();
        int i = 8;
        for(Piece piece : board.board_state.values())
        {
            if(piece != null)
                System.out.print(piece.toString());
            else
                System.out.print('0');
            i++;
            if(i % 8 == 0)
                System.out.println("");
        }
        for(Move move : board.getAllLegalMoves(board.getWhitePieces()))
        {
            System.out.println(move.toString());
        }
        GameScreen gameScreen = new GameScreen();

    }
}
