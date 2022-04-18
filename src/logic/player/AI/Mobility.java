package logic.player.AI;

import logic.Move;
import logic.player.Player;

public class Mobility {
    /**
     * this function evaluate player mobility
     * @param player the player we calculate his mobility
     * @return evaluation of the player's mobility
     */
    public static double mobility(Player player) {
        return player.getLegalMoves().size() * 0.01;
    }
}
