package logic.player.AI;

public class EvaluationAssistants {
    public static int size = 64;
    public static final int[][] BOARD_COORDINATES = boardInit();

    public static int[][] boardInit()
    {
        int[][] matrix = new int[8][8];
        for(int i = 0;i < 8;i++)
        {
            for(int j = 0;j < 8;j++)
            {
                matrix[i][j] = i * 8 + j;
            }
        }
        return matrix;
    }
}
