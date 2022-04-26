package logic.player.AI;

import logic.Color;

import static logic.Color.White;

public class CachedData {

    private int depth;
    private double score;
    private Color turnColor;

    public CachedData()
    {
        this.depth = 0;
        this.score = 0;
        this.turnColor = White;
    }

    public int getDepth() {
        return depth;
    }

    public double getScore() {
        return score;
    }

    public Color getTurnColor() {
        return turnColor;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setTurnColor(Color turnColor) {
        this.turnColor = turnColor;
    }
}
