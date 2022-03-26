package gui;

import logic.Board;
import logic.Pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TilePanel extends JPanel {
    private final Dimension TILE_DIMENSION = new Dimension(10, 10);
    private final int tileCoordinate;
    private Board board;

    private final Color whiteTileColor = Color.decode("#FFFDD0");
    private final Color blackTileColor = Color.decode("#D2691E");

    TilePanel(BoardPanel boardPanel, int tileCoordinate){
        super(new GridBagLayout());
        this.tileCoordinate = tileCoordinate;
        this.board = new Board();
        setPreferredSize(TILE_DIMENSION);
        putTileColor();
        putTilePiece(this.board);
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
    }
}
