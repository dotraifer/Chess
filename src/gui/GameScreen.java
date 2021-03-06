package gui;

import logic.Board;
import logic.Move;
import logic.Move.MoveStatus;
import logic.MoveTransition;
import logic.Pieces.Piece;
import logic.player.AI.Minimax;
import logic.player.AI.PositionEvaluation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;

/**
 * this function is responsible for all our gui
 */
public class GameScreen {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;

    private final Dimension TILE_DIMENSION = new Dimension(10, 10);

    private Board board;

    private final Color whiteTileColor = Color.decode("#FFFDD0");
    private final Color blackTileColor = Color.decode("#D2691E");
    private final Color greenTileColor = Color.decode("#00FF00");
    private final Color redTileColor = Color.decode("#FF0000");
    private final Color blueTileColor = Color.decode("#0000FF");
    private final Color lightGreenTileColor = Color.decode("#ADFF2F");
    private final Color darkGreenTileColor = Color.decode("#9ACD32");

    private static int sourceTile = -1;
    private static int destTile = -1;
    private static Piece pieceMoved;

    private static boolean isWhiteAi;
    private static boolean isBlackAi;

    private Move computerMove;

    /**
     * our game screen definition
     */
    public GameScreen()
    {
        chooseAiOrNotPopUp();
        this.board = new Board(Board.createNewBoard(isWhiteAi, isBlackAi));

        this.gameFrame = new JFrame("Chess");//creating instance of JFrame
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setSize(400, 400);

        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true);

        if(board.getTurn().isAi) {
            new AiMove().execute();
        }
    }

    /**
     * reset the game
     */
    public void resetGame()
    {
        chooseAiOrNotPopUp();
        this.board = new Board(Board.createNewBoard(isWhiteAi, isBlackAi));
        computerMove = null;
        boardPanel.drawBoard(board);
        this.gameFrame.setVisible(true);
        System.out.println("\n\n~NEW GAME~\n\n");
        if(board.getTurn().isAi) {
            new AiMove().execute();
        }
    }

    /**
     * pop up that asks the user to choose if he wants to play against Ai or not
     */
    public void chooseAiOrNotPopUp()
    {
        String[] options = {"AI", "PVP"};
        int choice = JOptionPane.showOptionDialog(null, "Choose game Mode",
                "Game mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if(choice == -1)
            System.exit(0);
        else if(choice == 0)
            chooseColor();
        else{
            isWhiteAi = false;
            isBlackAi = false;
        }
    }

    /**
     * pop up that asks the player to choose color
     */
    public void chooseColor()
    {
        String[] options = {"WHITE", "BLACK"};
        int choice = JOptionPane.showOptionDialog(null, "Choose Your Color",
                "Choose Color",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if(choice == -1)
            System.exit(0);
        else if(choice == 0)
        {
            isWhiteAi = false;
            isBlackAi = true;
        }
        else if(choice == 1)
        {
            isWhiteAi = true;
            isBlackAi = false;
        }
    }

        /**
     * this class represent the board panel
     */
    public class BoardPanel extends JPanel {
        private final Dimension BOARD_DIMENSION = new Dimension(400, 400);
        List<TilePanel> Tiles;

        BoardPanel(){

            super(new GridLayout(8, 8));
            this.Tiles = new ArrayList<>();
            // make a list of 64 tiles
            for (int i = 0; i < 64;i++)
            {
                TilePanel tilePanel = new TilePanel(this, i);
                this.Tiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_DIMENSION);
            validate();
        }



        /**
         * draw the board tiles
         * @param board the board to draw
         */
        public void drawBoard(Board board) {
            removeAll();
            for(TilePanel tilePanel : Tiles)
            {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
        /**
         * game over pop up
         */
        public void gameOver(Result result)
        {
            String message;
            if(result == Result.DRAW)
                message = "draw, Want to try again?";
            else
                message = result + " won, Want to try Again?";
            String title = "Game Over";
            int userPressed = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.OK_CANCEL_OPTION);

            if (userPressed == JOptionPane.OK_OPTION)
            {
                resetGame();
            }
            else
            {
                System.exit(0);
            }
        }
    }

    public class AiMove extends SwingWorker
    {

        @Override
        protected Object doInBackground() throws Exception {
            System.out.println(PositionEvaluation.evaluationDetails(board));
            System.out.println("calculating...\n");
            //boardPanel.drawBoard(board);
            aiMove();
            boardPanel.drawBoard(board);
            if(board.gameResult() != Result.NOT_FINISHED)
                boardPanel.gameOver(board.gameResult());
            return null;
        }

        /**
         * this function make an AI move for the current player
         */
        public void aiMove()
        {
            MoveTransition moveTransition;
            moveTransition = board.getTurn().makeMove(Minimax.IterativeDeepening(board));
            if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                board = moveTransition.getToBoard();
                computerMove = moveTransition.getTransitionMove();
            }
        }
    }

    /**
     * this class represent a single tile/box panel on the board
     */
    public class TilePanel extends JPanel {
        private final int tileCoordinate;

        TilePanel(BoardPanel boardPanel, int tileCoordinate){
            super(new GridBagLayout());
            this.tileCoordinate = tileCoordinate;
            setPreferredSize(TILE_DIMENSION);
            putTileColor();
            putTilePiece(board);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    MoveTransition moveTransition = null;
                    if(isLeftMouseButton(e) && !board.getTurn().isAi)
                    {
                        if(sourceTile == -1) {
                            // first click
                            sourceTile = tileCoordinate;
                            pieceMoved = board.getPieceAtCoordinate(sourceTile);
                            if(pieceMoved == null){
                                sourceTile = -1;
                            }
                        }
                        else{
                            // second click
                            destTile = tileCoordinate;
                            if(destTile != sourceTile)
                            {
                                Move move = Move.MoveFactory.createMove(board, pieceMoved.getPosition(), destTile);
                                moveTransition = board.getTurn().makeMove(move);
                                if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                                    computerMove = moveTransition.getTransitionMove();
                                    // if the move legal, make it
                                    board = moveTransition.getToBoard();
                                    //boardPanel.drawBoard(board);
                                    if(board.getTurn().isAi) {
                                        // if it's the AI turn, make an AI move
                                        new AiMove().execute();
                                    }
                                }
                            }
                            // initialize
                            sourceTile = -1;
                            destTile = -1;
                            pieceMoved = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(board);
                                if (board.gameResult() != Result.NOT_FINISHED) {
                                    boardPanel.gameOver(board.gameResult());
                                }
                            }
                        });
                    }

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            validate();

        }

        /**
         * put the piece on the tile
         * @param board the board we put his tiles
         */
        private void putTilePiece(Board board) {
            this.removeAll();
            Piece piece = board.board_state.get(tileCoordinate);
            String PieceIconPath = "resources/";
            if(piece != null) {
                try {
                    BufferedImage image =
                            ImageIO.read(new File(PieceIconPath + piece.getClass().getSimpleName() + piece.getColor() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * choose the current color for a tile
         */
        private void putTileColor() {
            boolean isLight = ((tileCoordinate + tileCoordinate / 8) % 2 == 0);
            setBackground(isLight ? whiteTileColor : blackTileColor);
            // if the tile of the chosen piece
            if(tileCoordinate == sourceTile && pieceMoved.getColor() == board.getTurn().getColor())
                setBackground(greenTileColor);
            // if the tile is the tile of a king under check
            if(tileCoordinate == board.getTurn().getKing().getPosition() && board.getTurn().isInCheck())
                setBackground(redTileColor);

        }

        /**
         * draw the tile, his color, and the piece on it(or not)
         * @param board the board to draw his tiles
         */
        public void drawTile(Board board) {
            putTileColor();
            putTilePiece(board);
            drawPossibleMoves(board);
            highlightLastMove();
            validate();
            repaint();
        }

        /**
         * draw the possible moves for a chosen piece
         * @param board the board the piece is in
         */
        public void drawPossibleMoves(Board board)
        {
            if(pieceMoved != null) {
                for (Move move : board.getTurn().getLegalMoves()) {
                    MoveTransition moveTransition = board.getTurn().makeMove(move);
                    if (pieceMoved == move.getPieceMoved() && move.getCoordinateMovedTo() == this.tileCoordinate
                    && board.getTurn().getLegalMoves().contains(move) && moveTransition.getMoveStatus() == MoveStatus.DONE) {
                        try {
                            BufferedImage image =
                                    ImageIO.read(new File("resources/green_dot.png"));
                            add(new JLabel(new ImageIcon(image)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
        private void highlightLastMove() {
            if(computerMove != null) {
                if(this.tileCoordinate == computerMove.getPieceMoved().getPosition()) {
                    setBackground(darkGreenTileColor);
                }
                else if(this.tileCoordinate == computerMove.getCoordinateMovedTo()){
                    setBackground(lightGreenTileColor);
                }

            }
        }
    }
}
