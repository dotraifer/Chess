package logic.player.AI;

import logic.Move;
import logic.player.Player;

public class Mobility {
    public static double mobility(Player player) {
        return player.getLegalMoves().size() * 0.01;
    }
}
