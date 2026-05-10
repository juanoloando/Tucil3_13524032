package model;

public enum Direction {
    UP(0, -1, 'U'),
    DOWN(0, 1, 'D'),
    LEFT(-1, 0, 'L'),
    RIGHT(1, 0, 'R');

    private final int dx;
    private final int dy;
    private final char symbol;

    Direction (int dx, int dy, char symbol){
        this.dx = dx;
        this.dy = dy;
        this.symbol = symbol;
    }

    public int getDx(){
        return dx;
    }

    public int getDy(){
        return dy;
    }

    public char getSymbol(){
        return symbol;
    }    
}
