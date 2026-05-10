package model;

public enum Tile {
    ICE,
    OBSTACLE,
    LAVA,
    START,
    EXIT, 
    GOAL_0,
    GOAL_1,
    GOAL_2,
    GOAL_3,
    GOAL_4,
    GOAL_5,
    GOAL_6,
    GOAL_7,
    GOAL_8,
    GOAL_9;

    //ordinal dimulai dari 0 yang menandakan urutan deklarasi.
    public boolean isGoal(){
        return this.ordinal() >= GOAL_0.ordinal() && this.ordinal() <= GOAL_9.ordinal();
     }

     public int getGoalIndex(){
        if(!isGoal()){
            throw new IllegalStateException("Tile ini bukan tile goal.");
        }
        return this.ordinal() - GOAL_0.ordinal();
     }

     public boolean isObstacle(){
        return this == OBSTACLE;
     }

     public boolean isLava(){
        return this == LAVA;
     }

     public static Tile fromChar(char c){
        return switch(c){
            case '*' -> ICE;
            case 'X' -> OBSTACLE;
            case 'L' -> LAVA;
            case 'Z' -> START;
            case 'O' -> EXIT;
            case '0' -> GOAL_0;
            case '1' -> GOAL_1;
            case '2' -> GOAL_2;
            case '3' -> GOAL_3;
            case '4' -> GOAL_4;
            case '5' -> GOAL_5;
            case '6' -> GOAL_6;
            case '7' -> GOAL_7;
            case '8' -> GOAL_8;
            case '9' -> GOAL_9;
            default -> throw new IllegalArgumentException("Karakter tidak valid untuk tile: " + c);
        };
     }

     public char toChar(){
        return switch(this){
            case ICE -> '.';
            case OBSTACLE -> 'X';
            case LAVA -> 'L';
            case START -> 'Z';
            case EXIT -> 'O';
            case GOAL_0 -> '0';
            case GOAL_1 -> '1';
            case GOAL_2 -> '2';
            case GOAL_3 -> '3';
            case GOAL_4 -> '4';
            case GOAL_5 -> '5';
            case GOAL_6 -> '6';
            case GOAL_7 -> '7';
            case GOAL_8 -> '8';
            case GOAL_9 -> '9';
        };
     }
     
}
