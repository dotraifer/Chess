package gui;

import javax.swing.*;

public class GameScreen {
    private final JFrame frame;
    public GameScreen()
    {
        this.frame = new JFrame("Chess");//creating instance of JFrame
        this.frame.setSize(400, 400);
        this.frame.setVisible(true);
    }
}
