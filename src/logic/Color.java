package logic;

/**
 * Enum of color
 */
public enum Color {
    White
            {
                public int getDirection(){return -1;}
            }
    , Black
            {
                public int getDirection(){return 1;}
            };
    abstract int getDirection();

}

