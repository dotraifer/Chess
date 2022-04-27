package logic.player.AI;

import logic.Color;

import static logic.Color.White;

public class CachedData {

    public enum Type
    {
        EXACT_VALUE, UPPERBOUND, LOWERBOUND
    }
    private int depth;
    private double score;
    private Color turnColor;
    private Type type;

    public CachedData(int depth, double score, Type type)
    {
        this.depth = depth;
        this.score = score;
        this.turnColor = White;
        this.type = type;
    }

    public int getDepth() {
        return depth;
    }

    public double getScore() {
        return score;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
