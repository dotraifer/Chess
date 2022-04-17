package gui;

import logic.Board;
import logic.Move;
import logic.Move.MoveStatus;
import logic.MoveTransition;
import logic.Pieces.Piece;
import logic.player.AI.Minimax;

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
import static logic.player.AI.PositionEvaluation.evaluate;

public class GameScreen {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;

    private final Dimension TILE_DIMENSION = new Dimension(10, 10);

    private Board board;

    private final Color whiteTileColor = Color.decode("#FFFDD0");
    private final Color blackTileColor = Color.decode("#D2691E");
    private final Color greenTileColor = Color.decode("#00FF00");
    private final Color redTileColor = Color.decode("#FF0000");

    private static int sourceTile = -1;
    private static int destTile = -1;
    private static Piece pieceMoved;

    private static boolean isWhiteAi;
    private static boolean isBlackAi;

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
    }
    public void resetGame()
    {
        chooseAiOrNotPopUp();
        this.board = new Board(Board.createNewBoard(isWhiteAi, isBlackAi));
        this.gameFrame.setVisible(true);
    }

    public void chooseAiOrNotPopUp()
    {
        String[] options = {"AI", "PVP"};
        int choice = JOptionPane.showOptionDialog(null, "Choose game Mode",
                "Game mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if(choice == 0)
            chooseColor();
        else{
            isWhiteAi = false;
            isBlackAi = false;
        }
    }
    public void chooseColor()
    {
        String[] options = {"WHITE", "BLACK"};
        int choice = JOptionPane.showOptionDialog(null, "Choose game Mode",
                "Game mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if(choice == 0)
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

    public class BoardPanel extends JPanel {
        private final Dimension BOARD_DIMENSION = new Dimension(400, 400);
        List<TilePanel> Tiles;

        BoardPanel(){
            super(new GridLayout(8, 8));
            this.Tiles = new ArrayList<>();
            for (int i = 0; i < 64;i++)
            {
                TilePanel tilePanel = new TilePanel(this, i);
                this.Tiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_DIMENSION);
            validate();
        }

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
        public void gameOver(logic.Color color)
        {
            String message;
            if(color == null)
                message = "draw, Want to try again?";
            else
                message = color + " won, Want to try Again?";
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
    public class TilePanel extends JPanel {
        private final int tileCoordinate;

        TilePanel(BoardPanel boardPanel, int tileCoordinate){
            super(new GridBagLayout());
            this.tileCoordinate = tileCoordinate;
            setPreferredSize(TILE_DIMENSION);
            putTileColor();
            putTilePiece(board);
            if(board.getTurn().isAi)
                AiMove();
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    MoveTransition moveTransition = null;
                    if(isLeftMouseButton(e))
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
                                System.out.println("sec"+board.getBlackPlayer().isAi);
                                if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                                    board = moveTransition.getToBoard();
                                    System.out.println("hey" + evaluate(board));
                                    boardPanel.drawBoard(board);
                                    if(board.getTurn().isInCheckMate())
                                    {
                                        boardPanel.gameOver(board.getOpponent().getColor());
                                    }
                                    if(board.getTurn().isInStaleMate())
                                    {
                                        boardPanel.gameOver(null);
                                    }
                                    if(board.getTurn().isAi) {
                                        putTileColor();
                                        putTilePiece(board);
                                        AiMove();
                                    }
                                }
                            }
                            sourceTile = -1;
                            destTile = -1;
                            pieceMoved = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(board);
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
        public void AiMove()
        {
            MoveTransition moveTransition = null;
            moveTransition = board.getTurn().makeMove(Minimax.execute(board, 5));
            if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                board = moveTransition.getToBoard();
                if(board.getTurn().isInCheckMate())
                    boardPanel.gameOver(board.getOpponent().getColor());
            }
        }

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

        public void drawTile(Board board) {
            putTileColor();
            putTilePiece(board);
            drawPossibleMoves(board);
            validate();
            repaint();
        }

        public void drawPossibleMoves(Board board)
        {
            if(pieceMoved != null) {
                for (Move move : board.getTurn().getLegalMoves()) {
                    if (pieceMoved == move.getPieceMoved() && move.getCoordinateMovedTo() == this.tileCoordinate
                    && board.getTurn().getLegalMoves().contains(move)) {
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
    }
}
