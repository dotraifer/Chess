package gui;

import logic.Board;

import javax.swing.*;
import java.awt.*;

public class GameScreen {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;

    public GameScreen()
    {
        this.gameFrame = new JFrame("Chess");//creating instance of JFrame
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setSize(400, 400);

        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true);
    }
}
