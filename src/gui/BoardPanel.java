package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

}
