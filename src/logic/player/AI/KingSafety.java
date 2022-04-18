package logic.player.AI;

import logic.Move;
import logic.Pieces.Piece;
import logic.player.Player;

import java.util.Collection;

public class KingSafety {
    public static double calculateKingTropism(final Player player) {
        final int playerKingSquare = player.getKing().getPosition();
        final Collection<Move> enemyMoves = player.getRival().getLegalMoves();
        Piece closestPiece = null;
        int closestDistance = Integer.MAX_VALUE;
        for(final Move move : enemyMoves) {
            final int currentDistance = calculateChebyshevDistance(playerKingSquare, move.getCoordinateMovedTo());
            if(currentDistance < closestDistance) {
                closestDistance = currentDistance;
                closestPiece = move.getPieceMoved();
            }
        }
        return closestPiece.value / 10 * closestDistance;
    }

    private static int calculateChebyshevDistance(int playerKingSquare, int coordinateMovedTo) {
        int kingColumn =  playerKingSquare % 8;
        int kingRow = playerKingSquare / 8;
        int enemyColumn = coordinateMovedTo % 8;
        int enemyRow = coordinateMovedTo / 8;

        int columnDistance = Math.abs(kingColumn - enemyColumn);
        int rowDistance = Math.abs(kingRow - enemyRow);

        return Math.max(columnDistance, rowDistance);


    }
}
