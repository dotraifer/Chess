package gui;

import logic.Board;
import logic.Move;
import logic.Move.MoveStatus;
import logic.MoveTransition;
import logic.Pieces.King;
import logic.Pieces.Piece;
import logic.player.Player;

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

    public GameScreen()
    {
        this.board = new Board(Board.createNewBoard());

        this.gameFrame = new JFrame("Chess");//creating instance of JFrame
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setSize(400, 400);

        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true);
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
    }
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
                    if(isLeftMouseButton(e))
                    {
                        if(sourceTile == -1) {
                            // first click
                            sourceTile = tileCoordinate;
                            pieceMoved = board.getPieceAtCoordinate(sourceTile);
                            if(pieceMoved == null){
                                sourceTile = -1;
                            }
                            else
                                System.out.println(pieceMoved.isFirstMove());
                        }
                        else{
                            // second click
                            destTile = tileCoordinate;
                            if(destTile != sourceTile)
                            {
                                Move move = Move.MoveFactory.createMove(board, pieceMoved.getPosition(), destTile);
                                moveTransition = board.getTurn().makeMove(move);
                                if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                                    board = moveTransition.getToBoard();
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
                    if (pieceMoved == move.getPieceMoved() && move.getCoordinateMovedTo() == this.tileCoordinate) {
                        try {
                            BufferedImage image =
                                    ImageIO.read(new File("resources/green_dot.png"));
                            add(new JLabel(new ImageIcon(image)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                if(pieceMoved.getClass() == King.class)
                {

                }
            }
        }
    }
}
